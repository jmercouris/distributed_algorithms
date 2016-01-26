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
    int default_value;
  
    //variables for the algorithm
    int v; //the value the commander proposes 
    Tree receivedValues; //the values received from the other generals are stored in a tree structure.
  
    int messageCount;//counts the messages received. After a message from every node was received, the round counter is increased.
    //the following two values are necessary to let the algorithm terminate, they are used in the receive sections
    int totalNumberOfMessages;
    int totalNumberOfMessagesBeforeLastRound; 
  
  
    /**
     * sets up the attributes by reading from the configuration file
     */
    public void setup(java.util.Map<String, Object> config){
	
	//setup variables
	this.nodeID = (Integer) config.get("node.id"); 
	this.networkSize = (Integer) config.get("network.size");
	this.m= (Integer) config.get("m");
	this.commanderIsFaulty= (Integer) config.get("commanderIsFaulty");
	this.default_value=(Integer) config.get("default_value");
	this.caption=""+nodeID+": ";
	
	
	this.receivedValues=new Tree(networkSize);
	
	this.messageCount=0;	
	this.totalNumberOfMessagesBeforeLastRound=computeTotalNumberOfMessagesBeforeLastRound();
	this.totalNumberOfMessages=totalNumberOfMessagesBeforeLastRound+computeTotalNumberOfMessagesInLastRound();
	
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
		value=generator.nextInt(2); //send a random value (0 or 1) to every general 
		int[] path={nodeID,};//path contains only the node's ID
		send(i,new NetworkMessage(value,path,true));//"true" indicates that the message is corrupted (see the constructor of NetworkMessage)
	    }
	} else {
	    value=generator.nextInt(2); //all generals receive the same random value (because its out of the for loop)
	    for(int i=1;i<networkSize;i++){ 
		int[] path={nodeID,};
		send(i,new NetworkMessage(value,path,false));
	    }
	}
    }

    /**
     * Upon receiving a message, the node stores the value of the message.
     * In addition, forwards the message appending his own node id to the message path
     * and in case it is a faulty general it corrupts the value of the message.
     */
    public void receive(int interf, Object mess){
	
	NetworkMessage message= (NetworkMessage) mess; //typecasting
	messageCount++;
	
	receivedValues.insert(message.messagePath,message.value); //store the value received
	
	if(messageCount==(totalNumberOfMessages)/(networkSize-1)){
	    doMajorityVoting();
	    return; //agreement is completed
	} else if( messageCount > (totalNumberOfMessagesBeforeLastRound)/(networkSize-1)){
	    //the last round has begun, stop sending messages and wait for all messages to arrive
	    return;
	}
		
	
	if(nodeIsFaulty){ //send a random value to all nodes
	    Random generator = new Random(); // Random number generator
	    int falseValue;
	    for(int i=1;i<networkSize;i++){ 
		falseValue=generator.nextInt(2);
		if(i!=0 && i!= this.nodeID && !contains(message.messagePath,i)){ 
		    //send to all neighbours except the commander (node 0), the node itself and all nodes already in the path				
		    send(i,new NetworkMessage(falseValue,append(message.messagePath,nodeID),true));
		}
	    }
		
	} else { //non-faulty general: sends along the received value
	    for(int i=1;i<networkSize;i++){ 
		if(i!=0 && i!= this.nodeID && !contains(message.messagePath,i)){ //send to all neighbours except the commander (node 0)
		    send(i,new NetworkMessage(message.value,append(message.messagePath,nodeID),message.isCorrupted));
		}
	    }
	}

    }
  
    /**
     * Checks if an array contains a given value.
     */
    public boolean contains(int[] path, int id){
	for(int i=0;i<path.length;i++){
	    if(id==path[i]){
		return true;
	    }
	}  
	return false;
    }
  
    /**
     * Appends a given id to the path array (at the front).
     * Returns a new array.
     */
    public int[] append(int[] oldPath, int id){
	int[] newPath=new int[oldPath.length+1];
	for(int i=0;i<oldPath.length;i++){
	    newPath[i+1]=oldPath[i];
	}
	newPath[0]=id;
	return newPath;
    }
  
    /**
     * This method executes the majority voting.
     * It calls the function getMajority of the tree class and provides graphical output.
     */
    public void doMajorityVoting(){
	
	int result=receivedValues.getMajority();
	  
	if(result==0){
	    this.v=0;
	    System.out.println(""+nodeID+": Wait!");
	} else {
	    this.v=1;
	    System.out.println(""+nodeID+": Attack!");
	} 
	  
    }
  
    /**
     * Returns the total number of messages sent by the algorithm BEFORE the last round has started.
     * The formula is given on slide 22 of lecture 11.
     */
    public int computeTotalNumberOfMessagesBeforeLastRound(){
	int sum=0;
	int n=networkSize;//(to make this function easier to read)
		
	for(int i=0;i<m;i++){
	    sum+=(factorial(n-1))/(factorial(n-2-i));
	}  
	return sum;
    }
  
    /**
     * Returns the total number of messages sent by the algorithm IN the last round.
     */
    public int computeTotalNumberOfMessagesInLastRound(){
	int n=networkSize;
	  
	return (factorial(n-1))/(factorial(networkSize-2-m));
    }
  
    /**
     * Helper function that computes the factorial of a natural number.
     */
    public int factorial(int n) {
	int fact = 1; 
	for (int i = 1; i <= n; i++) {
            fact *= i;
        }
	return fact;
    }
  
    /**
     * This class provides storage and some functions for majority voting.
     * See slide 25, lecture 11 for an example tree
     */
    public class Tree{
	
	int value; //(to compare with the slides, this is the value a or b)
	Tree[] children;
	
	/**
	 * Constructor
	 */
	public Tree(int numberOfChildrenPerNode){
	    this.children=new Tree[numberOfChildrenPerNode];
	    this.value=-1; //initialize to invalid value
	}
	
	/**
	 * Inserts a value along the given path.
	 * This is a quite complex helper function, but in the end it only creates trees like on slide 25, lecture 11
	 */
	public void insert(int[] path, int value){
	    Tree current=this;
	    for(int i=path.length-1;i>=0;i--){
		if(current.children[path[i]]==null){
		    current.children[path[i]]=new Tree(children.length);
		}
		current=current.children[path[i]];
	    }
	    current.value=value;
	}
	
	/**
	 * Determines the majority of received values (in the way described in lecture)
	 * and returns this value (0 or 1).
	 * It is implemented in a recursive way.
	 */
	public int getMajority(){
	    int zero_trees=0; //counter for trees evaluated to 0
	    int one_trees=0; //counter for trees evaluated to 1
		
	    //consider own value (i.e. the value in the current root)
	    if(this.value==0){
		zero_trees++;
	    }else if(this.value==1){
		one_trees++;
	    }
		
	    //recursively call the majority function on the children trees
	    for(int i=0;i<children.length;i++){
		if(children[i]!=null){
		    int tmpRes=children[i].getMajority();
		    if(tmpRes==0){
			zero_trees++;
		    } else {
			one_trees++;
		    }
		}
	    }
		
	    //finally, compare the numbers and return the respective result
	    if(zero_trees>one_trees){
		return 0;
	    } else if(one_trees>zero_trees){
		return 1;
	    } else { //if equal
		return default_value;
	    }
	}
	  
    }
  
}
