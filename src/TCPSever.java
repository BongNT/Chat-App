import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class TCPSever {
    public static final String SPLITSTRING = "@";
    static final int SEVERPORT = 6002;
    private ServerSocket serverSocket = null;
    private ArrayList<ClientManager> listClient = new ArrayList<>();
    private static TCPSever instance = null;

    public static TCPSever getInstance() {
        if (instance == null){
            instance = new TCPSever();
        }
        return instance;
    }


    private TCPSever() {
        try {
            serverSocket = new ServerSocket(SEVERPORT);
            System.out.println(serverSocket.toString());
        } catch (IOException e) {
            System.out.println("Port Error");
        }
    }

    public void run() {
        while (true) {
            try {
                System.out.println("wait socket");
                Socket socket =  serverSocket.accept();
                registerClient(socket);
//                Thread thread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                });
//                System.out.println("thread");
//                thread.start();
            } catch (IOException e) {
                System.out.println("Sever connect unsuccessfully");
            }
        }
    }
    private void registerClient(Socket socket) {
        try {
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            DataInputStream input = new DataInputStream(socket.getInputStream());
            boolean ck = true;
            String name = "";
            System.out.println("while");
            while (true) {
                System.out.println("do");
                ck = false;
                name = input.readUTF();
                name = name.split(SPLITSTRING)[1];
                System.out.println(name);
                for (ClientManager c : listClient) {
                    if (c.getName().equals(name)) {
                        output.writeUTF("false");
                        // day du lieu len sever
                        output.flush();
                        ck = true;
                        break;
                    }
                }
                System.out.println(listClient.size());
                if (!ck || listClient.size() ==0) {
                    System.out.println("sever 0");
                    output.writeUTF("true");
                    // gui du lieu cho client
                    output.flush();
                    break;
                }
            }
            System.out.println("New client request received : " + socket);
            ClientManager cm = new ClientManager(socket, name);
            listClient.add(cm);
            Thread thread = new Thread(cm);
            thread.start();
            System.out.println("endwhile");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendAllListClient() {
        String s ="";
        for (String t :getListNameClient()) {
            s += t + SPLITSTRING;
        }

        for (ClientManager c : listClient){
            if (c.isLogin) {
                c.send(s);
            }
        }
    }

    public ArrayList<String> getListNameClient() {
        ArrayList<String> listName = new ArrayList<>();
        for (ClientManager a : listClient) {
            listName.add(a.getName());
        }
        return listName;
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
            System.out.println("ClientManager : " + name);
        }

        @Override
        public void run() {
            while(true) {
                String data = receive();
                String[] temp = data.split(SPLITSTRING, 2);
                String fromClient = temp[0];
                //temp[1] la phan con lai
                if(temp[1].equals(Request.LOGOUT.toString())) {
                    this.isLogin = false;
                    this.close();
                    break;
                }else if (temp[1].equals(Request.GETLISTNAMECLIENT.toString())) {
                    sendAllListClient();
                }

                this.handleSendData(temp[0], temp[1]);
            }
            this.close();
        }

        private void handleSendData(String fromClient, String data) {
            String[] a = data.split(SPLITSTRING);
            String toClient = a[1];
            String message = a[0];
            System.out.println(fromClient+" "+toClient+" "+message);
            findClientToSend(fromClient, message, toClient);
        }

        private void findClientToSend(String fromClient, String message,String toClient) {
            for (ClientManager cm : listClient){
                if(cm.getName().equals(toClient) ) {
                    if (cm.isLogin) {
                        cm.send(fromClient + SPLITSTRING + message);
                    } else{
                        System.out.println(cm.name + "logged out");
                    }
                }
            }
        }

        private void send(String msg) {
            try {
                output.writeUTF(msg);
                // gui du lieu cho client
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
