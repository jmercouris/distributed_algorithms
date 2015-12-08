/*
 * Network Message class, this class is used to send messages between nodes in the system
 */
public class WeightedObject {
    String stringMessage = "";
    Object data;
    int color = 0;
    int value = -1;

    
    // Default Constructor
    public WeightedObject() {

    }

    public WeightedObject(Object inputData) {
	data = inputData;
    }

    public WeightedObject(String inputString) {
	stringMessage = inputString;
    }

    public String toString() {
	return "" + value;
    }
}
