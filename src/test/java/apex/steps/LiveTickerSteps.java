package apex.steps;

import apex.utils.TradingViewPage;
import apex.utils.DriverFactory;
import io.cucumber.java.en.*;
import static org.junit.Assert.*;

public class LiveTickerSteps {
  TradingViewPage tv;
  String a,b;

  @When("I capture price at time A")
  public void price_a(){ tv = new TradingViewPage(DriverFactory.get()); a = tv.readLivePrice(); }

  @When("I wait 5 seconds")
  public void wait5() throws InterruptedException { Thread.sleep(5000); }

  @When("I capture price at time B")
  public void price_b(){ b = tv.readLivePrice(); }

  @Then("price A should not equal price B")
  public void assert_diff(){ assertNotEquals(a, b); }

  @Then("both prices should match format {string}")
  public void assert_format(String regex){
    assertTrue(a.matches(regex)); assertTrue(b.matches(regex));
  }
}
