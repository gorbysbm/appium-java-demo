package automation.utility;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.internal.TestResult;
import org.testng.log4testng.Logger;

public class Retry implements IRetryAnalyzer {
    private int retryCount = 0;
    private static final int DEFAULT_MAX_RETRY_COUNT = 1;   // retry a failed test N additional times

    @Override
    public boolean retry(ITestResult result) {
        int maxRetry = DEFAULT_MAX_RETRY_COUNT;
        String maxRetryStr = ((TestResult) result).getTestContext().getSuite().getParameter("retryNumber");
        if (maxRetryStr != null) {
            maxRetry = Integer.parseInt(maxRetryStr);
        }

        if (retryCount < maxRetry) {
            retryCount++;
            return true;
        }
        return false;
    }

    public boolean isRetryAvailable() {
        return (retryCount > 0);
    }
}