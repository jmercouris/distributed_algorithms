import java.lang.Math;

/*
 * Class represents a weighted object
 */
public class WeightedObject {
    double weight = 0; // Represents the partial weight
    double prototypeWeight = 0; // Represents the original object weight
    int id = 0; // The ID of the object
    
    /**
     * Default Constructor
     */ 
    public WeightedObject() {

    }

    /**
     * Constructor with weight, initially partial weight and total weight are equal
     */
    public WeightedObject(double inputWeight, int inputID) {
	weight = inputWeight;
	prototypeWeight = weight;
	id = inputID;
    }

    /**
     * Split self in half, and give new reference
     */
    public WeightedObjectReference getWeightedObjectReference() {
	// Initialize reference weight to 0
	double referenceWeight = 0.0;

	// If our weight is 2 or above, give half our weight
	if (weight >= 2) {
	    weight = weight / 2;
	    referenceWeight = weight;
	}

	// If our weight is 1, 
	// set referenceWeight = 1, thereby giving away that weight, 
	// and add 2^10 to our weight for further references
	else if (weight == 1) {
	    prototypeWeight += Math.pow(2, 10);	    
	    weight = Math.pow(2, 10);
	    referenceWeight = 1;
	}

	return new WeightedObjectReference(id, referenceWeight);
    }

    /**
     * Returns weight to the reference so that it may discarded when the prototypeWeight (weight)
     * and weight (partial weight) are equal
     */ 
    public boolean returnReference (double inputWeight) {
	weight += inputWeight;
	if (weight == prototypeWeight) {
	    return true;
	}
        return false;
    }

    /**
     * Get the weight of the object
     */
    public double getWeight() { 
	return weight;
    }
    
    /** 
     * Get the ID of the object
     */
    public int getID() { 
	return id;
    }

    /**
     * Tostring to examine object
     */
    public String toString() {
	return "Total Weight: " + prototypeWeight + " Partial Weight: " + weight;
    }
}
