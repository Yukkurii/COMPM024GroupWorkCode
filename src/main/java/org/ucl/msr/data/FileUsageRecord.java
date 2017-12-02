package org.ucl.msr.data;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;

/**
 * Instances of this class contain information about the usage of the files.
 *
 * @author Blair Butterworth
 * @author Chenghui Fan
 */

public class FileUsageRecord {
	String fileName;
	ArrayList<ZonedDateTime> activateTime;
	ArrayList<ZonedDateTime> deactivateTime;
	
	public FileUsageRecord(String fileName) {
		this.fileName = fileName;
		this.activateTime = new ArrayList<ZonedDateTime>();
		this.deactivateTime = new ArrayList<ZonedDateTime>();
	}
	
	public Duration getDuration() {
		return null;//TODO
	}
	
	public void addUsageTime(ZonedDateTime triggeredTime, String type) {
		if(type.equals("active")) {
			activateTime.add(triggeredTime);
		}
		if(type.equals("deactive")) {
			deactivateTime.add(triggeredTime);
		}
	}
}
