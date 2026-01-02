@update-booking
Feature: Update Booking
  This feature allows consumers to update an existing booking
  using a booking ID and authentication token
  as defined in the API contract.

  Background:
    Given the booking service is available
    And a booking exists

  # -------------------------------------------------
  # Functional – Successful Update
  # -------------------------------------------------

  @update-booking-positive
  Scenario: Update booking with valid token and valid booking ID
    Given I am authenticated with a valid token
    When I update the booking with valid details
    Then the booking should be updated successfully with status 200

  # -------------------------------------------------
  # Contract – Unauthorized Access
  # -------------------------------------------------

  @update-booking-negative @auth @contract
  Scenario Outline: Update booking fails due to invalid or missing authentication
    Given I use a "<tokenType>" token
    When I update the booking with valid details
    Then the request should fail with status 401
    And the error message should be "Unauthorized"

    Examples:
      | tokenType |
      | missing   |
      | empty     |
      | invalid   |

  # -------------------------------------------------
  # Defensive – Invalid Booking ID (Non-success)
  # -------------------------------------------------

  @update-booking-negative @bookingid
  Scenario Outline: Update booking with invalid booking ID should not succeed
    Given I am authenticated with a valid token
    When I update the booking with ID "<bookingId>" using valid details
    Then the request should fail with status 400

    Examples:
      | bookingId |
      | 0         |
      | -1        |
      | 9999      |

  # -------------------------------------------------
  # Contract – Response Schema Validation
  # -------------------------------------------------

  @update-booking-contract @contract
  Scenario: Update booking response matches booking contract schema
    Given I am authenticated with a valid token
    When I update the booking with valid details
    Then the update booking response should match the booking schema