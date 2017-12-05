package org.ucl.msr.zip;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ZipStreamElement implements ZipElement
{
    private String name;
    private InputStream data;

    public ZipStreamElement(String name, byte[] data)
    {
        this.name = name;
        this.data = new ByteArrayInputStream(data);
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public InputStream getData()
    {
        return data;
    }
}
