@auth-token
Feature: Generate Authentication Token
  As a consumer of the Booking API I want to generate an authentication token
  So that I can access secured booking endpoints

  Background:
    Given the authentication service is available

  @auth-positive @contract
  Scenario: Generate authentication token with valid credentials
    When I authenticate with valid credentials
    Then a valid authentication token should be returned
    And the authentication request should match the auth-request schema
    And the authentication response should match the auth-response schema

  @auth-negative
  Scenario Outline: Authentication fails for invalid or incomplete credentials
    When I authenticate using "<scenario>"
    Then authentication should fail with status 401 and error "Invalid credentials"

    Examples:
      | scenario           |
      | InvalidUsername    |
      | InvalidPassword    |
      | BothInvalid        |
      | BlankUsername      |
      | BlankPassword      |
      | MissingUsername    |
      | MissingPassword    |