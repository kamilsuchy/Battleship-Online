import java.net.*;

public class ServerUtil {

    static InetAddress findAddress() throws SocketException, UnknownHostException {
        var lo = NetworkInterface.getByName("lo");
        return lo.inetAddresses()
                .filter(a -> a instanceof Inet4Address)
                .findFirst()
                .orElse(InetAddress.getLocalHost());
    }
}