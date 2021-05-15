import java.awt.*;

public class TestClient2 {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                Chat_Client client = new Chat_Client();
                //client.setVisible(true);
            }
        });
    }
}
