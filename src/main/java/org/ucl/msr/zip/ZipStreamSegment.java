package org.ucl.msr.zip;

import com.sun.corba.se.spi.orbutil.fsm.Input;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

public class ZipStreamSegment extends InputStream
{
    public ZipInputStream stream;

    public ZipStreamSegment(ZipInputStream stream)
    {
        this.stream = stream;
    }

    @Override
    public int read() throws IOException
    {
        return stream.read();
    }

    @Override
    public void close() throws IOException
    {
    }
}
