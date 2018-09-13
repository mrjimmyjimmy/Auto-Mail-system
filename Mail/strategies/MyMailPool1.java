package strategies;

import robots.*;
import util.Clock;
import util.robotSetting;
import util.robotSetting.RobotType;
import mailItems.*;
import exceptions.*;
import automail.*;

import java.awt.print.Printable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.sun.accessibility.internal.resources.accessibility;
import com.sun.glass.ui.CommonDialogs.Type;

//import automail.*;

public class MyMailPool1 implements IMailPool {
	
	
	/**
	* @DataStructures
	* Each robot has 2 states,  UNAVAILIABLE means returning and delivering  
	* States are linked to robot by key and value, Hashmap achieves O(1) data access
	* Six types of mails are stored in three different stack
	*/
	public enum RobotState { WAITING, UNAVAILIABLE }
	Map<Robot, RobotState> robots = new HashMap<Robot,RobotState>(); 
	
//	fragilePool store all fragile mails
//	weakPool store mails which weight are under 2000
//	strongPool store mails which weight are uper 2000
	private Stack<MailItem> fragilePool  = new Stack<MailItem>(); 
	private Stack<MailItem> weakPool = new Stack<MailItem>();
	private Stack<MailItem> strongPool = new Stack<MailItem>();
		
	
	
	@Override
	
	public void addToPool(MailItem mailItem) {
		
		System.out.println(mailItem.weight);
		if(mailItem.getFragile()) {
			fragilePool.push(mailItem);
			}
		else if (mailItem.getWeight() < robotSetting.WEAK_CAPACITY_WEIGHT) {
			weakPool.push(mailItem);	
		}
		else {
			strongPool.push(mailItem);

		}
		
//		sort the pool when a new mail is added
		sortPool(fragilePool);
		sortPool(strongPool);
		sortPool(weakPool);
	}
	
	
	
	
	private void sortPool(Stack<MailItem> stack) {
		Stack<MailItem> help = new Stack<MailItem>();
		while (!stack.isEmpty()) {
			MailItem cur = stack.pop();	
			double curScore = getScore(cur);
			while (!help.isEmpty() && getScore(help.peek()) > curScore) {
				
				stack.push(help.pop());
			}
			help.push(cur);
		}
		
		while (!help.isEmpty()) {
			stack.push(help.pop());
		}		
	
}

	/**
	 * 
	 * @param mailItem
	 * @return the score for the mailitem
	 */

	private static double getScore(MailItem mailItem) {	
		return Math.pow(Clock.Time() - mailItem.getArrivalTime(),1.2)*(1+Math.sqrt(mailItem.getWeight()));	
	}
	
	
	/**
	* Repeatedly check the available robot by check value of given
	* If there are available robots 
	*/
	@Override
	public void step() throws FragileItemBrokenException {
		// TODO Auto-generated method stub
		for (Robot currRobot : robots.keySet() ) {
			if(robots.get(currRobot) == RobotState.WAITING) {
				fillStorageTube(currRobot);
			}
		}
		
	}
	
	/**
	 * fill in the tube for the current robot
	 * contains four methods depending on the type of robots
	 * @param currRobot
	 */

	private void fillStorageTube(Robot robot) {
		RobotType type = robot.getRobotType();
		StorageTube tube = robot.getTube();
		try {
			pickMailFromPool(robot, tube, type);
		} catch (TubeFullException | FragileItemBrokenException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		startToLeave(robot, tube);
	}		

	

	
	private void startToLeave(Robot robot, StorageTube tube) {
		sortTube(tube);
		if (tube.getSize() > 0 ) {		
			robot.dispatch();
		}
	}
	
	private void pickMailFromPool(Robot robot, StorageTube tube, RobotType type) throws TubeFullException, FragileItemBrokenException {
		
		switch (type) {
		case Careful:
			int max = robotSetting.MAX_FRAGILE_MAIL;
			while (!fragilePool.isEmpty() && tube.getSize() < max) {
					tube.addItem(fragilePool.pop());
			}
			break;
		case Big:
			int max1 = robotSetting.BIG_CAPACITY;
			pickStrongpoolMail(robot, tube, max1);
			break;
		case Standard:
			int max2 = robotSetting.STANDARD_CAPACITY;
			pickStrongpoolMail(robot, tube, max2);
		
			break;
		case Weak:
			pickWeakpoolMail(robot, tube);
			
		default:
			break;
		}

		
	}


	private void pickStrongpoolMail(Robot robot, StorageTube tube, int max) throws TubeFullException, FragileItemBrokenException{
		while (!strongPool.isEmpty() && tube.getSize() < max) {
			tube.addItem(strongPool.pop());
		}
		pickWeakpoolMail(robot, tube);
	}

	private void pickWeakpoolMail(Robot robot, StorageTube tube) throws TubeFullException, FragileItemBrokenException{
		while (!weakPool.isEmpty() && tube.getSize() < robotSetting.STANDARD_CAPACITY) {
			tube.addItem(weakPool.pop());
		}
	}


//	this method to sort the mail_items in the tube, according to their destination_floor
	private void sortTube(StorageTube tube) {
		Stack<MailItem> help = new Stack<MailItem>();
		while (!tube.isEmpty()) {
			MailItem cur = tube.pop();	
			double curScore = cur.getDestFloor();
			while (!help.isEmpty() && help.peek().getDestFloor() > curScore) {
				
				try {
					tube.addItem(help.pop());
				} catch (TubeFullException | FragileItemBrokenException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			help.push(cur);
		}
		
		while (!help.isEmpty()) {
			try {
				tube.addItem(help.pop());
			} catch (TubeFullException | FragileItemBrokenException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	
}

	@Override
	public void registerWaiting(Robot robot) {
		// TODO Auto-generated method stub
		robots.put(robot, RobotState.WAITING);
	}

	@Override
	public void deregisterWaiting(Robot robot) {
		// TODO Auto-generated method stub
		robots.put(robot, RobotState.UNAVAILIABLE);
	}
	
	
	
}