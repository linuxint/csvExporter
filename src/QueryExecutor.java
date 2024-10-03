
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class QueryExecutor implements Runnable {
    private final SqlMapper sqlMapper;
    private final String sql;
    private final String csvFileName;
    private final boolean includeHeader;
    private final String delimiter;
    private final String lineTerminator;
    private final int batchSize;

    public QueryExecutor(SqlMapper sqlMapper, String sql, String csvFileName, boolean includeHeader, String delimiter, String lineTerminator, int batchSize) {
        this.sqlMapper = sqlMapper;
        this.sql = sql;
        this.csvFileName = csvFileName;
        this.includeHeader = includeHeader;
        this.delimiter = delimiter;
        this.lineTerminator = lineTerminator;
        this.batchSize = batchSize;
    }

    @Override
    public void run() {
        try {
            List<Map<String, Object>> data = sqlMapper.executeQuery(sql);
            writeCsv(data, csvFileName);
        } catch (Exception e) {
            e.printStackTrace(); // 예외 처리
        }
    }

    private void writeCsv(List<Map<String, Object>> data, String fileName) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(fileName))) {
            if (includeHeader && !data.isEmpty()) {
                // Write header
                String[] headers = data.get(0).keySet().toArray(new String[0]);
                writer.writeNext(headers);
            }

            // Write data in batches
            for (int i = 0; i < data.size(); i += batchSize) {
                int end = Math.min(i + batchSize, data.size());
                List<Map<String, Object>> batch = data.subList(i, end);

                for (Map<String, Object> row : batch) {
                    String[] stringRow = row.values().stream().map(Object::toString).toArray(String[]::new);
                    writer.writeNext(stringRow);
                }
            }
        }
    }
}