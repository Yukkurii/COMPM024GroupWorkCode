/*
 * UCL MSR Processor (c) by Blair Butterworth and Chenghui Fan
 *
 * This work is licensed under the Creative Commons Attribution 4.0
 * International License. To view a copy of this license, visit
 *
 *      http://creativecommons.org/licenses/by/4.0/
 */

package org.ucl.msr.utils.zip;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;

/**
 * Implementors of this interface represent an archive compressed using the ZIP
 * format.
 *
 * @author Blair Butterworth
 * @author Chenghui Fan
 */
public interface ZipArchive extends Iterable<ZipElement>, Closeable
{
    boolean hasNext();

    ZipEntry getNextEntry() throws IOException;

    ZipEntry getEntry(String name) throws IOException;

    InputStream getInputStream(ZipEntry entry) throws IOException;
}