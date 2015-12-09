/*
 * Network Message class, this class is used to send messages between nodes in the system
 */
public class NetworkMessage{
    String stringMessage = "";
	int sequenceNumber=0; //logical time
	int requestingNode;

    public NetworkMessage() {

    }
    public NetworkMessage(String inputString) {
	stringMessage = inputString;
    }
	
    public NetworkMessage(String inputString, int time) {
	stringMessage = inputString;
	sequenceNumber = time;
    }
	public NetworkMessage(String inputString, int time, int node) {
	stringMessage = inputString;
	sequenceNumber = time;
	requestingNode=node;
    }

    public String toString() {
		return "Node "+requestingNode+" requests";
    }
}
