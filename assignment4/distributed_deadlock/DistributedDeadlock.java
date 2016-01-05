import teachnet.algorithm.BasicAlgorithm;
import java.util.Random;
import java.lang.Integer;
import java.util.*;
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
    boolean blocked = false;
    ArrayList requiredResources = new ArrayList();
    ArrayList ownedResources = new ArrayList();

    /**
     * Method sets captions for all nodes to their ID, sets the weighted object up
     * for node one
     */
    public void setup(java.util.Map<String, Object> config){
	id = (Integer) config.get("node.id");
	caption = "" + id;
    
    // Create a contrived example by prepopulating required, and owned resources
    // Add a resource to the resource pool
    ownedResources.add(new ExclusiveResource(id));
    requiredResources.add(new ExclusiveResource(0));
    requiredResources.add(new ExclusiveResource(1));
    // End Contrived Example population
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
        printStatus();
        // Compare ownedResources to requiredResources, if the two lists are equal,
        // resume execution
        if (equalLists(ownedResources, requiredResources)) {
            System.out.println("Non blocking, all elements present");
            
        } else {
            // Initiate Diffusion Process to pre-empt and break potential deadlock
            System.out.println("Blocking, missing elements");
        }
    }
    /**
    * Act on recieved messages, based on what TYPE of message they are
    */ 
    public void receive(int interf, Object message){
    }
    
    public void printStatus() {
        System.out.println("Node: " + id);
        System.out.println("\tRequired: " + arrayListContents(requiredResources));
        System.out.println("\tOwned: " + arrayListContents(ownedResources));
    }
    
    /**
    * Generate a stirng that shows all of the contents of the arraylist
    */
    public String arrayListContents(List listA) {
        String tmp = "";
        for (Object element : listA) {
            tmp += element.toString();
        }
        return tmp;
    }
    
    /**
    * Helper method to compare arraylists
    */
    public  boolean equalLists(List listA, List listB){     
        return (listA.containsAll(listB) && listB.containsAll(listA));
    }
    
}
