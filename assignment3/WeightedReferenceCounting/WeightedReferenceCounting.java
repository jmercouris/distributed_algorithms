import teachnet.algorithm.BasicAlgorithm;
import java.util.Random;


/**
 * Group 11
 * Dan Drewes, Manuela Hopp, John Mercouris, Malte Siemers
 */
public class WeightedReferenceCounting extends BasicAlgorithm{
    // Variable Declarations
    int id; // ID of the node
    boolean informed = false; // Recieved explorer message
    String caption = ""; // String next to node
    Random generator = new Random(); // Random number generator, used for control
    int intervalDelayRange = 10;

    ////////////////////////////////////////////////////////////////////////////////
    // Setup Function
    ////////////////////////////////////////////////////////////////////////////////
    public void setup(java.util.Map<String, Object> config){
	id = (Integer) config.get("node.id");
	caption = "" + id;
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
    // Handle timeouts
    ////////////////////////////////////////////////////////////////////////////////
    public void timeout(Object inputObject) {
	setTimeout(generator.nextInt(intervalDelayRange), new Object());
	run();
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Run Method
    ////////////////////////////////////////////////////////////////////////////////
    public void run() { 
	sendAll(new NetworkMessage("Hey!"));
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
    // Convenience Method to send a message to all neighbors
    public void sendAllExcept(NetworkMessage inputMessage, int exceptInterface) { 
	for (int i = 0; i < checkInterfaces(); i++) {
	    if (i != exceptInterface) {
		send(i, inputMessage);
	    }
	}
    }

    ////////////////////////////////////////////////////////////////////////////////
    // End Class
    ////////////////////////////////////////////////////////////////////////////////
}
