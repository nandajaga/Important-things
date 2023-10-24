package extentReport.testng;

import org.testng.TestNG;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static java.util.logging.Level.CONFIG;


/**
 * TestNG looks for XML suites on the filesystem only.
 * This class allows users to pass relative paths to TestNG XML suites inside this jar as system properties.
 * Refer the javadoc of the main method for detailed instructions on how to run.
 *
 */
public class TestNGRunnerForXMLSuitesInJar {

    private final static String TEMPORARY_OUTPUT_FILE = "testng.xml";
    private final static Logger LOGGER = Logger.getLogger(TestNGRunnerForXMLSuitesInJar.class.getName());

    /**
     * Retrieve TestNG XML suite file from the jar and then write it out to the current folder when executing this jar.
     * The output file is marked for deletion upon virtual machine termination.
     *
     * @param inputFile relative input file path inside the jar
     * @return true in case of success and false in case an exception is thrown
     */
    private static boolean writeXMLtoFileSystem(final String inputFile) {
        File suite = new File(TEMPORARY_OUTPUT_FILE);
        try(InputStream is = TestNGRunnerForXMLSuitesInJar.class.getResourceAsStream(inputFile);
            OutputStream os = Files.newOutputStream(Paths.get(suite.toURI())))
        {
            if (is == null || is.available() == 0) {
                return false;
            }
            byte[] buff = new byte[1024];
            int len;
            while ((len = is.read(buff)) > 0) {
                os.write(buff,0, len);
            }
        } catch (IOException e) {
            LOGGER.log(CONFIG, "Cannot read testng.xml file");
            return false;
        }
        suite.deleteOnExit();
        return true;
    }

    /**
     * Run with:
     * java -DbaseURI=DOMAIN_URI -DtestSuite=/testng.xml -jar DomainFunctionalTests-VERSION.jar
     * Substitute "DOMAIN_URI" with an URI to the system under test (e.g. https://clinical.xformdevpoc.com).
     * Substitute "/testng.xml" with a relative path to the TestNG suite inside the jar.
     * Substitute "DomainFunctionalTests-VERSION.jar" with the name of the built jar (e.g. ClinicalServicesFunctionalTests-2.1.jar)
     * Make sure to pass system properties (-D) before the -jar option.
     *
     * @param args system properties are used instead.
     */
    public static void main(String[] args) {
        final String inputFilePath = System.getProperty("testSuite");
        if (inputFilePath == null) {
            System.err.println("Error: Missing \"testSuite\" system property.");
            System.out.println("Make sure to pass TestNG XML suite file path relative to the jar's root (with leading slash /) as a system property (-D) before the -jar option.");
            System.exit(1);
        }
        if (!writeXMLtoFileSystem(inputFilePath)) {
            System.err.println("Error: Could not locate TestNG suite \"" + inputFilePath + "\".");
            System.out.println("Maybe there's a missing slash (/) at the front?");
            System.exit(1);
        }

        final String baseURI = System.getProperty("baseURI");
        if (baseURI == null) {
            System.err.println("Error: Missing \"baseURI\" system property.");
            System.out.println("Make sure to pass \"baseURI\" that points to the system under test as a system property (-D) before the -jar option.");
            System.exit(1);
        }

        final List<String> xmlFileList = new ArrayList<>();
        xmlFileList.add(TEMPORARY_OUTPUT_FILE);

        final TestNG testng = new TestNG();
        testng.setTestSuites(xmlFileList);
        testng.run();
        if(testng.hasFailure())
        {
            LOGGER.info("Status Code : "+ testng.getStatus());
            System.exit(testng.getStatus());
        }
    }

}
