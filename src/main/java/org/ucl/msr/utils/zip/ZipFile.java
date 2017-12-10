/*
 * UCL MSR Processor (c) by Blair Butterworth and Chenghui Fan
 *
 * This work is licensed under the Creative Commons Attribution 4.0
 * International License. To view a copy of this license, visit
 *
 *      http://creativecommons.org/licenses/by/4.0/
 */

package org.ucl.msr.utils.zip;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.zip.ZipEntry;

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
    private Enumeration<? extends ZipEntry> entries;

    public ZipFile(File file)
    {
        try{
            this.archive = new java.util.zip.ZipFile(file);
            this.entries = archive.entries();
        }
        catch (IOException e){
            throw new ZipArchiveException(e);
        }
    }

    public ZipFile(String path)
    {
        this(new File(path));
    }

    @Override
    public Iterator<ZipElement> iterator()
    {
        return new ZipIterator(this);
    }

    @Override
    public void close() throws IOException
    {
        archive.close();
    }

    @Override
    public boolean hasNext()
    {
        return entries.hasMoreElements();
    }

    @Override
    public ZipEntry getNextEntry()
    {
        return entries.nextElement();
    }

    @Override
    public ZipEntry getEntry(String name) throws IOException
    {
        return archive.getEntry(name);
    }

    @Override
    public InputStream getInputStream(ZipEntry entry) throws IOException
    {
        return archive.getInputStream(entry);
    }
}
