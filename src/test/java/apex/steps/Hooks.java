package apex.steps;

import apex.utils.DriverFactory;
import apex.utils.ExtentManager;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import io.cucumber.java.*;

import java.io.FileInputStream;
import java.util.Properties;

public class Hooks {
  public static Properties cfg;
  public static ExtentReports extent;
  public static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

  @BeforeAll
  public static void beforeAll() throws Exception{
    cfg = new Properties();
    cfg.load(new FileInputStream("src/test/resources/config.properties"));
    extent = ExtentManager.get();
  }

  @Before
  public void before(Scenario sc){
    boolean headless = Boolean.parseBoolean(cfg.getProperty("HEADLESS","true"));
    DriverFactory.init(headless);
    test.set(extent.createTest(sc.getName()));
  }

  @After
  public void after(Scenario sc){
    if(sc.isFailed()){
      // (optional) attach screenshot to report
    }
    DriverFactory.quit();
  }

  @AfterAll
  public static void afterAll(){
    extent.flush();
  }
}
