package robots;

import automail.IMailDelivery;
import automail.Simulation;
import util.RobotSetting;
import util.RobotSetting.RobotType;
import exceptions.ItemTooHeavyException;
import strategies.IMailPool;

public class WeakRobot extends Robot {
	private int maxWeight = 2000;
	
	public WeakRobot(IMailDelivery delivery, IMailPool mailPool) {
		super(delivery, mailPool);
		setConfig();
	}
	
	public void setConfig() {
		type = util.RobotSetting.RobotType.Weak;
	    tube = new StorageTube(util.RobotSetting.STANDARD_CAPACITY);
	    maxCapacity = RobotSetting.STANDARD_CAPACITY;
	}
    /**
     * Sets the route for the robot
     */
    protected void setRoute() throws ItemTooHeavyException{
        /** Pop the item from the StorageUnit */
        deliveryItem = tube.pop();
        if (deliveryItem.weight > maxWeight) throw new ItemTooHeavyException(); 
        /** Set the destination floor */
        destination_floor = deliveryItem.getDestFloor();
    }
   
}
