import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Game {

    private final char FOG_OF_WAR = '~';
    private final char HIT = 'X';
    private final char MISS = 'M';
    private final char SHIP = 'O';

    private final int ROWS = 10;
    private final int COLUMNS = 10;

    private final String[] firstRow = {" ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    private final String[] firstColumn = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};

    private char[][] gameField;

    public Game() {

        // Inicializa o campo de batalha com
        // Fog Of War (o símbolo '~')
        this.gameField = new char[ROWS][COLUMNS];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                this.gameField[i][j] = FOG_OF_WAR;
            }
        }
    }

    // Inicializa o campo de batalha posicionando
    // os navios de acordo com o input do usuário
    public void initField() {

        for (Ships ship : Ships.values()) {
            placeShip(ship);
        }

    }

    // Testa se o navio recebeu o tamanho correto
    // e se o mesmo não foi colocado em um lugar errado no campo de batalha
    public void placeShip(Ships ship) {

        List<String> rowList = Arrays.asList(firstColumn);
        List<String> columnList = Arrays.asList(firstRow);
        System.out.printf("Enter the coordinates of the %s (%d cells):\n",
                ship.getName(), ship.getCells());

        Scanner scanner = new Scanner(System.in);

        boolean shipPlaced = false;

        while (!shipPlaced) {
            String[] cells = scanner.nextLine().toUpperCase().split("\\s+");
            String row0 = cells[0].substring(0, 1);
            String row1 = cells[1].substring(0, 1);

            String column0 = cells[0].substring(1);
            String column1 = cells[1].substring(1);

            if (!row0.equals(row1) && !column0.equals(column1)) {
                System.out.println("Error! Wrong ship location! Try again:\n");
            } else if (row0.equals(row1)) {

                int size = Math.abs(columnList.indexOf(column0) - columnList.indexOf(column1)) + 1;

                if (size != ship.getCells()) {
                    System.out.printf("Error! Wrong length of the %s! Try again:\n", ship.getName());
                } else {

                    int c0 = columnList.indexOf(column0);
                    int c1 = columnList.indexOf(column1);
                    int c = Math.min(c0, c1);
                    placeShipHorizontally(size, rowList.indexOf(row0) , c - 1);
                    shipPlaced = true;
                }

            } else if (column0.equals(column1)) {

                int size = Math.abs(rowList.indexOf(row0) - rowList.indexOf(row1)) + 1;

                if (size != ship.getCells()) {
                    System.out.printf("Error! Wrong length of the %s! Try again:\n", ship.getName());
                } else {

                    int r0 = rowList.indexOf(row0);
                    int r1 = rowList.indexOf(row1);
                    int r = Math.min(r0, r1);
                    placeShipVertically(size, r, columnList.indexOf(column0) - 1);
                    shipPlaced = true;
                }
            }

        }

        scanner.close();

        System.out.println(fieldState());

    }

    // Coloca os navios no tabuleiro quando
    // eles são colocados na horizontal
    public void placeShipHorizontally(int shipSize, int row, int column) {

        for (int i = column; i < column + shipSize; i++) {
            this.gameField[row][i] = SHIP;
        }
    }

    // Coloca os navios no tabuleiro quando
    // eles são colocados da vertical
    public void placeShipVertically(int shipSize, int row, int column) {
        for (int i = row; i < row + shipSize; i++) {
            this.gameField[i][column] = SHIP;
        }
    }

    // Mostra o estado atual do campo de batalha
    public String fieldState() {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i <= COLUMNS; i++) {
            result.append(firstRow[i] + " ");
        }
        result.append("\n");

        for (int i = 0; i < ROWS; i++ ) {
            result.append(firstColumn[i] + " ");
            for (int j = 0; j < COLUMNS; j++) {
                result.append(gameField[i][j] + " ");
            }
            result.append("\n");
        }

        return result.toString();
    }

}
