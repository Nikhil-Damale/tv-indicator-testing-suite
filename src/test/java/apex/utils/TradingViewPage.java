package apex.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.regex.*;

public class TradingViewPage {
  private final WebDriver driver;
  private final WebDriverWait wait;

  public TradingViewPage(WebDriver d){
    this.driver=d;
    this.wait=new WebDriverWait(d, Duration.ofSeconds(30));
  }

  public void open(String base){ driver.get(base); }

  public void loadSymbol(String symbol){
    // 🔎 Search icon/button (stable attr: data-name="header-toolbar-symbol-search")
    By searchBtn = By.cssSelector("button[data-name='header-toolbar-symbol-search']");
    wait.until(ExpectedConditions.elementToBeClickable(searchBtn)).click();

    // 🔎 Search input (role=textbox)
    By searchInput = By.cssSelector("input[role='combobox'], input[role='textbox']");
    WebElement box = wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
    box.sendKeys(symbol);
    // 🔎 First result (listbox options)
    By first = By.cssSelector("[role='listbox'] [role='option']");
    wait.until(ExpectedConditions.elementToBeClickable(first)).click();

    // 🔎 Chart loaded check: symbol title in header legend
    By legend = By.cssSelector("[data-name='legend-source-title']"); // shows ticker symbol
    wait.until(ExpectedConditions.textToBePresentInElementLocated(legend, symbol));
  }

  // Canvas element—mouse hover performed with pixel offsets
  private WebElement chartCanvas(){
    // 🔎 Main chart canvas (class usually 'chart-markup-table' or 'chart-markup-table-pane')
    return wait.until(d -> d.findElement(By.cssSelector("canvas.chart-markup-table, canvas.chart-markup-table-pane")));
  }

  // Hover candle roughly by x-offset from right edge (y at mid-height). Fine-tune if needed.
  public void hoverCandleFromRight(int pixelsFromRight){
    WebElement canvas = chartCanvas();
    Dimension size = canvas.getSize();
    int x = size.getWidth() - pixelsFromRight;
    int y = (int)(size.getHeight()*0.5);
    new Actions(driver).moveToElement(canvas, x - size.getWidth()/2, y - size.getHeight()/2).perform();
  }

  // Tooltip text (OHLC) – appears near top; container often has data-name='floating-tooltip'
  public String tooltipText(){
    By floating = By.cssSelector("[data-name='floating-tooltip'], .tooltip-2QCEh"); // fallback class
    WebElement tip = wait.until(ExpectedConditions.visibilityOfElementLocated(floating));
    return tip.getText();
  }

  // Parse OHLC from tooltip (common pattern: O, H, L, C labels + numeric)
  public Ohlc parseOhlc(String tip){
    Pattern p = Pattern.compile("O\\s*([0-9.,]+).*?H\\s*([0-9.,]+).*?L\\s*([0-9.,]+).*?C\\s*([0-9.,]+)", Pattern.DOTALL);
    Matcher m = p.matcher(tip);
    if(!m.find()) throw new RuntimeException("OHLC not found in tooltip: "+tip);
    return new Ohlc(clean(m.group(1)), clean(m.group(2)), clean(m.group(3)), clean(m.group(4)));
  }
  private String clean(String s){ return s.replaceAll(",", "").trim(); }

  public static record Ohlc(String open, String high, String low, String close){}

  // Open Indicators dialog and add MACD
  public void addIndicator(String name){
    // 🔎 Indicators button: data-name="indicator-button"
    By indicators = By.cssSelector("button[data-name='indicator-button']");
    wait.until(ExpectedConditions.elementToBeClickable(indicators)).click();

    // 🔎 Search field inside modal: role=searchbox or input[type='text']
    By indSearch = By.cssSelector("div[role='dialog'] input[type='text']");
    WebElement s = wait.until(ExpectedConditions.elementToBeClickable(indSearch));
    s.sendKeys(name);
    // 🔎 First result in modal list
    By first = By.cssSelector("div[role='dialog'] [role='listitem'], div[role='dialog'] .itemRow-"); // class prefix fallback
    wait.until(ExpectedConditions.elementToBeClickable(first)).click();

    // 🔎 MACD panel visible at bottom: aria-label contains MACD in legend/pane
    By macdPane = By.xpath("//*[contains(@aria-label,'MACD') or contains(.,'MACD')][contains(@class,'legend') or contains(@class,'pane')]");
    wait.until(ExpectedConditions.visibilityOfElementLocated(macdPane));
  }

  // Change timeframe using top toolbar quick buttons (text 1D/1W/1M) or data-interval
  public void changeTimeframe(String tf){
    // 🔎 Buttons usually have aria-label like "Time Interval" submenu; quick buttons have text
    By quick = By.xpath("//button[normalize-space()='"+tf+"']");
    if(driver.findElements(quick).size()>0){
      wait.until(ExpectedConditions.elementToBeClickable(quick)).click();
      return;
    }
    // Fallback: open interval menu and pick item by data-interval
    By intervalBtn = By.cssSelector("button[data-name='header-interval-dialog-button']");
    wait.until(ExpectedConditions.elementToBeClickable(intervalBtn)).click();
    By item = By.cssSelector(String.format("[data-interval='%s']", tf.toLowerCase())); // e.g., 1d/1w/1m
    wait.until(ExpectedConditions.elementToBeClickable(item)).click();
  }

  // Live price element (header) – often data-name='last-price-value' or role='heading' with price
  public String readLivePrice(){
    By price = By.cssSelector("[data-name='last-price-value'], .lastPrice-");
    WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(price));
    return el.getText().replaceAll("[^0-9.]", "");
  }
}
