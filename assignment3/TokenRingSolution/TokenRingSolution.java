import teachnet.algorithm.BasicAlgorithm;
import java.util.LinkedList;

/**
* Group 11
* Dan Drewes, Manuela Hopp, John Mercouris, Malte Siemers
* This class implements the Echo algorithm
*/
public class TokenRingSolution extends BasicAlgorithm{

  boolean informed = false;
  int id;
  int nnodes;
  int ltime=0;//logical time
  Token token;
  LinkedList<Nodes> children = new LinkedList(); //we use this to store children of the Node in the spanning tree

  /**
  *	Setup funktion to get the number of nodes in the graph.
  */
  public void setup(java.util.Map<String, Object> config)
	{
		nnodes = (Integer) config.get("network.size");
		id= (Integer) config.get("node.id");
		if(id == 0){
			token=new Token(nnodes, new int[nnodes]);
		}
		//rand = getRandom(); maybe use this later for access simulation
	}
	
	
  /**
  * initiates the algorithm;
  * sends an explorer message to all neighbours and sets informed and initiator to true
  */
  public void initiate(){
	
	if(id==0){ //initiate spanning tree construction only for one node
		for (int i = 0; i < checkInterfaces(); i++) {
			send(i, new NetworkMessage("ConstructSpanningTree"));
		}
	}
	
    for (int i = 0; i < checkInterfaces(); i++) {
      send(i, new NetworkMessage("Request",ltime));
    }
    ltime++;
  }

  /**
  * broadcasts request from other nodes and
  *	simulates token access and forwarding of the token
  */
  public void receive(int interf, Object message){
	  
	if(message instanceof NetworkMessage){ 
      if(message.stringMessage.equals("ConstructSpanningTree")){ //construct spanning tree
        for (int i = 0; i < checkInterfaces(); i++) { //TODO
          if(i != interf){
            send(i, "Explorer");
          }
        }
        informed=true;
      }
	  else{ //Request received, use spanning tree to broadcast
		for (int i = 0; i < checkInterfaces(); i++) { 
			if(i != interf){
				send(i, "Explorer");
			}
		}
	  }
    }
  }
}
