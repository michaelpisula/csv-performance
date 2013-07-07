package csv.performance;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

public class Java7GuavaSplit implements CsvReader {

    private final Splitter splitter = Splitter.on(',');

    @Override
    public long processFile(String path) throws IOException {
        long lineCount = 0;
        try (BufferedReader in = Files.newBufferedReader(Paths.get(path), StandardCharsets.UTF_8)) {
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
        Iterable<String> fields = splitter.split(line);
        return Iterables.toArray(fields, String.class);
    }
}
