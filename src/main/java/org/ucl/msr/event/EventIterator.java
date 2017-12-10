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
import org.ucl.msr.utils.stream.StreamUtils;
import org.ucl.msr.utils.zip.ZipArchive;
import org.ucl.msr.utils.zip.ZipElement;
import org.ucl.msr.utils.zip.ZipStream;

import java.io.InputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;

/**
 * Instances of the class iterate through a collection of MSR mining challenge
 * events.
 *
 * @author Blair Butterworth
 * @author Chenghui Fan
 */
public class EventIterator implements Callable<EventProcessor>
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
    public EventProcessor call()
    {
        try {
            processArchive(archive);
        }
        catch (CancellationException cancellation) {
            executor.shutdownNow();
        }
        catch (Throwable error){
            error.printStackTrace();
        }
        return processor;
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
        try {
            ZipArchive archive = new ZipStream(element.getData());
            EventIterator iterator = new EventIterator(archive, processor, executor);

            if (!executor.isShutdown()) {
                executor.submit(iterator); //TODO close archive
            }
        }
        catch (Exception error){
            error.printStackTrace();
        }
    }

    private void processJson(ZipElement element)
    {
        try (InputStream data = element.getData()) {
            String content = StreamUtils.getString(data);
            IDEEvent event = JsonUtils.fromJson(content, IDEEvent.class);
            processor.process(event);
        }
        catch (CancellationException cancellation){
            throw cancellation;
        }
        catch (Exception error){
            error.printStackTrace();
        }
    }
}