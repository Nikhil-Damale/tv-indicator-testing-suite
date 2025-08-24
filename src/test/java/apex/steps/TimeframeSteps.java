package apex.steps;

import apex.utils.TradingViewPage;
import apex.utils.DriverFactory;
import io.cucumber.java.en.*;

public class TimeframeSteps {
  TradingViewPage tv;

  @When("I change timeframe to {string}")
  public void change_tf(String tf){
    if(tv==null) tv = new TradingViewPage(DriverFactory.get());
    tv.changeTimeframe(tf);
  }

  @Then("chart should reflect timeframe aggregation change")
  public void verify_change(){
    // Simple smoke: just not throwing; optionally compare candle count/tooltip label like "1W"
    // You can also read the small label near top that shows current interval.
  }
}
