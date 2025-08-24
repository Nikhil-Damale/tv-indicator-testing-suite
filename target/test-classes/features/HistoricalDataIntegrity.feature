Feature: Historical Data Integrity (UI vs API)

  @historical
  Scenario: Validate daily OHLC on TradingView equals Alpha Vantage for yesterday
    Given I open TradingView advanced chart
    And I load symbol "AAPL"
    When I hover the candle for yesterday's date
    Then I capture OHLC from the UI tooltip
    And I fetch daily OHLC for yesterday from Alpha Vantage
    And I assert UI OHLC equals API OHLC
