import teachnet.algorithm.BasicAlgorithm;
import java.util.Random;
import java.lang.Integer;
import java.lang.Math;
import java.awt.Color;

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
    boolean requestedReference = false; // Node has requested a reference
    Color color;

    int requestProbability = 30; // Probability of making a request
    int discardProbability = 30; // Probability of discarding a reference

    /**
     * Method sets captions for all nodes to their ID, sets the weighted object up
     * for node one
     */
    public void setup(java.util.Map<String, Object> config){
	id = (Integer) config.get("node.id");
	caption = "" + id;

	// Node 0 creates an object that will be accessed remotely
	if (id == 0) {
	    weightedObject = new WeightedObject(Math.pow(2, 10), 0);
	}
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
     * Node action, controlled by probability
     */ 
    public void run() { 
	
	////////////////////////////////////////
	// Probability of making a request 70%
	if (generator.nextInt(100) > (100-requestProbability) 
	    && requestedReference == false && weightedObject == null) {
	    sendOne(new NetworkMessage(NetworkMessage.REQUEST_REFERENCE, new Integer(0), 
				       "Reference Request: " + id, id, -1));
	    requestedReference = true;
	    System.out.println("Node: " + id + " Request");
	}
	
	////////////////////////////////////////
	// If we already have a reference, Probability of discarding a reference 70%
	if (generator.nextInt(100) > (100-discardProbability)
	    && weightedObjectReference != null 
	    && weightedObject == null) {
	    System.out.println("Node: " + id + " Discard");
	    sendOne(new NetworkMessage(NetworkMessage.DISCARD_REFERENCE, weightedObjectReference, 
				       "Reference Discard: " + id, id, -1));
	    weightedObjectReference = null;
	}
    }

    /**
     * Act on recieved messages, based on what TYPE of message they are
     */ 
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

	    ////////////////////////////////////////
	    // If we have a weighted object, check we have the right one
	    if (weightedObject != null) {
		// We have the wrong weighted object, we should forward
		if (weightedObject.getID() != ((Integer)inputMessage.getData()).intValue()) {
		    sendAllExcept(inputMessage, sendingInterface);
		}
		// We have the corrected weighted object, we must return a reference
		else {
		    sendOne(new NetworkMessage(NetworkMessage.RETURN_REFERENCE, 
					       weightedObject.getWeightedObjectReference(), 
					       "Reference Return: " + inputMessage.getSender(), 
					       id, inputMessage.getSender()));
		}
	    }

	    ////////////////////////////////////////
	    // If we have a reference to an object already, check if we have it
	    // If we have it, make sure we can split it and give a reference
	    if (weightedObjectReference != null) {
		if (weightedObjectReference.canSplit()) {
		    sendOne(new NetworkMessage(NetworkMessage.RETURN_REFERENCE, 
					       weightedObjectReference.getWeightedObjectReference(), 
					       "Reference Return: " + inputMessage.getSender(), 
					       id, inputMessage.getSender()));
		} 
		// If we cant split our local copy forward the request
		else {
		    sendOne(inputMessage);
		}
	    }

	    ////////////////////////////////////////
	    // We don't have a weighted object or a reference we should forward
	    if (weightedObject == null && weightedObjectReference == null) {
		sendAllExcept(inputMessage, sendingInterface);
	    }
	    break;

	case NetworkMessage.RETURN_REFERENCE:

	    ////////////////////////////////////////
	    // If the message was intendend for me
	    if (inputMessage.getRecipient() == id) {
		System.out.println("Node: " + id + " Return");
		// Assign our local weighted object reference to our sent reference
		weightedObjectReference = (WeightedObjectReference) inputMessage.getData();
		System.out.println("Weight Obtained: " + weightedObjectReference.getWeight());
	    }
	    
	    ////////////////////////////////////////
	    // If the message is not meant for me, forward
	    else {
		sendOne(inputMessage);
	    }
	    break;

	case NetworkMessage.DISCARD_REFERENCE:
	    ////////////////////////////////////////
	    // If we have a weighted object, we must 
	    if (weightedObject != null) {
		WeightedObjectReference reference = (WeightedObjectReference) inputMessage.getData();
		System.out.println("Discard reference: " + reference.getWeight() + " Sender: "  + inputMessage.getSender());		

		if (weightedObject.returnReference(reference.getWeight())) {
		    System.out.println("All references discarded");
		}
		System.out.println(weightedObject.toString());
	    }

	    ////////////////////////////////////////
	    // If we aren't the intended recipient, forward
	    else {
		sendOne(inputMessage);
	    }
	    break;
	default:
	    break;
	}
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
