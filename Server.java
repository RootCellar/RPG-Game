import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * Use "Server.methodname()" to access these methods
 * 
 */
public class Server implements Runnable
{
    static ArrayList<readThread> r=new ArrayList<readThread>();
    static ArrayList<Thread> r2 = new ArrayList<Thread>();
    static ArrayList<Character> characters = new ArrayList<Character>();
    static ArrayList<Race> races = new ArrayList<Race>();
    static ArrayList<Class> classes = new ArrayList<Class>();
    static String in="";
    static CommandListener commandy;
    static int mapSize=100;
    static boolean isServer=true;
    static int port=0;
    static FileWriter toLogs;
    static boolean isHalted=false;
    public static void main(String args[]) {
        Server s = new Server();
        Thread t = new Thread(s);
        t.start();
    }

    public static int getPort() {
        return port;
    }

    public static void notServer() {
        isServer=false;
    }

    /**
     * This method runs the server
     */
    public void run() {
        System.out.println("Starting Server...");
        System.out.println("Starting command listener...");
        commandy = new CommandListener();
        //Definition of mods here
        commandy.add(new samplecommand());
        //End mods

        new File("Races").mkdir();
        new File("Classes").mkdir();
        new File("Players").mkdir(); //Makes sure that folders are set up
        new File("Mail").mkdir();
        new File("Logs").mkdir();
        
        makeRaces();
        makeClasses();

        checker w = new checker();
        Thread w2 = new Thread(w); //This thread reloads guns and keeps players in map bounds
        w2.start();

        Thread c2=null;
        if(isServer) {
            inThread c = new inThread();
            c2 = new Thread(c); //Allows input in server terminal
            c2.start();
        }

        readThread t;
        Thread t2; //Used when a player connects

        ServerSocket server=null; //Socket used to accept connections

        boolean done=false;
        int x=-1;
        int y=-1;
        port=0;
        for(int i=1; i<65535; i++) {
            try{
                server = new ServerSocket(i);
                server.setSoTimeout(100);
                port=i;
                break;
            }catch(Exception e) {

            }
        }
        if(server==null) {
            System.out.println("Failed to find a valid port");
            return;
        }
        Socket client=null;
        System.out.println("Server Started.");
        System.out.println("Use /help to get command list.");
        System.out.println("Port: "+port);
        long time1=System.nanoTime();
        int ticks=0;
        int ticks2=0;
        while(!done) { //Main Server loop
            //System.gc();
            try{
                try{
                    //Remove players that are no longer connected
                    for(int i=0; i<r.size(); i++) {
                        if(r.get(i).isDone()==true) {
                            deleteThread(i);
                            //System.out.println("DEBUG: Removed "+i);
                        }
                        if(r.get(i).isGoing()==false) {
                            r2.get(i).interrupt();
                        }
                    }
                    client = null;
                    client = server.accept(); //Tries to accept a client connection
                    try{
                        Thread.sleep(100);
                    }catch(Exception d) {
                    }
                    if(client!=null) { //If client connects, set them up
                        System.out.println("Connected to "+client.getRemoteSocketAddress());
                        System.out.println("Setting up threads...");
                        send(client.getRemoteSocketAddress()+" connected.");
                        t=new readThread();
                        t2=new Thread(t);
                        t.setSocket(client);
                        if(r.size()<50) {
                            r.add(t);
                            r2.add(t2);
                            t.setNumber(r.size()-1);
                            t2.start();
                        }
                        else { //If there are too many players, close connection
                            sendTo("Sorry, too many players!",client);
                            sendTo("You will now be kicked.",client);
                            send(client.getRemoteSocketAddress()+" disconnected (too many players)");
                            client.close();
                            t.stopGoing();
                        }
                    }
                }catch(SocketTimeoutException s) {

                }catch(IOException e) {
                    //e.printStackTrace();
                }
                //Commands
                if(isServer) {
                    if(in.equals("/races")) {
                        for(int i=0; i<races.size(); i++) {
                            System.out.println(i+". "+races.get(i).getName()+" "+races.get(i).getRegenMult()+" "+races.get(i).getHpMult()+" "+races.get(i).getAtkMult()+" "+races.get(i).getDefMult()+" "+races.get(i).getMAtkMult()+" "+races.get(i).getManaMult());
                        }
                        in="";
                    }
                    else if(in.equals("/classes")) {
                        for(int i=0; i<classes.size(); i++) {
                            System.out.println(i+". "+classes.get(i).getName()+" "+classes.get(i).getBHp()+" "+classes.get(i).getBMana()+" "+classes.get(i).getBAtk()+" "+classes.get(i).getBDef()+" "+classes.get(i).getBMatk());
                        }
                        in="";
                    }
                    else if(in.equals("/stop")) {
                        done=true;
                        send("Stopping server...");
                    }
                    else if(upToSpace(in,false).equals("/mail ")&& in.length()>6 && in.indexOf("END")==-1) {
                        mail(in.substring(in.indexOf(" ")+1)+" from Server");
                        in="";
                    }
                    else if(in.equals("/getmail")) {
                        System.out.println(getMail("Server"));
                        in="";
                    }
                    else if(in.equals("/delmail")) {
                        delMail("Server");
                        System.out.println("Mail deleted");
                        in="";
                    }
                    else if(in.equals("/help")) {
                        System.out.println("/Stop - Stop the server");
                        System.out.println("/help - view command list");
                        System.out.println("/list - view player list. \nIt shows number, ip, name(if set), Tank, and some debug info");
                        System.out.println("/kick <number> - use /list to \nfind the number of the player, than use /kick (number) to disconnect them");
                        System.out.println("/kickall - kicks every player \noff of the server");
                        System.out.println("/map <number> - sets the map size to number. The map starts out 200x200 (Map size 100)");
                        System.out.println("/mail <name> <message> - mails message to name");
                        System.out.println("/getmail - reads mail");
                        System.out.println("/delmail - deletes mail");
                        System.out.println("/ticks - View number of ticks over the last second");
                        System.out.println("/races - lists races");
                        System.out.println("/classes - lists classes");
                        in="";
                    }
                    else if(in.equals("/list")) { //Shows player list
                        in="";
                        for(int i=0; i<r.size(); i++) {
                            System.out.println(i+". "+r.get(i).getSocket().getRemoteSocketAddress()+" "+r.get(i).getName()+" Level "+r.get(i).getUser().getLevel()+" "+r.get(i).getUser().getRace().getName()+" "+r.get(i).getUser().getType().getName());
                        }
                    }
                    else if(in.length()>=6 && in.substring(0,6).equals("/kick ")) {
                        try{
                            kick(Integer.parseInt(in.substring(6)));
                        }catch(NumberFormatException e) {
                            System.out.println("Please type a number");
                        }
                        in="";
                    }
                    else if(in.length()>=5 && in.substring(0,5).equals("/map ")) { //Changes map size
                        try{
                            w.setMapSize((Integer.parseInt(in.substring(5))));
                            mapSize=(Integer.parseInt(in.substring(5)));
                            System.out.println("Map size changed");
                            send("Map size changed to "+Integer.parseInt(in.substring(5)));
                        }catch(NumberFormatException e) {
                            System.out.println("Please type a number");
                        }
                        in="";
                    }
                    else if(in.equals("/kickall")) {
                        send("Kicking everyone off of the server.");
                        for(int i=r.size()-1; i>-1; i--) {
                            kick(i);
                        }
                        in="";
                    }
                    else if(in.equals("/ticks")) {
                        System.out.println(ticks2);
                        in="";
                    }
                    else if(in.equals("/halt")) {
                        halt(!isHalted);
                        in="";
                    }
                    else if(in.length()>1 && in.substring(0,1).equals("/")) {
                        System.out.println("Invalid command. Use /help to get command list");
                        in="";
                    }
                    else if(!in.equals("")) {
                        send(in);
                        in="";
                    }
                }

            }catch(Exception e) { //Catches any errors
                //send("Server error");
                //e.printStackTrace();
                in="";
            }
            try{
                ticks++;
                if(System.nanoTime()-time1>=1000000000) {
                    //System.out.println(ticks);
                    time1=System.nanoTime();
                    ticks2=ticks;
                    ticks=0;
                }
            }catch(Exception e) {

            }
        }
        w2.stop(); //Closes down threads
        if(isServer) {
            c2.stop();
        }
        for(int i=0; i<r.size(); i++) {
            r2.get(i).stop();
            deleteThread(i);
        }

        try{
            server.close(); //Closes server socket
        }catch(Exception e) {

        }
        return;
    }
    
