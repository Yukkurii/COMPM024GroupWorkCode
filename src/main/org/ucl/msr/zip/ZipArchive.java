/*
 * UCL MSR Processor (c) by Blair Butterworth and Chenghui Fan
 *
 * This work is licensed under the Creative Commons Attribution 4.0
 * International License. To view a copy of this license, visit
 *
 *      http://creativecommons.org/licenses/by/4.0/
 */

package org.ucl.msr.zip;

import java.io.Closeable;

/**
 * Implementors of this interface represent an archive compressed using the ZIP
 * format.
 *
 * @author Blair Butterworth
 * @author Chenghui Fan
 */
public interface ZipArchive extends Iterable<ZipElement>, Closeable
{
}