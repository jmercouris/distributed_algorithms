import teachnet.algorithm.BasicAlgorithm;

/**
 * Group 11
 * Dan Drewes, Manuela Hopp, John Mercouris, Malte Siemers
 * This class implements the Hirschberg-Sinclair-Election algorithm
 */
public class HirschbergSinclairElection extends BasicAlgorithm{

    int id;
    int phaseNumber = 0;
    boolean active=true; //status of the node
    int conqueredDirections=0;
    boolean receivedOwnId =false; //to avoid twice sending of win notification
    int winner;
    String caption;

  
    /**
     * setup function
     */
    public void setup(java.util.Map<String, Object> config){
	id = (Integer) config.get("node.id");
	caption = ""+id;
    }
  
    /**
     * initiates the algorithm;
     * sends a "Conquer"-Message to its neighbours
     */
    public void initiate(){
	for (int i = 0; i < checkInterfaces(); i++) {
	    TupelMsg conquer=new TupelMsg(id,(int) Math.pow(2,phaseNumber),"Conquer");
	    send(i, conquer);
	}
    }

    /**
     * implements the functionality of Hirschberg-Sinclair-Election algorithm
     */
    public void receive(int interf, Object rmessage){
	TupelMsg message= (TupelMsg) rmessage;
	
	if(active){
	    if(message.message.equals("Conquer")){ //received a Conquer-Message
		if(message.conquererID>this.id){
		    if(message.counter>1){ //broadcast
			for (int i = 0; i < checkInterfaces(); i++) { 
			    if(i != interf){
				TupelMsg conquer=new TupelMsg(message.conquererID,message.counter-1,"Conquer");
				send(i, conquer);
			    }
			}
		    } else{ //message.counter expired;
			TupelMsg conquered=new TupelMsg(message.conquererID,-1,"Conquered"); //send back a message that informs the conquerer
			send(interf,conquered);
		    }
		} else if(message.conquererID==this.id && !receivedOwnId){ //won the election, inform other nodes; turn passive
		    receivedOwnId=true;
		    winner=id;
		    for (int i = 0; i < checkInterfaces(); i++) {
			TupelMsg win=new TupelMsg(id,(int) Math.pow(2,phaseNumber),"Winner");
			send(i, win);
		    }
		    active=false;
		} else { //own id is higher
		    TupelMsg veto=new TupelMsg(message.conquererID,-1,"Veto"); //send back a veto
		    send(interf,veto);
		}		
	    } else if(message.message.equals("Conquered")){
		if(message.conquererID != this.id){ //broadcast
		    for (int i = 0; i < checkInterfaces(); i++) { 
			if(i != interf){
			    TupelMsg conquered=new TupelMsg(message.conquererID,message.counter,"Conquered");
			    send(i, conquered);
			}
		    }
		} else if(conqueredDirections==0){ //conquered one direction area wait for other direction
		    conqueredDirections++;
		} else { //conquered both directions -> start new phaseNumber and set conqueredDirections back to 0;
		    phaseNumber++;
		    conqueredDirections=0;
		    for (int i = 0; i < checkInterfaces(); i++) {
			TupelMsg conquer=new TupelMsg(id,(int) Math.pow(2,phaseNumber),"Conquer");
			send(i, conquer);
		    }
		}
	    } else if(message.message.equals("Winner")){
		winner=message.conquererID;
		caption=""+winner+" won!";
		for (int i = 0; i < checkInterfaces(); i++) { 
		    if(i != interf){
			TupelMsg msg=new TupelMsg(message.conquererID,message.counter-1,message.message);//TupelMsg.message is final
			send(i, msg);
		    }
		}
		active=false;
	    } else{ //message equals Veto
		if(message.conquererID!=this.id){ //broadcast
		    for (int i = 0; i < checkInterfaces(); i++) { 
			if(i != interf){
			    TupelMsg veto=new TupelMsg(message.conquererID,message.counter,"Veto");
			    send(i, veto);
			}
		    }
		} else { //veto is for me -> turn passive
		    active=false;
		}
	    }
	} else { //passive 
	    if(message.message.equals("Winner")){ //winner announcement received
		winner=message.conquererID;
		caption=""+winner+" won!";
		if(message.conquererID!=this.id){ //broadcast (else halt)
		    for (int i = 0; i < checkInterfaces(); i++) { 
			if(i != interf){
			    TupelMsg msg=new TupelMsg(message.conquererID,message.counter-1,message.message);//TupelMsg.message is final
			    send(i, msg);
			}
		    }
		}
	    } else if(message.counter<=1 && message.message.equals("Conquer") && message.conquererID!=this.id){ //message.counter expired and message is not addressed to the node)
		TupelMsg conquered=new TupelMsg(message.conquererID,-1,"Conquered"); //send back a message that informs the conquerer
		send(interf,conquered);
	    } else if(message.conquererID!=this.id){ //just broadcast any conquer, conquered or veto message (that is not addressed to the node itself)
		for (int i = 0; i < checkInterfaces(); i++) { 
		    if(i != interf){
			TupelMsg msg=new TupelMsg(message.conquererID,message.counter-1,message.message);//TupelMsg.message is final
			send(i, msg);
		    }
		}
	    }
	}
    
    }
}
