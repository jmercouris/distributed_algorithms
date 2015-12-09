import teachnet.algorithm.BasicAlgorithm;


/**
 * Group 11
 * Dan Drewes, Manuela Hopp, John Mercouris, Malte Siemers
 */
public class WeightedReferenceCounting extends BasicAlgorithm{
    // Variable Declarations
    int id; // ID of the node
    boolean informed = false; // Recieved explorer message
    String caption = ""; // String next to node

    // Setup Function
    public void setup(java.util.Map<String, Object> config){
	id = (Integer) config.get("node.id");
	caption = "" + id;

    }

    // Initiate the Algorithm by awakening all nodes
    public void initiate() {
	sendAll(new NetworkMessage(NetworkMessage.EXPLORE));

	// double delayTime = Math.random();
	setTimeout(2, new Object());
	
    }

    // Handle timeouts
    public void timeout(Object inputObject) {
	System.out.println("Timeout callback!");
	sendAll(new NetworkMessage("Hey!"));
	if (!informed) {
	    setTimeout(2, new Object());
	}
    }

    // Act on Recieved Messages
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
