package org.ucl.msr.data;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Instances of this class contain information about the performance of each user in each day.
 *
 * @author Blair Butterworth
 * @author Chenghui Fan
 */

public class PerformanceData {
	
	//<sessionID, PerformanceDetails>
	private Map<String, FileUsageRecord> performance;
	
	public PerformanceData() {
		this.performance = new ConcurrentHashMap<>();
	}
	
	public void addFileUsageRecord(String IDESessionUUID, String fileName, ZonedDateTime triggeredTime, String type){
		//TODO
	}
	
	public Map<String, Float> getPerformance(){
		return null;//TODO
	}
}
