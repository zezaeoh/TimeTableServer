package informations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class TheaterSet {
	private HashMap<Integer,LinkedList<String>> theaterMap;
	private String[][] thIDList = {{"100", "CGV"}, {"200", "롯데시네마"}};


	public TheaterSet() {
		/*
		 * CGV: 100
		 * LotteCinema: 200
		 * MegaBox: 300
		 */
		theaterMap = new HashMap<Integer, LinkedList<String>>();
		theaterMap.put(100,
				new LinkedList<String>(Arrays.asList("CGV", "cgv","씨지브이","씨지비","시지비")));
		theaterMap.put(200,
				new LinkedList<String>(Arrays.asList("롯데시네마", "lotte","lottecinema","롯시")));
		theaterMap.put(300,
				new LinkedList<String>(Arrays.asList("메가박스", "megabox","mega","box","메박")));
	}
	
	public HashMap<Integer, LinkedList<String>> getTheaterMap() {
		return theaterMap;
	}
	public String[][] getThIDList() {
		return thIDList;
	}
}
