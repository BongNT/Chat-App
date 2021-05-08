import java.net.InetAddress;
import java.net.UnknownHostException;

public class TestClient2 {
    public static void main(String[] args) {
        try
        {
            InetAddress ip = InetAddress.getByName("localhost");
            System.out.println (ip.toString());
            TCPClient client2 = new TCPClient(ip);
            client2.run();
        }
        catch (UnknownHostException e)
        {
            System.out.println("Could not find local address!");
        }

    }
}
