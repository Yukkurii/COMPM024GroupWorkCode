package org.ucl.msr.report;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.ucl.msr.data.EventData;
import org.ucl.msr.data.PerformanceData;
import org.ucl.msr.data.ProfileDetails;

public class PerformanceReport {
	private EventData eventData;

    public PerformanceReport(EventData eventData)
    {
        this.eventData = eventData;
    }

    public void writeToFile(String directory, String name) throws IOException
    {
        File destination = createFile(directory, name);

        try (FileWriter fileWriter = new FileWriter(destination))
        {
            writeToFile(fileWriter);
        }
    }

    private File createFile(String directory, String name)
    {
        File file = new File(directory, name);
        file.mkdirs();
        file.delete();
        return file;
    }

    private void writeToFile(FileWriter fileWriter) throws IOException
    {
        PerformanceData performanceData = eventData.getPerformanceData();

        Map<String, ArrayList<Long>> sessionPerformance = performanceData.getPerformanceAsLong();
        Map<String, ArrayList<Long>> userPerformance = convertSessionsToProfiles(sessionPerformance);

        writeToFile(fileWriter, userPerformance);
    }

    private Map<String, ArrayList<Long>> convertSessionsToProfiles(Map<String, ArrayList<Long>> sessionPerformance)
    {
    	Map<String, ArrayList<Long>> result = new HashMap<>();
        Map<String, ProfileDetails> profiles = eventData.getProfileData().getProfiles();
        
        for(String profileID : profiles.keySet()) {
        	int sessionNum = 0;
        	long userPerformance = 0;
        	long fileNum = 0;
        	long duration = 0;
        	Collection<String> sessionIDs = profiles.get(profileID).getSessions();
        	for(String sessionID : sessionIDs) {
        		if(sessionPerformance.get(sessionID)!= null) {
        			duration = duration + sessionPerformance.get(sessionID).get(0);
        			fileNum = fileNum + sessionPerformance.get(sessionID).get(1);
        			userPerformance = userPerformance + sessionPerformance.get(sessionID).get(2);
            		sessionNum++;
        		}
        	}
        	if(sessionNum != 0) {
        		ArrayList<Long> performance = new ArrayList<Long>();
        		performance.add((long)sessionNum);		//sessionNum
        		performance.add(duration/sessionNum);	//duration per session
        		performance.add(fileNum/sessionNum);	//fileNum per session
        		performance.add(userPerformance/sessionNum);	//avg duration per file
        		result.put(profileID, performance);
        	}	
        }
        return result;
    }

    private void writeToFile(FileWriter fileWriter, Map<String, ArrayList<Long>> performance) throws IOException
    {
    	fileWriter.append("profileID,Number of days,AVG duration per day,AVG number of file per day,AVG duration per file");
    	for (Map.Entry<String, ArrayList<Long>> entry : performance.entrySet()) { 
    		fileWriter.append(entry.getKey() + "," + eventData.getProfileData().getProfileDetails(entry.getKey()).getEducation().toString() + "," + entry.getValue().get(0) + "," + entry.getValue().get(1) + "," + entry.getValue().get(2) + "," + entry.getValue().get(3) + "\n");
    	}
    }
}
