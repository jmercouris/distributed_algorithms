/*
 * Network Message class, this class is used to send messages between nodes in the system
 */
public class Token {
	int[] l; //logical time

    public NetworkMessage(int nnodes, int[] l) {
	this.l=l;
    }

    public String toString() {
	return "Token";
    }
}
