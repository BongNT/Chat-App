import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class TCPSever {
    static final int SEVERPORT = 6002;
    private ServerSocket serverSocket = null;
    private Vector<ClientManager> listClient = new Vector<>();

    public TCPSever() {
        try {
            serverSocket = new ServerSocket(SEVERPORT);
            System.out.println(serverSocket.toString());
        } catch (IOException e) {
            System.out.println("Port Error");
        }
    }

    public void run() {
        int i=1;
        while (true) {
            try {
                Socket socket =  serverSocket.accept();
                System.out.println("New client request received : " + socket);
                ClientManager cm = new ClientManager(socket, "client"+i);
                i++;
                listClient.add(cm);
                Thread thread = new Thread(cm);
                thread.start();
            } catch (IOException e) {
                System.out.println("Sever connect unsuccessfully");
            }
        }
    }


    private class ClientManager implements Runnable{
        private String name;
        private DataInputStream input;
        private DataOutputStream output;
        private Socket socket = null;
        private boolean isLogin = true;

        public ClientManager(Socket socket, String name) {
            this.socket = socket;
            this.name = name;
            try {
                output = new DataOutputStream(socket.getOutputStream());
                input = new DataInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(name);

        }

        @Override
        public void run() {
            while(true) {
                String data = receive();
                if(data.equals("logout")) {
                    this.isLogin = false;
                    this.close();
                    break;
                }
                this.handleData(data);
            }
            this.close();
        }

        private void handleData(String data) {
            String[] a = data.split("@");
            String toClient = a[1];
            String message = a[0];
            System.out.println(toClient+message);
            for (ClientManager cm : listClient){
                if(cm.getName().equals(toClient) ) {
                    if (cm.isLogin) {
                        cm.send(message);
                    } else{
                        System.out.println(cm.name + "logged out");
                    }

                }
            }
        }

        private void send(String msg) {
            try {
                output.writeUTF( this.name + " : " + msg);
                // day du lieu len sever
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String receive() {
            try {
                String s = input.readUTF();
                return s;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }

        private void close(){
            try {
                input.close();
                output.close();
                socket.close();
            } catch (IOException e) {
                System.out.println("ClientManager closed unsuccessfully");
            }
        }

        public String getName() {
            return name;
        }
    }

}
