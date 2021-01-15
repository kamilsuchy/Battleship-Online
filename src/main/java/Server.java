import java.io.File;
import java.io.IOException;
import java.net.*;

public class Server implements Runnable {

    private final ServerSocket serverSocket;
    private static String[] arguments;

    private Server(InetAddress address, int port) throws IOException {
        serverSocket = new ServerSocket(port, 10000, address);
        System.out.println("Running Server at address: " + address + ", port: " + port);
    }

    @SuppressWarnings("ConstantConditions")
    public static void main(String[] args) throws Exception {
        arguments = args;
        InetAddress addr = ServerUtil.findAddress();
        Server server = new Server(addr,Integer.parseInt(args[3]));
        new Thread(server, "server").start();
//        BoardUtility.displayBoard(BoardUtility.load(new File(args[5])));
        GeneratorBoard.boardToFile(args[5]);
        BoardUtility.displayBoard(BoardUtility.load(new File(args[5])));
    }

    @Override
    public void run() {
        try {
            Socket server = serverSocket.accept();
            Gameplay play = new Gameplay(server,State.ENEMYTURN, new File(arguments[5]));
            new Thread(play, "server").start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}