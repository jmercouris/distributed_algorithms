import java.awt.Color;

/*
 * Network Message class, this class is used to send messages between nodes in the system
 */
public class NetworkMessage {
	
	Color color;//indicates if corrupted (red) or not (green)
	boolean isCorrupted;
    String stringMessage = "";
    int value = -1;

    public NetworkMessage(int inputValue, String inputString, boolean inputIsCorrupted) {
		value = inputValue;
		stringMessage = inputString;
		isCorrupted=inputIsCorrupted;
		if(isCorrupted){
			color=new Color(255,0,0);
		} else {
			color=new Color(0,255,0);
		}
    }
	
	 public NetworkMessage(int inputValue, String inputString, Color c) {
		value = inputValue;
		stringMessage = inputString;
		color=c;
    }

    public String toString() {
	return stringMessage + " --> " + value;
    }
}
