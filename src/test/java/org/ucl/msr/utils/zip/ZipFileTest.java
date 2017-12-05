package org.ucl.msr.utils.zip;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class ZipFileTest
{
    @Test
    public void testIterator() throws IOException
    {
        File file = new File(ZipStreamTest.class.getResource("/Archive.zip").getFile());
        
        try (ZipFile archive = new ZipFile(file))
        {
        		Collection<String> contents = new ArrayList<String>();
            for (ZipElement element: archive){
                contents.add(element.getName());
            }

            Assert.assertTrue("iteration returned incorrect number of contents", contents.size() == 4);
            Assert.assertTrue("file1.txt missing", contents.contains("file1.txt"));
            Assert.assertTrue("file2.txt missing", contents.contains("file2.txt"));
            Assert.assertTrue("directory/ missing", contents.contains("directory/"));
            Assert.assertTrue("directory.file3.txt missing", contents.contains("directory/file3.txt"));
        }
    }
}
