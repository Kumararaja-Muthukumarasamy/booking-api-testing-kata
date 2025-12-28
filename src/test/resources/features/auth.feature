@auth
Feature: Authentication API
  The authentication API generates tokens for accessing protected booking endpoints.

  Background:
    Given the authentication service is available

  @auth-positive @contract
  Scenario: Generate authentication token with valid credentials
    When I authenticate with valid credentials
    Then a valid authentication token should be returned
    And the authentication request should match the auth-request schema
    And the authentication response should match the auth-response schema

  @auth-negative @contract
  Scenario Outline: Authentication fails with invalid or missing credentials
    When I authenticate with username "<username>" and password "<password>"
    Then authentication should fail with status 401 and error "Invalid credentials"
    And the authentication response should match the auth-response schema

    Examples:
      | username | password |
      | wrong    | password |
      | admin    | wrong    |
      | wrong    | wrong    |
      |          |          |
      | admin    |          |
      |          | password |