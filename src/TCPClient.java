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
    private String name;

    public TCPClient(InetAddress host, String name) {
        try {
            this.name = name;
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

    public void send(String msg) {
        //data : from a with msg to b
        // thay doi gui den ten nao ?

        try {
            msg = name + TCPSever.SPLITSTRING + msg;
            output.writeUTF(msg);
            // day du lieu len sever
            output.flush();
            System.out.println("end send");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String receive() {
        try {
            if (input == null) {
                System.out.println("client receive error");
                return "";
            }
            System.out.println("begin");
            String s = input.readUTF();
            System.out.println("end rec");
            return s;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void close() {
        try {
            input.close();
            socket.close();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setName(String name) {
        this.name = name;
        send("");
    }

    public String getName() {
        return name;
    }
}
