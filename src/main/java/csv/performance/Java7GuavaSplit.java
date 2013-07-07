package csv.performance;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.common.base.Splitter;

public class Java7GuavaSplit implements CsvReader {

    private final Splitter splitter = Splitter.on(',');

    @Override
    public long processFile(String path) throws IOException {
        long lineCount = 0;
        try (BufferedReader in = Files.newBufferedReader(Paths.get(path), StandardCharsets.UTF_8)) {
            String line;
            while ((line = in.readLine()) != null) {
                Iterable<String> fields = processLineWithGuava(line);
                lineCount++;
            }
        }
        return lineCount;
    }

    public Iterable<String> processLineWithGuava(String line) {
        return splitter.split(line);
    }

    @Override
    public String[] processLine(String line) {
        throw new RuntimeException("not implemented");
    }
}