    public static void halt(boolean b) {
        isHalted=b;
        if(b) {
            System.out.println("Server activity halted");
        }
        else {
            System.out.println("Server activity not halted");
        }
    }
    
    public static boolean isHalted() {
        return isHalted;
    }

    public static ArrayList<Race> getRaces() {
        return races;
    }

    public static ArrayList<Class> getClasses() {
        return classes;
    }

    /**
     * Looks for race files and has another method read them
     */
    public static void makeRaces() {
        for(int i=0; i<100; i++) {
            if(new File("Races/"+Integer.toString(i)+".txt").canRead()==true) {
                readRace(i);
            }
        }
    }

    /**
     * Used to read a race file and add it to race list
     */
    public static void readRace(int i) {
        try{
            Scanner in = new Scanner(new File("Races/"+Integer.toString(i)+".txt"));
            races.add(new Race(in.next(),in.nextDouble(),in.nextDouble(),in.nextDouble(),in.nextDouble(),in.nextDouble(),in.nextDouble()));
        }catch(Exception e) {

        }
    }

    /**
     * Looks for class files and has another method read them
     */
    public static void makeClasses() {
        for(int i=0; i<100; i++) {
            if(new File("Classes/"+Integer.toString(i)+".txt").canRead()==true) {
                readClass(i);
            }
        }
    }

