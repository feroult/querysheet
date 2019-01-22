package querysheet;

import com.github.feroult.gapi.BatchOptions;
import com.github.feroult.gapi.GoogleAPI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import querysheet.batch.ResultSetToSpreadsheetBatch;
import querysheet.batch.TableToSpreadsheetBatch;
import querysheet.db.DatabaseAPI;
import querysheet.utils.Setup;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class QuerySheet {

    private static final int TRUNCATE_LENGTH = 10;

    private static Logger logger = LoggerFactory.getLogger(QuerySheet.class);

    private GoogleAPI google = new GoogleAPI();

    private DatabaseAPI db;

    public static void main(String[] args) {
        String key = Setup.getSetupSpreadsheetKey();

        if (args.length != 0) {
            key = args[0];
        }

        new QuerySheet().process(key);
    }

    public void process(String key) {
        logger.info("setup key: " + key);

        db = new DatabaseAPI();

        try {
            int  time = 0;
            int  row = 1;
            Date data = new Date();

            List<Map<String, String>> queries = google.spreadsheet(key).worksheet("setup").asMap();

            for (Map<String, String> querySetup : queries) {

                row++;
                data = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                try {
                    time += processQuery(querySetup.get("query"), querySetup.get("spreadsheet"), querySetup.get("worksheet"),
                            querySetup.get("batch"), createtBatchOptions(querySetup.get("options")));

                    google.spreadsheet(key).worksheet("setup").setValue(row,1,"Success - "+sdf.format(data));

                }catch (Exception e) { 
                    google.spreadsheet(key).worksheet("setup").setValue(row,1,"Error       - "+sdf.format(data));
                }

            }

            logger.info(String.format("total=%d ms", time));

        } finally {
            db.close();
        }
        
    }

    private BatchOptions[] createtBatchOptions(String optionsString) {
        if (optionsString == null) {
            return new BatchOptions[]{};
        }

        optionsString = optionsString.toUpperCase();

        List<BatchOptions> options = new ArrayList<BatchOptions>();

        if (optionsString.contains(BatchOptions.SHRINK.toString())) {
            options.add(BatchOptions.SHRINK);
        }

        return options.toArray(new BatchOptions[]{});
    }

    private long processQuery(String query, String key, String worksheet, String batchClass, BatchOptions[] batchOptions) {
        long time = System.currentTimeMillis();
        google.spreadsheet(key).worksheet(worksheet).batch(createBatch(query, batchClass), batchOptions);
        time = System.currentTimeMillis() - time;

        logger.info(String.format("elapsed=%d ms, query=%s, spreadsheet=%s, worksheet=%s", time, truncate(query), key, worksheet));

        return time;
    }

    private Object truncate(String s) {
        if (s.length() > TRUNCATE_LENGTH) {
            return s.substring(0, TRUNCATE_LENGTH) + "...";
        }
        return s;
    }

    private ResultSetToSpreadsheetBatch createBatch(String query, String batchClass) {
        ResultSetToSpreadsheetBatch batch = createBatchInstance(batchClass);
        batch.load(db.query(query).resultSet());
        return batch;
    }

    private ResultSetToSpreadsheetBatch createBatchInstance(String batchClass) {
        if (batchClass == null || batchClass.equals("")) {
            return new TableToSpreadsheetBatch();
        }

        try {
            return (ResultSetToSpreadsheetBatch) Class.forName(batchClass).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
