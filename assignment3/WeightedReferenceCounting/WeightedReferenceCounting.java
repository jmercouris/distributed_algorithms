import teachnet.algorithm.BasicAlgorithm;
import java.util.Random;
import java.lang.Integer;


/**
 * Group 11
 * Dan Drewes, Manuela Hopp, John Mercouris, Malte Siemers
 */
public class WeightedReferenceCounting extends BasicAlgorithm {
    ////////////////////////////////////////////////////////////////////////////////
    // Variable Declarations
    ////////////////////////////////////////////////////////////////////////////////
    int id; // ID of the node
    boolean informed = false; // Recieved explorer message
    String caption = ""; // String next to node
    Random generator = new Random(); // Random number generator
    int intervalDelayRange = 10; // Maximum delay time until next node execution
    WeightedObjectReference weightedObjectReference; // Reference to an object
    WeightedObject weightedObject; // A weighted object

    ////////////////////////////////////////////////////////////////////////////////
    // Setup Function
    ////////////////////////////////////////////////////////////////////////////////
    public void setup(java.util.Map<String, Object> config){
	id = (Integer) config.get("node.id");
	caption = "" + id;

	// Node 0 creates an object that will be accessed remotely
	if (id == 0) {
	    weightedObject = new WeightedObject(32, 0);
	}
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Initiate the Algorithm by awakening all nodes
    ////////////////////////////////////////////////////////////////////////////////
    public void initiate() {
	sendAll(new NetworkMessage(NetworkMessage.EXPLORE));
	// Initialize independent node loops
	setTimeout(0, new Object());
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Handle timeouts - Schedule next interruption at random interval
    ////////////////////////////////////////////////////////////////////////////////
    public void timeout(Object inputObject) {
	setTimeout(generator.nextInt(intervalDelayRange), new Object());
	run();
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Run Method
    ////////////////////////////////////////////////////////////////////////////////
    public void run() { 
	// Probability of making a request, 70%
	if (generator.nextInt(100) > 70) {
	    sendOne(new NetworkMessage(NetworkMessage.REQUEST_REFERENCE, new Integer(0), "Request"));
	}
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Act on Recieved Messages
    ////////////////////////////////////////////////////////////////////////////////
    public void receive(int sendingInterface, Object message) {
	// Cast Message to Network Message Object
	NetworkMessage inputMessage = (NetworkMessage) message;
	switch (inputMessage.getType()) {
	case NetworkMessage.EXPLORE:
	    if (!informed) {
		informed = true;
		sendAllExcept(inputMessage, sendingInterface);
	    }
	    break;
	case NetworkMessage.REQUEST_REFERENCE:
	    System.out.println(" REQUEST REFERENCE");
	    // If we have a weighted object, check we have the right one
	    if (weightedObject != null) {
		// We have the wrong weighted object, we should forward
		if (weightedObject.getID() != ((Integer)inputMessage.getData()).intValue()) {
		    sendAllExcept(inputMessage, sendingInterface);
		}
		// We have the corrected weighted object, we must return a reference
		else {

		}
	    } 
	    // We don't have a weighted object, we should forward
	    if (weightedObject == null) {
		sendAllExcept(inputMessage, sendingInterface);
	    }
	    break;
	default:
	    break;
	}
    }

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

    ////////////////////////////////////////////////////////////////////////////////
    // End Class
    ////////////////////////////////////////////////////////////////////////////////
}
