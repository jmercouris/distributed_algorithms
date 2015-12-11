import java.awt.Color;
/*
 * Network Message class, this class is used to send messages between nodes in the system
 */
public class NetworkMessage {
    
    // Static list of Message Types, Invokes different node behavior
    public static final int EXPLORE = 0;           // Explore the nodes around yourself
    public static final int REQUEST_REFERENCE = 1; // Ask object owner for reference
    public static final int RETURN_REFERENCE = 2;  // Return reference to reference requester
    public static final int DISCARD_REFERENCE = 3; // Send discard message to object owner

    // Instance Variables
    Object data;
    int type = 0;
    String stringMessage = "";
    int sender;
    int recipient;
    

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

    public NetworkMessage(int inputType, Object inputData) {
	type = inputType;
	data = inputData;
    }

    public NetworkMessage(int inputType, Object inputData, String inputString) {
	type = inputType;
	data = inputData;
	stringMessage = inputString;
    }

    public NetworkMessage(int inputType, Object inputData, String inputString, 
			  int inputSender, int inputRecipient) {
	type = inputType;
	data = inputData;
	stringMessage = inputString;
	recipient = inputRecipient;
	sender = inputSender;
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

    public Object getData() { 
	return data;
    }

    public void setSender(int inputSender) {
	sender = inputSender;
    }
    
    public int getSender() {
	return sender;
    }

    public void setRecipient(int inputRecipient) {
	recipient = inputRecipient;
    }

    public int getRecipient() {
	return recipient;
    }

    public void setMessage(String inputString) {
	stringMessage = inputString;
    }

    public String toString() {
	return "" + stringMessage;
    }
}
