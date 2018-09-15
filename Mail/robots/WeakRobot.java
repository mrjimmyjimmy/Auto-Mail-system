package robots;

import automail.IMailDelivery;
import util.RobotSetting;
import exceptions.ItemTooHeavyException;
import strategies.IMailPool;
/**
 * WeakRobot: with a tube capacity of four, and able to carry mail items no more than 2000 grams.
 */
public class WeakRobot extends Robot {
	
	public WeakRobot(IMailDelivery delivery, IMailPool mailPool) {
		super(delivery, mailPool);
		setConfig();
	}
	/**
	 * Set up the configuration to Weak robot according to the specification
	 */
	public void setConfig() {
		type = util.RobotSetting.RobotType.Weak;
	    tube = new StorageTube(util.RobotSetting.STANDARD_CAPACITY);
	    maxCapacity = RobotSetting.STANDARD_CAPACITY;
	    this.speed = 1;
	}
    /**
     * Sets the route for the robot
     * Throw exception when weak robot trying to contain item that is greater than 2000 grams
     */
    protected void setRoute() throws ItemTooHeavyException{
        /** Pop the item from the StorageUnit */
        deliveryItem = tube.pop();
        if (deliveryItem.weight > RobotSetting.WEAK_CAPACITY_WEIGHT) throw new ItemTooHeavyException(); 
        /** Set the destination floor */
        destination_floor = deliveryItem.getDestFloor();
    }
   
}
