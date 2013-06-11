package csv.performance;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.google.common.base.Splitter;

public class Java7GuavaSplit implements CsvReader {

    @Override
    public long processFile(String path) throws IOException {
        long lineCount = 0;
        try (BufferedReader in = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = in.readLine()) != null) {
                Iterable<String> fields = processLineWithGuava(line);
                lineCount++;
            }
        }
        return lineCount;
    }

    public Iterable<String> processLineWithGuava(String line) {
    	return Splitter.on(',').split(line);
    }
    
    @Override
    public String[] processLine(String line) {
        return new String[0];
    }
}