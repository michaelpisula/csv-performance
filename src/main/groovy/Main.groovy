import csv.performance.CsvReader
import csv.performance.Java7Base
import csv.performance.Java7CommonsSplit
import csv.performance.Java7GuavaSplit
import csv.performance.Java7RandomAccessFile

class Main {

    static void main(String[] args) {
        def path = 'data.csv'
        generateTestFile(path, [lineCount: 100_000, fieldCount: 50, fieldLength: 10])
        benchmarkImplementation(Java7Base, path)
        benchmarkImplementation(Java7CommonsSplit, path)
        benchmarkImplementation(Java7GuavaSplit, path)
        benchmarkImplementation(Java7RandomAccessFile, path)
    }

    private static void benchmarkImplementation(Class<? extends CsvReader> csvReaderClass, String path) {
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

    private static generateTestFile(path, csvParams) {
        def file = new File(path)
        file.delete()
        def fields = (0 ..< csvParams.fieldCount).collect{ 'x' * csvParams.fieldLength }.join(',')
        file.withWriterAppend { out ->
            for (lineIndex in (0 ..< csvParams.lineCount)) {
                out.println("${lineIndex},${fields}")
            }
        }
    }
}
