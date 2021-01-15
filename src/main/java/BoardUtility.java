import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BoardUtility {

    public static boolean isLast(char[][] board) {
        for (char[] row : board) {
            for (Character x : row) {
                if (x == '#') {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean lastMastInShip(char [][] board, int x, int y) {

        if (checkDiagonals(board,x,y, '#') || checkNeighbours(board,x,y, '#')){
            return false;
        }

        List<Point> list = checkNeighboursList(board,x,y);
        if(list.size() == 0){
            return true;
        }

        var result =  list.stream()
                .map((Point point) -> checkNeighbours(board, point.x, point.y, '#') || checkDiagonals(board, point.x, point.y, '#'))
                .collect(Collectors.toList())
                .stream()
                .filter(k -> k)
                .count();

        return result == 0;
    }

    private static List<Point> checkNeighboursList(char[][] board, int x, int y){
        List<Point> list = new ArrayList<>();
        if (x - 1 >= 0 && y+1< 10) {
            if (board[x - 1][y+1] == '@' ||  board[x - 1][y+1] == '#') {
                list.add(new Point(x-1, y+1));
            }
        }
        if (x - 1 >=0 && y-1 >=0){
            if (board[x-1][y-1] == '@' || board[x-1][y-1] == '#'){
                list.add(new Point(x-1, y-1));
            }
        }
        if (x + 1 < 10 && y-1 >= 0){
            if (board[x+1][y-1] == '@' || board[x+1][y-1] == '#'){
                list.add(new Point(x+1,y-1));
            }
        }
        if (x+1 < 10 && y+1 < 10){
            if (board[x+1][y+1] == '@' || board[x+1][y+1] == '#'){
                list.add(new Point(x+1, y+1));
            }
        }
        if (x - 1 >= 0) {
            if (board[x - 1][y] == '@' || board[x - 1][y] == '#') {
                list.add(new Point(x - 1, y));
            }
        }
        if (x + 1 < 10){
            if (board[x+1][y] == '@' || board[x+1][y] == '#'){
                list.add(new Point(x+1, y));
            }
        }
        if (y-1 >= 0){
            if (board[x][y-1] == '@' || board[x][y-1] == '#'){
                list.add(new Point(x, y-1));
            }
        }
        if (y+1 < 10){
            if (board[x][y+1] == '@' || board[x][y+1] == '#'){
                list.add(new Point(x, y+1));
            }
        }
        return list;
    }

    private static boolean checkDiagonals(char[][] board, int x, int y, char ch){
        if (x - 1 >= 0 && y+1< 10) {
            if (board[x - 1][y+1] == ch ) {
                return true;
            }
        }
        if (x - 1 >=0 && y-1 >=0){
            if (board[x-1][y-1] == ch ){
                return true;
            }
        }
        if (x + 1 < 10 && y-1 >= 0){
            if (board[x+1][y-1] == ch ){
                return true;
            }
        }
        if (x+1 < 10 && y+1 < 10){
            if (board[x+1][y+1] == ch ){
                return true;
            }
        }
        return false;
    }

    private static boolean checkNeighbours(char[][] board, int x, int y, char ch) {
        if (x - 1 >= 0) {
            if (board[x - 1][y] == ch ) {
                return true;
            }
        }
        if (x + 1 < 10){
            if (board[x+1][y] == ch ){
                return true;
            }
        }
        if (y-1 >= 0){
            if (board[x][y-1] == ch ){
                return true;
            }
        }
        if (y+1 < 10){
            if (board[x][y+1] == ch ){
                return true;
            }
        }
        return false;
    }

    public static void displayBoard(char [][] board){
        for (char [] row : board){
            for (char x : row){
                System.out.print(x + " ");
            }
            System.out.println();
        }
    }

    public static char [][] load(File map){
        char [][] board = new char[10][10];
        try {
            FileInputStream in = new FileInputStream(map);
            int c;
            int pos = 0;
            while (( c = in.read()) != -1){
                board[pos/10][pos%10] = (char) c;
                pos++;
            }
            in.close();
            return board;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static char get(char [][] board, int x, int y){
        return board[x][y];
    }

    public static void set(char [][] board, int x, int y, char c){
        board[x][y] = c;
    }

    private static void setAllNeighbours(char [][] board, int x, int y){
        if (x - 1 >= 0 && y+1< 10) {
            if (board[x - 1][y+1] == '?' ) {
                board[x - 1][y+1] = '.';
            }
        }
        if (x - 1 >=0 && y-1 >=0){
            if (board[x-1][y-1] == '?' ){
                board[x-1][y-1] = '.';
            }
        }
        if (x + 1 < 10 && y-1 >= 0){
            if (board[x+1][y-1] == '?' ){
                board[x+1][y-1]= '.';
            }
        }
        if (x+1 < 10 && y+1 < 10){
            if (board[x+1][y+1] == '?' ){
                board[x+1][y+1]= '.';
            }
        }
        if (x - 1 >= 0) {
            if (board[x - 1][y] == '?' ) {
                board[x - 1][y]= '.';
            }
        }
        if (x + 1 < 10){
            if (board[x+1][y] == '?' ){
                board[x+1][y]= '.';
            }
        }
        if (y-1 >= 0){
            if (board[x][y-1] == '?' ){
                board[x][y-1]= '.';
            }
        }
        if (y+1 < 10){
            if (board[x][y+1] == '?' ){
                board[x][y+1]= '.';
            }
        }
    }

    public static void unmarkUnknownFields(char [][] board, int x, int y){
        setAllNeighbours(board,x,y);
    }

    public static void replace(char[][] board, char c, char c1) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (board[i][j] == c){
                    board[i][j] = c1;
                }
            }
        }
    }
}