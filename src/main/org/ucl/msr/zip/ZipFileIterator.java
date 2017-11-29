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
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Instances of this class implement the {@link Iterator} interface, iterating
 * through the contents of a {@link ZipFile}.
 *
 * @author Blair Butterworth
 * @author Chenghui Fan
 */
public class ZipFileIterator implements Iterator<ZipElement>
{
    private ZipFile archive;
    private Enumeration<? extends ZipEntry> entries;

    public ZipFileIterator(ZipFile archive)
    {
        this.archive = archive;
        this.entries = archive.entries();
    }

    @Override
    public boolean hasNext()
    {
        return entries.hasMoreElements();
    }

    @Override
    public ZipElement next()
    {
        ZipEntry entry = entries.nextElement();
        String name = entry.getName();
        byte[] data = getData(entry);
        boolean isDirectory = entry.isDirectory();
        return new ZipElement(name, data, isDirectory);
    }

    private byte[] getData(ZipEntry entry)
    {
        try
        {
            if (!entry.isDirectory())
            {
                InputStream in = archive.getInputStream(entry);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                IOUtils.copy(in, out);
                IOUtils.closeQuietly(in);
                out.close();
                return out.toByteArray();
            }
            return new byte[0];
        }
        catch (Exception e)
        {
            throw new RuntimeException(e); //TODO: Need better error handling
        }
    }
}
