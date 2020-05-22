# automation-testing-demo
    mvn clean test -Dsurefire.suiteXmlFiles=src/test/resources/testsuites/localAndroidAndiOSParallel.testng.xml
    
    Selenium grid with docker:
    docker-compose -d -f src/main/resources/config/selenium/docker-compose.yml up 
