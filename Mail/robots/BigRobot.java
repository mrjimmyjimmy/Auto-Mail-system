package robots;

import automail.IMailDelivery;
import util.RobotSetting;
import strategies.IMailPool;

/**
 * This class extends standard robot class to implement Big robot 
 * BigRobot : with a tube capacity of six, and effectively able to carry mail items of any weight.
 */
public class BigRobot extends Robot{
	
	public BigRobot(IMailDelivery delivery, IMailPool mailPool) {
		super(delivery, mailPool);
		setConfig();
	}
	/**
	 * Set up the configuration to Big robot according to the specification
	 */
	public void setConfig() {
		this.type = util.RobotSetting.RobotType.Big;
		this.tube = new StorageTube(util.RobotSetting.BIG_CAPACITY);
		this.maxCapacity = RobotSetting.BIG_CAPACITY;
		this.speed = 1;
	}

}
