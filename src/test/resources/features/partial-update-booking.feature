@patch-booking
Feature: Partially Update Booking
  This feature allows consumers to partially update an existing booking
  using a booking ID and authentication token as defined in the API contract.

  Background:
    Given the booking service is available
    And a booking exists

  # -------------------------------------------------
  # Contract – Successful Partial Update
  # -------------------------------------------------

  @patch-booking-positive
  Scenario: Patch booking with valid token and valid booking ID
    Given I am authenticated with a valid token
    When I patch the booking with partial details
    Then the booking should be partially updated successfully with status 200
    And the patch booking response should match the booking schema

  # -------------------------------------------------
  # Contract – Unauthorized Access
  # -------------------------------------------------

  @patch-booking-negative @auth @contract
  Scenario Outline: Patch booking fails due to invalid or missing authentication
    Given I use a "<tokenType>" token
    When I patch the booking with partial details
    Then the request should fail with status 401

    Examples:
      | tokenType |
      | missing   |
      | empty     |
      | invalid   |

  # -------------------------------------------------
  # Defensive – Invalid Booking ID (Non-success)
  # -------------------------------------------------

  @patch-booking-negative @bookingid
  Scenario Outline: Patch booking with invalid booking ID should not succeed
    Given I am authenticated with a valid token
    When I patch the booking with ID "<bookingId>" using partial details
    Then the request should fail with status 400

    Examples:
      | bookingId |
      | 0         |
      | -1        |
      | 9999      |

  # -------------------------------------------------
  # Contract – Response Schema Validation
  # -------------------------------------------------

  @patch-booking-contract @contract
  Scenario: Patch booking response matches booking contract schema
    Given I am authenticated with a valid token
    When I patch the booking with partial details
    Then the patch booking response should match the booking schema