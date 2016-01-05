import java.awt.Color;

/*
 * Network Message class, this class is used to send messages between nodes in the system
 */
public class NetworkMessage {
    public static final int PROBE = 0;           // Probe for Usage
    String stringMessage = "";
    int value = -1;
    int type = -1; // The type of message, used by the node recieve method to determine a course of action, initializes to invalid value
    // Variables needed for Chandy Misra Haas Probe
    int rootSender; // The Id of the originally sending node
    int sender; // The Id of the most recent sending node
    int recipient; // The Id of the intended recipient
    ExclusiveResource exclusiveResource; // The required resource object

    public NetworkMessage() {

    }

    public NetworkMessage(String inputString) {
	stringMessage = inputString;
    }

    public NetworkMessage(String inputString, int inputValue) {
	stringMessage = inputString;
	value = inputValue;
    }

    public NetworkMessage(int inputValue) {
	value = inputValue;
    }
    
    public int getType() {
	return type;
    }

    public String toString() {
	return "" + value;
    }
}
