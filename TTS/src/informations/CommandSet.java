package informations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.activation.CommandMap;

public class CommandSet {
	private ArrayList<String> screeningScheduleLeaf;
	private ArrayList<String> CGVLeaf;
	private HashMap<String,ArrayList<String>>commandMap =new HashMap<String, ArrayList<String>>();

	public CommandSet() {
		screeningScheduleLeaf = new ArrayList<String>(Arrays.asList("상영시간표", "시간표", "영화상영시간표"));
		CGVLeaf = new ArrayList<String>(Arrays.asList("cgv","씨지브이","씨지비","시지비"));
		commandMap.put("상영시간표",screeningScheduleLeaf);
		commandMap.put("cgv",screeningScheduleLeaf);
	}
	
	public HashMap<String, ArrayList<String>> getCommandMap() {
		return commandMap;
	}
	
	
//	CommandMap.put("상영시간")
}

