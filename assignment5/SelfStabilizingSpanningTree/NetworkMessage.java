import java.awt.Color;

/*
 * Network Message class, this class is used to send messages between nodes in the system
 */
public class NetworkMessage {
    
    String stringMessage = "";
    int value = -1;
    Color color = Color.WHITE;


    public NetworkMessage() {

    }

    public String toString() {
	return "" + value;
    }
}
