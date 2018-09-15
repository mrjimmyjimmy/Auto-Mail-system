package robots;

import automail.IMailDelivery;
import exceptions.FragileItemBrokenException;
import util.RobotSetting;
import strategies.IMailPool;

/**
 * Careful Robot : with a tube capacity of three, and effectively able to carry mail items of any weight, 
 * but slower than the other robots
 */
public class CarefulRobot extends Robot {

	private int step = 0;
	public CarefulRobot(IMailDelivery delivery, IMailPool mailPool) {
		super(delivery, mailPool);
		setConfig();
	}
	
	/**
	 * Set up the configuration to Careful robot according to the specification
	 */
	public void setConfig() {
		type = util.RobotSetting.RobotType.Careful;
		tube = new StorageTube(util.RobotSetting.CAREFUL_CAPACITY);
		maxCapacity = RobotSetting.CAREFUL_CAPACITY;
		this.speed = 1;
	}
	
	/**
	 * According to specification
	 * the movement of careful robot is half the speed of the existing robots
	 * the first step, the Careful robot will not move; the next step (and every subsequent second step),
	 * the careful robot will move one floor
	 */
	protected void moveTowards(int destination) throws FragileItemBrokenException {
		step = step + 1;
		if (step == 2) {
			if(current_floor < destination){
				current_floor = current_floor + speed;
			}
			else{
	          current_floor = current_floor - speed;
			}
			step = 0;
          }
	   }
}
