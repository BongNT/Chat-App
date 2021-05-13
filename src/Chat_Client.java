import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;

import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.CENTER;

public class Chat_Client extends JFrame {
    private JTextArea message;
    private JButton btnSend;
    private JTextArea showMsg;
    private JList<String> listClient;
    private JScrollPane scroll1, scroll2;
    private JPanel jPanel1, jPanel2;
    private JLabel title;
    private TCPClient client;
    private String client_name;

    public Chat_Client() {
        begin();
    }

    private void start() {
        try
        {
            InetAddress ip = InetAddress.getByName("localhost");
            System.out.println (ip.toString());
            client = new TCPClient(ip,client_name);
            client.run();
        }
        catch (UnknownHostException e)
        {
            System.out.println("Could not find local address!");
        }
    }

    private void begin() {
        //setVisible(false);
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
            //Xử lí kiểm tra tên ở đây, nếu hợp lệ thì chạy 3 câu lệnh dưới, sai thì sẽ yêu cầu nhập lại.

            //nếu hợp lệ:
            client_name = n;
            this.setVisible(true);
            crClient.setVisible(false);
            initComponent();
            //chưa hợp lệ: 2 cau lenh duoi.
            //JOptionPane.showMessageDialog(null, "This name is available. Please enter other name!");
            //enterName.setText("");
        });
    }

    private void initComponent() {
        start();

        setSize(550,550);
        setResizable(false);

        btnSend = new JButton("Send");


        message = new JTextArea();
        message.setColumns(23);
        message.setRows(3);
        showMsg = new JTextArea();
        showMsg.setEditable(false);
        showMsg.setColumns(30);
        showMsg.setRows(25);


        scroll1 = new JScrollPane(this.listClient);
        scroll1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //setListClient();


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
            String msg = message.getText();
            message.setText("");
            client.send(msg);
            showMsg.append('\n' +client_name + ":" + msg );
        });
           // Xử lí nhận tin từ client khác:
        if (client.)






        setTitle("Client");
        setLocationRelativeTo(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    private void setListClient() {
        // lấy list client từ Sever, lấy ra tên đưa vào mảng đặt tên list_name.

        //listClient.setListData(list_name);
        listClient.setLayoutOrientation(JList.VERTICAL);



    }



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
