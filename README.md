# 📊 Apex – Real-Time Data Integrity Framework  

## 📌 Overview  
This repository contains **automation scripts and validation tests** for a financial charting platform.  
The project focuses on validating **TradingView’s Advanced Chart** against the **Alpha Vantage API** to ensure **data accuracy and integrity**.  

The framework handles **dynamic data, complex UI automation, and precision API vs. UI validation**.  

## 🎯 Objectives  
- Validate TradingView chart’s OHLC data against Alpha Vantage API.  
- Automate chart interactions (adding indicators, switching timeframes).  
- Verify real-time price ticker updates and format consistency.  
- Deliver BDD-driven tests with professional reporting and CI/CD integration.  

## 🛠️ Tools & Technologies  
- Selenium WebDriver (Java + Maven)  
- Cucumber (BDD Scenarios)  
- Extent Reports (Detailed Reporting)  
- Alpha Vantage API (Stock Market Data)  
- GitHub Actions (CI/CD)  

## 🔎 Test Coverage  
- Historical OHLC Data Validation (UI vs. API)  
- Adding and verifying MACD Indicator  
- Switching chart timeframes (Daily, Weekly, Monthly)  
- Real-time ticker liveness and regex validation  
- Detailed Extent Reports with API vs. UI comparison  

## 🚀 How to Run Tests  
```bash
# Clone repo
git clone https://github.com/Nikhil-Damale/tv-indicator-testing-suite
cd tv-indicator-testing-suite

# Add Alpha Vantage API key in config
# Example: src/test/resources/config.properties

# Run Maven tests
mvn clean test
