package extentReport.testng.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import java.util.HashMap;
import java.util.Map;

/*
 * 
 * this Class contains the method which will invoke Test Start , End and Get Test
 * This class instance is used in Testng Testlistener to create the report
 */

public class ExtentTestReport {
    private Map<Integer, ExtentTest> extentTestMap = new HashMap<>();
    private ExtentReports extent = ExtentRepotSetup.getInstance();

    public void setExtent(ExtentReports extent) {
        this.extent = extent;
    }

    public ExtentReports getExtent() {
        return extent;
    }


    public synchronized ExtentTest getTest() {
        return (ExtentTest) extentTestMap.get((int) (long) (Thread.currentThread().getId()));
    }

    public synchronized void endTest() {
        extentTestMap.get((int) (long) (Thread.currentThread().getId()));
        extent.flush();
    }

    public synchronized ExtentTest startTest(String testName) {
        ExtentTest test = extent.createTest(testName);
        extentTestMap.put((int) (long) (Thread.currentThread().getId()), test);
        return test;
    }
}
