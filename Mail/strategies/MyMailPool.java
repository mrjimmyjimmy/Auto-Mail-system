package strategies;

import robots.*;
import util.Clock;
import util.RobotSetting;
import util.RobotSetting.RobotType;
import mailItems.*;
import exceptions.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/** 
 * @Author : Ziang Chen, Zhe Xu, Jiaqi Li, The University of Melbourne
 * SWEN 30006 SEM2 - 2018 , PartB - Judgment Day
 * This class implements the strategy of selecting the mails from mail pool
 * The strategy following 4 principles:
 * 1. Pick from sorted pool, as the pool is sorted, selection is from top to bottom
 * to make sure the item with higher priority is selected 
 * 2. Careful robot first seeks one fragile mail and then other non-fragile mail
 * 3. Standard, Big robot first seek from heavy pool
 * 4. Weak Robot only seek from light pool
**/
public class MyMailPool implements IMailPool {
	/**
	* @DataStructures
	* Each robot has 2 states,  UNAVAILIABLE means returning and delivering  
	* States are linked to robot by key and value, Hashmap achieves O(1) data access
	* Six types of mails are stored in three different stack
	*/
	public enum RobotState { WAITING, UNAVAILIABLE }
	Map<Robot, RobotState> robots = new HashMap<Robot,RobotState>(); 
    private RobotTypesRegister typeRegister = new RobotTypesRegister();

    /**
     * @DataStructures
     * 	fragilePool store all fragile mails
     * 	lightPool store mails which weight are under 2000
     * 	heavyPool store mails which weight are bigger than 2000
     */
	private Stack<MailItem> fragilePool  = new Stack<MailItem>(); 
	private Stack<MailItem> lightPool = new Stack<MailItem>();
	private Stack<MailItem> heavyPool = new Stack<MailItem>();
	
	@Override
	public void connectRegister(RobotTypesRegister typeRegister) {
		this.typeRegister = typeRegister;
	}
	
