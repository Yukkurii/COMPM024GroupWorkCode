/*
 * UCL MSR Processor (c) by Blair Butterworth and Chenghui Fan
 *
 * This work is licensed under the Creative Commons Attribution 4.0
 * International License. To view a copy of this license, visit
 *
 *      http://creativecommons.org/licenses/by/4.0/
 */

package org.ucl.msr.zip;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Instances of this class represent a stream whose contents are compressed
 * using the ZIP format. Methods are provide to iterate through its contents.
 *
 * @author Blair Butterworth
 * @author Chenghui Fan
 */
public class ZipStream implements ZipArchive
{
    private ZipInputStream stream;

    @Deprecated
    public ZipStream(byte[] archive)
    {
        this(new ByteArrayInputStream(archive));
    }

    public ZipStream(InputStream inputStream)
    {
        stream = new ZipInputStream(inputStream);
    }

    @Override
    public Iterator<ZipElement> iterator()
    {
        return new ZipStreamIterator(stream);
    }

    @Override
    public void close() throws IOException
    {
        stream.close();
    }
}
