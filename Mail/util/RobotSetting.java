package util;

public class RobotSetting {
	
	/** Capacity for standard robot **/
    public final static int STANDARD_CAPACITY = 4;
    
    /** Capacity for big robot **/
    public final static int BIG_CAPACITY = 6;
    
    /** Capacity for careful robot **/
    public final static int CAREFUL_CAPACITY = 3;
    
    /** Maximum weight for an item for weak robot **/
    public final static int WEAK_CAPACITY_WEIGHT = 2000;
    
    /** Standard Speed **/
    public final static int STANDARD_SPEED = 1;
    
    /** Predefined Robot types **/
    public static enum RobotType { Big, Careful, Standard, Weak };
}
