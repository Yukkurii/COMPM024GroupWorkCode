/*
 * UCL MSR Processor (c) by Blair Butterworth and Chenghui Fan
 *
 * This work is licensed under the Creative Commons Attribution 4.0
 * International License. To view a copy of this license, visit
 *
 *      http://creativecommons.org/licenses/by/4.0/
 */

package org.ucl.msr;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.zip.ZipFile;

/**
 * Instances of this class represent an archive compressed using the ZIP
 * format.
 *
 * @author Blair Butterworth
 * @author Chenghui Fan
 */
public class ZipArchive implements Iterable<ZipElement>, Closeable
{
    private ZipFile archive;

    public ZipArchive(File file) throws IOException
    {
        archive = new ZipFile(file);
    }

    @Override
    public Iterator<ZipElement> iterator()
    {
        return new ZipIterator(archive);
    }

    @Override
    public void close() throws IOException
    {
        archive.close();
    }
}