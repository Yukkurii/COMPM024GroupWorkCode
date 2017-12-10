/*
 * UCL MSR Processor (c) by Blair Butterworth and Chenghui Fan
 *
 * This work is licensed under the Creative Commons Attribution 4.0
 * International License. To view a copy of this license, visit
 *
 *      http://creativecommons.org/licenses/by/4.0/
 */

package org.ucl.msr.utils.zip;

/**
 * Instances of this class represent an error that may occur when operating
 * with a {@link ZipArchive}
 *
 * @author Blair Butterworth
 * @author Chenghui Fan
 */
public class ZipArchiveException extends RuntimeException
{
    public ZipArchiveException(Throwable cause)
    {
        super(cause);
    }
}
