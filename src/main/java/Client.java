import java.io.File;
import java.io.IOException;
import java.net.Socket;


public class Client {

    @SuppressWarnings("ConstantConditions")
    public static void main(String[] args) throws IOException {

        String address = ServerUtil.findAddress().toString();
        Socket socket = new Socket(address.substring(1), Integer.parseInt(args[3]));
        Gameplay play = new Gameplay(socket, State.MYTURN, new File(args[5]));
        new Thread(play, "client").start();
        System.out.println("Client is starting " + "address: " + address + ", port: " + args[3]);
//        BoardUtility.displayBoard(BoardUtility.load(new File(args[5])));
        GeneratorBoard.boardToFile(args[5]);
        BoardUtility.displayBoard(BoardUtility.load(new File(args[5])));
    }
}