@get-booking
Feature: Retrieve Booking by ID
  This feature allows consumers to retrieve booking details
  using a booking ID and authentication token
  as defined in the API contract.

  Background:
    Given the booking service is available

  # -------------------------------------------------
  # Positive – Successful Retrieval
  # -------------------------------------------------

  @get-booking-positive
  Scenario: Retrieve booking with valid token and valid booking ID
    Given a booking exists
    And I am authenticated with a valid token
    When I retrieve the booking by ID
    Then the booking details should be returned successfully with status 200

  # -------------------------------------------------
  # Contract – Schema Validation
  # -------------------------------------------------

  @get-booking-positive @contract
  Scenario: Retrieve booking response should match schema
    Given a booking exists
    And I am authenticated with a valid token
    When I retrieve the booking by ID
    Then the get booking response should match the booking schema

  # -------------------------------------------------
  # Contract – Unauthorized Access
  # -------------------------------------------------

  @get-booking-negative @auth @contract
  Scenario Outline: Retrieve booking fails due to invalid or missing authentication
    Given a booking exists
    And I use a "<tokenType>" token
    When I retrieve the booking by ID
    Then the request should fail with status 401
    And the error message should be "Unauthorized"

    Examples:
      | tokenType |
      | missing   |
      | empty     |
      | invalid   |

  # -------------------------------------------------
  # Negative – Invalid Booking ID
  # -------------------------------------------------

  @get-booking-negative @invalid-id
  Scenario Outline: Retrieve booking with invalid booking ID should fail with bad request
    Given I am authenticated with a valid token
    When I retrieve booking with ID "<bookingId>"
    Then the request should fail with status 400

    Examples:
      | bookingId |
      | 0         |
      | -1        |
      | abc       |

  # -------------------------------------------------
  # Negative – Not Found (404)
  # -------------------------------------------------

  @get-booking-negative @not-found
  Scenario Outline: Retrieve booking with non-existing booking ID should fail with not found
    Given I am authenticated with a valid token
    When I retrieve booking with ID "<bookingId>"
    Then the request should fail with status 404

    Examples:
      | bookingId |
      | 9999      |