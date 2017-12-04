package org.ucl.msr.report;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

        Map<String, Long> sessionPerformance = performanceData.getPerformanceAsLong();
        Map<String, Long> userPerformance = convertSessionsToProfiles(sessionPerformance);

        writeToFile(fileWriter, userPerformance);
    }

    private Map<String, Long> convertSessionsToProfiles(Map<String, Long> sessionPerformance)
    {
        Map<String, Long> result = new HashMap<>();
        Map<String, ProfileDetails> profiles = eventData.getProfileData().getProfiles();
        
        for(String profileID : profiles.keySet()) {
        	int sessionNum = 0;
        	long userPerformance = 0;
        	Collection<String> sessionIDs = profiles.get(profileID).getSessions();
        	for(String sessionID : sessionIDs) {
        		if(sessionPerformance.get(sessionID)!= null) {
        			userPerformance = userPerformance + sessionPerformance.get(sessionID);
            		sessionNum++;
        		}
        	}
        	if(sessionNum != 0) {
        		result.put(profileID, userPerformance/sessionNum);
        	}	
        }
        return result;
    }

    private void writeToFile(FileWriter fileWriter, Map<String, Long> performance) throws IOException
    {
    	for (Map.Entry<String, Long> entry : performance.entrySet()) { 
    		fileWriter.append(entry.getKey() + "," + eventData.getProfileData().getProfileDetails(entry.getKey()).getEducation().toString() + "," + entry.getValue() + "\n");
    	}
    }
}
