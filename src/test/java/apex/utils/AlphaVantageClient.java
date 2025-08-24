package apex.utils;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import java.util.Map;

public class AlphaVantageClient {

  private final String base;
  private final String key;

  public AlphaVantageClient(String baseUrl, String apiKey){
    this.base = baseUrl;
    this.key = apiKey;
  }

  public Map<String,String> getDailyOhlc(String symbol, String yyyy_mm_dd){
    // TIME_SERIES_DAILY returns "Time Series (Daily)": { "YYYY-MM-DD": { "1. open": "...", ... } }
    String url = String.format("%s?function=TIME_SERIES_DAILY&symbol=%s&apikey=%s", base, symbol, key);
    String res = RestAssured.get(url).asString();
    JsonPath jp = new JsonPath(res);
    Map<String,Map<String,String>> series = jp.getMap("Time Series (Daily)");
    if(series==null || !series.containsKey(yyyy_mm_dd))
      throw new RuntimeException("No OHLC for date "+yyyy_mm_dd+" (holiday/old?)");
    return series.get(yyyy_mm_dd);
  }

  public String getGlobalQuotePrice(String symbol){
    String url = String.format("%s?function=GLOBAL_QUOTE&symbol=%s&apikey=%s", base, symbol, key);
    String res = RestAssured.get(url).asString();
    JsonPath jp = new JsonPath(res);
    return jp.getString("Global Quote.05. price");
  }
}
