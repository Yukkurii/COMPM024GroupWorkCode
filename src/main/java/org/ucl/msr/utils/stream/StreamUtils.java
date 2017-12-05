package org.ucl.msr.utils.stream;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class StreamUtils
{
    public static String getString(InputStream inputStream) throws IOException
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        IOUtils.copy(inputStream, outputStream);
        IOUtils.closeQuietly(inputStream);
        outputStream.close();
        return new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
    }
}
