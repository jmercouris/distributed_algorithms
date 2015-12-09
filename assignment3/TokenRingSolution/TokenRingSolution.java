import teachnet.algorithm.BasicAlgorithm;
import java.util.LinkedList;

/**
* Group 11
* Dan Drewes, Manuela Hopp, John Mercouris, Malte Siemers
* This class implements the Echo algorithm
*/
public class TokenRingSolution extends BasicAlgorithm{

  String caption="";
  int id;
  int nnodes;
  int nintervals;
  double latestRequest;
  boolean informed = false;
  int ltime=0;//logical time
  Token token; //not null if this node currently holds the token
  int[] hsn;//highest Sequence Numbers
  boolean tokenWanted=false; //

  /**
  *	Setup funktion to get the number of nodes in the graph.
  */
  public void setup(java.util.Map<String, Object> config)
	{
		nnodes = (Integer) config.get("network.size");
		hsn=new int[nnodes];
		id= (Integer) config.get("node.id");
		caption+=id;
		nintervals=(Integer) config.get("nintervals");
		latestRequest=(Double) config.get("latestRequest");
		if(id == 0){ //initially the the token is located at node 0
			this.token=new Token(nnodes);
		}
		
	}
	
	
  /**
  * initiates the algorithm;
  * sends request around the ring
  */
  public void initiate(){ 
	Object o=new Object();//dummy
	
	double wtime=Math.random();
	wtime*=latestRequest;
	double intervalDuration=(latestRequest/nintervals);
	for(double t=0;t<latestRequest;t+=intervalDuration){
		if(wtime<t+intervalDuration){
			setTimeout(t,o);
			return;
		}
	}
  }
  
  public void timeout(Object o){
	tokenWanted=true;
    send(0, new NetworkMessage("Request",++hsn[id],id));
	caption+= " requesting";
  }

  /**
  * broadcasts request from other nodes and
  *	simulates token access and forwarding of the token
  */
  public void receive(int interf, Object message){
	  
	if(message instanceof NetworkMessage){ //Request received, store highest sequence number and broadcast
		NetworkMessage mess= (NetworkMessage) message;
		if(mess.requestingNode!=id){ //do not forward own request
			this.hsn[mess.requestingNode]=mess.sequenceNumber;
			if(token!=null){ //this node currently holds the token
				token.q[mess.requestingNode]=true; //add the requesting Node to the queue of the token
				for(int i=0;i<checkInterfaces();i++){ //send the token around the ring until it reaches the requesting node
					if(i!=interf){
						send(i,new Token(token.l,token.q));
						token=null;
					}
				}
			} else{ //only forward the request
				for(int i=0;i<checkInterfaces();i++){
					if(i!=interf){
						send(i,new NetworkMessage("Request",mess.sequenceNumber,mess.requestingNode));
					}
				}
			}
		}
	}
	else if(message instanceof Token){ //got the token
		Token mess= (Token) message;
		this.token=new Token(mess.l,mess.q);
		if(tokenWanted){
			//access
			mess.l[id]=hsn[id];
			caption=""+id;
			tokenWanted=false;
		}
		for(int j=0;j<nnodes;j++){ //update the queue in the token
			if(token.l[j]<hsn[j]){
				token.q[j]=true;
			}
		}
		this.token.q[id]=false;//delete itself from Queue in token
		for(int j=0;j<nnodes;j++){ //send token to next node if there is a request that is not fulfilled yet
			if(token.q[j]){
				for(int i=0;i<checkInterfaces();i++){					
					if(i!=interf){
						send(i,new Token(token.l,token.q));
						token=null;
					}
				}
			}
		}
	}
  }
}
