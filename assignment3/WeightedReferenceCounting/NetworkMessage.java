import java.awt.Color;
/*
 * Network Message class, this class is used to send messages between nodes in the system
 */
public class NetworkMessage {
    
    // Static list of Message Types, Invokes different node behavior
    public static final int EXPLORE = 0;

    // Instance Variables
    Object data;
    int type = 0;
    String stringMessage = "";    
    

    ////////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////////
    public NetworkMessage() {
    }

    public NetworkMessage(Object inputData) {
	data = inputData;
    }

    public NetworkMessage(int inputType) {
	type = inputType;
    }

    public NetworkMessage(String inputString) {
	stringMessage = inputString;
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Getters and Setters
    ////////////////////////////////////////////////////////////////////////////////
    public int getType() {
	return type;
    }

    public void setMessage(String inputString) {
	stringMessage = inputString;
    }

    public String toString() {
	return "" + stringMessage;
    }
}
