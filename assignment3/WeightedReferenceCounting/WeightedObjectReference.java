/*
 * 
 */
public class WeightedObjectReference extends WeightedObject {
    // Default Constructor
    public WeightedObjectReference() {

    }

    public WeightedObjectReference (int inputID, double inputWeight) {
	id = inputID;
	weight = inputWeight;
    }

    // Split self in half, and give new reference
    public WeightedObjectReference getWeightedObjectReference() {
	return super.getWeightedObjectReference();
    }

}