	@Override
	/**
	 * @param mailItem
	 * Add a mail to one of the three pools
	 */
	public void addToPool(MailItem mailItem) {
		if(mailItem.getFragile()) {
			fragilePool.push(mailItem);
		}
		else if (mailItem.getWeight() < RobotSetting.WEAK_CAPACITY_WEIGHT) {
			lightPool.push(mailItem);	
		}
		else {
			heavyPool.push(mailItem);
		}
		// sort the pool when a new mail is added
		sortPool(fragilePool);
		sortPool(heavyPool);
		sortPool(lightPool);
	}
	/**
	 * Sort the pool, according to their delivery score. 
	 * Make sure every time the robot will pick up a mail with smallest score.
	 * @param pool
	 */
	private void sortPool(Stack<MailItem> stack) {
		// create an helpBuffer_stack to helpBuffer sorting.
		Stack<MailItem> helpBuffer = new Stack<MailItem>();
		while (!stack.isEmpty()) {
			//	pop an item from stack and compare its score with the one from the top of the helpBuffer_stack
			MailItem cur = stack.pop();	
			double curScore = getScore(cur);
			while (!helpBuffer.isEmpty() && getScore(helpBuffer.peek()) > curScore) {
				stack.push(helpBuffer.pop());
			}
			helpBuffer.push(cur);
		}		
		while (!helpBuffer.isEmpty()) {
			stack.push(helpBuffer.pop());
		}	
	}
	/**
	 * Calculate score of delivering a mail item.
	 * @param mailItem
	 * @return the score for the mailItem
	 */
	private static double getScore(MailItem mailItem) {	
		return Math.pow(Clock.Time() - mailItem.getArrivalTime(),1.2)*(1+Math.sqrt(mailItem.getWeight()));	
	}
	/**
	* Repeatedly check the available robot by check value of given.
	* If there are available robots.
	* After that, if any robots are waiting, fill the robots with mails.
	* Check If there exists condition that all robot unable to deliver the mail left
	* @throws FragileItemCannotDeliverException 
	* @throws HeavyItemCannotDeliverException 
	*/
	@Override
	public void step() throws FragileItemBrokenException, HeavyItemCannotDeliverException, FragileItemCannotDeliverException {
		// TODO Auto-generated method stub
		if(waitingRobotNum() == typeRegister.sizeOfRobotList() 
				&& (lightPool.size() == 0 || fragilePool.size() > 0 
				&& heavyPool.size() == 0 && lightPool.size() == 0)) {
			checkUnableDeliveryCondition();
		}
		
		for (Robot currRobot : robots.keySet() ) {
			if(robots.get(currRobot) == RobotState.WAITING) {
				fillStorageTube(currRobot);
			}
		}
	}
	/**
	 * The strong/fragile Pool have items and there are no Standard/Big/Careful robots to delivery these items.
	 * Throw the exception to show the mail left are not able to finish because there requires particular robot types
	 * @throws HeavyItemCannotDeliverException
	 * @throws FragileItemCannotDeliverException
	 */
	private void checkUnableDeliveryCondition() throws HeavyItemCannotDeliverException, FragileItemCannotDeliverException {
		if(heavyPool.size() > 0 && typeRegister.isOnlyWeak()) {
			throw new HeavyItemCannotDeliverException();
		}else if(fragilePool.size() > 0 && !typeRegister.isHasCareful()) {
			throw new FragileItemCannotDeliverException();
		}
	}
	/**
	 * fill in the tube for the current robot
	 * @param currRobot
	 */
	private void fillStorageTube(Robot robot) {
		RobotType type = robot.getRobotType();
		StorageTube tube = robot.getTube();
		try {
			pickMailFromPool(robot, tube, type);
		} catch (TubeFullException | FragileItemBrokenException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		startToLeave(robot, tube);
	}		
	/**
	 * After the robot's tube is filled with item, set the path and start to delivery
	 * @param robot
	 * @param tube
	 */
	private void startToLeave(Robot robot, StorageTube tube) {
		sortTube(tube);
		if (tube.getSize() > 0 ) {		
			robot.dispatch();
		}
	}
	/**
	 * Fill the robot's tube with mail according to their type.
	 * For Careful robot, it first check the fragile item pool, then heavy pool and light pool
	 * For Weak robot, it only checks light pool
	 * For Standard robot, it first check the strong pool then heavy pool
	 * For Big robot, it has same sequence as the standard robot
	 */
	private void pickMailFromPool(Robot robot, StorageTube tube, RobotType type) throws TubeFullException, FragileItemBrokenException {
		//	the max capability of the robot's tube
		int max = robot.getMaximumCapacity();
		switch (type) {
		case Careful:
		    pickFragilePoolMail(robot, tube, max);
			break;
		case Big:
			pickHeavyPoolMail(robot, tube, max);
			break;
		case Standard:
			pickHeavyPoolMail(robot, tube, max);
			break;
		case Weak:
			pickLightPoolMail(robot, tube, max);
			break;
		}
	}

	/**
	 * For Strong and Big robots
	 * First pick up mails from Strong pool
	 * Then pick up item from Weak pool
	 * @param robot
	 * @param tube
	 * @param max
	 * @throws TubeFullException
	 * @throws FragileItemBrokenException
	 */
	private void pickHeavyPoolMail(Robot robot, StorageTube tube, int max) throws TubeFullException, FragileItemBrokenException{
		while (!heavyPool.isEmpty() && tube.getSize() < max) {
			tube.addItem(heavyPool.pop());
		}
		pickLightPoolMail(robot, tube, max);
	}
	
	/**
	 * For Careful robot, pick up item from fragile pool
	 * @param robot
	 * @param tube
	 * @param max
	 * @throws TubeFullException
	 * @throws FragileItemBrokenException
	 */
	private void pickFragilePoolMail(Robot robot, StorageTube tube, int max) throws TubeFullException, FragileItemBrokenException{
		if(!fragilePool.isEmpty() && !tube.isContainFragile()) {
			tube.addItem(fragilePool.pop());
		}else {
			pickHeavyPoolMail(robot, tube, max);
		}
	}
	/**
	 * For Weak robot, only pick up item from weak pool
	 * @param robot
	 * @param tube
	 * @param max
	 * @throws TubeFullException
	 * @throws FragileItemBrokenException
	 */
	private void pickLightPoolMail(Robot robot, StorageTube tube, int max) throws TubeFullException, FragileItemBrokenException{
		while (!lightPool.isEmpty() && tube.getSize() < max) {
			tube.addItem(lightPool.pop());
		}
	}
	/**
	 * this method to sort the mail_items in the tube, according to their destination_floor
	 * @param tube
	 */
	private void sortTube(StorageTube tube) {
		Stack<MailItem> helpBuffer = new Stack<MailItem>();
		while (!tube.isEmpty()) {
			MailItem cur = tube.pop();	
			double curScore = cur.getDestFloor();
			while (!helpBuffer.isEmpty() && helpBuffer.peek().getDestFloor() > curScore) {
				try {
					tube.addItem(helpBuffer.pop());
				} catch (TubeFullException | FragileItemBrokenException e) {
					e.printStackTrace();
				}
			}
			helpBuffer.push(cur);
		}
		while (!helpBuffer.isEmpty()) {
			try {
				tube.addItem(helpBuffer.pop());
			} catch (TubeFullException | FragileItemBrokenException e) {
				e.printStackTrace();
			}
		}		
    }
	/**
	 * Get robots number who are in waiting state
	 * @return number
	 */
	public int waitingRobotNum() {
		int waitings = 0;
		for (Robot currRobot : robots.keySet()) {
			if(robots.get(currRobot) == RobotState.WAITING) {
				waitings++;
			}
		}
		return waitings;
	}
	@Override
	/**
	 * Register waiting robot
	 */
	public void registerWaiting(Robot robot) {
		// TODO Auto-generated method stub
		robots.put(robot, RobotState.WAITING);
	}
	@Override
	/**
	 * Register unavailable robot
	 */
	public void deregisterWaiting(Robot robot) {
		// TODO Auto-generated method stub
		robots.put(robot, RobotState.UNAVAILIABLE);
	}
}