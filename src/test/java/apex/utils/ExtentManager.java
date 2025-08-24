package apex.utils;

import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.*;

public class ExtentManager {
  private static ExtentReports extent;
  public static ExtentReports get() {
    if (extent==null) {
      ExtentSparkReporter spark = new ExtentSparkReporter("reports/ExtentReport.html");
      spark.config().setTheme(Theme.STANDARD);
      spark.config().setDocumentTitle("Apex Test Report");
      spark.config().setReportName("Apex – Data Integrity");
      extent = new ExtentReports();
      extent.attachReporter(spark);
    }
    return extent;
  }
}
