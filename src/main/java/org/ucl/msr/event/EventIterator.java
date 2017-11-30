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
import cc.kave.commons.utils.io.json.JsonUtils;
import org.ucl.msr.zip.ZipArchive;
import org.ucl.msr.zip.ZipElement;
import org.ucl.msr.zip.ZipStream;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Instances of the class iterate through a collection of MSR mining challenge
 * events.
 *
 * @author Blair Butterworth
 * @author Chenghui Fan
 */
public class EventIterator implements Runnable
{
    private ZipArchive archive;
    private EventProcessor processor;
    private ExecutorService executor;

    public EventIterator(ZipArchive archive, EventProcessor processor, ExecutorService executor)
    {
        this.archive = archive;
        this.processor = processor;
        this.executor = executor;
    }

    @Override
    public void run()
    {
        try
        {
            processArchive(archive);
        }
        catch (CancellationException e)
        {
            executor.shutdownNow();
        }
    }

    private void processArchive(ZipArchive archive)
    {
        for (ZipElement element : archive)
        {
            processAchieveElement(element);
        }
    }

    private void processAchieveElement(ZipElement element)
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

    private void processArchive(ZipElement element)
    {
        ZipArchive archive = new ZipStream(element.getData());
        EventIterator iterator = new EventIterator(archive, processor, executor);
        executor.submit(iterator);
    }

    private void processJson(ZipElement element)
    {
        String content = new String(element.getData(), StandardCharsets.UTF_8);
        IDEEvent event = JsonUtils.fromJson(content, IDEEvent.class);
        processor.process(event);
    }
}