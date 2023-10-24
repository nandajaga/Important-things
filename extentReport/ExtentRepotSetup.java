package extentReport.testng.listeners;

import com.aim.automation.helpers.PropertiesUtils;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.reporter.configuration.ViewName;
import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.apache.commons.io.FilenameUtils;
import org.testng.ITestResult;

import java.io.File;
import java.util.Properties;

/*
 * Created by sbioi 07/01/2020
 */

public class ExtentRepotSetup {

    private static final String reportFileName = "Test-Automation-Extent-Report" + ".html";
    private static final String fileSeperator = System.getProperty("file.separator");
    static final String reportFilepath = System.getProperty("user.dir") + "/build" + fileSeperator + "extentreport";
    static final String reportFileLocation = reportFilepath + fileSeperator + reportFileName;
    private static ExtentReports reports;
    private static String parameter;

    /*
     * This Method is invoked to fetch the Parameter passed by the Dataprovider and returns the parameter
     */
    public String getParameter(ITestResult result) {
        StringBuilder params = new StringBuilder();

        Object[] var = result.getParameters();

        for (Object param : var) {
            params.append(param).append(' ');
        }
        return parameter = params.toString().trim();
    }

    public static ExtentReports getInstance() {
        if (reports == null) createInstance();
        return reports;
    }

    /*
     * Method is used to enable login in Console
     * If isLogEnabled is true Logs will be captured by Consoloutput method and returns in String
     * @Created by sbioi 07/02/2020
     */
    public boolean enableLogging() {
        String logEnabled;
        logEnabled = System.getProperty("isLogEnabled");
        Properties props = (new PropertiesUtils()).loadProps("config.properties");
        if (logEnabled == null) {
            logEnabled = props.getProperty("isLogEnabled");
        }

        boolean isLogEnabled = Boolean.parseBoolean(logEnabled);
        if (isLogEnabled) {
            RestAssured.replaceFiltersWith(new RequestLoggingFilter(LogDetail.ALL), new ResponseLoggingFilter(LogDetail.ALL));
            return true;
        } else {
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
            return false;
        }
    }

    /*
     *  Create an extent report instance
     * Designing html report with various parameter
     * Assigning the System, Project and Environment detail
     */
    public static void createInstance() {

        String fileName = getReportFilePath(reportFilepath);
        ExtentSparkReporter extentHtmlReporter = new ExtentSparkReporter(fileName).viewConfigurer().viewOrder().
                as(new ViewName[]{ViewName.DASHBOARD,
                        ViewName.TEST,
                        ViewName.CATEGORY,
                        ViewName.AUTHOR,
                        ViewName.DEVICE,
                        ViewName.EXCEPTION,
                        ViewName.LOG
                }).apply();

        extentHtmlReporter.config().setDocumentTitle(reportFileName);
        extentHtmlReporter.config().setEncoding("utf-8");
        extentHtmlReporter.config().setReportName(reportFileName);
        extentHtmlReporter.config().setTimeStampFormat("MMMM dd, yyyy, hh:mm a '('zzz')'");
        extentHtmlReporter.config().setTheme(Theme.STANDARD);

        RestAssured.baseURI = System.getProperty("baseURI");
        Properties props = (new PropertiesUtils()).loadProps("config.properties");
        if (RestAssured.baseURI == null || RestAssured.baseURI.isEmpty()) {
            RestAssured.baseURI = props.getProperty("baseURI");
        }

        reports = new ExtentReports();
        reports.attachReporter(extentHtmlReporter);

        //Set environment,Project   and system details
        reports.setSystemInfo("Test User", System.getProperty("user.name"));
        reports.setSystemInfo("Operating System Type", System.getProperty("os.name"));

        if (!RestAssured.baseURI.equals("http://localhost")) {
            try {
                String domainName = RestAssured.baseURI.split("\\.")[0].split("//")[1].toUpperCase();
                if (!domainName.equalsIgnoreCase("WEB")) {
                    reports.setSystemInfo("Application Name", domainName.split("-")[0] + "_Domain_Service");
                } else {
                    reports.setSystemInfo("Application Name", "Check the BaseUri");

                }

                String envUsed;
                if (domainName.equalsIgnoreCase("web")) {
                    envUsed = (RestAssured.baseURI.split("\\."))[3].toUpperCase();
                } else {
                    envUsed = (RestAssured.baseURI.split("-"))[1].toUpperCase().split("\\.")[0];
                }
                switch (envUsed) {
                    case "SIT":
                        reports.setSystemInfo("Environment", "SIT");
                        break;
                    case "DEV":
                        reports.setSystemInfo("Environment", "DEV");
                        break;
                    case "QA":
                        reports.setSystemInfo("Environment", "QA");
                        break;
                    case "STAGING":
                        reports.setSystemInfo("Environment", "STAGING");
                        break;
                    case "UAT":
                        reports.setSystemInfo("Environment", "UAT");
                        break;
                    case "LDT":
                        reports.setSystemInfo("Environment", "LDT");
                        break;
                    default:
                        reports.setSystemInfo("Environment", "Check the BaseUri");
                        break;
                }
                reports.setSystemInfo("baseURI", RestAssured.baseURI);
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }

    /**
     * Method will find the Reportpath if not  available then creates the file on the specified system location
     *
     * @param path
     * @return
     */
    private static String getReportFilePath(String path) {
        File testDirectory = new File(FilenameUtils.getName(path));
        if (!testDirectory.exists()) {
            if (testDirectory.mkdir()) {
                System.out.println("DIRECTORY: " + path + " IS CREATED!");
                return reportFileLocation;
            } else {
                System.out.println("FAILED TO CREATE DIRECTORY " + path);
                return System.getProperty("user.dir");
            }
        } else {
            System.out.println("DIRECTORY ALREADY EXISTS: " + path);
        }
        return reportFileLocation;
    }
}