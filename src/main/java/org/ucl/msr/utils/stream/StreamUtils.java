/*
 * UCL MSR Processor (c) by Blair Butterworth and Chenghui Fan
 *
 * This work is licensed under the Creative Commons Attribution 4.0
 * International License. To view a copy of this license, visit
 *
 *      http://creativecommons.org/licenses/by/4.0/
 */

package org.ucl.msr.utils.stream;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Instances of this class provide utility functions for working with
 * {@link InputStream}s and {@link java.io.OutputStream}s
 *
 * @author Blair Butterworth
 * @author Chenghui Fan
 */
public class StreamUtils
{
    private StreamUtils()
    {
    }

    public static String getString(InputStream inputStream) throws IOException
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        IOUtils.copy(inputStream, outputStream);
        IOUtils.closeQuietly(inputStream);
        outputStream.close();
        return new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
    }
}
