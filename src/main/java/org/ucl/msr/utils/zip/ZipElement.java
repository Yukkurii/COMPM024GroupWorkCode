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
import java.util.zip.ZipEntry;

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
    private ZipArchive archive;

    public ZipElement(String name, ZipArchive archive)
    {
        this.name = name;
        this.archive = archive;
    }

    public String getName()
    {
        return name;
    }

    public InputStream getData() throws IOException
    {
        ZipEntry entry = archive.getEntry(name);
        return archive.getInputStream(entry);
    }
}