import java.net.*;
import java.io.*;
import java.util.Scanner;
public class Client {
    static Socket server=null;
    public static void main(String args[]) {
        sendThread t = new sendThread();
        Thread t2=new Thread(t);
        //t2.start();
        Scanner in2 = new Scanner(System.in);
        String in="";
        String addr="";
        String port="";
        while(true) {
            System.out.println("Host or Join? (h or j)");
            in=in2.next();
            if(in.equals("j")) {
                System.out.println("Server address?");
                addr=in2.next();
                System.out.println("Server port?");
                port=in2.next();
                try{
                    System.out.println("Trying to connect to the server...");
                    server = new Socket(addr,Integer.parseInt(port));
                    System.out.println("Connected.");
                    t2.start();
                    while(true) {
                        in=new DataInputStream(server.getInputStream()).readUTF();
                        System.out.println(in);
                        //System.out.println("DEBUG: "+in.getBytes());
                    }
                }catch(IOException e) {
                    System.out.println("\n\nCan't connect to server");
                    return;
                }
            }
            if(in.equals("h")) {
                System.out.println("Creating private server...");
                Server s = new Server();
                Thread s2 = new Thread(s);
                s2.start();
                s.notServer();
                while(s.getPort()==0) {
                    try{
                        Thread.sleep(100);
                    }catch(Exception e) {

                    }
                }
                try{
                    System.out.println("Trying to connect to the server...");
                    server = new Socket("localhost",s.getPort());
                    System.out.println("Connected.");
                    t2.start();
                    while(true) {
                        in=new DataInputStream(server.getInputStream()).readUTF();
                        System.out.println(in);
                        //System.out.println("DEBUG: "+in.getBytes());
                    }
                }catch(IOException e) {
                    System.out.println("\n\nCan't connect to server");
                    return;
                }   
            }
        }
    }

    public static void send(String m) {
        if(server==null) {
            return;
        }
        try{
            new DataOutputStream(server.getOutputStream()).writeUTF(m);
        }catch(IOException e) {
            System.out.println("Failed to send message to server");
        }
    }
}