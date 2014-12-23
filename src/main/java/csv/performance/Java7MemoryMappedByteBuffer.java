package csv.performance;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

// http://chickenstew511.blogspot.com/2011/09/java-mmio-mappedbytebuffer-with.html
public class Java7MemoryMappedByteBuffer implements CsvReader {

    @Override
    public long processFile(String path) throws IOException {
        long lineCount = 0;

        try (FileInputStream fis = new FileInputStream(new File(path)); FileChannel fc = fis.getChannel()) {
            byte[] buffer = new byte[(int) fc.size()];
            MappedByteBuffer mmb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            mmb.get(buffer);
            BufferedReader in = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buffer)));

            String line;
            while ((line = in.readLine()) != null) {
                String[] fields = processLine(line);
                lineCount++;
            }

            in.close();
        }

        return lineCount;
    }

    @Override
    public String[] processLine(String line) {
        return line.split(",", -1);
    }
}
