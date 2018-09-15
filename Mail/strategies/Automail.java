package strategies;

import java.util.List;

import automail.IMailDelivery;
import robots.BigRobot;
import robots.CarefulRobot;
import robots.Robot;
import robots.WeakRobot;
import util.RobotSetting.RobotType;

public class Automail {
	      
    public Robot[] robot;
    public IMailPool mailPool;
    public IMailDelivery delivery;
    List<RobotType> RobotTypes;
    /**
     * This class acts as a creator to  create the robot according to the given robot
     */
    public Automail(IMailPool mailPool, IMailDelivery delivery,List<RobotType> RobotTypes) {
    	// Swap between simple provided strategies and your strategies here
    	/** Initialize the MailPool */  	
    	this.mailPool = mailPool;
    	this.delivery = delivery;
    	this.RobotTypes = RobotTypes;
    	/** Initialize robots */
    	robot = new Robot[this.RobotTypes.size()];
    	init();
    }
    /**
     * fill the robot into the robot list
     */
    public void init() {
    	for(int i = 0 ; i < RobotTypes.size(); i++) {
    		robot[i] = createRobotByType(RobotTypes.get(i));
    	}
    }
    /**
     * Create robot by the given type
     * @param type
     * @return
     */
    public Robot createRobotByType(RobotType type) {
    	Robot robot = null;
    	switch(type) {
    	case Weak: 
    		robot = new WeakRobot(delivery, mailPool);
    		break;
    	case Careful:
    		robot = new CarefulRobot(delivery, mailPool);
    		break;
    	case Standard:
    		robot = new Robot(delivery, mailPool);
    		break;
    	case Big:
    		robot = new BigRobot(delivery, mailPool);
    		break;
    	}
    	return robot;
    }
}
