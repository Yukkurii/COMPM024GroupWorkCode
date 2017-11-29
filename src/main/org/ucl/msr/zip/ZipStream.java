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
import java.util.Iterator;
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

    public ZipStream(byte[] archive)
    {
        stream = new ZipInputStream(new ByteArrayInputStream(archive));
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
