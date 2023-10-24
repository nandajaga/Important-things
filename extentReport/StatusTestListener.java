package extentReport.testng.listeners;

import org.testng.*;

import java.util.Iterator;

public class StatusTestListener extends TestListenerAdapter {

    @Override
    public void onTestSuccess(ITestResult tr) {

        for (int i = 0; i < Reporter.getOutput().size(); i++) {
            if (Reporter.getOutput(tr).contains(Reporter.getOutput().get(i))) {
                Reporter.getOutput().set(i, "");
            }
        }

        System.out.println(String.format("NUMBER OF SUCCESSFULLY EXECUTED TEST CASES: %d. TEST CASE: %s IN %s",
                tr.getTestContext().getPassedTests().size() + 1,
                tr.getName(),
                tr.getTestClass().getName()));
    }

    @Override
    public void onTestFailure(ITestResult tr) {
        StringBuilder params = new StringBuilder();

        for (Object param : tr.getParameters()) {
            params.append(param).append(' ');
        }

        String parameters = params.toString().trim();

        System.out.println(String.format("TEST CASE FAILED: %s.%s[%s] %nNUMBER OF FAILED TEST CASES: %d %nREASON: %s",
                tr.getTestClass().getName(),
                tr.getName(),
                parameters,
                tr.getTestContext().getFailedTests().size() + 1,
                tr.getThrowable().getMessage()));

        for (String s : Reporter.getOutput(tr)) {
            System.out.println(String.format("%s%n", s));
        }
    }

    @Override
    public void onTestSkipped(ITestResult tr) {
        System.out.println(String.format("TEST CASE SKIPPED: %s.%s %nNUMBER OF SKIPPED TEST CASES: %d",
                tr.getTestClass().getName(),
                tr.getName(),
                tr.getTestContext().getSkippedTests().size() + 1));
    }

    @Override
    public void onConfigurationFailure(ITestResult tr) {
        System.out.println(String.format("CONFIGURATION FAILED: %s.%s %nNUMBER OF FAILED CONFIGURATIONS: %d %nREASON: %s",
                tr.getTestClass().getName(),
                tr.getName(),
                tr.getTestContext().getSkippedTests().size() + 1,
                tr.getThrowable().getMessage()));

        // Marking a test as failure instead of skipped when we have configuration failure because of wrong status in UCD
        ITestContext tc = tr.getTestContext();
        tc.getFailedTests().addResult(tr, tr.getMethod().getTestClass().getTestMethods()[0]);
        tc.getSkippedTests().getAllMethods().remove(tr.getMethod().getTestClass().getTestMethods()[0]);

        for (String s : Reporter.getOutput(tr)) {
            System.out.println(String.format("%s%n", s));
        }
    }

    @Override
    public void onConfigurationSuccess(ITestResult tr) {
        for (int i = 0; i < Reporter.getOutput().size(); i++) {
            if (Reporter.getOutput(tr).contains(Reporter.getOutput().get(i))) {
                Reporter.getOutput().set(i, "");
            }
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        Iterator<ITestResult> skippedTestCases = context.getSkippedTests().getAllResults().iterator();

        while (skippedTestCases.hasNext()) {
            ITestResult skippedTestCase = skippedTestCases.next();
            ITestNGMethod method = skippedTestCase.getMethod();

            // Removing retried tests with filed or passed result from skipped results
            if (context.getFailedTests().getResults(method).size() > 0 || context.getPassedTests().getResults(method).size() > 0) {
                skippedTestCases.remove();
            }
        }
    }
}
