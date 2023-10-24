package extentReport.testng.listeners;

import com.abc.automation.helpers.PropertiesUtils;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class ExtentTestListener implements ITestListener {


    int failedCount;
    private ConsoleOutputStream consoleOutput = new ConsoleOutputStream();
    private ExtentRepotSetup extentRepotSetup = new ExtentRepotSetup();
    private ExtentTestReport extentTestReport = new ExtentTestReport();
    private String consoleSeparator = "&&&&";
    private String testMethodName = "Method name=:  ";
    private String testClassName = "   ,Class name=: ";
    private String testParamUsed = "Parameter =:  ";

    public ExtentTestListener() {
    }

    public String methodName(ITestResult result) {
        return result.getMethod().getMethodName();
    }

    public String className(ITestResult result) {
        return result.getMethod().getTestClass().getName();
    }

    public void writeStatusToReport(Status status, String info) {
        this.extentTestReport.getTest().log(status, info);
    }

    public void onStart(ITestContext context) {
        // context.getSuite().getXmlSuite().setThreadCount(3);
        System.out.println("*** TEST SUITE " + context.getName() + " started ***");
        this.extentTestReport.getExtent().getStats();
        System.out.println("threadCount " + context.getSuite().getXmlSuite().getThreadCount());
    }

    public void onFinish(ITestContext context) {
        System.out.println("*** TEST SUITE " + context.getName() + " ending ***");

        try {
            FileWriter myWriter = new FileWriter("failedTest.txt");
            Throwable var3 = null;

            try {
                if (this.failedCount >= 1) {
                    myWriter.write("failed count :" + this.failedCount);
                    System.out.println("** failed count :" + this.failedCount);
                }
            } catch (Throwable var13) {
                var3 = var13;
                throw var13;
            } finally {
                if (myWriter != null) {
                    if (var3 != null) {
                        try {
                            myWriter.close();
                        } catch (Throwable var12) {
                            var3.addSuppressed(var12);
                        }
                    } else {
                        myWriter.close();
                    }
                }

            }
        } catch (IOException var15) {
            var15.printStackTrace();
        }

        this.extentTestReport.endTest();
        // System.gc();
    }

    public void onTestStart(ITestResult result) {
        System.out.println("\r\n");
        System.out.println("Thread id  : " + Thread.currentThread().getId());
        System.out.println("Thread name  : " + Thread.currentThread().getName());
        System.out.println("Failed Count  : " + result.getTestContext().getFailedTests().size());
        System.out.println("Passed Count  : " + result.getTestContext().getPassedTests().size());
        System.out.println(this.consoleSeparator);
        System.out.println("*** RUNNING TEST METHOD " + this.methodName(result) + "...");
        this.consoleOutput.start();
        this.extentRepotSetup.enableLogging();
        this.extentTestReport.startTest((this.className(result) + " :: " + this.methodName(result)).concat(" [").concat(this.extentRepotSetup.getParameter(result)).concat("]"));
        this.extentTestReport.getTest().assignCategory(new String[]{this.className(result)});
    }

    public void onTestFailure(ITestResult result) {
        this.extentTestReport.getTest().fail(MarkupHelper.createLabel(" - Test Case Failed", ExtentColor.RED));
        this.writeStatusToReport(Status.FAIL, this.testMethodName + this.methodName(result) + this.testClassName + this.className(result));
        this.writeStatusToReport(Status.FAIL, this.testParamUsed + this.extentRepotSetup.getParameter(result));
        this.failedCount = result.getTestContext().getFailedTests().size() + 1;
        System.out.println(" : Test execution failed " + this.methodName(result));
        System.out.println(this.consoleSeparator);

        try {
            String[] outPutData = this.consoleOutput.stop().split(this.consoleSeparator);

            for (String outMessage : outPutData) {
                if (outMessage.contains("Test execution failed")) {
                    this.extentTestReport.getTest().fail(new AssertionError("\n" + outMessage + "\n" + result.getThrowable()));
                }
            }
        } catch (IOException var7) {
            var7.getMessage();
        }

    }

    public void onTestSuccess(ITestResult result) {
        this.extentTestReport.getTest().pass(MarkupHelper.createLabel(" - Test Case Passed", ExtentColor.GREEN));
        this.writeStatusToReport(Status.PASS, this.testMethodName + this.methodName(result) + this.testClassName + this.className(result));
        this.writeStatusToReport(Status.PASS, this.testParamUsed + this.extentRepotSetup.getParameter(result));
        System.out.println(String.format("NUMBER OF SUCCESSFULLY EXECUTED TEST CASES: %d. TEST CASE: %s IN %s", result.getTestContext().getPassedTests().size() + 1, result.getName(), result.getTestClass().getName()));

        String[] outPutData;
        try {
            outPutData = this.consoleOutput.stop().split(this.consoleSeparator);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (String outMessage : outPutData) {
            if (outMessage.contains("SUCCESSFULLY EXECUTED") && enableLogging()) {
                this.extentTestReport.getTest().pass(MarkupHelper.createCodeBlock("\n" + outMessage + "\n"));
            }
        }
    }

    public void onTestSkipped(ITestResult result) {
        this.extentTestReport.getTest().warning(MarkupHelper.createLabel(" - Test Case Skipped", ExtentColor.ORANGE));
        this.writeStatusToReport(Status.SKIP, this.testMethodName + this.methodName(result) + this.testClassName + this.className(result));
        this.writeStatusToReport(Status.SKIP, result.getThrowable().getMessage());
        System.out.println("TEST CASE SKIPPED: NUMBER OF SKIPPED TEST CASES");
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        System.out.println("TEST FAILED BUT WITHIN PERCENTAGE % ");
    }

    public boolean enableLogging() {
        String logEnabled;
        logEnabled = System.getProperty("isLogEnabled");
        Properties props = (new PropertiesUtils()).loadProps("config.properties");
        if (logEnabled == null) {
            logEnabled = props.getProperty("isLogEnabled");
        }
        return Boolean.parseBoolean(logEnabled);
    }
}

