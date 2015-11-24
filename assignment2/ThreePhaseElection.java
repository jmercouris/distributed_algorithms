import teachnet.algorithm.BasicAlgorithm;

/**
 * Group 11
 * Dan Drewes, Manuela Hopp, John Mercouris, Malte Siemers
 * This class implements the ThreePhaseElection algorithm
 */
public class ThreePhaseElection extends BasicAlgorithm {

    String caption;
    
    /* Phase Identifiers */
    int PHASE_EXPLOSION   = 0;
    int PHASE_CONTRACTION = 1;
    int PHASE_INFORMATION = 2;
	
    boolean informed = false;
    boolean initiator = false;

    int id;
    int phase = PHASE_EXPLOSION;

    /**
     * setup function
     */
    public void setup(java.util.Map<String, Object> config){
	id = (Integer) config.get("node.id");
	caption = ""+id;
    }

    /**
     * initiates the algorithm;
     * sends an explorer message to all neighbors and sets informed and initiator to true
     */
    public void initiate(){
	for (int i = 0; i < checkInterfaces(); i++) {
	    send(i, "Explorer");
	}
    }

    /**
     * sends out explorer messages to all neighbors if not yet informed
     */
    public void receive(int interf, Object message){

	// If Node is a Leaf
	if (checkInterfaces() == 1) {
	    send(0, ""+id);
	    System.out.println("LEAF");
	}

	for (int i = 0; i < checkInterfaces(); i++) {
	    send(i, "Explorer");
	}
	

    }
}

