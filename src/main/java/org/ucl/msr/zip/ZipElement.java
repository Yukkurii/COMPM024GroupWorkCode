/*
 * UCL MSR Processor (c) by Blair Butterworth and Chenghui Fan
 *
 * This work is licensed under the Creative Commons Attribution 4.0
 * International License. To view a copy of this license, visit
 *
 *      http://creativecommons.org/licenses/by/4.0/
 */

package org.ucl.msr.zip;

/**
 * Instances of this class represent a single compressed element, a single
 * constituent of a {@link ZipArchive}. Methods are provided to obtain its
 * name, data and whether its a directory or not.
 *
 * @author Blair Butterworth
 * @author Chenghui Fan
 */
public class ZipElement
{
    private String name;
    private byte[] data;
    private boolean isDirectory;

    public ZipElement(String name, byte[] data, boolean isDirectory)
    {
        this.name = name;
        this.data = data;
        this.isDirectory = isDirectory;
    }

    public String getName()
    {
        return name;
    }

    public byte[] getData()
    {
        return data;
    }

    public boolean isDirectory()
    {
        return isDirectory;
    }
}
