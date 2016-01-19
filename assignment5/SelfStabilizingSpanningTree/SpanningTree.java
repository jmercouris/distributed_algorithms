import teachnet.algorithm.BasicAlgorithm;
import java.awt.Color;

/**
 * Group 11
 * Dan Drewes, Manuela Hopp, John Mercouris, Malte Siemers
 */
public class SpanningTree extends BasicAlgorithm{

    // Caption Next to Node
    String caption = "<0>→<0, 0, 0>";
    // Integer ID of the Node
    int id = 0;
    // Color of the Node - Default Black
    Color defaultColor = Color.BLACK;
    Color rootColor = Color.GREEN;
    Color faultyColor = Color.RED;
    Color color = defaultColor;
    // Relational Information
    int rootID = 0;
    int treeLevel = 0;
    int parentNode = 0;
    // Interface Coloring
    int markInterface = -1;

    /**
     * Setup the algorithm
     */
    public void setup(java.util.Map<String, Object> config)
    {
	id = (Integer) config.get("node.id");
	updateCaption();
    }
    
    /**
     * initiates the algorithm
     */
    public void initiate() {
	sendAll(new NetworkMessage());
    }

    /**
     * Receive Messages
     */
    public void receive(int interf, Object message){

    }

    ////////////////////////////////////////////////////////////////////////////////
    // Helper Functions Node Specific Behavior
    ////////////////////////////////////////////////////////////////////////////////
    public void updateCaption() {
	caption = "<" + id + ">→" +
	    "<" +  rootID + ", " + treeLevel + ", " + parentNode + ">";
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
