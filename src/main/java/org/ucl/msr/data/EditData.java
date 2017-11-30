/*
 * UCL MSR Processor (c) by Blair Butterworth and Chenghui Fan
 *
 * This work is licensed under the Creative Commons Attribution 4.0
 * International License. To view a copy of this license, visit
 *
 *      http://creativecommons.org/licenses/by/4.0/
 */

package org.ucl.msr.data;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Instances of this class contain information about edits the user has made to
 * test and non-test files.
 *
 * @author Blair Butterworth
 * @author Chenghui Fan
 */
public class EditData
{
    private Map<String, Map<ZonedDateTime, Integer>> testEdits;
    private Map<String, Map<ZonedDateTime, Integer>> mainEdits;

    public EditData()
    {
        testEdits = new ConcurrentHashMap<>();
        mainEdits = new ConcurrentHashMap<>();
    }

    public Map<String, Map<ZonedDateTime, Integer>> getTestEdits()
    {
        return testEdits;
    }

    public Map<String, Map<ZonedDateTime, Integer>> getMainEdits()
    {
        return mainEdits;
    }
}
