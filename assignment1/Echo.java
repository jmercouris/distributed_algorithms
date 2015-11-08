import teachnet.algorithm.BasicAlgorithm;
import java.awt.Color;


public class Echo extends BasicAlgorithm{
	
	boolean informed = false;
	boolean initiator = false;
	int activator;
	int count = 0;
	int markInterface =-1;
	
	
	
	public void initiate(){
		for (int i = 0; i < checkInterfaces(); i++) {
			send(i, "Explorer");
		}
		informed=true;
		initiator=true;
	}
	
	public void receive(int interf, Object message){
			if(!informed){
				for (int i = 0; i < checkInterfaces(); i++) { //flood
					if(i != interf){
						send(i, "Explorer");
					}			
				}
				informed=true;
				activator=interf; //remember activator
				markInterface=interf; //highlight spanning tree
			}			
			count++;
			if(count==checkInterfaces()){ //all neighbours informed
				if(!initiator){
					send(activator,"Echo");
				}
				//no explicit exit possible in teachnet
			}

	}
	
	
	
	
}