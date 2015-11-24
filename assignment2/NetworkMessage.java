
////////////////////////////////////////////////////////////////////////////////
// Network Message Class
////////////////////////////////////////////////////////////////////////////////
public class NetworkMessage {
    String stringMessage = "";
    int value = -1;
    boolean maximumValue = false;

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
    public NetworkMessage(int inputValue, boolean inputMaximumValue) {
	value = inputValue;
	maximumValue = inputMaximumValue;
    }

    public String toString() {
	return "" + value;
    }
}
