

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import org.testng.Reporter;

public class ReadContentResourceFiles {
    public ReadContentResourceFiles() {
    }

    public InputStream readContentResourceFile(String filePath) {
        InputStream is = ReadContentResourceFiles.class.getClassLoader().getResourceAsStream(filePath);
        if (is == null) {
            throw new RuntimeException(filePath);
        } else {
            return is;
        }
    }

    public <T> T readDtoFromFile(String filePath, Class<T> tClass) {
        InputStream inputStream = this.readContentResourceFile(filePath);
        Object result = null;

        try {
            result = (new ObjectMapper()).readValue(inputStream, tClass);
        } catch (IOException var6) {
            Reporter.log(String.format("Could not load JSON file data from file %s%n%s", filePath, var6));
        }

        return result;
    }
}


