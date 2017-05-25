import java.util.ArrayList;
/**
 * This thread is used to reload tank guns and make sure that players don't leave the area boundaries
 */
public class checker implements Runnable
{
    int mapSize=100;
    public void run() {
        while(true) {
            
            try{
                Thread.sleep(100);
            }catch(InterruptedException e) {
                
            }
        }
    }
    
    public void setMapSize(int x) {
        mapSize=x;
    }
    
    public int getMapSize() {
        return mapSize;
    }
}