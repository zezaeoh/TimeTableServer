package informations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class CommandSet {
	private HashMap<String,LinkedList<String>>commandMap;

	public CommandSet() {
		commandMap = new HashMap<String, LinkedList<String>>();
		commandMap.put("상영시간표",
				new LinkedList<String>(Arrays.asList("상영시간표", "시간표", "영화상영시간표")));
		commandMap.put("CGV",
				new LinkedList<String>(Arrays.asList("CGV", "cgv","씨지브이","씨지비","시지비")));
	}
	
	public HashMap<String, LinkedList<String>> getCommandMap() {
		return commandMap;
	}
	
}

