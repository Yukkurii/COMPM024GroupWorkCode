package org.ucl.msr.zip;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;

public class ZipStreamTest
{
    @Test
    public void testIterator() throws IOException
    {
        InputStream inputStream = ZipStreamTest.class.getResourceAsStream("/Archive.zip");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        IOUtils.copy(inputStream, outputStream);
        ZipStream zipStream = new ZipStream(outputStream.toByteArray());

        Collection<String> contents = new ArrayList<String>();
        for (ZipElement element: zipStream){
            contents.add(element.getName());
        }

        Assert.assertTrue("iteration returned incorrect number of contents", contents.size() == 4);
        Assert.assertTrue("file1.txt missing", contents.contains("file1.txt"));
        Assert.assertTrue("file2.txt missing", contents.contains("file2.txt"));
        Assert.assertTrue("directory/ missing", contents.contains("directory/"));
        Assert.assertTrue("directory.file3.txt missing", contents.contains("directory/file3.txt"));
    }
}
