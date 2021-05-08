import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient {
    private Socket socket = null;
    private DataOutputStream output;
    private DataInputStream input;

    public TCPClient(InetAddress host) {
        try {

            socket = new Socket(host, TCPSever.SEVERPORT);
            this.output = new DataOutputStream(socket.getOutputStream());
            this.input = new DataInputStream(socket.getInputStream());
            System.out.println(socket);
            System.out.println("Client connect successfully");
        } catch (IOException e) {
            System.out.println("Client connect unsuccessfully");
        }
    }
    public void run(){

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                Scanner sc = new Scanner(System.in);
                while (true) {
                    String msg = sc.nextLine();
                    send(msg);
                }
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    System.out.println(receive());
                }
            }
        });
        thread1.start();
        thread2.start();
    }


    private void send(String msg) {
        // thay doi gui den ten nao ?
        try {
            output.writeUTF(msg);
            // day du lieu len sever
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String receive() {
        try {
            if (input == null) {
                System.out.println("input=0");
                return "";
            }
            String s = input.readUTF();
            return s;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void close() {
        try {
            input.close();
            socket.close();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
