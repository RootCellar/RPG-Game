import java.util.ArrayList;
/**
 * This class listens for commands, and sends the info to the command mods
 */
public class CommandListener implements Command
{
    private static ArrayList<Command> to;
    public CommandListener() {
        to = new ArrayList<Command>();
    }
    
    public void add(Command add) {
        to.add(add);
    }
    
    public void command(String m, readThread from) {
        for(int i=0; i<to.size(); i++) {
            to.get(i).command(m,from);
        }
    }
}