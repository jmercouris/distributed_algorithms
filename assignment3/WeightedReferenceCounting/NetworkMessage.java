/*
 * Network Message class, this class is used to send messages between nodes in the system
 */
public class NetworkMessage {
    String stringMessage = "";
    Object data;
    int color = 0;
    int value = -1;

    
    // Default Constructor
    public NetworkMessage() {

    }

    public NetworkMessage(Object inputData) {
	data = inputData;
    }

    public NetworkMessage(String inputString) {
	stringMessage = inputString;
    }

    public String toString() {
	return "" + value;
    }
}
