package automation.report;

import automation.utility.FilePaths;
import io.appium.java_client.AppiumDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class CaptureArtifact {
    /**
     * This method is used to capture a screenshot then write to the TestNG
     * Logger
     *
     *
     *
     * @return A html tag that reference to the image, it's attached to the
     *         report.html
     * @throws Exception
     */
    public static String takeScreenshot(WebDriver driver) throws Exception {

        String failureImageFileName = new SimpleDateFormat("MM-dd-yyyy_HH-mm-ss.SSS")
                .format(new GregorianCalendar().getTime()) + ".jpg";
        try {
            if (driver != null) {
                File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                String screenShotDirector = FilePaths.getScreenshotFolder();
                FileUtils.copyFile(scrFile, new File(screenShotDirector + File.separator + failureImageFileName));

                return screenShotDirector + File.separator + failureImageFileName;
            }
        } catch (Error | Exception e) {
            HtmlReporter.info(">> Unable to capture screenshot due to: " + e.getStackTrace());
        }
        return "";
    }

    /**
     * This method is used to capture a screenshot
     *
     *
     *
     * @return A html tag that reference to the image, it's attached to the
     *         report.html
     * @throws Exception
     */
    public String takeScreenshot(AppiumDriver driver, String filename) throws Exception {

        String screenShotDirector = FilePaths.getScreenshotFolder();
        String screenshotFile = FilePaths.correctPath(screenShotDirector + filename);

        try {
            if (driver != null) {

                File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

                FileUtils.copyFile(scrFile, new File(screenshotFile));

                return screenshotFile;

            } else {
                return "";
            }
        } catch (Error | Exception e) {
            Log.error("Can't capture the screenshot");
            Log.error(e.getMessage());
            throw e;
        }
    }

    /**
     * This method is used to capture a screenshot with Ashot
     *
     *
     * @return The screenshot path
     * @throws Exception
     */
    public String takeScreenshotWithAshot(AppiumDriver driver, String fileDir) throws Exception {

        fileDir = FilePaths.correctPath(fileDir);
        try {

            if (driver != null) {
                Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(100))
                        .takeScreenshot(driver);
                ImageIO.write(screenshot.getImage(), "jpg", new File(fileDir));
            } else {
                fileDir = "";
            }

        } catch (Error | Exception e) {
            Log.error("Can't capture the screenshot");
            Log.error(e.getMessage());
            throw e;
        }
        return fileDir;
    }

    /**
     * This method is used to capture an element's screenshot with Ashot
     *
     *
     * @return The screenshot path
     * @throws Exception
     */
    public String takeScreenshotWithAshot(AppiumDriver driver, String fileDir, WebElement element) throws Exception {

        fileDir = FilePaths.correctPath(fileDir);
        try {

            if (driver != null) {
                Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(100))
                        .takeScreenshot(driver, element);
                ImageIO.write(screenshot.getImage(), "jpg", new File(fileDir));
            }

        } catch (Error | Exception e) {
            Log.error("Can't capture the screenshot");
            Log.error(e.getMessage());
            throw e;
        }
        return fileDir;
    }


}
