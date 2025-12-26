@auth
Feature: Authentication API
  Authentication allows users to access protected booking services

  Background:
    Given the authentication service is available

  @auth-positive
  Scenario: Generate Authentication token
    When I authenticate with valid credentials
    Then an authentication token should be returned

  @auth-negative
  Scenario Outline: Authenticate with invalid credentials
    When I authenticate with username "<username>" and password "<password>"
    Then authentication should fail with status 401 and error message "Invalid credentials"
    Examples:
      | username | password |
      | wrong    | password |
      | admin    | wrong    |
      | wrong    | wrong    |
      | admin    |          |
      |          | password |
      |          |          |