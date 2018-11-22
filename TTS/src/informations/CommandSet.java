package informations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.activation.CommandMap;

public class CommandSet {
	private ArrayList<String> timeTableList;
	private HashMap<String,ArrayList<String>>commandMap =new HashMap<String, ArrayList<String>>();

	public CommandSet() {
		timeTableList = new ArrayList<String>(Arrays.asList("상영시간표", "시간표", "영화상영시간표"));
		commandMap.put("상영시간표",timeTableList);
	}
	
	public HashMap<String, ArrayList<String>> getCommandMap() {
		return commandMap;
	}
	
	
//	CommandMap.put("상영시간")
}

