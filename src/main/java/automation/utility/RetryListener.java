package automation.utility;

import org.testng.IAnnotationTransformer;
import org.testng.IRetryAnalyzer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class RetryListener implements IAnnotationTransformer {
    public void transform(ITestAnnotation annotation, Class testClass,
                          Constructor testConstructor, Method testMethod) {
        //TODO check that still works after update to fix deprecation?
        Class<? extends IRetryAnalyzer> retry = annotation.getRetryAnalyzerClass();
        if (retry == null) {
            annotation.setRetryAnalyzer(Retry.class);
        }
    }
}