package automation.utility;

import automation.report.Log;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONObject;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestContext;
import org.testng.ITestResult;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class BrowserStackCapabilities {
    private Method method;
    private JSONObject config;
    private DesiredCapabilities capabilities;
    private ITestContext context;
    private static String username = System.getenv("BROWSERSTACK_USERNAME");
    private static String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");

    public BrowserStackCapabilities(Method method, JSONObject config, DesiredCapabilities capabilities, ITestContext context) {
        this.method = method;
        this.config = config;
        this.capabilities = capabilities;
        this.context = context;
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
            capabilities.setCapability("name", method.getDeclaringClass().getSimpleName()+" :: "
                    +method.getName());
        }

        if (capabilities.getCapability("build").toString().isEmpty()){
            capabilities.setCapability("build",
                    context.getSuite().getName()
                            +" :: "+ StringUtilities.getFormattedDate(context.getStartDate().getTime(),"yyyy-MM-dd HH:mm:ss z"));
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

    public void markTests(String status, String reason, String sessionId, ITestResult result) throws URISyntaxException, UnsupportedEncodingException, IOException {
        Log.info(">>Session id: "+sessionId);
        URI uri = new URI("https://"+username+":"+accessKey+"@api-cloud.browserstack.com/app-automate/sessions/"+sessionId+".json");
        HttpPut putRequest = new HttpPut(uri);

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add((new BasicNameValuePair("status", status)));
        nameValuePairs.add((new BasicNameValuePair("reason", reason)));
        nameValuePairs.add((new BasicNameValuePair("name",    result.getMethod().getQualifiedName() +" :: " +sessionId)));
        putRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        HttpClientBuilder.create().build().execute(putRequest);
    }
}