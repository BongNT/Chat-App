import java.awt.*;
import java.util.ArrayList;

public class test {
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
