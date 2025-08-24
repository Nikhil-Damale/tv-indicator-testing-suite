package apex.steps;

import apex.utils.*;
import apex.utils.TradingViewPage.Ohlc;
import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;

import java.util.Map;

import static org.junit.Assert.*;

public class HistoricalDataSteps {

  private WebDriver driver(){ return DriverFactory.get(); }
  private TradingViewPage tv;
  private String tipText;
  private Ohlc ui;
  private Map<String,String> api;
  private AlphaVantageClient client;

  @Given("I open TradingView advanced chart")
  public void open_chart() {
    tv = new TradingViewPage(driver());
    tv.open(Hooks.cfg.getProperty("BASE_URL"));
    Hooks.test.get().info("Opened: "+Hooks.cfg.getProperty("BASE_URL"));
  }

  @Given("I load symbol {string}")
  public void load_symbol(String sym) {
    tv.loadSymbol(sym);
    Hooks.test.get().info("Loaded symbol: "+sym);
    client = new AlphaVantageClient(Hooks.cfg.getProperty("ALPHA_BASE"), Hooks.cfg.getProperty("API_KEY"));
  }

  @When("I hover the candle for yesterday's date")
  public void hover_yesterday() {
    // Heuristic: move ~120 px per candle on default zoom. Adjust if needed.
    tv.hoverCandleFromRight(120); // << tweak if you want different day
    tipText = tv.tooltipText();
    Hooks.test.get().info("Tooltip: "+tipText);
  }

  @Then("I capture OHLC from the UI tooltip")
  public void capture_ui_ohlc() {
    ui = tv.parseOhlc(tipText);
    Hooks.test.get().info(String.format("UI OHLC: O=%s H=%s L=%s C=%s", ui.open(), ui.high(), ui.low(), ui.close()));
  }

  @Then("I fetch daily OHLC for yesterday from Alpha Vantage")
  public void fetch_api_ohlc() {
    String day = DateUtils.yesterdayYYYYMMDD();
    api = client.getDailyOhlc(Hooks.cfg.getProperty("SYMBOL","AAPL"), day);
    Hooks.test.get().info("API("+day+") OHLC: "+api);
  }

  @Then("I assert UI OHLC equals API OHLC")
  public void assert_equal() {
    // Allow minor rounding differences (2 decimals)
    assertEquals(round(api.get("1. open")), round(ui.open()));
    assertEquals(round(api.get("2. high")), round(ui.high()));
    assertEquals(round(api.get("3. low")),  round(ui.low()));
    assertEquals(round(api.get("4. close")),round(ui.close()));
  }

  private String round(String v){
    if(v==null) return "";
    return String.format("%.2f", Double.parseDouble(v.replaceAll(",", "")));
  }
}
