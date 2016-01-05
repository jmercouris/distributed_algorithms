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
            // Processing Complete Reinitialize variables
            System.out.println("Non blocking, all elements present");
            blocked = false;
            requiredResources = new ArrayList();
            
        } else {
            // Initiate Diffusion Process to pre-empt and break potential deadlock
            System.out.println("Blocking, missing elements");
            blocked = true;
            updateRequiredElements(ownedResources, requiredResources);
            sendOne(new NetworkMessage(NetworkMessage.PROBE, id, id, requiredResources));
        }
    }
    /**
     * Act on recieved messages, based on what TYPE of message they are
     */ 
    public void receive(int sendingInterface, Object message) {
	// Cast Message to Network Message Object
	NetworkMessage inputMessage = (NetworkMessage) message;
	// Switch type of Message
	switch (inputMessage.getType()) {
        // Diffusion Probe
        case NetworkMessage.PROBE:
            System.out.println("Received Probe");
            // If we are the original sender, and we are blocked then we know we have deadlock
            if (id == inputMessage.getRootSender() && blocked) {
                System.out.println("Blocking Detected");
                NetworkMessage tmpMessage = new NetworkMessage();
                tmpMessage.setType(NetworkMessage.PREEMPTION);
                tmpMessage.setRootSender(id);
                tmpMessage.setExclusiveResources(requiredResources);
                sendOne(tmpMessage);
            }
            // If we are blocked and the message has not made a round trip forward
            else if (blocked) {
                updateRequiredElements(ownedResources, requiredResources);
                forwardMessage(new NetworkMessage(NetworkMessage.PROBE, inputMessage.getRootSender(), id, requiredResources), sendingInterface);
                System.out.println(new NetworkMessage(NetworkMessage.PROBE, inputMessage.getRootSender(), id, requiredResources).toString());
            }
            break;
        // Preemption Message to break deadlock
        case NetworkMessage.PREEMPTION:
        // If we are not the original requester, Preempt and release resources to break deadlock
        if (id != inputMessage.getRootSender()) {
            ownedResources.removeAll(inputMessage.getExclusiveResources());
        } 
        // If we are the original sender, then we know that the message has made a round trip
        // Meaning all necessary resources have been released, and we can resume execution
        else {
            ownedResources.add(requiredResources);
        }
            break;
	}
    }
    
    /**
    * Print the status of a Node including listing all of its elements needed and owned
    */
    public void printStatus() {
        System.out.println("Node: " + id);
        System.out.println("\tRequired: " + arrayListContents(requiredResources));
        System.out.println("\tOwned: " + arrayListContents(ownedResources));
    }
    
    /**
     * Generate a string that shows all of the contents of the arraylist
     */
    public String arrayListContents(List listA) {
        String tmp = "";
        for (Object element : listA) {
            tmp += element.toString() + ",";
        }
        return tmp;
    }
    
    /**
     * Generate a list of the remaining elements that need to be captured, function has a side effect
     * of settings requiredElements to the remaining needed elements
     */
    public void updateRequiredElements(List inputOwnedElements, List inputRequiredElements) {
        // Remove all elements in firstList from secondList
        inputRequiredElements.removeAll(inputOwnedElements);
    }
    
    /**
     * Helper method to compare arraylists
     */
    public  boolean equalLists(List listA, List listB){     
        return (listA.containsAll(listB) && listB.containsAll(listA));
    }
    
    ////////////////////////////////////////////////////////////////////////////////
    // Helper Functions
    ////////////////////////////////////////////////////////////////////////////////
    // Convenience Method to send a message to all neighbors
    public void sendAll(NetworkMessage inputMessage) { 
	for (int i = 0; i < checkInterfaces(); i++) {
	    send(i, inputMessage);
	}
    }
    // Convenience Method to send a message to all neighbors except one
    public void sendAllExcept(NetworkMessage inputMessage, int exceptInterface) { 
	for (int i = 0; i < checkInterfaces(); i++) {
	    if (i != exceptInterface) {
		send(i, inputMessage);
	    }
	}
    }
    // Send to one node
    public void sendOne(NetworkMessage inputMessage) {
	send(0, inputMessage);
    }

    // Will send to the OTHER interface, thus forwarding a message
    public void forwardMessage(NetworkMessage inputMessage, int inputSendingInterface) {
	for (int i = 0; i < checkInterfaces(); i++) {
	    if (i != inputSendingInterface) {
		send(i, inputMessage);
	    }
	}
    }

    // End class
    
}
