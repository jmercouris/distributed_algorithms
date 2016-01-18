import java.awt.Color;

/*
 * Network Message class, this class is used to send messages between nodes in the system
 */
public class NetworkMessage {
	
	Color color;//indicates if corrupted (red) or not (green)
	boolean isCorrupted;
	int[] messagePath;
    int value = -1;

    public NetworkMessage(int inputValue, int[] path, boolean inputIsCorrupted) {
		value = inputValue;
		messagePath=path;
		isCorrupted=inputIsCorrupted;
		if(isCorrupted){
			color=new Color(255,0,0);
		} else {
			color=new Color(0,255,0);
		}
    }
	
	 public NetworkMessage(int inputValue, int[] path, Color c) {
		value = inputValue;
		messagePath=path;
		color=c;
    }

    public String toString() {
		String result="";
		for(int i=0;i<messagePath.length-1;i++){
			result+=messagePath[i]+" : ";
		}
		return result + messagePath[messagePath.length-1]+" --> " + value;
    }
}
