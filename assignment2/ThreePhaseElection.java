import teachnet.algorithm.BasicAlgorithm;
import java.util.Random;
import java.util.LinkedList;

/**
 * Group 11
 * Dan Drewes, Manuela Hopp, John Mercouris, Malte Siemers
 * This class implements the ThreePhaseElection algorithm
 */
public class ThreePhaseElection extends BasicAlgorithm {

    // Variable Definitions
    Random r = new Random();

    /* Phase Identifiers */
    int PHASE_EXPLOSION   = 0;
    int PHASE_CONTRACTION = 1;
    int PHASE_INFORMATION = 2;
	
    // Node Specific Variables
    boolean initialReceipt = false;
    boolean[] messageReceivedFromInterface;
    int messagesReceived;
    String caption;
    int nodeID;
    int nodeValue;
    int maximumValue;
    boolean maximumSent = false;

 
    /**
     * setup function
     */
    public void setup(java.util.Map<String, Object> config){
	nodeID = (Integer) config.get("node.id");
	caption = "" + nodeID;
	nodeValue = r.nextInt(100);
	maximumValue = nodeValue;
	messageReceivedFromInterface = new boolean[checkInterfaces()];
    }

    /**
     * initiates the algorithm;
     * sends an explorer message to all neighbors and sets informed and initiator to true
     */
    public void initiate(){
	sendToAll(new NetworkMessage());
	initialReceipt = true;
    }

    /**
     * sends out explorer messages to all neighbors if not yet informed
     */
    public void receive(int inputInterface, Object message) {

	// Check to see if received message has greater value than our own maximum
	NetworkMessage inputMessage = (NetworkMessage) message;

	// Keep Track of Largest value seen
	if (inputMessage.value > maximumValue) {
	    maximumValue = inputMessage.value;
	}
	
	// Flag Indicating message value has been set
	if (inputMessage.value != -1) {
	    messagesReceived++;
	    // Mark Interface that sent it
	    messageReceivedFromInterface[inputInterface] = true;
	}

	// Final Step
	if (maximumSent == true) {
	    System.out.println("Maximum value is:" + maximumValue);
	}

	// Check to see if all child messages have been received and NOT a leaf
	if (messagesReceived == checkInterfaces() - 1 && checkInterfaces() != 1) {
	    // Send to Parent Interface
	    sendToAll(new NetworkMessage(maximumValue), messageReceivedFromInterface);
	    maximumSent = true;
	}

	// If Node is a Leaf
	if (checkInterfaces() == 1) {
	    sendToAll(new NetworkMessage(nodeValue));
	}
	
	// If Node is NOT a leaf
	if (checkInterfaces() > 1) {
	    // If INITIAL Receipt
	    if (!initialReceipt) {
		initialReceipt = true;
		sendToAll(new NetworkMessage(), inputInterface);
	    }
	}
    }


    ////////////////////////////////////////////////////////////////////////////////
    // Helper Method to send to all Connecting Nodes
    ////////////////////////////////////////////////////////////////////////////////
    public void sendToAll(Object message){
	for (int i = 0; i < checkInterfaces(); i++) {
	    send(i, message);
	}
    }
    public void sendToAll(Object message, int exceptInterface){
	for (int i = 0; i < checkInterfaces(); i++) {
	    if (i != exceptInterface) {
		send(i, message);
	    }
	}
    }
    public void sendToAll(Object message, boolean[] exceptInterfaces){
	for (int i = 0; i < checkInterfaces(); i++) {
	    if (!exceptInterfaces[i]) {
		send(i, message);
	    }
	}
    }
}

