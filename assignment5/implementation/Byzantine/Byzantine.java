import teachnet.algorithm.BasicAlgorithm;
import java.util.Random;

/**
* Group 11
* Dan Drewes, Manuela Hopp, John Mercouris, Malte Siemers
* This class implements the byzantine generals agreement.
*/
public class Byzantine extends BasicAlgorithm{

  //variables necessary to set up the network
  int networkSize;
  int nodeID; //node 0 is always the general, nodes 0 to 3 can be faulty (this is equal to "randomly chosen" because we have a fully meshed network)
  int m; //the number of faulty generals. m is set in the configuration file
  int commanderIsFaulty; //if set to 1 in the configuration file, the commander will be faulty
  String caption; //the string stored in caption is written next to the node by teachnet
  //I use the caption to indicate weather the node represents a faulty commander, a non-faulty commander, a faulty lieutenant or a non-faulty lieutenant
  boolean nodeIsFaulty; //this additional boolean is used to decide if wrong values are sent to other nodes
  
  //variables for the algorithm
  int v; //the value the commander proposes
  int receivedValues[]; //the values received from the other generals. 
  //the value of node 0 is stored at index 0, the one of node 1 at index 1 and so on
  int messageCount;//counts the messages received. After a message from every node was received, the majority voting can be applied.
  int round; //lieutenants may only send messages in the first round 
  //(because the messages do not really arive at the same point in time, only very close after each other,
  //it is necessary to check the current round to make sure message forwarding is stopped, see line 99)
  
  
  /**
  * sets up the attributes by reading from the configuration file
  */
  public void setup(java.util.Map<String, Object> config){
	this.nodeID = (Integer) config.get("node.id"); 
	this.networkSize = (Integer) config.get("network.size");
	this.m= (Integer) config.get("m");
	this.commanderIsFaulty= (Integer) config.get("commanderIsFaulty");
	this.caption=""+nodeID+": ";
	
	//decide, what kind of node this node is (faulty and/or commander)
	if(this.nodeID==0){
		if(commanderIsFaulty==1){
			caption+="faulty commander";
			nodeIsFaulty=true;
		} else {
			caption+="non-faulty commander";
		}		
	} else if(this.nodeID <= (m-commanderIsFaulty)){
		caption+="faulty lieutenant";
		nodeIsFaulty=true;
	} else {
		caption+="non-faulty lieutenant";
	}		
	
	this.receivedValues=new int[networkSize];
	this.round=0;
	this.messageCount=0;
	
  }

  /**
  * initiates the agreement; is only executed on the general (node 0; defined so in the config file)
  * Sends out a message to each lieutenant.
  * If the general is faulty, random values are sent. If not, all lieutenant receive the same value.
  * The value(s) are generated randomly.
  */
  public void initiate(){
	Random generator = new Random(); // Random number generator
	int value;
	if(nodeIsFaulty){
		for(int i=1;i<networkSize;i++){ //fully meshed -> one interface for each node -> interface number = nodeID
			value=generator.nextInt(2); //every general receives a random value (0 or 1)
			send(i,new NetworkMessage(value,""+nodeID,true));//"true" indicates that the message is not corrupted (see the constructor of NetworkMessage)
		}
	} else {
		value=generator.nextInt(2); //all generals receive the same random value
		for(int i=1;i<networkSize;i++){ 
			send(i,new NetworkMessage(value,""+nodeID,false));//
		}
	}
  }

  /**
  * Upon receiving a message, the node stores the value of the message.
  * In addition, forwards the message appending his own node id to the message string
  * and in case it is a faulty general it corrupts the value of the message.
  */
  public void receive(int interf, Object mess){
	
	NetworkMessage message= (NetworkMessage) mess;
	round++;
	messageCount++;
	
	receivedValues[interf]=message.value; //store the value received
	
	if(messageCount==networkSize-1){ //received all messages
		doMajorityVoting();
		return; //agreement is completed
	} else if(round>1){ //round=1 after received initial message from commander. Only then we want to send any messages.
		return; //stop sending messages, just wait for other messages
	}
	
	if(nodeIsFaulty){ //send a random value to all nodes
		Random generator = new Random(); // Random number generator
		int falseValue;
		for(int i=1;i<networkSize;i++){ 
			falseValue=generator.nextInt(2);
			if(i!=interf && i!= nodeID){ //send to all neighbours except the one we received the message from
				send(i,new NetworkMessage(falseValue,nodeID+" : "+message.stringMessage,true));
			}
		}
		
	} else { //send along the received value
		for(int i=1;i<networkSize;i++){ 
			if(i!=interf && i!= nodeID){ //send to all neighbours except the one we received the message from
				send(i,new NetworkMessage(message.value,nodeID+" : "+message.stringMessage,message.isCorrupted));
			}
		}
	}

  }
  
  /**
  * This method executes the majority voting by a simple walk-through of the array receivedValues.
  */
  public void doMajorityVoting(){
	  int zeros=0;
	  int ones=1;
	  for(int i=0;i<networkSize;i++){
		  if(receivedValues[i]==0){
			  zeros++;
		  } else if(receivedValues[i]==1){
			  ones++;
		  }
	  }
	  
	  if(zeros > ones){
		  this.v=0;
		  caption = "Wait!";
		  System.out.println(""+nodeID+": Wait!");
	  } else {
		  this.v=1;
		  caption = "Attack!";
		  System.out.println(""+nodeID+": Attack!");
	  } 
	  
  }
}
