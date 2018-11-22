package informations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class CommandSet {
	private LinkedList<String> screeningScheduleLeaf;
	private LinkedList<String> CGVLeaf;
	private HashMap<String,LinkedList<String>>commandMap =new HashMap<String, LinkedList<String>>();

	public CommandSet() {
		screeningScheduleLeaf = new LinkedList<String>(Arrays.asList("상영시간표", "시간표", "영화상영시간표"));
		CGVLeaf = new LinkedList<String>(Arrays.asList("cgv","씨지브이","씨지비","시지비"));
		commandMap.put("상영시간표",screeningScheduleLeaf);
		commandMap.put("cgv",screeningScheduleLeaf);
	}
	
	public HashMap<String, LinkedList<String>> getCommandMap() {
		return commandMap;
	}
	
	
//	CommandMap.put("상영시간")
}

