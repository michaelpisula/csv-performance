package csv.performance;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Java7ReadingAndSplittingThread implements CsvReader {

    private final String EOF = new String();

    @Override
    public long processFile(String path) throws IOException {
        Path inputFile = Paths.get(path);
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(1000);

        FileReadingTask fileReadingTask = new FileReadingTask(inputFile, queue);
        LineSplittingTask lineSplittingTask = new LineSplittingTask(queue);

        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(fileReadingTask);
        executor.execute(lineSplittingTask);
        executor.shutdown();

        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return lineSplittingTask.getLinesProcessed();
    }

    @Override
    public String[] processLine(String line) {
        return line.split(",", -1);
    }

    // ----------------------------------------------------

    class FileReadingTask implements Runnable {

        private final Path inputFile;
        private final BlockingQueue<String> queue;

        public FileReadingTask(Path inputFile, BlockingQueue<String> queue) {
            this.inputFile = inputFile;
            this.queue = queue;
        }

        @Override
        public void run() {
            try (BufferedReader in = Files.newBufferedReader(inputFile, StandardCharsets.UTF_8)) {
                String line;
                while ((line = in.readLine()) != null) {
                    queue.put(line);
                }
                queue.put(EOF);
            } catch (IOException | InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };

    // ----------------------------------------------------

    class LineSplittingTask implements Runnable {

        private long linesProcessed;
        private final BlockingQueue<String> queue;

        public LineSplittingTask(BlockingQueue<String> queue) {
            this.linesProcessed = 0;
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String line = queue.take();
                    if (EOF.equals(line))
                        break;
                    String[] fields = processLine(line);
                    linesProcessed++;
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        public long getLinesProcessed() {
            return linesProcessed;
        }
    };
}