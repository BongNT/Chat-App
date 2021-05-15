import java.awt.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class TestClient3 {
    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                Chat_Client client = new Chat_Client();
                //client.setVisible(true);
            }
        });
    }
}
