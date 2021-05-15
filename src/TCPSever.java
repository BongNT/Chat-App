import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class TCPSever {

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
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        registerClient(socket);
                    }
                });
                thread.start();
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
            while (true) {
                ck = false;
                name = input.readUTF();
                System.out.println("@@@@@@@@    " +name);
                name = name.split(Request.SPLITSTRING)[1];
                System.out.println("register name : " + name);
                for (ClientManager c : listClient) {
                    if (c.getName().equals(name)) {
                        output.writeUTF("false");
                        // day du lieu len sever
                        output.flush();
                        ck = true;
                        break;
                    }
                }
                if (!ck || listClient.size() ==0) {
                    output.writeUTF("true");
                    // gui du lieu cho client
                    output.flush();
                    break;
                }
            }
            System.out.println("New client request received : " + socket);
            System.out.println("Sever has "+listClient.size() + " connecting clients");
            ClientManager cm = new ClientManager(socket, name);
            listClient.add(cm);
            Thread thread = new Thread(cm);
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendAllListClient() {
        String s =Request.GETLISTNAMECLIENT.toString() + Request.SPLITSTRING;
        for (String t :getListNameClient()) {
            s += t + Request.SPLITSTRING;
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
                String[] temp = data.split(Request.SPLITSTRING, 2);
                if (temp.length >=2) {
                    String fromClient = temp[0];
                    //temp[1] la phan con lai
                    if(temp[1].equals(Request.LOGOUT.toString())) {
                        // gui ve client tin loggout
                        //tim va xoa khoi sever
                        ClientManager deleteClient = null;
                        for (ClientManager cm : listClient){
                            cm.send(fromClient + Request.SPLITSTRING + Request.LOGOUT.toString());
                            if(cm.getName().equals(fromClient) ) {
                                if (cm.isLogin) {
                                    cm.isLogin = false;
                                    deleteClient = cm;
                                } else{
                                    System.out.println(cm.name + "logged out");
                                }
                            }
                        }
                        deleteClient.close();
                        listClient.remove(deleteClient);
                        System.out.println("sever close this client");
                        System.out.println(listClient.size());
                        break;
                    }else if (temp[1].equals(Request.GETLISTNAMECLIENT.toString())) {
                        sendAllListClient();
                    }
                    else{
                        this.handleSendData(temp[0], temp[1]);
                    }
                }
            }
            //this.close();
        }

        private void handleSendData(String fromClient, String data) {
            String[] a = data.split(Request.SPLITSTRING);
            String toClient = a[1];
            String message = a[0];
            System.out.println("handle" + fromClient+" "+toClient+" "+message);
            findClientToSend(fromClient, message, toClient);
        }

        private void findClientToSend(String fromClient, String message,String toClient) {
            for (ClientManager cm : listClient){
                if(cm.getName().equals(toClient) ) {
                    if (cm.isLogin) {
                        //sever send msg to client
                        cm.send(fromClient + Request.SPLITSTRING + message);
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
