// Java implementation for multithreaded chat client
// Save file as Client.java

import java.awt.*;

public class TestClient1 {
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
