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
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Instances of this class write user edit data gathered by iterating through
 * the MSR data set to file in CSV format.
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

        writeToFile(fileWriter, profileMainEdits, profileTestEdits);
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

    private void writeToFile(
            FileWriter fileWriter,
            Map<String, Map<ZonedDateTime, Integer>> mainEdits,
            Map<String, Map<ZonedDateTime, Integer>> testEdits) throws IOException
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        Duration oneDay = Duration.of(1, ChronoUnit.DAYS);

        fileWriter.append("User, date, main edits, test edits\n");

        Collection<String> users = getUsers(mainEdits.keySet(), testEdits.keySet());
        for (String user: users)
        {
            Map<ZonedDateTime, Integer> userMainEdits = getUserEdits(user, mainEdits);
            Map<ZonedDateTime, Integer> userTestEdits = getUserEdits(user, testEdits);

            if (! userMainEdits.isEmpty() || ! userTestEdits.isEmpty())
            {
                Collection<ZonedDateTime> mainDates = userMainEdits.keySet();
                Collection<ZonedDateTime> testDates = userTestEdits.keySet();

                ZonedDateTime oldestDate = getOldestDate(mainDates, testDates);
                ZonedDateTime newestDate = getNewestDate(mainDates, testDates);
                ZonedDateTime dateIteration = oldestDate;

                while (dateIteration.compareTo(newestDate) <= 0)
                {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(user);
                    stringBuilder.append(",");
                    stringBuilder.append(dateIteration.format(formatter));
                    stringBuilder.append(",");
                    stringBuilder.append(getEditCount(userMainEdits, dateIteration));
                    stringBuilder.append(",");
                    stringBuilder.append(getEditCount(userTestEdits, dateIteration));
                    stringBuilder.append("\n");

                    fileWriter.append(stringBuilder.toString());
                    dateIteration = dateIteration.plus(oneDay);
                }
            }
        }
    }

    private Collection<String> getUsers(Collection<String> mainUsers, Collection<String> testUsers)
    {
        Set<String> result = new HashSet<>();
        result.addAll(mainUsers);
        result.addAll(testUsers);
        return result;
    }

    private Map<ZonedDateTime, Integer> getUserEdits(String user, Map<String, Map<ZonedDateTime, Integer>> edits)
    {
        if (edits.containsKey(user)){
            return edits.get(user);
        }
        return Collections.emptyMap();
    }

    private ZonedDateTime getOldestDate(Collection<ZonedDateTime> datesA, Collection<ZonedDateTime> datesB)
    {
        ZonedDateTime oldestA = getOldestDate(datesA);
        ZonedDateTime oldestB = getOldestDate(datesB);

        if (oldestA == null) return oldestB;
        if (oldestB == null) return oldestA;

        return getOldestDate(oldestA, oldestB);
    }

    private ZonedDateTime getOldestDate(Collection<ZonedDateTime> dates)
    {
        if (!dates.isEmpty()) {
            ZonedDateTime result = dates.iterator().next();
            for (ZonedDateTime date : dates) {
                result = getOldestDate(result, date);
            }
            return result;
        }
        return null;
    }

    private ZonedDateTime getOldestDate(ZonedDateTime dateA, ZonedDateTime dateB)
    {
        if (dateA == null) return dateB;
        if (dateB == null) return dateA;
        return dateA.compareTo(dateB) <= 0 ? dateA : dateB;
    }

    private ZonedDateTime getNewestDate(Collection<ZonedDateTime> datesA, Collection<ZonedDateTime> datesB)
    {
        ZonedDateTime newestA = getNewestDate(datesA);
        ZonedDateTime newestB = getNewestDate(datesB);

        if (newestA == null) return newestB;
        if (newestB == null) return newestA;

        return getNewestDate(newestA, newestB);
    }

    private ZonedDateTime getNewestDate(Collection<ZonedDateTime> dates)
    {
        if (!dates.isEmpty()) {
            ZonedDateTime result = dates.iterator().next();
            for (ZonedDateTime date : dates) {
                result = getNewestDate(result, date);
            }
            return result;
        }
        return null;
    }

    private ZonedDateTime getNewestDate(ZonedDateTime dateA, ZonedDateTime dateB)
    {
        if (dateA == null) return dateB;
        if (dateB == null) return dateA;
        return dateA.compareTo(dateB) >= 0 ? dateA : dateB;
    }

    private int getEditCount(Map<ZonedDateTime, Integer> edits, ZonedDateTime date)
    {
        return edits.containsKey(date) ? edits.get(date) : 0;
    }
}
