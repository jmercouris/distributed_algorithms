import teachnet.algorithm.BasicAlgorithm;

public class ChangRoberts extends BasicAlgorithm {

  int Mp = 0;
  int id;

  public void initiate(){
	Mp = id;
	for (int i = 0; i < checkInterfaces(); i++) {
	  send(i, Mp);
	}
  }
  
  public void receive(int interf, Object message){
	if (message.equals("I am the master")); // then we are done
	else {
	  int k = (int) message;
	  if (Mp < k){
		Mp = k;
		for (int i = 0; i < checkInterfaces(); i++){
		  send(i, Mp);
		}
	  } else {
		if (j == Mp){
		  for (int i = 0; i < checkInterfaces(); i++){
			send(i,"I am the master");
		  }
		}
	  }
	}
  }
}