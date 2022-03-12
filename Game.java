public class Game {

    private final char FOG_OF_WAR = '~';
    private final char HIT = 'X';
    private final char MISS = 'M';

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
