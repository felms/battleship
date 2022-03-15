import java.util.Scanner;

public class Main {

    public static Player player01;
    public static Player player02;
    public static Scanner scanner;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        initGame();
        play();
    }

    public static void initGame() {
        player01 = new Player("Player 1");
        player02 = new Player("Player 2");

        System.out.println(player01.getName() + ", place your ships on the game field\n");
        player01.initField();

        System.out.println("Press Enter and pass the move to another player");
        scanner.nextLine();

        System.out.println(player02.getName() + ", place your ships on the game field\n");
        player02.initField();

        System.out.println("Press Enter and pass the move to another player");
        scanner.nextLine();
    }

    public static void play() {
        Player currentPlayer = player01;
        Player opponent = player02;
        boolean gameFinished = false;

        while (!gameFinished) {
            System.out.println(opponent.getCloudedFieldState());
            System.out.println("---------------------");
            System.out.println(currentPlayer.getGameFieldState());
            System.out.println(currentPlayer.getName() + ", it's your turn:");
            opponent.shoot();

            if (opponent.lostTheGame()) {
                gameFinished = true;
            } else {
                System.out.println("Press Enter and pass the move to another player");
                scanner.nextLine();
            }

            opponent = currentPlayer;
            currentPlayer = currentPlayer == player01 ? player02 : player01;
        }
    }
}
