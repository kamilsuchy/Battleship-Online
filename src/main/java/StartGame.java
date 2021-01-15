import java.util.Arrays;

public class StartGame {

    public static void main(String[] args) throws Exception {
        System.out.println("[StartGame] " + Arrays.toString(args));
        String mode  = args[1];
        if (mode.equals("server")){
            Server.main(args);
        }else{
            Client.main(args);
        }
    }
}