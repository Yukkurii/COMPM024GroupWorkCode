/*
 * UCL MSR Processor (c) by Blair Butterworth and Chenghui Fan
 *
 * This work is licensed under the Creative Commons Attribution 4.0
 * International License. To view a copy of this license, visit
 *
 *      http://creativecommons.org/licenses/by/4.0/
 */

package org.ucl.msr.zip;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * Instances of this class represent a file compressed using the ZIP format.
 * Methods are provide to iterate through its contents.
 *
 * @author Blair Butterworth
 * @author Chenghui Fan
 */
public class ZipFile implements ZipArchive
{
    private java.util.zip.ZipFile archive;

    public ZipFile(File file)
    {
        try
        {
            archive = new java.util.zip.ZipFile(file);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e); //TODO: Bad - improve
        }
    }

    public ZipFile(String path)
    {
        this(new File(path));
    }

    @Override
    public Iterator<ZipElement> iterator()
    {
        return new ZipFileIterator(archive);
    }

    @Override
    public void close() throws IOException
    {
        archive.close();
    }
}
