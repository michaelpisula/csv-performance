package csv.performance;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.lang3.StringUtils;

public class Java7CommonsSplit implements CsvReader {

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
        return StringUtils.split(line, ',');
    }
}
