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
        return new ZipFileElement(entry.getName(), archive);
    }
}
