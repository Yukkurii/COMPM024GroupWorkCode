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
import java.util.Iterator;
import java.util.zip.ZipEntry;

/**
 * Instances of this class implement the {@link Iterator} interface, iterating
 * through the contents of a {@link ZipArchive}.
 *
 * @author Blair Butterworth
 * @author Chenghui Fan
 */
public class ZipIterator implements Iterator<ZipElement>
{
    private ZipArchive archive;

    public ZipIterator(ZipArchive archive)
    {
        this.archive = archive;
    }

    @Override
    public boolean hasNext()
    {
        return archive.hasNext();
    }

    @Override
    public ZipElement next()
    {
        try {
            ZipEntry entry = archive.getNextEntry();
            return new ZipElement(entry.getName(), archive);
        }
        catch (IOException e){
            throw new RuntimeException(e); //TODO
        }
    }
}
