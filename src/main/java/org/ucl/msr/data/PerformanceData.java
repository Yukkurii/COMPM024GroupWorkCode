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
	
	public void addFileUsageRecord(String IDESessionUUID, String fileName, ZonedDateTime triggeredTime, String type){
		Collection<FileUsageRecord> fileUsageRecords = this.performance.get(IDESessionUUID);
		fileUsageRecords = fileUsageRecords == null ? Collections.synchronizedCollection(new ArrayList<FileUsageRecord>()) : fileUsageRecords;
		synchronized(fileUsageRecords) {
			fileUsageRecords.add(new FileUsageRecord(fileName, triggeredTime, type));
		}
		performance.put(IDESessionUUID, fileUsageRecords);
	}
	
	//return <sessionID, performance>
	public Map<String, Long> getPerformance(){
		Map<String, Long> outputPerformance = new HashMap<String, Long>();
		Iterator<String> i = performance.keySet().iterator();
		while(i.hasNext()) {
			String sessionID = i.next();
			long minutesPerFile = calcDuration(sessionID).toMinutes()/calcFileNum(sessionID);
			outputPerformance.put(sessionID, 1/minutesPerFile);
		}
		return outputPerformance;
	}
	
	private Duration calcDuration(String IDESessionUUID) {
		ArrayList<FileUsageRecord> fileUsageRecords = (ArrayList<FileUsageRecord>) performance.get(IDESessionUUID);
		if (fileUsageRecords == null) {	//in case the dataset has some unexpected data missing
			return null;
		}
		Collections.sort(fileUsageRecords);	//sort by triggeredTime
		
		Map<String, ZonedDateTime> activatedFile = new HashMap<String, ZonedDateTime>();
		Duration duration = Duration.ZERO;
		ZonedDateTime lastActiveTime = null;
		
		Iterator<FileUsageRecord> i = fileUsageRecords.iterator();
		while(i.hasNext()) {
			FileUsageRecord r = i.next();
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
		}
		return duration;
	}
	
	private int calcFileNum(String IDESessionUUID) {
		ArrayList<FileUsageRecord> fileUsageRecords = (ArrayList<FileUsageRecord>) performance.get(IDESessionUUID);
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
