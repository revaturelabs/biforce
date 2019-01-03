package com.revature.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * helper class to find modes
 * @Deprecated as of 11-29-18, replaced by static method com.revature.reducer.BatteryOutlierReducer.mode(List<Integer>);
 * @author Evan Diamond
 *
 */
public class Mode {
	
	/**
	 * Calculates mode of a List of Integers
	 * @Deprecated as of 11-29-18, obsolete due to existence BatteryOutlierDrver
	 */
	@Deprecated public static int mode(List<Integer> list){
		Map<Integer, Integer> counts = new HashMap<>();
		for (Integer number : list){
			Integer count = counts.get(number);
			if (count != null) count++;
			else counts.put(number, 1);
		}
		Integer max = Collections.max(counts.keySet());
		return max;
	}

}
