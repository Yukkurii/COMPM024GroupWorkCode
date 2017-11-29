/*
 * UCL MSR Processor (c) by Blair Butterworth and Chenghui Fan
 *
 * This work is licensed under the Creative Commons Attribution 4.0
 * International License. To view a copy of this license, visit
 *
 *      http://creativecommons.org/licenses/by/4.0/
 */

package org.ucl.msr.event;

import org.ucl.msr.zip.ZipArchive;
import org.ucl.msr.zip.ZipElement;
import org.ucl.msr.zip.ZipFile;

import java.nio.file.Path;

/**
 * Instances of the class iterate through a collection of MSR mining challenge
 * events.
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
        try (ZipArchive archive = new ZipFile(path.toFile()))
        {
            for (ZipElement element: archive)
            {
                String elementName = element.getName();

                if (elementName.endsWith("json"))
                {
                    processJson(element);
                }
                else if (elementName.endsWith("zip"))
                {
                    processArchive(element);
                }
            }
        }
        catch (Exception exception)
        {
            throw new RuntimeException(exception); //TODO: Improve error handling
        }
    }

    private void processArchive(ZipElement element)
    {
        System.out.println(element.getName());
    }

    private void processJson(ZipElement element)
    {
        System.out.println(element.getName());

//        String content = new String(element.getData(), StandardCharsets.UTF_8);
//        IDEEvent event = JsonUtils.fromJson(content, IDEEvent.class);
//        processor.process(event);
    }
}