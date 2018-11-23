package informations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class CommandSet {
	private HashMap<String,LinkedList<String>>commandMap;

	public CommandSet() {
		commandMap = new HashMap<String, LinkedList<String>>();
		commandMap.put("상영시간표",
				new LinkedList<String>(Arrays.asList("상영시간표", "시간표", "영화상영시간표", "영화시간표", "영화", "영화들")));
	}
	
	public HashMap<String, LinkedList<String>> getCommandMap() {
		return commandMap;
	}
	
}