    /**
     * Used to read a class file and add it to class list
     */
    public static void readClass(int i) {
        try{
            Scanner in = new Scanner(new File("Classes/"+Integer.toString(i)+".txt"));
            classes.add(new Class(in.next(),in.nextDouble(),in.nextDouble(),in.nextDouble(),in.nextDouble(),in.nextDouble()));
        }catch(Exception e) {

        }
    }

    public static void print(String s) {
        if(isServer) {
            System.out.println(s);
        }
    }

    /**
     * Returns all of the threads, which contain info for the logged in players
     */
    public static ArrayList<readThread> getThreads() {
        return r;
    }

    /**
     * Used by readThread to get the command listener
     */
    public static CommandListener getListener() {
        return commandy;
    }

    /**
     * Returns the remainder of the string given after the first space
     */
    public static String subUpToSpace(String s) {
        String s2="";
        int x=0;
        if(s==null) {
            return "";
        }
        if(s.length()==0) {
            return s;
        }
        while(!s2.equals(" ")) {
            s2=s.substring(x,x+1);
            x++;
            if(x>s.length()-1) {
                return "";
            }
        }

        return s.substring(x,s.length()); 
    }

    /**
     * If b is true, this method returns s up to the first space.
     * If b is false, this method returns s up to and including the first space
     */
    public static String upToSpace(String s, boolean b) {
        String s2="";
        int x=0;
        if(s==null) {
            return "";
        }
        if(s.length()==0) {
            return s;
        }
        while(!s2.equals(" ")) {
            s2=s.substring(x,x+1);
            x++;
            if(x>s.length()-1) {
                return s;
            }
        }
        if(b==true) {
            return s.substring(0,x-1);
        }
        return s.substring(0,x);
    }

    /**
     * Kicks a player based on their number
     */
    public static void kick(int i) {
        try{
            r.get(i).getSocket().close();
            r.get(i).stopGoing();
        }catch(Exception e) {

        }
        System.out.println("Kicked ("+i+") "+r.get(i).getName());
    }

    /**
     * Used to remove variables of disconnected user
     */
    public static void deleteThread(int i) {
        r.remove(i);
        r2.remove(i);
        for(int w=0; w<r.size();w++) {
            r.get(w).setNumber(w);
        }
    }

    /**
     * Sends a message to every connected client in the format "Server: <message>"
     */
    public static void send(String message) {
        Socket client;
        print("Server: "+message);
        for(int i=0; i<r.size(); i++) {
            client=r.get(i).getSocket();
            try{
                new DataOutputStream(client.getOutputStream()).writeUTF("Server: "+message);
            }catch(IOException e) {

            }
        }
    }

    /**
     * Sends a message to every connected client in the format "<message>"
     */
    public static void sendAll(String message) {
        Socket client;
        print(message);
        for(int i=0; i<r.size(); i++) {
            client=r.get(i).getSocket();
            try{
                new DataOutputStream(client.getOutputStream()).writeUTF(message);
            }catch(IOException e) {

            }
        }
    }

    /**
     * Sends message to socket "to" in the format "Server: <message>"
     */
    public static void send(String message, Socket to) {
        try{
            new DataOutputStream(to.getOutputStream()).writeUTF("Server: "+message);
        }catch(IOException e) {

        }
    }

