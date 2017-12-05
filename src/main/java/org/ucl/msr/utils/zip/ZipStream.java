/*
 * UCL MSR Processor (c) by Blair Butterworth and Chenghui Fan
 *
 * This work is licensed under the Creative Commons Attribution 4.0
 * International License. To view a copy of this license, visit
 *
 *      http://creativecommons.org/licenses/by/4.0/
 */

package org.ucl.msr.utils.zip;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Instances of this class represent a delegate whose contents are compressed
 * using the ZIP format. Methods are provide to iterate through its contents.
 *
 * @author Blair Butterworth
 * @author Chenghui Fan
 */
public class ZipStream implements ZipArchive
{
    private ZipEntry current;
    private ZipEntry next;
    private ZipInputStream stream;

    public ZipStream(InputStream inputStream) throws IOException
    {
        stream = new ZipInputStream(inputStream);
        current = null;
        next = stream.getNextEntry();
    }

    @Override
    public Iterator<ZipElement> iterator()
    {
        return new ZipIterator(this);
    }

    @Override
    public void close() throws IOException
    {
        stream.close();
    }

    @Override
    public boolean hasNext()
    {
        return next != null;
    }

    @Override
    public ZipEntry getNextEntry() throws IOException
    {
        current = next;
        next = stream.getNextEntry();
        return current;
    }

    @Override
    public ZipEntry getEntry(String name) throws IOException
    {
        if (Objects.equals(current.getName(), name)) {
            return current;
        }
        throw new UnsupportedOperationException();
    }

    public InputStream getInputStream(ZipEntry entry) throws IOException
    {
        if (Objects.equals(current, entry)) {
            return new ZipStreamSegment(stream);
        }
        throw new UnsupportedOperationException();
    }
}
