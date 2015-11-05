import teachnet.algorithm.BasicAlgorithm;


public class Flooding extends BasicAlgorithm{
	
	boolean informed = false;
	boolean initiator = false;
	int activator;
	int count = 0;
	
	
	public void initiate(){
		for (int i = 0; i < checkInterfaces(); i++) {
			send(i, "Explorer");
		}
		informed=true;
		initiator=true;
	}
	
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
			}
			else{
				send(interf,"Confirmation");
			}
		}
		else if(message.equals("Confirmation")){ //Confirmation recieved
			count++;
			if(count==checkInterfaces()-1 && !initiator){ //all neighbours informed
				send(activator,"Confirmation");
			}
			//no exit possible in teachnet
		}
	}
}