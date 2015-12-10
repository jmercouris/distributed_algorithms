/*
 * 
 */
public class WeightedObject {
    double weight = 0;
    int id = 0;
    
    // Default Constructor
    public WeightedObject() {

    }

    public WeightedObject(double inputWeight, int inputID) {
	weight = inputWeight;
	id = inputID;
    }

    public double getWeight() { 
	return weight;
    }
    
    public int getID() { 
	return id;
    }

    public String toString() {
	return "";
    }
}
