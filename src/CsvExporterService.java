
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class CsvExporterService {

    @Autowired
    private SqlMapper sqlMapper;

    @Value("${csv.output.path:output/}")
    private String csvOutputPath;

    @Autowired
    private Environment env;

    @PostConstruct
    public void exportQueriesToCsv() throws IOException {
        String[] queryKeys = {"query1", "query2"};
        ExecutorService executorService = Executors.newCachedThreadPool(); // 쿼리마다 하나의 쓰레드

        for (String key : queryKeys) {
            String sql = env.getProperty(key);
            String csvFileName = env.getProperty("csv.output." + key);
            boolean includeHeader = Boolean.parseBoolean(env.getProperty("csv.header." + key, "true"));
            String delimiter = env.getProperty("csv.delimiter." + key, ",");
            String lineTerminator = env.getProperty("csv.line.terminator." + key, "\n");
            int batchSize = Integer.parseInt(env.getProperty("csv.batch.size." + key, "100")); // 기본값은 100

            executorService.submit(new QueryExecutor(sqlMapper, sql, csvFileName, includeHeader, delimiter, lineTerminator, batchSize));
        }

        executorService.shutdown(); // 모든 작업이 끝난 후 종료
    }
}