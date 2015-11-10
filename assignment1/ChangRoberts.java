import teachnet.algorithm.BasicAlgorithm;

/**
* Group 11
* Dan Drewes, Manuela Hopp, John Mercouris, Malte Siemers
* The class implements the Chang-Roberts algorithm
*/

public class ChangRoberts extends BasicAlgorithm {

  int Mp = 0;
  int id;
  
/**
* setup function
*/
  public void setup(java.util.Map<String, Object> config){
    int id = (Integer) config.get("node.id");
  }
  
/**
* initiates the algorithm by sending the node's own id to all its neighbors
*/
  public void initiate(){
    Mp = id;
    for (int i = 0; i < checkInterfaces(); i++) {
      send(i, Mp);
    }
  }
  
/**
* forwards messages when the id received is higher and declares the node master if it receives its own id
*/
  public void receive(int interf, Object message){
    if (message.equals("I am the master")); // then we are done
    else {
      int k = (int) message;
      if (Mp < k){ // received higher id, forward it to all neighbors
        Mp = k;
        for (int i = 0; i < checkInterfaces(); i++){
          send(i, Mp);
        }
      }
      if (k == Mp){ // got own id, declare master
        for (int i = 0; i < checkInterfaces(); i++){ 
          send(i, "I am the master");
        }
      }
    }
  }
}