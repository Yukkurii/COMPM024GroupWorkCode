/*
 * UCL MSR Processor (c) by Blair Butterworth and Chenghui Fan
 *
 * This work is licensed under the Creative Commons Attribution 4.0
 * International License. To view a copy of this license, visit
 *
 *      http://creativecommons.org/licenses/by/4.0/
 */

package org.ucl.msr.event;

import cc.kave.commons.model.events.IDEEvent;
import cc.kave.commons.model.events.userprofiles.UserProfileEvent;

import org.ucl.msr.data.EventData;
import org.ucl.msr.data.ProfileData;
import org.ucl.msr.data.ProfileDetails;

/**
 * Instances of this class search the data set for user profile events,
 * recording session, education and position information.
 *
 * @author Blair Butterworth
 * @author Chenghui Fan
 */
public class ProfileProcessor implements EventProcessor
{
    private ProfileData profileData;

    public ProfileProcessor(EventData eventData)
    {
        this.profileData = eventData.getProfileData();
    }

    @Override
    public void process(IDEEvent event)
    {
        if (event instanceof UserProfileEvent)
        {
            UserProfileEvent profileEvent = (UserProfileEvent)event;
            updateProfile(profileEvent);
        }
    }

    private void updateProfile(UserProfileEvent event)
    {
        ProfileDetails profileDetails = profileData.getProfileDetails(event.ProfileId);
        if (profileDetails == null)
        {
            profileDetails = new ProfileDetails();
            profileDetails.setIdentifier(event.ProfileId);
            profileDetails.setEducation(event.Education);
            profileDetails.setPosition(event.Position);
            profileData.addProfileDetails(profileDetails);
        }
        profileDetails.addSession(event.IDESessionUUID);
    }
}
