import java.awt.Color;

/*
 * Network Message class, this class is used to send messages between nodes in the system
 */
public class NetworkMessage {

    // Static Message Types
    public static final int PROBE = 0;           // Message Probe for diffusion
    public static final int ELECTION = 1;        // Message for Election
    public static final int HEARTBEAT = 2;       // Message for Heartbeat

    // Used for External Rendering
    Color color = Color.WHITE;
    String stringMessage = "";
    
    int value = -1;

    // Use Public Variables to avoid gratuitous use of getters and setters
    public int type;
    public int senderNode;
    public int rootNode;
    public int treeLevel;



    public NetworkMessage() {

    }

    public void setType(int inputType) {
	type = inputType;
	// Change the color of the message based on the type of message
	switch (type) {
	case PROBE:
	    color = Color.BLACK;
	    break;
	case ELECTION:
	    color = Color.GREEN;
	    break;
	case HEARTBEAT:
	    color = Color.RED;
	default:
	    color = Color.BLACK;
	    break;
	}
    }
    
    public int getType() {
	return type;
    }

    public String toString() {
	return "Sender: " + senderNode;
    }
}
