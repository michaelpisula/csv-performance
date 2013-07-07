package csv.performance;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Java7Base implements CsvReader {

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
        return line.split(",", -1);
    }
}