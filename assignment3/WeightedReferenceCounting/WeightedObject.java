/*
 * 
 */
public class WeightedObject {
    double weight = 0;
    double partialWeight = 0;
    int id = 0;
    
    // Default Constructor
    public WeightedObject() {

    }

    // Constructor with weight, initially partial weight and total weight are equal
    public WeightedObject(double inputWeight, int inputID) {
	weight = inputWeight;
	partialWeight = weight;
	id = inputID;
    }

    // Split self in half, and give new reference
    public WeightedObjectReference getWeightedObjectReference() {
	weight = weight / 2;
	return new WeightedObjectReference(id, weight);
    }

    public boolean returnReference (double inputWeight) {
	weight += inputWeight;
	if (weight == partialWeight) {
	    return true;
	}
        return false;
    }

    // Get the weight of the object
    public double getWeight() { 
	return weight;
    }
    
    // Get the ID of the object
    public int getID() { 
	return id;
    }

    // Tostring to examine object
    public String toString() {
	return "Total Weight: " + partialWeight + " Partial Weight: " + weight;
    }
}
