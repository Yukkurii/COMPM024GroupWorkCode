package org.ucl.msr.zip;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipFileElement implements ZipElement
{
    private String name;
    private ZipFile archive;

    public ZipFileElement(String name, ZipFile archive)
    {
        this.name = name;
        this.archive = archive;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public InputStream getData() throws IOException
    {
        ZipEntry entry = archive.getEntry(name);
        return archive.getInputStream(entry);
    }
}
