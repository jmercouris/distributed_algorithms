import java.awt.Color;
import java.util.*;

/*
 * Network Message class, this class is used to send messages between nodes in the system
 */
public class NetworkMessage {
    public static final int PROBE = 0;           // Probe for diffusion
    public static final int PREEMPTION = 1;      // Preemption code to give up resource and break lock
    String stringMessage = "";
    int value = -1;
    int type = -1; // The type of message, used by the node recieve method to determine a course of action, initializes to invalid value
    // Variables needed for Chandy Misra Haas Probe
    int rootSender; // The Id of the originally sending node
    int sender; // The Id of the most recent sending node
    int recipient; // The Id of the intended recipient
    ExclusiveResource exclusiveResource; // The required resource object
    ArrayList exclusiveResources = new ArrayList();

    public NetworkMessage() {

    }

    public NetworkMessage(String inputString) {
        stringMessage = inputString;
    }

    public NetworkMessage(int inputValue) {
        value = inputValue;
    }

    public NetworkMessage(String inputString, int inputValue) {
        stringMessage = inputString;
        value = inputValue;
    }

    public NetworkMessage(int inputRootSender, int inputSender, int inputRecipient, ExclusiveResource inputExclusiveResource){
        rootSender = inputRootSender;
        sender = inputSender;
        recipient = inputRecipient;
        exclusiveResource = inputExclusiveResource;
    }
    
    // We do not input a recipient because we don't know who has exclusive control of our resource
    public NetworkMessage(int inputRootSender, int inputSender, ExclusiveResource inputExclusiveResource){
        rootSender = inputRootSender;
        sender = inputSender;
        exclusiveResource = inputExclusiveResource;
    }
    
    // We do not input a recipient because we don't know who has exclusive control of our resource
    public NetworkMessage(int inputRootSender, int inputSender, ArrayList inputExclusiveResource){
        rootSender = inputRootSender;
        sender = inputSender;
        exclusiveResources = inputExclusiveResource;
    }
    
    // We do not input a recipient because we don't know who has exclusive control of our resource
    public NetworkMessage(int inputType, int inputRootSender, int inputSender, ArrayList inputExclusiveResource){
        type = inputType;
        rootSender = inputRootSender;
        sender = inputSender;
        exclusiveResources = inputExclusiveResource;
    }
        
    public int getType() {
        return type;
    }
    
    public void setType(int inputType) {
        type = inputType;
    }
    
    public int getRootSender() {
        return rootSender;
    }
    
    public void setSender(int inputSender) {
        sender = inputSender;
    }
    
    public void setRootSender(int inputRootSender){
        rootSender = inputRootSender;
    }
    public void setExclusiveResources(ArrayList inputExclusiveResources) {
        exclusiveResources = inputExclusiveResources;
    }
    
    public List getExclusiveResources() {
        return exclusiveResources;
    }

    public String toString() {
        return "Root Sender: " + rootSender + " Sender: " + sender + " Recipient: " + recipient;
    }
}
