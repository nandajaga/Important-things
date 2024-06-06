import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

//csv to data provider with return type as Map
public class CsvToDataProvider {
@DataProvider(name ="yourDataProviderName")
    public Iterator<Object[]> yourDataProviderName(){
        return CsvToDataProvider.csvDataProvider("data.csv");
    }

@Test(dataProvider = "yourDataProviderName")
    public void testUsingCsvData(Map<String, String> dataMap) {
        // Your test logic here
        System.out.println( "Value: " + dataMap.get("Value");
    }

 public static Iterator<Object[]> csvDataProvider(String csvFilePath) {
        List<Object[]> testData = new ArrayList<>();
        InputStream inputStream = CsvToDataProvider.class.getClassLoader().getResourceAsStream(csvFilePath);

        try {
            assert inputStream != null;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                String[] headers = null;
                while ((line = br.readLine()) != null) {
                    String[] rows = line.split(","); // Assuming CSV columns are comma-separated
                    if (headers == null) {
                        // First row contains headers
                        headers = rows;
                    } else if (rows.length >= headers.length) {
                        Map<String, String> dataMap = new HashMap<>();
                        for (int i = 0; i < headers.length; i++) {
                            dataMap.put(headers[i].trim(), rows[i].trim());
                        }
                        testData.add(new Object[]{dataMap});
                    } else {
                        System.err.println("Error in creating data provider");
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }

        return testData.iterator();
    }
}
