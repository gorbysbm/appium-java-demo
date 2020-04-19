package automation.utility;

import automation.appium.driver.AppiumDriverManager;
import automation.report.HtmlReporter;
import automation.report.Log;
import com.browserstack.local.Local;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONObject;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BrowserStackCapabilities {
    private Method method;
    private JSONObject config;
    private DesiredCapabilities capabilities;
    private static String username = System.getenv("BROWSERSTACK_USERNAME");
    private static String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");

    public BrowserStackCapabilities(Method method, JSONObject config, DesiredCapabilities capabilities) {
        this.method = method;
        this.config = config;
        this.capabilities = capabilities;
    }

    public BrowserStackCapabilities() {
    }

    public String getUsername() {
        return username;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public BrowserStackCapabilities invoke() throws Exception {
        String app = System.getenv("BROWSERSTACK_APP_ID");

        if(username == null) {
            username = (String) config.get("user");
        }

        if(accessKey == null) {
            accessKey = (String) config.get("key");
        }

        if(app != null && !app.isEmpty()) {
            capabilities.setCapability("app", app);
        }

        if (capabilities.getCapability("name").toString().isEmpty()){
            capabilities.setCapability("name", method.getDeclaringClass().getSimpleName()+" :: " +method.getName());
        }

//        if(capabilities.getCapability("browserstack.local") != null
//                && capabilities.getCapability("browserstack.local") == "true"){
//            browserStackLocal = new Local();
//            Map<String, String> options = new HashMap<String, String>();
//            options.put("key", accessKey);
//            browserStackLocal.start(options);
//        }
        return this;
    }

    public void markTests(String status, String reason) throws URISyntaxException, UnsupportedEncodingException, IOException {
        String sessionId = AppiumDriverManager.getDriver().getSessionId().toString();
        Log.info(">>Session id: "+sessionId);
        URI uri = new URI("https://"+username+":"+accessKey+"@api-cloud.browserstack.com/app-automate/sessions/"+sessionId+".json");
        HttpPut putRequest = new HttpPut(uri);

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add((new BasicNameValuePair("status", status)));
        nameValuePairs.add((new BasicNameValuePair("reason", reason)));
        putRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        HttpClientBuilder.create().build().execute(putRequest);
    }
}