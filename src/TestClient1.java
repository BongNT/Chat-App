// Java implementation for multithreaded chat client
// Save file as Client.java

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TestClient1
{
    public static void main(String[] args) {
        try
        {
            InetAddress ip = InetAddress.getByName("localhost");
            System.out.println (ip.toString());
            TCPClient client1 = new TCPClient(ip,"b");
            client1.run();
        }
        catch (UnknownHostException e)
        {
            System.out.println("Could not find local address!");
        }
    }
}
