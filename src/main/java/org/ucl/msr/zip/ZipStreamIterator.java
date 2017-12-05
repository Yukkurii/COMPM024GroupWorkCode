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
    private ZipStream stream;

    public ZipStreamIterator(ZipStream stream)
    {
        this.stream = stream;
    }

    @Override
    public boolean hasNext()
    {
        return stream.hasNext();
    }

    @Override
    public ZipElement next()
    {
        try
        {
            ZipEntry next = stream.getNextEntry();
            String name = next.getName();
            return new ZipStreamElement(name, stream);
        }
        catch (Exception exception)
        {
            throw new RuntimeException(exception); //TODO: Bad - improve
        }
    }
}
