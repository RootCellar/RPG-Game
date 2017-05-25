import java.net.*;
import java.io.*;
import java.util.Random;
/**
 * This class is run as a separate thread to handle player commands
 */
public class readThread implements Runnable
{
    Socket readFrom = null;
    String name="";
    String use="";
    String use2="";
    String pass="";
    int number=-1;
    boolean isLoggedIn=false;
    Character user=null;
    boolean isGoing=true;
    int gold=0;
    public void run() {
        DataInputStream in=null;
        String in2="";
        while((readFrom==null || number==-1)) {
            try{
                Thread.sleep(100);
            }catch(Exception e) {

            }
        }
        try{
            in = new DataInputStream(readFrom.getInputStream());
        }catch(Exception e) {

        }

        while(isGoing) {
            try{
                while(!isLoggedIn) {
                    Server.sendTo("/login or /register?",readFrom);
                    in2=in.readUTF();
                    if(in2.equals("/login")) {
                        Server.sendTo("Username: ",readFrom);
                        use=in.readUTF();
                        Server.sendTo("Password: ",readFrom);
                        pass=in.readUTF();
                        in2=Server.authen(use,pass,this);
                        Server.sendTo(in2,readFrom);
                        if(in2.equals("Logged in")) {
                            name=use;
                            Server.read(this);
                            isLoggedIn=true;
                        }
                    }
                    if(in2.equals("/register")) {
                        Server.sendTo("Username: ",readFrom);
                        use=in.readUTF();
                        Server.sendTo("Password: ",readFrom);
                        pass=in.readUTF();
                        in2=Server.authenr(use,pass,this);
                        Server.sendTo(in2,readFrom);
                        if(in2.equals("Register")) {
                            Server.sendTo("Log in to your new account to continue",readFrom);
                        }
                    }
                }
                while(user.getType()==null) {
                    for(int i=0; i<Server.getClasses().size(); i++) {
                        Server.sendTo(i+". "+Server.getClasses().get(i).getName(),readFrom);
                    }
                    Server.sendTo("Pick a class by number: ",readFrom);
                    in2=in.readUTF();
                    try{
                        user=new Character(name,Server.getClasses().get(Integer.parseInt(in2)),user.getRace(),1.1);
                    }catch(Exception e) {
                        Server.sendTo("Please type an integer",readFrom);
                    }
                }
                while(user.getRace()==null) {
                    for(int i=0; i<Server.getRaces().size(); i++) {
                        Server.sendTo(i+". "+Server.getRaces().get(i).getName(),readFrom);
                    }
                    Server.sendTo("Pick a race by number: ",readFrom);
                    in2=in.readUTF();
                    try{
                        user=new Character(name,user.getType(),Server.getRaces().get(Integer.parseInt(in2)),1.1);
                    }catch(Exception e) {
                        Server.sendTo("Please type an integer",readFrom);
                    }
                }
                in2=in.readUTF();
                if(in2.equals("/help")) {
                    Server.sendTo("/mychar - gives info about your character",readFrom);
                }
                else if(in2.equals("/mychar")) {
                    Server.sendTo(user.toString(),readFrom);
                }
                else if(!in2.equals("")) {
                    Server.send(readFrom,in2,name);
                }
                Server.save(this,name,pass);
                user.calcStats();
                Server.getListener().command(in2,this);
            }catch(IOException e) { //Connection closed, player is quitting
                //System.out.println(readFrom.getRemoteSocketAddress()+ " ("+name+") Disconnected");
                Server.send(readFrom.getRemoteSocketAddress()+"("+name+") Disconnected");
                //Server.deleteThread(number);
                isGoing=false;
                try{
                    readFrom.close();
                }catch(IOException f) {

                }
                break;
            }catch(Exception e) {
                //Server.send("Server error");
                //e.printStackTrace();
            }
        }
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int i) {
        gold=i;
    }

    public boolean isGoing() {
        return isGoing;
    }

    public void setNumber(int num) {
        number=num;
    }

    public void setSocket(Socket use) {
        readFrom=use;
    }

    /**
     * Returns the socket connection to the player
     */
    public Socket getSocket() {
        return readFrom;
    }

    /**
     * Returns the player's name
     */
    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public Character getUser() {
        return user;
    }

    public void setUser(Character u) {
        user=u;
    }
}