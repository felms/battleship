import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Game {

    private final Scanner scanner;

    private final char FOG_OF_WAR = '~';
    private final char HIT = 'X';
    private final char MISS = 'M';
    private final char SHIP = 'O';

    private final int ROWS = 10;
    private final int COLUMNS = 10;

    private final String[] firstRow = {" ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    private final String[] firstColumn = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};

    private final Node[][] gameField;
    private final Node[][] cloudedField;

    private int sunkenShips;

    public Game() {

        scanner = new Scanner(System.in);
        this.sunkenShips = 0;

        // Inicializa os campos de batalha com
        // Fog Of War (o símbolo '~')
        this.gameField = new Node[ROWS][COLUMNS];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                this.gameField[i][j] = new Node(null, FOG_OF_WAR);
            }
        }

        this.cloudedField = new Node[ROWS][COLUMNS];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                this.cloudedField[i][j] = new Node(null, FOG_OF_WAR);
            }
        }
    }

    // Inicializa o campo de batalha posicionando
    // os navios de acordo com o input do usuário
    public void initField() {

        System.out.println(fieldState(this.gameField));

        Ship aircraftCarrier = new Ship(5, "Aircraft Carrier");
        placeShip(aircraftCarrier);

        Ship battleShip = new Ship(4, "Battleship");
        placeShip(battleShip);

        Ship submarine = new Ship(3, "Submarine");
        placeShip(submarine);

        Ship cruiser = new Ship(3, "Cruiser");
        placeShip(cruiser);

        Ship destroyer = new Ship(2, "Destroyer");
        placeShip(destroyer);

    }

    public void play() {
        System.out.println("\nThe game starts!");
        System.out.println(fieldState(this.cloudedField));

        System.out.println("Take a shot!");

        while(this.sunkenShips < 5) {
            shoot();
        }
    }

    // Executa os tiros nos navios
    private void shoot() {

        List<String> rowList = Arrays.asList(firstColumn);
        List<String> columnList = Arrays.asList(firstRow);

        boolean validShot = false;

        while(!validShot) {
            String s = scanner.nextLine().toUpperCase();
            String row = s.substring(0, 1);
            String column = s.substring(1);

            // Teste se é um tiro válido
            if (!rowList.contains(row) || !columnList.contains(column)) {
                System.out.println("Error! You entered the wrong coordinates! Try again:");
            } else {
                int r = rowList.indexOf(row);
                int c = columnList.indexOf(column);
                placeShot(r, c - 1);
                validShot = true;
            }
        }

        System.out.println(fieldState(this.gameField));

    }

    // Testa se foi um tiro certeiro e coloca
    // 'tiro' no local correto no campo de batalha
    // além de atualizar a contagaem de tiros recebidos
    // pelo navio a contagem de navios afundados
    private void placeShot(int row, int column) {

        if (this.gameField[row][column].cell == SHIP) {
            this.gameField[row][column].cell = HIT;
            this.gameField[row][column].ship.takeAShot();
            this.cloudedField[row][column].cell = HIT;
            System.out.println("\n" + fieldState(this.cloudedField));

            int hits = this.gameField[row][column].ship.getHits();
            int cells = this.gameField[row][column].ship.getCells();

            if (hits == cells) {

                this.sunkenShips++;
                if (this.sunkenShips == 5) {
                    System.out.println("You sank the last ship. You won. Congratulations!");
                } else {
                    System.out.println("You sank a ship! Specify a new target:");
                }
            } else {
                System.out.println("You hit a ship! Try again:");
            }

        } else if (this.gameField[row][column].cell == HIT) {
            System.out.println("You hit a ship! Try again:");
        } else {
            this.gameField[row][column].cell = MISS;
            this.cloudedField[row][column].cell = MISS;
            System.out.println("\n" + fieldState(this.cloudedField));
            System.out.println("You missed! Try again:");
        }

    }

    // Testa se o navio recebeu o tamanho correto
    // e se o mesmo não foi colocado em um lugar errado no campo de batalha
    private void placeShip(Ship ship) {

        List<String> rowList = Arrays.asList(firstColumn);
        List<String> columnList = Arrays.asList(firstRow);
        System.out.printf("\nEnter the coordinates of the %s (%d cells):\n",
                ship.getName(), ship.getCells());

        boolean shipPlaced = false;

        while (!shipPlaced) {
            String[] cells = scanner.nextLine().toUpperCase().split("\\s+");
            String row0 = cells[0].substring(0, 1);
            String row1 = cells[1].substring(0, 1);

            String column0 = cells[0].substring(1);
            String column1 = cells[1].substring(1);

            if (!row0.equals(row1) && !column0.equals(column1)) {
                System.out.println("\nError! Wrong ship location! Try again:");
            } else if (row0.equals(row1)) {

                int size = Math.abs(columnList.indexOf(column0) - columnList.indexOf(column1)) + 1;
                int c0 = columnList.indexOf(column0);
                int c1 = columnList.indexOf(column1);
                int c = Math.min(c0, c1);

                if (size != ship.getCells()) {
                    System.out.printf("\nError! Wrong length of the %s! Try again:", ship.getName());

                } else if (horizontalCollision(size, rowList.indexOf(row0), c - 1)) {
                    System.out.println("\nError! You placed it too close to another one. Try again:");

                } else {
                    placeShipHorizontally(ship, rowList.indexOf(row0) , c - 1);
                    shipPlaced = true;
                }

            } else if (column0.equals(column1)) {

                int size = Math.abs(rowList.indexOf(row0) - rowList.indexOf(row1)) + 1;
                int r0 = rowList.indexOf(row0);
                int r1 = rowList.indexOf(row1);
                int r = Math.min(r0, r1);

                if (size != ship.getCells()) {
                    System.out.printf("\nError! Wrong length of the %s! Try again:", ship.getName());

                } else if (verticalCollision(size, r, columnList.indexOf(column0) - 1)) {
                    System.out.println("\nError! You placed it too close to another one. Try again:");

                } else {
                    placeShipVertically(ship, r, columnList.indexOf(column0) - 1);
                    shipPlaced = true;
                }
            }

        }

        System.out.println(fieldState(this.gameField));

    }

    // Testa se existirá colisão caso o navio seja colocado horizontalmente
    private boolean horizontalCollision(int shipSize, int row, int column) {

        for (int i = column; i < column + shipSize; i++) {

            if (row > 0) {
                if (this.gameField[row - 1][i].cell == SHIP) {
                    return true;
                }
            }

            if (this.gameField[row][i].cell == SHIP) {
                return true;
            }

            if (row < ROWS - 1) {
                if (this.gameField[row + 1][i].cell == SHIP) {
                    return true;
                }
            }

        }

        if (column > 0) {
            if (this.gameField[row][column - 1].cell == SHIP) {
                return true;
            }
        }

        if (column + shipSize < COLUMNS) {
            if (this.gameField[row][column + shipSize].cell == SHIP) {
                return true;
            }
        }

        return false;
    }

    // Testa se existirá colisão caso o navio seja colocado verticalmente
    private boolean verticalCollision(int shipSize, int row, int column) {

        for (int i = row; i < row + shipSize; i++) {

            if (column > 0) {
                if (this.gameField[i][column - 1].cell == SHIP) {
                    return true;
                }
            }

            if (this.gameField[i][column].cell == SHIP) {
                return true;
            }

            if (column < COLUMNS - 1) {
                if (this.gameField[i][column + 1].cell == SHIP) {
                    return true;
                }
            }
        }

        if (row > 0) {
            if (this.gameField[row - 1][column].cell == SHIP) {
                return true;
            }
        }

        if (row + shipSize < ROWS) {
            if (this.gameField[row + shipSize][column].cell == SHIP) {
                return true;
            }
        }

        return false;
    }


    // Coloca os navios no tabuleiro quando
    // eles são colocados na horizontal
    private void placeShipHorizontally(Ship ship, int row, int column) {

        for (int i = column; i < column + ship.getCells(); i++) {
            this.gameField[row][i].cell = SHIP;
            this.gameField[row][i].ship = ship;
        }
    }

    // Coloca os navios no tabuleiro quando
    // eles são colocados da vertical
    private void placeShipVertically(Ship ship, int row, int column) {
        for (int i = row; i < row + ship.getCells(); i++) {
            this.gameField[i][column].cell = SHIP;
            this.gameField[i][column].ship = ship;
        }
    }

    // Mostra o estado atual do campo de batalha
    private String fieldState(Node[][] field) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i <= COLUMNS; i++) {
            result.append(firstRow[i] + " ");
        }
        result.append("\n");

        for (int i = 0; i < ROWS; i++ ) {
            result.append(firstColumn[i] + " ");
            for (int j = 0; j < COLUMNS; j++) {
                result.append(field[i][j].cell + " ");
            }
            result.append("\n");
        }

        return result.toString();
    }

    // Precisei criar essa classe para armazenar a referência de um
    // navio e o valor do campo.
    // Não gostei. Ficou com muita cara de gambiarra mas é o que
    // temos pra hj.
    private class Node {

        Ship ship;
        char cell;

        Node(Ship ship, char cell) {
            this.ship = ship;
            this.cell = cell;
        }
    }

}
