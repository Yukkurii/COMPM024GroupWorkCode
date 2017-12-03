package org.ucl.msr.data;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Instances of this class contain information about the performance of each user in each day.
 * 		performance = 1/ (duration/workload)
 * 		duration 	= sum( Duration.between(windowActive, windowDeactive) )
 * 		workload	= fileNum
 *
 * @author Blair Butterworth
 * @author Chenghui Fan
 */

public class PerformanceData {
	
	//<sessionID, <FileUsageRecord>>
	private Map<String, Collection<FileUsageRecord>> performance;
	
	public PerformanceData() {
		this.performance = new ConcurrentHashMap<>();
	}
	
	public Map<String, Collection<FileUsageRecord>> getPerformance(){
		return performance;
	}
	
	//return <sessionID, performance>
	public Map<String, Long> getPerformanceAsLong(){
		Map<String, Long> outputPerformance = new HashMap<String, Long>();
		Iterator<String> i = performance.keySet().iterator();
//		System.out.println("performancesize:" + performance.size());
		while(i.hasNext()) {
			String sessionID = i.next();
			Duration duration = calcDuration(sessionID);
			int fileNum = calcFileNum(sessionID);
			if(duration != null && fileNum != -1 && duration.toMinutes() != 0) {
				long minutesPerFile = duration.toMinutes()/fileNum;
				outputPerformance.put(sessionID, 1/minutesPerFile);
			}
		}
		return outputPerformance;
	}
	
	private Duration calcDuration(String IDESessionUUID) {
		Collection<FileUsageRecord> fileUsageRecords = performance.get(IDESessionUUID);
		if (fileUsageRecords == null) {	//in case the dataset has some unexpected data missing
			return null;
		}
		
		ArrayList<FileUsageRecord> sortedFileUsageRecords = new ArrayList<FileUsageRecord>();
		Iterator<FileUsageRecord> fileUsageRecordsIterator = fileUsageRecords.iterator();
		while(fileUsageRecordsIterator.hasNext()) {
			FileUsageRecord r = fileUsageRecordsIterator.next();
			sortedFileUsageRecords.add(r);
		}
		
		Collections.sort(sortedFileUsageRecords);	//sort by triggeredTime
		
		Map<String, ZonedDateTime> activatedFile = new HashMap<String, ZonedDateTime>();
		Duration duration = Duration.ZERO;
		ZonedDateTime lastActiveTime = null;
		ZonedDateTime lastOperateTime = null;
		
		Iterator<FileUsageRecord> sortedFileUsageRecordsIterator = sortedFileUsageRecords.iterator();
		while(sortedFileUsageRecordsIterator.hasNext()) {
			FileUsageRecord r = sortedFileUsageRecordsIterator.next();
			if(r.getType().equals("active")) {
				activatedFile.put(r.getFileName(), r.getTriggeredTime());
				if(lastActiveTime == null) {
					lastActiveTime = r.getTriggeredTime();
				}
			}
			if(r.getType().equals("deactive")) {
				activatedFile.remove(r.getFileName());
				if(activatedFile.isEmpty() && lastActiveTime != null) {
					duration = duration.plus(Duration.between(lastActiveTime, r.getTriggeredTime()).abs());
					lastActiveTime = null;
				}
			}
			lastOperateTime = r.getTriggeredTime();
		}
		if(lastActiveTime != null) {	//in case the coding crossed the midnight
			duration = duration.plus(Duration.between(lastActiveTime, lastOperateTime).abs());
		}
		return duration;
	}
	
	private int calcFileNum(String IDESessionUUID) {
		Collection<FileUsageRecord> fileUsageRecords = performance.get(IDESessionUUID);
		if (fileUsageRecords == null) {	//in case the dataset has some unexpected data missing
			return -1;
		}
		
		ArrayList<String> fileNames = new ArrayList<String>();
		int fileNum = 0;
		
		Iterator<FileUsageRecord> i = fileUsageRecords.iterator();
		while(i.hasNext()) {
			FileUsageRecord r = i.next();
			if(!fileNames.contains(r.getFileName())) {
				fileNum++;
				fileNames.add(r.getFileName());
			}
		}
		return fileNum;
	}
}
