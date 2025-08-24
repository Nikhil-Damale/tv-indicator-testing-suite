Feature: Real-time ticker liveness

  @live
  Scenario: Price changes within 5 seconds and matches format
    Given I open TradingView advanced chart
    And I load symbol "AAPL"
    When I capture price at time A
    And I wait 5 seconds
    And I capture price at time B
    Then price A should not equal price B
    And both prices should match format "\d{1,4}\.\d{2}"
