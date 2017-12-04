/*
 * UCL MSR Processor (c) by Blair Butterworth and Chenghui Fan
 *
 * This work is licensed under the Creative Commons Attribution 4.0
 * International License. To view a copy of this license, visit
 *
 *      http://creativecommons.org/licenses/by/4.0/
 */

package org.ucl.msr.report;

import org.ucl.msr.data.EditData;
import org.ucl.msr.data.EventData;
import org.ucl.msr.data.ProfileDetails;

import java.io.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Instances of this class write the edit data gathered by iterating through
 * the MSR data set to file in CSV format.
 *
 * REMOVE ME
 *
 * @author Blair Butterworth
 * @author Chenghui Fan
 */
public class EditReport
{
    private EventData eventData;

    public EditReport(EventData eventData)
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
        EditData editData = eventData.getEditData();

        Map<String, Map<ZonedDateTime, Integer>> mainEdits = editData.getMainEdits();
        Map<String, Map<ZonedDateTime, Integer>> testEdits = editData.getTestEdits();

        Map<String, Map<ZonedDateTime, Integer>> profileMainEdits = convertSessionsToProfiles(mainEdits);
        Map<String, Map<ZonedDateTime, Integer>> profileTestEdits = convertSessionsToProfiles(testEdits);

        writeToFile(fileWriter, "main", profileMainEdits);
        writeToFile(fileWriter,"test", profileTestEdits);
    }

    private Map<String, Map<ZonedDateTime, Integer>> convertSessionsToProfiles(Map<String, Map<ZonedDateTime, Integer>> edits)
    {
        Map<String, Map<ZonedDateTime, Integer>> result = new HashMap<>();
        Map<String, ProfileDetails> profiles = eventData.getProfileData().getProfiles();

        for (Map.Entry<String, ProfileDetails> session: profiles.entrySet())
        {
            Map<ZonedDateTime, Integer> sessionEdits = new HashMap<>();
            for (String alias: session.getValue().getSessions())
            {
                Map<ZonedDateTime, Integer> aliasEdits = edits.get(alias);
                if (aliasEdits != null)
                {
                    for (Map.Entry<ZonedDateTime, Integer> aliasEdit : aliasEdits.entrySet())
                    {
                        Integer sessionEdit = sessionEdits.get(aliasEdit.getKey());
                        sessionEdit = sessionEdit == null ? aliasEdit.getValue() : sessionEdit + aliasEdit.getValue();
                        sessionEdits.put(aliasEdit.getKey(), sessionEdit);
                    }
                }
            }
            result.put(session.getKey(), sessionEdits);
        }
        return result;
    }

    private void writeToFile(FileWriter fileWriter, String id, Map<String, Map<ZonedDateTime, Integer>> edits) throws IOException
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        for (Map.Entry<String, Map<ZonedDateTime, Integer>> session: edits.entrySet())
        {
            for (Map.Entry<ZonedDateTime, Integer> edit: session.getValue().entrySet())
            {
                String date = edit.getKey().format(formatter);
                fileWriter.append(id + "," + session.getKey() + "," + date + "," + edit.getValue() + "\n");
            }
        }
    }
}
