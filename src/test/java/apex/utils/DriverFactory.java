package apex.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.WebDriver;

public class DriverFactory {
  private static final ThreadLocal<WebDriver> TL = new ThreadLocal<>();

  public static void init(boolean headless) {
    WebDriverManager.chromedriver().setup();
    ChromeOptions opt = new ChromeOptions();
    if (headless) opt.addArguments("--headless=new");
    opt.addArguments("--window-size=1600,1000","--disable-gpu","--no-sandbox");
    TL.set(new ChromeDriver(opt));
  }

  public static WebDriver get() { return TL.get(); }

  public static void quit() {
    if (TL.get()!=null) { TL.get().quit(); TL.remove(); }
  }
}