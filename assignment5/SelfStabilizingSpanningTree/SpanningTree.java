import teachnet.algorithm.BasicAlgorithm;
import java.awt.Color;

/**
 * Group 11
 * Dan Drewes, Manuela Hopp, John Mercouris, Malte Siemers
 * Election based on lowest ID
 */
public class SpanningTree extends BasicAlgorithm{

    // Static Status
    public static final int STATUS_DEFAULT = 0;
    public static final int STATUS_ROOT = 1;
    public static final int STATUS_FAULTY = 2;

    // Caption Next to Node
    String caption = "<0>→<0, 0, 0>";
    // Color of the Node - Default Black
    Color defaultColor = Color.BLACK;
    Color rootColor = Color.GREEN;
    Color faultyColor = Color.RED;
    Color color = defaultColor;
    int nodeStatus = STATUS_DEFAULT;
        
    // Relational Information
    int id = 0;
    int rootNode = 0;
    int treeLevel = 0;
    int parentNode = 0;

    /**
     * Setup the algorithm
     */
    public void setup(java.util.Map<String, Object> config)
    {
	// Setup our IDs
	id = (Integer) config.get("node.id");
	// Default to ourselves as root
	rootNode = id;
	nodeStatus = STATUS_ROOT;
	updateCaption();
	updateColorStatus();
    }
    
    /**
     * initiates the algorithm
     */
    public void initiate() {
	NetworkMessage message = new NetworkMessage();
	message.setType(NetworkMessage.ELECTION);
	message.senderNode = id;
	message.rootNode = id;
	message.treeLevel = 0;
	sendAll(message);
    }

    /**
     * Receive Messages
     */
    public void receive(int sendingInterface, Object message) {
	// Cast Message to Network Message Object
	NetworkMessage inputMessage = (NetworkMessage) message;
	switch (inputMessage.getType()) {
	// Received Probe
	case NetworkMessage.PROBE:
	    break;
	// Attempting to Elect
	case NetworkMessage.ELECTION:
	    // If the New Root Node ID lower than our current, REPLACE
	    if (inputMessage.rootNode < rootNode) {
		rootNode = inputMessage.rootNode;
		treeLevel = inputMessage.treeLevel + 1;
		parentNode = inputMessage.senderNode;

		// Forward Election to Future Nodes
		NetworkMessage tmpMessage = new NetworkMessage();
		tmpMessage.setType(NetworkMessage.ELECTION);
		tmpMessage.senderNode = id;
		tmpMessage.rootNode = rootNode;
		tmpMessage.treeLevel = treeLevel;
		sendAllExcept(tmpMessage, sendingInterface);
	    }
	    // If we have the same Root Node ID then we agree
	    if (inputMessage.rootNode == rootNode) {
		
	    }
	    // If we are lower than the Node advertised, Ignore
	    if (inputMessage.rootNode > rootNode) {
		
	    }
	    if (rootNode != id) {
		nodeStatus = STATUS_DEFAULT;
	    }

	    break;
	}

	// Update Caption Everytime we Receive a Message Pending Changes
	updateCaption();
	updateColorStatus();
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Helper Functions Node Specific Behavior
    ////////////////////////////////////////////////////////////////////////////////
    // Updates the caption of the node based upon its' current state
    public void updateCaption() {
	caption = "<" + id + ">→" +
	    "<" +  rootNode + ", " + treeLevel + ", " + parentNode + ">";
    }
    // Updates the color of the node based upon its' current state
    public void updateColorStatus() {
	switch (nodeStatus) {
	case STATUS_DEFAULT:
	    color = defaultColor;
	    break;
	case STATUS_ROOT:
	    color = rootColor;
	    break;
	case STATUS_FAULTY:
	    color = faultyColor;
	    break;
	default:
	    color = defaultColor;
	    break;
	}
    }
    
    ////////////////////////////////////////////////////////////////////////////////
    // Helper Functions Message Forwarding
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
