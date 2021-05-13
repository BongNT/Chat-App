import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.CENTER;

public class Chat_Client extends JFrame {
    private JTextArea message;
    private JButton btnSend, btnCreate;
    private JTextArea showMsg;
    private JList<String> listClient;
    private JScrollPane scroll1, scroll2;
    private JPanel jPanel1, jPanel2;
    private JLabel title;
    private TCPClient client;


    public Chat_Client() {
        initComponent();
    }

    private void start() {
        try
        {
            InetAddress ip = InetAddress.getByName("localhost");
            System.out.println (ip.toString());
            client = new TCPClient(ip);
            client.run();
        }
        catch (UnknownHostException e)
        {
            System.out.println("Could not find local address!");
        }
    }

    private void initComponent() {
        //start();

        setSize(550,550);
        setResizable(false);

        btnSend = new JButton("Send");
        btnSend.addActionListener(l->{
            //Xử lí việc gửi đi tin
             String msg = message.getText();
             message.setText("");
             client.send(msg);
             showMsg.append('\n' + msg);
        });

        message = new JTextArea();
        message.setColumns(23);
        message.setRows(3);
        showMsg = new JTextArea();
        showMsg.setEditable(false);
        showMsg.setColumns(30);
        showMsg.setRows(25);

        // Test fram chat rieng
        DefaultListModel<String> l1 = new DefaultListModel<>();
        String a = "Client2";
        l1.addElement(a);
        listClient = new JList<>(l1);

        scroll1 = new JScrollPane(this.listClient);
        scroll1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        listClient.getSelectionModel().addListSelectionListener(e -> {
            JFrame private_chat = new JFrame();
            private_chat.setSize(350,350);

            JTextArea box_msg = new JTextArea();
            box_msg.setEditable(false);
            box_msg.setRows(15);
            box_msg.setColumns(15);
            JTextArea enterMsg = new JTextArea();
            enterMsg.setRows(3);
            enterMsg.setColumns(12);

            JButton send = new JButton("Send");
            JScrollPane scr1 = new JScrollPane(box_msg);
            JScrollPane scr2 = new JScrollPane(enterMsg);

            GroupLayout layout = new GroupLayout(private_chat.getContentPane());
            private_chat.getContentPane().setLayout(layout);
            layout.setHorizontalGroup(layout.createParallelGroup().addComponent(scr1)
                                  .addGroup(layout.createSequentialGroup().addComponent(scr2).addGap(20).addComponent(send))
            );
            layout.setVerticalGroup(layout.createSequentialGroup().addComponent(scr1).addGap(10)
                               .addGroup(layout.createParallelGroup().addComponent(scr2).addComponent(send))
            );
            private_chat.setVisible(true);
        });


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


        setTitle("Client");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    private void setListClient(TCPSever sever) {
        // lấy list client từ Sever, rồi truy cập tên để thêm vào Jlist

    }

    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                Chat_Client client = new Chat_Client();
                client.setVisible(true);
            }
        });
    }
}
