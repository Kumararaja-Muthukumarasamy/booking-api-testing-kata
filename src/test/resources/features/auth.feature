@auth
Feature: Authentication API

  Scenario: Generate Authentication token
    When I authenticate with valid credentials
    Then an authentication token should be returned