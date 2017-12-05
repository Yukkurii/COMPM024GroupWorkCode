/*
 * UCL MSR Processor (c) by Blair Butterworth and Chenghui Fan
 *
 * This work is licensed under the Creative Commons Attribution 4.0
 * International License. To view a copy of this license, visit
 *
 *      http://creativecommons.org/licenses/by/4.0/
 */

package org.ucl.msr.zip;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Instances of this class implement the {@link Iterator} interface, iterating
 * through the contents of a {@link ZipStream}.
 *
 * @author Blair Butterworth
 * @author Chenghui Fan
 */
public class ZipStreamIterator implements Iterator<ZipElement>
{
    private ZipInputStream stream;
    private ZipEntry next;

    public ZipStreamIterator(ZipInputStream stream)
    {
        try
        {
            this.stream = stream;
            this.next = stream.getNextEntry();
        }
        catch (Exception exception)
        {
            throw new RuntimeException(exception); //TODO: Bad - improve
        }
    }

    @Override
    public boolean hasNext()
    {
        return next != null;
    }

    @Override
    public ZipElement next()
    {
        try
        {
            ZipElement result = getNextElement();
            next = stream.getNextEntry();
            return result;
        }
        catch (Exception exception)
        {
            throw new RuntimeException(exception); //TODO: Bad - improve
        }
    }

    private ZipElement getNextElement()
    {
        String name = next.getName();
        byte[] data = getData(next);
        return new ZipStreamElement(name, data);
    }

    private byte[] getData(ZipEntry entry)
    {
        try
        {
            if (!entry.isDirectory())
            {
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                IOUtils.copy(stream, byteStream);
                byteStream.close();
                return byteStream.toByteArray();
            }
            return new byte[0];
        }
        catch (Exception exception)
        {
            throw new RuntimeException(exception); //TODO: Bad - improve
        }
    }
}
