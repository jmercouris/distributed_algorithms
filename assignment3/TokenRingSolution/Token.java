import java.util.LinkedList;
/*
 * Network Message class, this class is used to send messages between nodes in the system
 */
public class Token {
	int[] l; //logical time
	boolean[] q; //we use an array instead of a queue and indicate a current request as an "true" entry

    public Token(int nnodes) {
		this.l=new int[nnodes];
		this.q=new boolean[nnodes];
		for(int j=0;j<nnodes;j++){
			q[j]=false;
		}
    }
	
	public Token(int[] l, boolean[] q) {
	this.l=l;
	this.q=q;
    }

    public String toString() {
		String ret="Token";
		for(int i=0;i<q.length;i++){
			if(q[i]){
				ret+=" "+i;
			}
		}
	return ret;
    }
}
