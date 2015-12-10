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
