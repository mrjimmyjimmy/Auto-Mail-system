package strategies;

import robots.*;
import util.Clock;
import util.robotSetting.RobotType;
import mailItems.*;
import exceptions.*;

import java.awt.print.Printable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.sun.accessibility.internal.resources.accessibility;
import com.sun.glass.ui.CommonDialogs.Type;

import automail.*;

public class MyMailPool1 implements IMailPool {
	
	public enum RobotState { WAITING, UNAVAILIABLE }
	Map<Robot, RobotState> robots = new HashMap<Robot,RobotState>(); 
	private Stack<MailItem> fragilePool  = new Stack<MailItem>();
	private Stack<MailItem> weakPool = new Stack<MailItem>();
	private Stack<MailItem> strongPool = new Stack<MailItem>();
		
	
	
	@Override
	
	public void addToPool(MailItem mailItem) {
		
		System.out.println(mailItem.weight);
		if(mailItem.getFragile()) {
			fragilePool.push(mailItem);
			}
		else if (mailItem.getWeight() < 2000) {
			weakPool.push(mailItem);
			
			
			
		}
		else {
			strongPool.push(mailItem);

		}
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


	private static double getScore(MailItem mailItem) {
		
		return Math.pow(Clock.Time() - mailItem.getArrivalTime(),1.2)*(1+Math.sqrt(mailItem.getWeight()));
		
	}
	

	@Override
	public void step() throws FragileItemBrokenException {
		// TODO Auto-generated method stub
		for (Robot currRobot : robots.keySet() ) {
			if(robots.get(currRobot) == RobotState.WAITING) {
				fillStorageTube(currRobot);
			}
		}
		
	}

	private void fillStorageTube(Robot currRobot) {
		// TODO Auto-generated method stub
		RobotType type = currRobot.getRobotType();
		switch (type) {
		case Careful:
			fillCarefulRobots(currRobot);
			break;
			
		case Weak:
			fillWeakRobots(currRobot);
			break;
			
		case Standard:
			fillStandardRobots(currRobot);
			break;
			
		case Big:
			fillBigRobots(currRobot);
			break;

		default:
			break;
		}
		
		
	}

	private void fillCarefulRobots(Robot robot) {
		// TODO Auto-generated method stub
		StorageTube tube = robot.getTube();
		try {
			while (!fragilePool.isEmpty() && tube.getSize() < 1) {
				tube.addItem(fragilePool.pop());
			}
			if (tube.getSize() > 0 ) {
				
//				sortTube(tube);
				robot.dispatch();
			}
		} catch (TubeFullException | FragileItemBrokenException e) {
			e.printStackTrace();
		}
	}

	private void fillBigRobots(Robot robot) {
		StorageTube tube = robot.getTube();
		try {
			while (!strongPool.isEmpty() && tube.getSize() < 6) {
				tube.addItem(strongPool.pop());
			}
			while (!weakPool.isEmpty() && tube.getSize() < 4) {
				tube.addItem(weakPool.pop());
			}
			
			if (tube.getSize() > 0 ) {
				
//				sortTube(tube);
				robot.dispatch();
			}
		} catch (TubeFullException | FragileItemBrokenException e) {
			e.printStackTrace();
		}
		
	}

	private void fillStandardRobots(Robot robot) {
		StorageTube tube = robot.getTube();
		try {
			while (!strongPool.isEmpty() && tube.getSize() < 4) {
				tube.addItem(strongPool.pop());
			}
			while (!weakPool.isEmpty() && tube.getSize() < 4) {
				tube.addItem(weakPool.pop());
			}
			if (tube.getSize() > 0 ) {
				
//				sortTube(tube);
				robot.dispatch();
			}
		} catch (TubeFullException | FragileItemBrokenException e) {
			e.printStackTrace();
		}
		
	}

	private void fillWeakRobots(Robot robot) {
		StorageTube tube = robot.getTube();
		try {
			while (!weakPool.isEmpty() && tube.getSize() < 4) {
				tube.addItem(weakPool.pop());
			}
			if (tube.getSize() > 0 ) {
				
//				sortTube(tube);
				robot.dispatch();
			}
		} catch (TubeFullException | FragileItemBrokenException e) {
			e.printStackTrace();
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