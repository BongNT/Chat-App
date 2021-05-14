import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicBoolean;

import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.CENTER;

public class Chat_Client extends JFrame {
    private JTextArea message;
    private JButton btnSend;
    private JTextArea showMsg;
    private JList<String> listClient;
    DefaultListModel<String> list;
    private JScrollPane scroll1, scroll2;
    private JPanel jPanel1, jPanel2;
    private JLabel title;
    private TCPClient client;

    public Chat_Client() {
        list = new DefaultListModel<>();
        listClient = new JList<>(list);
        start();
        begin();
    }

    private void start() {
        try
        {
            InetAddress ip = InetAddress.getByName("localhost");
            System.out.println (ip.toString());
            client = new TCPClient(ip,"default");
        }
        catch (UnknownHostException e)
        {
            System.out.println("Could not find local address!");
        }
    }

    private void begin() {
        JFrame crClient = new JFrame();
        crClient.setSize(250,150);
        crClient.setResizable(false);
        JLabel content = new JLabel("Type your name:");
        JTextField enterName = new JTextField();
        enterName.setSize(150,30);
        JButton btnCreate = new JButton("Create");
        GroupLayout gr = new GroupLayout(crClient.getContentPane());
        crClient.getContentPane().setLayout(gr);
        gr.setAutoCreateContainerGaps(true);
        gr.setAutoCreateGaps(true);
        gr.setHorizontalGroup(gr.createParallelGroup().addComponent(content).addComponent(enterName).addComponent(btnCreate));
        gr.setVerticalGroup(gr.createSequentialGroup().addComponent(content).addComponent(enterName).addComponent(btnCreate));
        crClient.setVisible(true);

        btnCreate.addActionListener(e-> {
            String n = enterName.getText();
            if (n == "") {
                JOptionPane.showMessageDialog(null, "Please enter your name!");
            } else {
                //Xử lí kiểm tra tên ở đây, nếu hợp lệ thì chạy , sai thì sẽ yêu cầu nhập lại.
                //gui ten len sever
                client.send(n);
                //ket qua sever tra ve
                String data = client.receive();
                System.out.println(data);
                if (data.equals("true")) {
                    crClient.setVisible(false);
                    initComponent();
                    this.setVisible(true);
                    client.setName(n);
                    handleReceive();
                } else {
                    JOptionPane.showMessageDialog(null, "This name is available. Please enter other name!");
                    enterName.setText("");
                }
            }
        });


        crClient.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("xxxx");
                client.send(client.getName());
                client.send(Request.LOGOUT.toString());
                client.close();
                System.exit(0);
            }
        });

    }

    private void initComponent() {
        setListClient();
        setSize(550,550);
        setResizable(false);

        btnSend = new JButton("Send");


        message = new JTextArea();
        message.setColumns(23);
        message.setRows(3);
        message.setEditable(true);
        showMsg = new JTextArea();
        showMsg.setEditable(false);
        showMsg.setColumns(30);
        showMsg.setRows(25);


        scroll1 = new JScrollPane(this.listClient);
        scroll1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);


        scroll2 = new JScrollPane(this.showMsg);
        scroll2.setViewportView(this.showMsg);
        JScrollPane scroll3 = new JScrollPane(message);
        title = new JLabel("Online User");

        jPanel1 = new JPanel();
        GroupLayout grLayout1 = new GroupLayout(jPanel1);
        jPanel1.setLayout(grLayout1);
        grLayout1.setAutoCreateGaps(true);
        grLayout1.setAutoCreateContainerGaps(true);
        grLayout1.setHorizontalGroup(grLayout1.createParallelGroup().addComponent(title,CENTER)
                .addComponent(scroll1)
        );
        grLayout1.setVerticalGroup(grLayout1.createSequentialGroup().addComponent(title).addComponent(scroll1));

        jPanel2 = new JPanel();
        GroupLayout grLayout2 = new GroupLayout(jPanel2);
        grLayout2.setHorizontalGroup(grLayout2.createParallelGroup().addComponent(scroll2)
                .addGroup(grLayout2.createSequentialGroup().addComponent(scroll3).addComponent(btnSend))
        );
        grLayout2.setVerticalGroup(grLayout2.createSequentialGroup().addComponent(scroll2)
                .addGap(20)
                .addGroup(grLayout2.createParallelGroup(BASELINE).addComponent(scroll3).addGap(10).addComponent(btnSend))
        );

        GroupLayout layout = new GroupLayout(this.getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(layout.createSequentialGroup().addComponent(jPanel1,-1,150,-1).addComponent(jPanel2,-1,400,-1));
        layout.setVerticalGroup(layout.createParallelGroup(BASELINE).addComponent(jPanel2).addComponent(jPanel1));

        // Cac action

        btnSend.addActionListener(l->{
            //Xử lí việc gửi đi tin
            //xem gui den thang nao
            //msg = msg + TCPSever.SPLITSTRING + ten thang doMsg

            // gửi lên xong đồng thời lưu vào rồi load list lên luôn list thoại tương ứng giữa 2 client vì nếu đối
            if (listClient.getSelectedIndex() != -1 && !listClient.getSelectedValue().equals(client.getName())) {

                String toClient = listClient.getSelectedValue();
                String msg = message.getText();
                message.setText("");
                String requestMsg = msg + Request.SPLITSTRING + toClient;
                client.send(requestMsg);
                showMsg.append("\n" +client.getName() + " : " + msg);


            } else {
                JOptionPane.showMessageDialog(null, "Please choose people in the list to send!");
                message.setText("");
            }
        });
            listClient.getSelectionModel().addListSelectionListener(e-> {
                // mk sẽ load list tin nhắn từ list thoại tương ứng lên showMsg khi chọn đối tượng chat
                showMsg.setText(""); //ban đầu để rỗng trc nha tại chưa có cái lưu.
            });



        setTitle("Client");
        setLocationRelativeTo(null);
        CloseWindow();
    }
    private void handleReceive() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Simulate doing something useful.
                while(true) {
                    /*
                    Xử lí tin nhắn đến, kiểm tra có tin nhắn đến -> append vào showMsg để hiển thị.
                    Code dưới.
                    */
                    String data = client.receive();
                    System.out.println(data);
                    String fromClient = data.split(Request.SPLITSTRING)[0];
                    String msg = data.split(Request.SPLITSTRING)[1];
                    //setListClient();
                    if (msg.equals(Request.LOGOUT.toString())) {
                        //logout
                    } else if(msg.equals(Request.GETLISTNAMECLIENT.toString())) {

                    }else {
                        //sua day
                        // Sửa chỗ này nhận điều kiện là chỉ có 1 cái "@".
                       /*
                      Cái này nhận đk msg sau đó lưu vào list thoại tương ứng.
                       - nếu như cái fromClient = đối tượng mk đang chat thì pải load lên luôn cái list thoại.

                        */
                        showMsg.append("\n" + fromClient + " : " + msg);
                        System.out.println(fromClient +" : " + msg);
                    }
                }

            }
        };
        worker.execute();
    }

    private void setListClient() {
        // lấy list client từ Sever, lấy ra tên đưa vào mảng đặt tên list_name.
        //client.send(Request.GETLISTNAMECLIENT.toString());
        client.send(Request.GETLISTNAMECLIENT.toString());
        String data = client.receive();
        String[] list_name = data.split(Request.SPLITSTRING);

        for (String s : list_name){
            list.addElement(s);
            System.out.println("list name " + s);
        }

        //listClient.setListData(list_name);

    }

    public void CloseWindow() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                client.send(Request.LOGOUT.toString());
                client.close();
                System.exit(0);
            }
        });
    }


}