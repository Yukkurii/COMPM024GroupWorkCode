/*
 * UCL MSR Processor (c) by Blair Butterworth and Chenghui Fan
 *
 * This work is licensed under the Creative Commons Attribution 4.0
 * International License. To view a copy of this license, visit
 *
 *      http://creativecommons.org/licenses/by/4.0/
 */

package org.ucl.msr.data;

/**
 * Instances of this class contain the data gathered by iterating through the
 * MSR data set.
 *
 * @author Blair Butterworth
 * @author Chenghui Fan
 */
public class EventData
{
    private EditData editData;
    private ProfileData profileData;

    public EventData()
    {
        editData = new EditData();
        profileData = new ProfileData();
    }

    public ProfileData getProfileData()
    {
        return profileData;
    }

    public EditData getEditData()
    {
        return editData;
    }
}
