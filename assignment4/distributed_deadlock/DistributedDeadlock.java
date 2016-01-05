import teachnet.algorithm.BasicAlgorithm;
import java.util.Random;
import java.lang.Integer;
import java.lang.Math;
import java.awt.Color;

/**
* Group 11
* Dan Drewes, Manuela Hopp, John Mercouris, Malte Siemers
* This class implements the Echo algorithm
*/
public class DistributedDeadlock extends BasicAlgorithm{

    ////////////////////////////////////////////////////////////////////////////////
    // Variable Declarations
    ////////////////////////////////////////////////////////////////////////////////
    int id; // ID of the node
    String caption = ""; // String next to node
    Random generator = new Random(); // Random number generator
    int intervalDelayRange = 10; // Maximum delay time until next node execution
    Color color; // Color of the node

    /**
     * Method sets captions for all nodes to their ID, sets the weighted object up
     * for node one
     */
    public void setup(java.util.Map<String, Object> config){
	id = (Integer) config.get("node.id");
	caption = "" + id;
    }

    /**
     * The program loops the timeout method to control node behavior
     */
    public void initiate() {
	// Initialize independent node loops
	setTimeout(0, new Object());
    }
    
    /**
     * Handle timeouts - Schedule next interruption at random interval
     */
    public void timeout(Object inputObject) {
	setTimeout(generator.nextInt(intervalDelayRange), new Object());
	run();
    }

    /**
     * Node action, controlled by probability, run method gets called by setTimeout on a loop
     */ 
    public void run() { 
        System.out.println("Entered Execution Loop.");
    }
    /**
    * Act on recieved messages, based on what TYPE of message they are
    */ 
    public void receive(int interf, Object message){
    }
}
