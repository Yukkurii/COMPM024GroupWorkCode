/*
 * UCL MSR Processor (c) by Blair Butterworth and Chenghui Fan
 *
 * This work is licensed under the Creative Commons Attribution 4.0
 * International License. To view a copy of this license, visit
 *
 *      http://creativecommons.org/licenses/by/4.0/
 */

package org.ucl.msr.event;

import java.io.IOException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cc.kave.commons.model.events.IDEEvent;
import cc.kave.commons.model.events.userprofiles.UserProfileEvent;
import cc.kave.commons.model.events.versioncontrolevents.VersionControlAction;
import cc.kave.commons.model.events.versioncontrolevents.VersionControlEvent;
import cc.kave.commons.model.events.visualstudio.EditEvent;
import cc.kave.commons.model.events.visualstudio.WindowEvent;

/**
 * Instances of this class search the MSR data set calculating each users
 * performance.
 *
 * @author Blair Butterworth
 * @author Chenghui Fan
 */

//Goal: 𝑇𝑖𝑚𝑒𝑆𝑝𝑒𝑛𝑡/(𝑁𝑢𝑚𝑏𝑒𝑟𝑂𝑓𝐹𝑖𝑙𝑒𝑀𝑜𝑑𝑖𝑓𝑖𝑒𝑑 ∗ 𝐴𝑣𝑒𝑟𝑎𝑔𝑒𝑇𝑖𝑚𝑒𝑂𝑛𝐸𝑎𝑐ℎ𝐹𝑖𝑙𝑒), ProfileID known	
public class PerformanceProcessor implements EventProcessor
{	
	private Map<String, Collection<String>> sessionsRef;	//<ProfileID, <sessionID>>
	private Collection<EditEvent> editEvents;
	
    public PerformanceProcessor() {
    	sessionsRef = new ConcurrentHashMap<>();
    	editEvents = Collections.synchronizedList(new ArrayList<EditEvent>());
    }
	
	@Override
    public void process(IDEEvent event)
    {
        if (event instanceof EditEvent)
        {
            EditEvent editEvent = (EditEvent)event;
            synchronized(editEvents) {
            	this.editEvents.add(editEvent);
            }
        }
        else if (event instanceof UserProfileEvent)
        {
            UserProfileEvent profileEvent = (UserProfileEvent)event;
            associateSession(profileEvent);
        }
    }
	
    private void associateSession(UserProfileEvent event)
    {
        Collection<String> sessionList = sessionsRef.get(event.ProfileId);
        sessionList = sessionList == null ? Collections.synchronizedList(new ArrayList<String>()) : sessionList;
        sessionList.add(event.IDESessionUUID);
        sessionsRef.put(event.ProfileId, sessionList);
    }
	
	private long calcTimeSpent(String profileID) {
		Collection<String> sessions = sessionsRef.get(profileID);
		Iterator<EditEvent> iterator = this.editEvents.iterator();
		long duration = 0;
		while(iterator.hasNext()) {
			EditEvent e = iterator.next();
			if(sessions.contains(e.IDESessionUUID)) {
				duration += e.Duration.toMinutes();
			}
		}
		return duration;
	}
	
//	private void calcAvgTimeOnOneFile() {}
    
	private int calcFileNum(String profileID) {
		Collection<String> sessions = sessionsRef.get(profileID);
		Iterator<EditEvent> iterator = this.editEvents.iterator();
		int fileNum = 0;
		while(iterator.hasNext()) {
			EditEvent e = iterator.next();
			if(sessions.contains(e.IDESessionUUID)) {
				fileNum++;
			}
		}
		return fileNum;
	}
	
//	public void generateCSVfile() throws IOException {
//
//	}
}
