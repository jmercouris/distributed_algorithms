import teachnet.algorithm.BasicAlgorithm;

/**
* Group 11
* Dan Drewes, Manuela Hopp, John Mercouris, Malte Siemers
* This class implements the Flooding algorithm
*/
public class Flooding extends BasicAlgorithm{

  boolean informed = false;
  boolean initiator = false;
  int activator;
  int count = 0;

  /**
  * initiates the algorithm
  * sends explorer messages to all neighbors and sets informed and initiator to true
  */
  public void initiate(){
    for (int i = 0; i < checkInterfaces(); i++) {
      send(i, "Explorer");
    }
    informed=true;
    initiator=true;
  }

  /**
  * sends explorers to all neighbors if not yet informed or confirmations otherwise
  * if all neighbors have sent confirmations, sends confirmation to activator
  */
  public void receive(int interf, Object message){
    if(message.equals("Explorer")){ //Explorer recieved
      if(!informed){
        for (int i = 0; i < checkInterfaces(); i++) { //flood
          if(i != interf){
            send(i, "Explorer");
          }
        }
        informed=true;
        activator=interf; //remember activator
      } else {
        send(interf,"Confirmation");
      }
    } else { 
      if(message.equals("Confirmation")){ //Confirmation received
        count++;
        if(count==checkInterfaces()-1 && !initiator){ //all neighbours informed
          send(activator,"Confirmation");
        }
      //no exit possible in teachnet
      }
    }
  }
}