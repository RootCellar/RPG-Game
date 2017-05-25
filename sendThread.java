import java.util.Scanner;
public class sendThread implements Runnable
{
    public void run() {
        Scanner in= new Scanner(System.in);
        while(true) {
            Client.send(in.nextLine());
        }
    }
}