package robots;

import automail.IMailDelivery;
import automail.Simulation;
import exceptions.ExcessiveDeliveryException;
import exceptions.FragileItemBrokenException;
import exceptions.ItemTooHeavyException;
import mailItems.MailItem;
import robots.Robot.RobotState;
import util.Building;
import util.Clock;
import util.RobotSetting;
import strategies.IMailPool;

public class BigRobot extends Robot{

	public BigRobot(IMailDelivery delivery, IMailPool mailPool) {
		super(delivery, mailPool);
		setConfig();
	}

	public void setConfig() {
		type = util.RobotSetting.RobotType.Big;
		tube = new StorageTube(util.RobotSetting.BIG_CAPACITY);
		maxCapacity = RobotSetting.BIG_CAPACITY;
	}

}
