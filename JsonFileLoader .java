package helpers;

import com.aim.automation.helpers.ReadContentResourceFiles;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * This class is responsible for loading JSON data from file and mapping it to a POJO.
 */
public class JsonFileLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonFileLoader.class);

    /**
     * This is a generic method that reads a file as string, then converts it to the desired type
     *
     * @param filePath the path to the file (starts at resources level)
     * @param <T>      desired return type
     * @return the file content as dto
     */
    public <T> T readDtoFromFile(String filePath, Class<T> tClass) {
        InputStream inputStream = new ReadContentResourceFiles().readContentResourceFile(filePath);

        T result = null;
        try {
            result = new ObjectMapper().readValue(inputStream, tClass);
        } catch (IOException e) {
            LOGGER.error("Could not load JSON file data from file {}", filePath, e);
        }

        return result;
    }

}