    /**
     * Used by readThread to send a message to another player
     */
    public static void sendTo(String message, int who) {
        try{
            new DataOutputStream(r.get(who).getSocket().getOutputStream()).writeUTF(message);
        }catch(IOException e) {

        }
    }

    /**
     * Sends message to socket "to" in the format "<message>"
     */
    public static void sendTo(String message, Socket to) {
        try{
            new DataOutputStream(to.getOutputStream()).writeUTF(message);
        }catch(IOException e) {

        }
    }

    /**
     * Used by readThread to send chat messages
     */
    public static void send(Socket from, String message, String m2) {
        Socket client;
        print(from.getRemoteSocketAddress()+"("+m2+")"+": "+message);
        for(int i=0; i<r.size(); i++) {
            client=r.get(i).getSocket();
            try{
                new DataOutputStream(client.getOutputStream()).writeUTF(from.getRemoteSocketAddress()+"("+m2+")"+": "+message);
            }catch(IOException e) {

            }
        }
    }

    /**
     * Deletes mail file of a given player name
     */
    public static void delMail(String name) {
        new File("Mail/"+name+".txt").delete();
    }

    /**
     * Gets the players mail (username given)
     */
    public static String getMail(String name) {
        if(new File("Mail/"+name+".txt").canRead()==false) {
            return "No mail!";
        }
        try{
            Scanner in = new Scanner(new File("Mail/"+name+".txt"));
            String ret="";
            String use="";
            while(in.hasNext()) {
                //ret+=in.next();
                use="";
                while(!use.equals("END") && in.hasNext()) {
                    use=in.next();
                    if(!use.equals("END")) {
                        ret+=use+" ";
                    }
                }
                //System.out.println(ret);
                ret+="\n";
            }
            in.close();
            return ret;
        }catch(Exception e) {
            e.printStackTrace();
            return "Exception when reading mail";
        }
    }

    /**
     * Sends mail to another player
     */
    public static String mail(String message) {
        String to = upToSpace(message,true);
        if(new File("Players/"+to+".txt").canRead()==false && !to.equals("Server")) { //If the receiving player does not exist
            return "Player not found!";
        }
        try{ //Writes mail to file
            FileWriter outf=new FileWriter(new File("Mail/"+to+".txt"),true);
            outf.write(message.substring(to.length()+1)+" END ");
            outf.flush();
            outf.close();
            System.out.println("Message: "+message);
            return "Mail sent!";
        }catch(Exception e) {
            e.printStackTrace();
            return "Error when sending mail (Try again)";
        }
        //return to;
    }

    public static void save(readThread user, String name, String pass) {
        try{
            FileWriter outf = new FileWriter(new File("Players/"+name+".txt"));
            outf.write(pass+" ");
            outf.write("Class: "+user.getUser().getType().getName()+" ");
            outf.write("Race: "+user.getUser().getRace().getName()+ " ");
            outf.write("XP: "+user.getUser().getXP()+" ");
            outf.write("Gold: "+user.getGold());
            outf.flush();
            outf.close();
        }catch(Exception e) {
            System.out.println("Could not save "+name);
            return;
        }
    }

    public static void savef(readThread user, String name, String pass) {
        try{
            FileWriter outf = new FileWriter(new File("Players/"+name+".txt"));
            outf.write(pass+" ");
            outf.flush();
            outf.close();
        }catch(Exception e) {
            System.out.println("Could not save "+name);
            return;
        }
    }

    public static void read(readThread user) {
        try{
            Scanner inf = new Scanner(new File("Players/"+user.getName()+".txt"));
            inf.next();
            String in="";
            Class forUser=null;
            Race forUser2=null;
            int xp=0;
            while(inf.hasNext()) {
                in=inf.next();
                if(in.equals("Class:")) {
                    in=inf.next();
                    for(int i=0; i<classes.size(); i++) {
                        if(classes.get(i).getName().equals(in)) {
                            forUser=classes.get(i);
                        }
                    }
                }
                if(in.equals("Race:")) {
                    in=inf.next();
                    for(int i=0; i<races.size(); i++) {
                        if(races.get(i).getName().equals(in)) {
                            forUser2=races.get(i);
                        }
                    }
                }
                if(in.equals("XP:")) {
                    try{
                        xp=Integer.parseInt(inf.next());
                    }catch(Exception e) {

                    }
                }
                if(in.equals("Gold:")) {
                    user.setGold(Integer.parseInt(inf.next()));
                }
            }
            user.setUser(new Character(user.getName(),forUser, forUser2, 1.1));
            user.getUser().setXp(xp);
        }catch(Exception e) {
            System.out.println("Could not read "+user.getName());
            e.printStackTrace();
            return;
        }
    }

