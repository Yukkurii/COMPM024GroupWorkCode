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

import org.ucl.msr.data.EventData;
import org.ucl.msr.data.PerformanceData;
import org.ucl.msr.data.ProfileData;

import cc.kave.commons.model.events.IDEEvent;
import cc.kave.commons.model.events.userprofiles.UserProfileEvent;
import cc.kave.commons.model.events.versioncontrolevents.VersionControlAction;
import cc.kave.commons.model.events.versioncontrolevents.VersionControlEvent;
import cc.kave.commons.model.events.visualstudio.EditEvent;
import cc.kave.commons.model.events.visualstudio.WindowEvent;

/**
 * Instances of this class search the MSR data set calculating each users
 * performance for each day.
 * 
 * performance 		= time/workload
 * 		time 		= sum(windowOpen to windowClose)
 * 		workload 	= number of file involved 
 *
 * @author Blair Butterworth
 * @author Chenghui Fan
 */

public class PerformanceProcessor implements EventProcessor
{	
	private PerformanceData performanceData;
	
    public PerformanceProcessor(EventData eventData) {
    	performanceData = eventData.getPerformanceData();
    }
	
	@Override
    public void process(IDEEvent event)
    {
        if (event instanceof WindowEvent)
        {
        	WindowEvent we = (WindowEvent)event;
        	if(we.ActiveWindow.getCaption().contains(".cs")) {
            	if(we.Action.toString().equals("Create") || we.Action.toString().equals("Activate")) {
            		this.performanceData.addFileUsageRecord(we.IDESessionUUID, we.ActiveWindow.getCaption(), we.TriggeredAt, "active");
            	}
            	else if(we.Action.toString().equals("Close") || we.Action.toString().equals("Deactivate")) {
            		this.performanceData.addFileUsageRecord(we.IDESessionUUID, we.ActiveWindow.getCaption(), we.TriggeredAt, "deactive");
            	}
        	}
        }
    }
	
}
