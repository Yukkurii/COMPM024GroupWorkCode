/*
 * UCL MSR Processor (c) by Blair Butterworth and Chenghui Fan
 *
 * This work is licensed under the Creative Commons Attribution 4.0
 * International License. To view a copy of this license, visit
 *
 *      http://creativecommons.org/licenses/by/4.0/
 */

package org.ucl.msr;

import cc.kave.commons.model.events.IDEEvent;
import cc.kave.commons.utils.io.json.JsonUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Instances of the class iterate through the contents of an TODO
 *
 * @author Blair Butterworth
 * @author Chenghui Fan
 */
public class EventIterator implements Runnable
{
    private Path path;
    private EventProcessor processor;

    public EventIterator(Path path, EventProcessor processor)
    {
        this.path = path;
        this.processor = processor;
    }

    @Override
    public void run()
    {
        try (ZipArchive archive = new ZipArchive(path.toFile()))
        {
            for (ZipElement element: archive)
            {
                if (! element.isDirectory())
                {
                    String content = new String(element.getData(), StandardCharsets.UTF_8);
                    IDEEvent event = JsonUtils.fromJson(content, IDEEvent.class);
                    processor.process(event);
                }
            }
        }
        catch (Exception exception)
        {
            throw new RuntimeException(exception); //TODO: Need better error handling
        }
    }
}