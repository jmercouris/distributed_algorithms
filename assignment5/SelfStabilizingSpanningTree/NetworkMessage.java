import java.awt.Color;

/*
 * Network Message class, this class is used to send messages between nodes in the system
 */
public class NetworkMessage {

    // Static Message Types
    public static final int PROBE = 0;           // Probe for diffusion
    public static final int ELECTION = 1;        // Probe for Election


    // Used for External Rendering
    Color color = Color.WHITE;
    String stringMessage = "";
    
    int value = -1;
    int type;



    public NetworkMessage() {

    }

    public int getType() {
	return type;
    }

    public String toString() {
	return "" + value;
    }
}
