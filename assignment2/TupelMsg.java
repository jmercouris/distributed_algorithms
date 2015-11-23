public class TupelMsg {
	
	public int counter; //to resolve the length of the chain, negative if not valid ("way back")
	public int conquererID; //transmit the ID of the node trying to conquer
	public final String message; //communication

	public TupelMsg(int id, int counter, String message) {
		this.conquererID=id;
		this.counter=counter;
		this.message=message;
	}

	public String toString() {
		return "Node "+conquererID+" "+message;
	}
}