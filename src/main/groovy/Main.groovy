import csv.performance.CsvReader
import csv.performance.Java7Base
import csv.performance.Java7CommonsSplit
import csv.performance.Java7GuavaSplit
import csv.performance.Java7MemoryMappedByteBuffer
import csv.performance.Java7RandomAccessFile
import csv.performance.Java7ReadingAndSplittingThread
import csv.performance.Java8Stream

class Main {

    static void main(String[] args) {
        def path = 'data.csv'
        generateTestFile(path, [lineCount: 1_000_000, fieldCount: 50, fieldLength: 10])
        benchmarkImplementation(Java7Base, path)
        benchmarkImplementation(Java8Stream, path)
        benchmarkImplementation(Java7CommonsSplit, path)
        benchmarkImplementation(Java7GuavaSplit, path)
        benchmarkImplementation(Java7MemoryMappedByteBuffer, path)
        benchmarkImplementation(Java7RandomAccessFile, path)
        benchmarkImplementation(Java7ReadingAndSplittingThread, path)
        deleteTestFile(path)
    }

    private static void benchmarkImplementation(Class<? extends CsvReader> csvReaderClass, String path) {
        3.times{
            try {
                long start = System.currentTimeMillis()
                CsvReader csvReader = csvReaderClass.newInstance()
                long lineCount = csvReader.processFile(path)
                long end = System.currentTimeMillis()
                long executionTimeMs = end - start
                long linesPerSecond = lineCount / executionTimeMs * 1000
                println("${csvReaderClass.simpleName}: ${executionTimeMs} ms / ${lineCount} lines => ${linesPerSecond} lines/s")
            } catch (e) {
                println("An error occured for class ${csvReaderClass.canonicalName}: ${e.message}")
            }
        }
    }

    private static generateTestFile(path, csvParams) {
        deleteTestFile(path)
        def file = new File(path)
        def fields = (0 ..< csvParams.fieldCount).collect{ 'x' * csvParams.fieldLength }.join(',')
        file.withWriterAppend('UTF-8') { out ->
            for (lineIndex in (0 ..< csvParams.lineCount)) {
                out.println("${lineIndex},${fields}")
            }
        }
    }

    private static deleteTestFile(path) {
        new File(path).delete()
    }
}
