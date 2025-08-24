package apex.steps;

import apex.utils.TradingViewPage;
import apex.utils.DriverFactory;
import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.Assert.assertTrue;

public class IndicatorSteps {
  TradingViewPage tv;

  @When("I add indicator {string}")
  public void add_indicator(String name){
    tv = new TradingViewPage(DriverFactory.get());
    tv.addIndicator(name);
  }

  @Then("MACD panel should be visible")
  public void macd_visible(){
    WebDriverWait w = new WebDriverWait(DriverFactory.get(), Duration.ofSeconds(10));
    // 🔎 MACD legend/pane visible (aria-label/text contains MACD)
    assertTrue(
      w.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
        By.xpath("//*[contains(@aria-label,'MACD') or contains(.,'MACD')]")
      )).size() > 0
    );
  }
}
