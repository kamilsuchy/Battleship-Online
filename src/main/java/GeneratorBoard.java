import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneratorBoard  {

    private static Boolean[][] initBoard() {
        Boolean[][] board = new Boolean[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                board[i][j] = false;
            }
        }
        return board;
    }

    private static Boolean availableField(Boolean[][] board, Point field) {

        if (field.x + 1 < 10 && field.y - 1 >= 0) {
            if (board[field.x + 1][field.y - 1])
                return false;
        }
        if (field.x - 1 >= 0 && field.y + 1 < 10) {
            if (board[field.x - 1][field.y + 1]) {
                return false;
            }
        }
        return checkLeftAndUpField(board, field.x, field.y) &&
                checkRightAndDownField(board, field.x, field.y);
    }

    private static Point makeFirstMast(Boolean[][] board) {

        Random random = new Random();
        Point coordinatesOfFirstMast = new Point(random.nextInt(10), random.nextInt(10));

        while (!availableField(board, coordinatesOfFirstMast)) {
            coordinatesOfFirstMast.x = random.nextInt(10);
            coordinatesOfFirstMast.y = random.nextInt(10);
        }
        board[coordinatesOfFirstMast.x][coordinatesOfFirstMast.y] = true;
        return coordinatesOfFirstMast;
    }

    private static boolean checkLeftAndUpField(Boolean [][] board, int fieldX, int fieldY){ //method checks common conditions for left field and up field
        if (fieldX - 1 >= 0 && fieldY - 1 >= 0) {
            if (board[fieldX - 1][fieldY - 1]) {
                return false;
            }
        }
        if (fieldX - 1 >= 0) {
            if (board[fieldX - 1][fieldY]) {
                return false;
            }
        }
        if (fieldY - 1 >= 0) {
            if (board[fieldX][fieldY - 1]) {
                return false;
            }
        }
        return !board[fieldX][fieldY];
    }

    private static boolean checkRightAndDownField(Boolean [][] board, int fieldX, int fieldY){ //method checks common conditions for right field and down field
        if (fieldX + 1 < 10 && fieldY + 1 < 10) {
            if (board[fieldX + 1][fieldY + 1]) {
                return false;
            }
        }
        if (fieldY + 1 < 10) {
            if (board[fieldX][fieldY + 1]) {
                return false;
            }
        }
        if (fieldX + 1 < 10) {
            if (board[fieldX + 1][fieldY]) {
                return false;
            }
        }
        return !board[fieldX][fieldY];
    }

    private static boolean leftFieldAvailable(int leftFieldX, int leftFieldY, Boolean[][] board) {
        if (leftFieldY < 0) {
            return false;
        }
        if (leftFieldX + 1 < 10 && leftFieldY - 1 >= 0) {
            if (board[leftFieldX + 1][leftFieldY - 1]) {
                return false;
            }
        }
        if (leftFieldX + 1 < 10) {
            if (board[leftFieldX + 1][leftFieldY]) {
                return false;
            }
        }
        return checkLeftAndUpField(board, leftFieldX, leftFieldY);
    }

    private static boolean upFieldAvailable(int upFieldX, int upFieldY, Boolean[][] board) {

        if (upFieldX < 0) {
            return false;
        }
        if (upFieldY + 1 < 10) {
            if (board[upFieldX][upFieldY + 1]) {
                return false;
            }
        }
        if (upFieldX - 1 >= 0 && upFieldY + 1 < 10) {
            if (board[upFieldX - 1][upFieldY + 1]) {
                return false;
            }
        }
        return checkLeftAndUpField(board, upFieldX, upFieldY);
    }

    private static boolean rightFieldAvailable(int rightFieldX, int rightFieldY, Boolean[][] board) {
        if (rightFieldY >= 10) {
            return false;
        }
        if (rightFieldX - 1 >= 0) {
            if (board[rightFieldX - 1][rightFieldY]) {
                return false;
            }
        }
        if (rightFieldX - 1 >= 0 && rightFieldY + 1 < 10) {
            if (board[rightFieldX - 1][rightFieldY + 1]) {
                return false;
            }
        }
        return checkRightAndDownField(board, rightFieldX, rightFieldY);
    }

    private static boolean downFieldAvailable(int downFieldX, int downFieldY, Boolean[][] board) {

        if (downFieldX >= 10) {
            return false;
        }
        if (downFieldX + 1 < 10 && downFieldY - 1 >= 0) {
            if (board[downFieldX + 1][downFieldY - 1]) {
                return false;
            }
        }
        if (downFieldY - 1 >= 0) {
            if (board[downFieldX][downFieldY - 1]) {
                return false;
            }
        }
        return checkRightAndDownField(board, downFieldX, downFieldY);
    }

    private static void addAvailableFields(List<Point> list, Point coordinates, Boolean[][] board) {
        if (leftFieldAvailable(coordinates.x, coordinates.y - 1, board)) {
            list.add(new Point(coordinates.x, coordinates.y - 1));
        }
        if (upFieldAvailable(coordinates.x - 1, coordinates.y, board)) {
            list.add(new Point(coordinates.x - 1, coordinates.y));
        }
        if (rightFieldAvailable(coordinates.x, coordinates.y + 1, board)) {
            list.add(new Point(coordinates.x, coordinates.y + 1));
        }
        if (downFieldAvailable(coordinates.x + 1, coordinates.y, board)) {
            list.add(new Point(coordinates.x + 1, coordinates.y));
        }
    }

    private static void drawMast(Boolean[][] board, int numberOfMasts) {
        Point coordinates = makeFirstMast(board);
        List<Point> ship = new ArrayList<>();
        ship.add(coordinates);

        Random random = new Random();
        List<Point> list = new ArrayList<>();

        while (ship.size() != numberOfMasts) {

            addAvailableFields(list, coordinates, board);

            if (list.isEmpty()) {
                for (Point point : ship) {
                    board[point.x][point.y] = false;
                }
                ship.clear();
                coordinates = makeFirstMast(board);
                ship.add(coordinates);
            } else {
                int randomIndex = random.nextInt(list.size());
                coordinates = list.get(randomIndex);
                ship.add(coordinates);
                board[coordinates.x][coordinates.y] = true;
                list.remove(randomIndex);
            }
        }
    }

    private static void generateMast(Boolean [][] board){
        drawMast(board, 4);
        drawMast(board, 3);
        drawMast(board, 3);
        drawMast(board, 2);
        drawMast(board, 2);
        drawMast(board, 2);
        drawMast(board, 1);
        drawMast(board, 1);
        drawMast(board, 1);
        drawMast(board, 1);
    }

    private static String boardToString(Boolean [][] board){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (board[i][j]) {
                    stringBuilder.append("#");
                } else {
                    stringBuilder.append(".");
                }
            }
        }
        return stringBuilder.toString();
    }

    public static String generateMap() {
        Boolean[][] board = initBoard();
        generateMast(board);
        return boardToString(board);
    }

    public static void boardToFile(String fileName){
        try {
            File file = new File(fileName);
            FileWriter out = new FileWriter(file);
            out.write(generateMap());
            out.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}