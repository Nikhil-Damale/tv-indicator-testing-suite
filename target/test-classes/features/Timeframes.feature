Feature: Timeframe switching

  @timeframes
  Scenario: Switch 1D -> 1W -> 1M and verify change
    Given I open TradingView advanced chart
    And I load symbol "AAPL"
    When I change timeframe to "1W"
    And I change timeframe to "1M"
    Then chart should reflect timeframe aggregation change
