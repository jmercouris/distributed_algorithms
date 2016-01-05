/*
 * Exclusive Resource can only have one instance of self
 */
public class ExclusiveResource {
    int id;
    public ExclusiveResource() {

    }
    public ExclusiveResource(int inputId) {
        id = inputId;
    }
    public int getId() {
        return id;
    }
    
    public String toString() {
        return "" + id;
    }
}
