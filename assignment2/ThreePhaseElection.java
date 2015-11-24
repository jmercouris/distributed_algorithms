import teachnet.algorithm.BasicAlgorithm;
import java.util.Random;

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
    int nodeID;
    int nodeValue;
    int maximumValue;
    String caption;
 
    /**
     * setup function
     */
    public void setup(java.util.Map<String, Object> config){
	nodeID = (Integer) config.get("node.id");
	caption = "" + nodeID;
	nodeValue = r.nextInt(100);
	maximumValue = nodeValue;
    }

    /**
     * initiates the algorithm;
     * sends an explorer message to all neighbors and sets informed and initiator to true
     */
    public void initiate(){
	sendToAll(new NetworkMessage());
    }

    /**
     * sends out explorer messages to all neighbors if not yet informed
     */
    public void receive(int inputInterface, Object message) {

	// Check to see if received message has greater value than our own maximum
	NetworkMessage inputMessage = (NetworkMessage) message;
	if (inputMessage.value > maximumValue) {
	    maximumValue = inputMessage.value;
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
	    
	    // If NOT INITIAL Receipt
	    if (initialReceipt) {
		//sendToAll("" + maximumValue);
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
}

