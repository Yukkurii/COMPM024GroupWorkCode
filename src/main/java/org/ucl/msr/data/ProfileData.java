/*
 * UCL MSR Processor (c) by Blair Butterworth and Chenghui Fan
 *
 * This work is licensed under the Creative Commons Attribution 4.0
 * International License. To view a copy of this license, visit
 *
 *      http://creativecommons.org/licenses/by/4.0/
 */

package org.ucl.msr.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Instances of this class contain a collection of information about user
 * profiles.
 *
 * @author Blair Butterworth
 * @author Chenghui Fan
 */
public class ProfileData
{
    private Map<String, ProfileDetails> profiles;

    public ProfileData()
    {
        profiles = new ConcurrentHashMap<>();
    }

    public void addProfileDetails(ProfileDetails details)
    {
        profiles.put(details.getIdentifier(), details);
    }

    public ProfileDetails getProfileDetails(String identifier)
    {
        return profiles.get(identifier);
    }

    public Map<String, ProfileDetails> getProfiles()
    {
        return profiles;
    }
}
