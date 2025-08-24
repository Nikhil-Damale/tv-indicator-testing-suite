Feature: Indicators manipulation

  @macd
  Scenario: Add MACD indicator and verify panel appears
    Given I open TradingView advanced chart
    And I load symbol "AAPL"
    When I add indicator "MACD"
    Then MACD panel should be visible
