@delete-booking
Feature: Delete Booking
  This feature allows consumers to delete an existing booking
  using a valid booking ID and authentication token
  as defined in the API contract.

  Background:
    Given the booking service is available

  # -------------------------------------------------
  # Functional – Positive Scenario
  # -------------------------------------------------

  @delete-positive
  Scenario: Delete booking with valid ID and valid token
    Given a booking exists
    And I am authenticated with a valid token
    When I delete the booking
    Then the booking should be deleted successfully with status 201

  # -------------------------------------------------
  # Contract – Unauthorized Access
  # -------------------------------------------------

  @delete-contract
  Scenario: Delete booking request is rejected for invalid authentication
    Given a booking exists
    And I use a "invalid" token
    When I attempt to delete the booking
    Then the error response should match the common error schema

  # -------------------------------------------------
  # Functional - Invalid ID Validation
  # -------------------------------------------------

  @delete-negative @invalid-id
  Scenario Outline: Delete booking with invalid ID should fail with bad request
    Given I am authenticated with a valid token
    When I attempt to delete the booking using invalid id "<id>"
    Then the request should fail with status 400

    Examples:
      | id   |
      | 0    |
      | -1   |
      | 9999 |

  # -------------------------------------------------
  # Functional – Authentication Validation
  # -------------------------------------------------

  @delete-negative @auth
  Scenario Outline: Delete booking fails with invalid or missing token
    Given a booking exists
    And I use a "<tokenType>" token
    When I attempt to delete the booking
    Then the request should fail with status 401
    And the error message should be "Unauthorized"

    Examples:
      | tokenType |
      | invalid   |
      | empty     |
      | missing   |