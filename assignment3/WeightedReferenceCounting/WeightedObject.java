/*
 * 
 */
public class WeightedObject {
    int weight = 0;
    int id = 0;
    
    // Default Constructor
    public WeightedObject() {

    }

    public WeightedObject(int inputWeight, int inputID) {
	weight = inputWeight;
	id = inputID;
    }

    public int getWeight() { 
	return weight;
    }
    
    public int getID() { 
	return id;
    }

    public String toString() {
	return "";
    }
}
