package extentReport.testng.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private int counter = 0;
    private int retryLimit = 3;

    @Override
    public boolean retry(ITestResult result) {
        if (counter < retryLimit) {
            counter++;

            return true;
        }

        return false;
    }

}
