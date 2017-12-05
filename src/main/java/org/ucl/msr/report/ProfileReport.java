/*
 * UCL MSR Processor (c) by Blair Butterworth and Chenghui Fan
 *
 * This work is licensed under the Creative Commons Attribution 4.0
 * International License. To view a copy of this license, visit
 *
 *      http://creativecommons.org/licenses/by/4.0/
 */

package org.ucl.msr.report;

import org.ucl.msr.data.EventData;
import org.ucl.msr.data.ProfileDetails;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

/**
 * Instances of this class write profile data gathered by iterating through
 * the MSR data set to file in CSV format.
 *
 * @author Blair Butterworth
 * @author Chenghui Fan
 */
public class ProfileReport
{
    private EventData eventData;

    public ProfileReport(EventData eventData)
    {
        this.eventData = eventData;
    }

    public void writeToFile(String directory, String name) throws IOException
    {
        File destination = createFile(directory, name);
        try (FileWriter fileWriter = new FileWriter(destination)){
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
        Collection<ProfileDetails> profiles = eventData.getProfileData().getProfiles().values();
        fileWriter.append("Identifier, education, position\n");

        for (ProfileDetails profile: profiles)
        {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(profile.getIdentifier());
            stringBuilder.append(",");
            stringBuilder.append(profile.getEducation());
            stringBuilder.append(",");
            stringBuilder.append(profile.getPosition());
            stringBuilder.append("\n");
            fileWriter.append(stringBuilder.toString());
        }
    }
}
