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

public class FileUsageRecord implements Comparable<FileUsageRecord> {
	private ZonedDateTime triggeredTime;
	private String type;
	private String fileName;
	
	public FileUsageRecord(String fileName, ZonedDateTime triggeredTime, String type) {
		this.fileName = fileName;
		this.triggeredTime = triggeredTime;
		this.type = type;
	}
	
	public ZonedDateTime getTriggeredTime() {
		return this.triggeredTime;
	}
	
	public String getType() {
		return this.type;
	}
	
	public String getFileName() {
		return this.fileName;
	}

	@Override
	public int compareTo(FileUsageRecord otherRecord) {
		if(Duration.between(triggeredTime, otherRecord.getTriggeredTime()).toMillis() > 0) {
			return -1;
		}
		if(Duration.between(triggeredTime, otherRecord.getTriggeredTime()).toMillis() < 0) {
			return 1;
		}
		return 0;
	}
}
