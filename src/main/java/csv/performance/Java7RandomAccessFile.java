package csv.performance;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Java7RandomAccessFile implements CsvReader {

    @Override
    public long processFile(String path) throws IOException {
        long lineCount = 0;
        try (RandomAccessFile in = new RandomAccessFile(new File(path), "r")) {
            String line;
            while ((line = in.readLine()) != null) {
                String[] fields = processLine(line);
                lineCount++;
            }
        }
        return lineCount;
    }

    @Override
    public String[] processLine(String line) {
        return line.split(",", -1);
    }
}