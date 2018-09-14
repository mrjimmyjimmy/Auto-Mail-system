package robots;
import java.util.List;

import util.RobotSetting;
import util.RobotSetting.RobotType;

public class RobotTypesRegister {
	public List<RobotType> robotTypes;
	public RobotTypesRegister(){
		
	}
	
	public RobotTypesRegister(List<RobotType> robotTypes){
		this.robotTypes = robotTypes;
	}
	
	public boolean isOnlyWeak() {
		int weekCount = 0;
		for(RobotType type : robotTypes) {
			if (type == RobotSetting.RobotType.Weak) {
				weekCount ++;
			}
		}
		return (weekCount == robotTypes.size()) ? true : false;
	}
	
	public boolean isHasCareful() {
		for(RobotType type : robotTypes) {
			if(type == RobotSetting.RobotType.Careful) {
				return true;
			}
		}
		return false;
	}
	
	public int sizeOfRobotList() {
		return robotTypes.size();
	}
}
