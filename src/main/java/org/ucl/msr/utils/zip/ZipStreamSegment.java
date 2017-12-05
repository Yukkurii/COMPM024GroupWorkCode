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
import java.util.zip.ZipInputStream;

/**
 * Instances of this class represent a single segment of a
 * {@link ZipInputStream}.
 *
 * @author Blair Butterworth
 * @author Chenghui Fan
 */
public class ZipStreamSegment extends InputStream
{
    public ZipInputStream delegate;

    public ZipStreamSegment(ZipInputStream stream)
    {
        this.delegate = stream;
    }

    @Override
    public int read() throws IOException
    {
        return delegate.read();
    }

    @Override
    public void close() throws IOException
    {
        // Delegate designed for reuse. Doesn't need to be closed for each
        // segment read.
    }
}
