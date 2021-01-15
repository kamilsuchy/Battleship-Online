import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;


public class Gameplay implements Runnable {

    private Socket socket;
    private String message;
    private BufferedWriter out;
    private BufferedReader in;
    private State state;
    private char[][] myBoard;
    private char[][] enemyBoard;


    public Gameplay(Socket socket, State state, File map) throws IOException {
        this.socket = socket;
        this.out = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.state = state;
        this.message = "";
        this.myBoard = BoardUtility.load(map);
        enemyBoard = new char[10][10];
        for (char[] row : enemyBoard)
            Arrays.fill(row, '?');
    }

    @Override
    public void run() {
        try {
            while (state != State.END) {
                if (state == State.MYTURN) {
                    myTurn();
                } else if (state == State.ENEMYTURN) {
                    enemyTurn();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void displayFinalBoards() {
        System.out.println("enemy board");
        BoardUtility.displayBoard(enemyBoard);
        System.out.println("\n my board");
        BoardUtility.displayBoard(myBoard);
    }

    private void hit(int firstIndex, int secondIndex) {
        BoardUtility.set(myBoard, firstIndex, secondIndex, '@');
        if (BoardUtility.isLast(myBoard)) {
            message = "ostatni zatopiony";
            sendMess(message);
            System.out.println("Przegrana");
            state = State.END;
            displayFinalBoards(); //todo
        } else if (BoardUtility.lastMastInShip(myBoard, firstIndex, secondIndex)) {
            message = "trafiony zatopiony";
            state = State.MYTURN;
        } else {
            message = "trafiony";
            state = State.MYTURN;
        }
    }

    private void miss(int firstIndex, int secondIndex) {
        message = "pudło";
        BoardUtility.set(myBoard, firstIndex, secondIndex, '~');
        state = State.MYTURN;
    }

    private void win() {
        try {
            System.out.println("Wygrana");
            state = State.END;

            System.out.println("enemy board");
            BoardUtility.replace(enemyBoard, '?', '.');
            BoardUtility.displayBoard(enemyBoard);
            System.out.println("\n my board");
            BoardUtility.displayBoard(myBoard);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveEnemyBoard(String status) {
        if (status.equals("ostatni zatopiony") || status.equals("trafiony") || status.equals("trafiony zatopiony")) {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (enemyBoard[i][j] == '!') {
                        enemyBoard[i][j] = '@';
                        BoardUtility.unmarkUnknownFields(enemyBoard, i , j);
                    }
                }
            }

        } else {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (enemyBoard[i][j] == '!') {
                        enemyBoard[i][j] = '~';
                    }
                }
            }
        }
    }

    private void enemyTurn() {

        String input = readInput();

        System.out.println("response: " + input);

        String status = input;

        if (input.contains(";")) {
            status = input.substring(0, input.indexOf(';'));
        }

        saveEnemyBoard(status);
        if (status.equals("ostatni zatopiony")) {
            win();
        } else {
            input = input.substring(input.indexOf(';') + 1);

            int first = input.charAt(0) - 'A';
            int second = Integer.parseInt(input.substring(1)) - 1;

            if (BoardUtility.get(myBoard, first, second) == '#' ||
                    BoardUtility.get(myBoard, first, second) == '@') {
                hit(first, second);
            } else if (BoardUtility.get(myBoard, first, second) == '.' ||
                    BoardUtility.get(myBoard, first, second) == '~') {
                miss(first, second);
            }
        }
    }

    private void myTurn() {
        Scanner scanner = new Scanner(System.in);
        String field = scanner.nextLine();
        int first, second;
        if (message.equals("")) {
            sendMess(field);
            String input = field.substring(field.indexOf(';') + 1);

            first = input.charAt(0) - 'A';
            second = Integer.parseInt(input.substring(1)) - 1;
            BoardUtility.set(enemyBoard, first, second, '!');
        } else {
            sendMess(message + ";" + field);
            first = field.charAt(0) - 'A';
            second = Integer.parseInt(field.substring(1)) - 1;
        }
        BoardUtility.set(enemyBoard, first, second, '!');
        state = State.ENEMYTURN;
    }

    private void sendMess(String mess) {
        try {
            this.out.write(mess);
            this.out.newLine();
            this.out.flush();
            System.out.println("sent: " + mess);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readInput() {
        String input = null;
        try {
            do {
                input = in.readLine();
            } while (input == null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return input;
    }
}