import teachnet.algorithm.BasicAlgorithm;

/**
* Group 11
* Dan Drewes, Manuela Hopp, John Mercouris, Malte Siemers
*/
public class SpanningTree extends BasicAlgorithm{

  boolean informed = false;
  boolean initiator = false;
  int activator;
  int count = 0;
  int markInterface =-1;

  /**
  * initiates the algorithm;
  * sends an explorer message to all neighbors and sets informed and initiator to true
  */
  public void initiate(){
    for (int i = 0; i < checkInterfaces(); i++) {
      send(i, "Explorer");
    }
    informed=true;
    initiator=true;
  }

  /**
  * sends out explorer messages to all neighbors if not yet informed
  * sends echo to activator if all neighbors are informed
  */
  public void receive(int interf, Object message){
    if(!informed){
      for (int i = 0; i < checkInterfaces(); i++) { //flood
        if(i != interf){
           send(i, "Explorer");
        }
      }
      informed = true;
      activator = interf; // remember activator
      markInterface = interf; // highlight spanning tree
    }
    count++;
    if(count == checkInterfaces()){ //all neighbours informed
      if(!initiator){
        send(activator,"Echo");
      }
      //no explicit exit possible in teachnet
    }
  }
}