    /**
     * Registers a player
     * @param u Username
     * @param p Password
     */
    public static String authenr(String u, String p, readThread rt) {
        try{
            String use="";
            boolean isValid;
            char validChar;
            //Makes sure that username and password have decent lengths,
            //And that they have letters and numbers ONLY
            if(u.length()<3) {
                return "Username too short!";
            }
            if(u.length()>10) {
                return "Username too long!";
            }
            if(p.length()<3) {
                return "Password too short!";
            }
            if(p.length()>10) {
                return "Password too long!";
            }
            for(int i=0; i<u.length(); i++) {
                isValid=false;
                validChar='a';
                for(int w=0; w<26; w++) {
                    if(u.substring(i,i+1).equals(String.valueOf(validChar))) {
                        isValid=true;
                    }
                    validChar++;
                }

                validChar='A';
                for(int w=0; w<26; w++) {
                    if(u.substring(i,i+1).equals(String.valueOf(validChar))) {
                        isValid=true;
                    }
                    validChar++;
                }

                validChar='0';
                for(int w=0; w<10; w++) {
                    if(u.substring(i,i+1).equals(String.valueOf(validChar))) {
                        isValid=true;
                    }
                    validChar++;
                }
                if(isValid==false) {
                    return "Invalid Username. Please use Letters and numbers only";
                }
            }

            for(int i=0; i<p.length(); i++) {
                isValid=false;
                validChar='a';
                for(int w=0; w<26; w++) {
                    if(p.substring(i,i+1).equals(String.valueOf(validChar))) {
                        isValid=true;
                    }
                    validChar++;
                }

                validChar='A';
                for(int w=0; w<26; w++) {
                    if(p.substring(i,i+1).equals(String.valueOf(validChar))) {
                        isValid=true;
                    }
                    validChar++;
                }

                validChar='0';
                for(int w=0; w<10; w++) {
                    if(p.substring(i,i+1).equals(String.valueOf(validChar))) {
                        isValid=true;
                    }
                    validChar++;
                }
                if(isValid==false) {
                    return "Invalid Password. Please use Letters and numbers only";
                }
            }

            if(new File("Players/"+u+".txt").canRead()==true) { //If username is already used
                return "Username already taken";
            }
            if(new File("Players/"+u+".txt").canRead()==false) { //Creates new player file
                //FileWriter outf = new FileWriter(new File("Players/"+u+".txt"));
                savef(rt,u,p);
                //outf.flush();
                //outf.close();
                System.out.println("Registered "+u+" with password "+p);
                return "Register";
            }
        }catch(Exception e) {
            e.printStackTrace();
            return "Error when finding file";
        }
        return "";
    }

    /**
     * Logs a player in
     * @param u Username
     * @param p Password
     * @param to Used to give player data to thread
     */
    public static String authen(String u, String p, readThread to) {
        try{
            String use="";
            for(int i=0; i<r.size(); i++) { //Checks to see if the account is already logged into
                if(r.get(i).getName().equals(u)) {
                    return "That player is already logged in!";
                }
            }
            if(new File("Players/"+u+".txt").canRead()==true) {
                Scanner in = new Scanner(new File("Players/"+u+".txt"));
                if(in.next().equals(p)) {
                    in.close();
                    return "Logged in";
                }
                else { //If player used wrong password
                    System.out.println(u+" used the wrong password ( "+p+" )");
                    return "Wrong password";
                }
            }
            if(new File("Players/"+u+".txt").canRead()==false) { //If username is not set
                return "That username is not set";
            }
        }catch(Exception e) {
            e.printStackTrace();
            return "Error when finding file";
        }
        return "";
    }

    /**
     * Used by inThread to receive input from server terminal
     */
    public static void setin(String m) {
        in=m;
    }

    /**
     * Returns a socket from a player number
     */
    public static Socket getSocket(int i) {
        return r.get(i).getSocket();
    }

    /**
     * Returns a name from a player number
     */
    public static String getName(int i) {
        return r.get(i).getName();
    }

    /**
     * Allows other classes to get the map size
     */
    public static int getMapSize() {
        return mapSize;
    }
}