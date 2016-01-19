import teachnet.algorithm.BasicAlgorithm;
import java.awt.Color;
import java.util.Random;

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
    public static final int HEARTBEAT_INTERVAL = 5;

    // Maximum Delay between levels
    int MIN_LEVEL_DELAY = 1;
    int MAX_LEVEL_DELAY = 2;

    // Caption Next to Node
    String caption = "<0>→<0, 0, 0>";
    // Color of the Node - Default Black
    Color defaultColor = Color.BLACK;
    Color rootColor = Color.GREEN;
    Color faultyColor = Color.RED;
    Color color = defaultColor;
    int nodeStatus = STATUS_DEFAULT;
    boolean heartbeatReceived;

    // Random number generator, used for arbitrary failure
    Random generator = new Random();
    
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

	// Initialize Timeout Loop
	setTimeout(0, new Object());
    }

    /**
     * Handle timeouts - Schedule next interruption at interval
     */
    public void timeout(Object inputObject) {
	// Randomly set Status to Failure
	if (generator.nextInt(100) > 95) {
	    nodeStatus = STATUS_FAULTY;
	}
	// Randomly set Status to Valid
	if (generator.nextInt(100) > 95 && nodeStatus != STATUS_ROOT){
	    nodeStatus = STATUS_DEFAULT;
	}
	// If we are the root node we must set our heartbeat interval
	if (nodeStatus == STATUS_ROOT) {
	    setTimeout(HEARTBEAT_INTERVAL, new Object());
	}
	// If we are NOT the root node, we set our timeout based on our
	// level in the tree and the maximum message delay between tree levels
	if (nodeStatus != STATUS_ROOT) {
	    setTimeout((MAX_LEVEL_DELAY * treeLevel) + HEARTBEAT_INTERVAL, new Object());
	}
	run();
	updateColorStatus();
    }

    /**
     * Node action, controlled by probability, run method gets called by setTimeout on a loop
     */ 
    public void run() {
	////////////////////////////////////////
	// If we are the root node we must send out a heartbeat
	if (nodeStatus == STATUS_ROOT) {
	    NetworkMessage message = new NetworkMessage();
	    message.setType(NetworkMessage.HEARTBEAT);
	    message.senderNode = id;
	    message.rootNode = id;
	    sendAll(message);
	}

	// We've encountered a timeout as not Root, this means
	// messages have not reached us in enough time, the network
	// is possibly broken!!!
	if (nodeStatus != STATUS_ROOT && heartbeatReceived == false) {
	    System.out.println("Timeout " + id);
	    NetworkMessage message = new NetworkMessage();
	    message.setType(NetworkMessage.ELECTION);
	    message.senderNode = id;
	    message.rootNode = id;
	    message.treeLevel = 0;
	    sendAll(message);

	} else {
	    // Reset heartbeatRecieved Flag for next iteration
	    heartbeatReceived = false;
	}
    }

    /**
     * Receive Messages
     */
    public void receive(int sendingInterface, Object message) {
	// If we are "Faulty" Break, we don't react to anything
	if (nodeStatus == STATUS_FAULTY) {
	    return;
	}
	// Cast Message to Network Message Object
	NetworkMessage inputMessage = (NetworkMessage) message;
	switch (inputMessage.getType()) {
	////////////////////////////////////////
	// Received Probe
	case NetworkMessage.PROBE:
	    break;
	////////////////////////////////////////
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
	    if (inputMessage.rootNode == rootNode) {}
	    // If we are lower than the Node advertised, Ignore
	    if (inputMessage.rootNode > rootNode) {}

	    // If the Declared rootNode ID is not equal to our own ID, we aren't Root
	    if (rootNode != id) {
		nodeStatus = STATUS_DEFAULT;
	    }
	    // If the rootNode is equal to our own ID, we are Root
	    if (rootNode == id) {
		nodeStatus = STATUS_ROOT;
	    }
	    break;
	////////////////////////////////////////
	// Case Heartbeat
	case NetworkMessage.HEARTBEAT:
	    // Only act upon messages from your parent
	    if (inputMessage.senderNode == parentNode) {
		NetworkMessage tmp0message = new NetworkMessage();
		tmp0message.setType(NetworkMessage.HEARTBEAT);
		tmp0message.senderNode = id;
		tmp0message.rootNode = rootNode;

		sendAllExcept(tmp0message, sendingInterface);
		heartbeatReceived = true;
	    }
	    break;
	////////////////////////////////////////
	// Default Case
	default:
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
