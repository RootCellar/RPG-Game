/**
 * Just an example mod class
 */
public class samplecommand implements Command
{
    /**
     * The constructor allows you to initialize your command mod.
     * You can do nothing, print a message like "samplemod started!",
     * or anything else needed to initialize the mod.
     */
    public samplecommand() {
        System.out.println("Sample mod initialized");
    }

    /**
     * Called when a player executes a command
     * This is where you add commands
     */
    public void command(String m, readThread from) {
        /**
         * Sample command
         * When the player uses "/sayhi", it tells the player that they said hi,
         * and makes the player send "Hello! I am testing out the /sayhi command!"
         */
        if(m.equals("/sayhi")) {
            Server.sendTo("You said hi!",from.getSocket());
            Server.send(from.getSocket(), "Hello! I am testing out the /sayhi command!", from.getName());
        }
        /**
         * There can be more than one command!
         * This could be a huge list of commands!
         */
        
        /**
         * This command shows the player list to the player upon using /list
         */
        if(m.equals("/list")) {
            for(int i=0; i<Server.getThreads().size(); i++) {
                Server.sendTo(i+". "+Server.getThreads().get(i).getName(),from.getSocket());
            }
        }
        
        /**
         * /emote command!
         * Added just for fun
         */
        if(Server.upToSpace(m,true).equals("/emote")) {
            Server.sendAll(from.getName()+" "+Server.subUpToSpace(m));
        }
        
        /**
         * You can add your commands to /help like this!
         * Now, players can see the /list and /sayhi commands.
         * Without this, the players won't know about these commands unless told about them.
         */
        if(m.equals("/help")) {
            Server.sendTo("Supplied by the SampleCommand mod",from.getSocket());
            Server.sendTo("/sayhi - Says hi!",from.getSocket());
            Server.sendTo("/list - lists players",from.getSocket());
            Server.sendTo("/emote <message> - Sends a message in the format '<yourname> <message>'",from.getSocket());
        }
    }
}