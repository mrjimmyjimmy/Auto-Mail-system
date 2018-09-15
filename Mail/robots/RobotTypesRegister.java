package robots;
import java.util.List;

import util.RobotSetting;
import util.RobotSetting.RobotType;

/**
 * This class registered the current robot by its type
 * It can be used to check if there exists the condition that the robots unable to deliver
 * For example, if there are only weak robots, the items that exceeds 2000 grams can not be delivered eventually
 */
public class RobotTypesRegister {
	public List<RobotType> robotTypes;
	/**
	 * Constructor that is used to initial the register
	 */
	public RobotTypesRegister(){
		
	}
	/**
	 * Constructor that is used to set up the register list
	 * @param robotTypes
	 */
	
	public RobotTypesRegister(List<RobotType> robotTypes){
		this.robotTypes = robotTypes;
	}
	
	/**
	 * Check if there is only weak robot in the register list
	 * @return
	 */
	public boolean isOnlyWeak() {
		int weekCount = 0;
		for(RobotType type : robotTypes) {
			if (type == RobotSetting.RobotType.Weak) {
				weekCount ++;
			}
		}
		return (weekCount == robotTypes.size()) ? true : false;
	}
	/**
	 * check if there is careful robot in the register list
	 * @return
	 */
	public boolean isHasCareful() {
		for(RobotType type : robotTypes) {
			if(type == RobotSetting.RobotType.Careful) {
				return true;
			}
		}
		return false;
	}
	/**
	 * get the current robot type register list
	 * which is equal to the number robots 
	 * @return
	 */
	public int sizeOfRobotList() {
		return robotTypes.size();
	}
}
