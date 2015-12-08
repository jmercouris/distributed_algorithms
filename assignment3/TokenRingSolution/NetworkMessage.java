/*
 * Network Message class, this class is used to send messages between nodes in the system
 */
public class NetworkMessage {
    String stringMessage = "";
	int timeCounter=0; //logical time

    public NetworkMessage() {

    }
    public NetworkMessage(String inputString) {
	stringMessage = inputString;
    }
	//this one we use
    public NetworkMessage(String inputString, int time) {
	stringMessage = inputString;
	timeCounter = time;
    }

    public NetworkMessage(int inputValue) {
	value = inputValue;
    }

    public String toString() {
	return stringMessage+ " at " + timeCounter;
    }
}
