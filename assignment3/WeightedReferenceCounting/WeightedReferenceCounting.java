import teachnet.algorithm.BasicAlgorithm;

/**
 * Group 11
 * Dan Drewes, Manuela Hopp, John Mercouris, Malte Siemers
 */
public class WeightedReferenceCounting extends BasicAlgorithm{
    // Flag if received explore message
    boolean informed = false;

    // Initiate the Algorithm by awakening all nodes
    public void initiate() {
	sendAll(new NetworkMessage(NetworkMessage.EXPLORE));
    }

    // Act on Recieved Messages
    public void receive(int sendingInterface, Object message) {
	// Cast Message to Network Message Object
	NetworkMessage inputMessage = (NetworkMessage) message;
	switch (inputMessage.getType()) {
	case NetworkMessage.EXPLORE:
	    if (!informed) {
		informed = true;
		sendAllExcept(inputMessage, sendingInterface);
		run();
	    }
	    break;
	default:
	    break;
	}
    }

    // Main Loop Thread
    public void run() { 
	while (true) { 
	    
	}
    }

    // Convenience Method to send a message to all neighbors
    public void sendAll(NetworkMessage inputMessage) { 
	for (int i = 0; i < checkInterfaces(); i++) {
	    send(i, inputMessage);
	}
    }
    // Convenience Method to send a message to all neighbors
    public void sendAllExcept(NetworkMessage inputMessage, int exceptInterface) { 
	for (int i = 0; i < checkInterfaces(); i++) {
	    if (i != exceptInterface) {
		send(i, inputMessage);
	    }
	}
    }

    ////////////////////////////////////////////////////////////////////////////////
    // End Class
    ////////////////////////////////////////////////////////////////////////////////
}
