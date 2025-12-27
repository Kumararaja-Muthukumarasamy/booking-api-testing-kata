@get-booking
Feature: Get Booking by ID
  Retrieve booking details using booking ID with authentication

  Background:
    Given the booking service for GetBookingByID is available

  @get-booking-positive
  Scenario: Retrieve booking with valid token and valid booking ID
    Given a booking exists
    And I am authenticated
    When I retrieve the booking by ID
    Then the booking details should be returned successfully

  @get-booking-negative @auth
  Scenario Outline: Unauthorized or forbidden access to booking
    Given a booking exists
    And I use a "<tokenType>" token
    When I retrieve the booking by ID
    Then the request should fail with status <statusCode> and error message "<errorMessage>"

    Examples:
      | tokenType | statusCode | errorMessage                 |
      | missing   | 401        | Authentication required      |
      | empty     | 403        | Failed to fetch booking: 403 |
      | invalid   | 403        | Failed to fetch booking: 403 |

  @get-booking-negative @bookingid
  Scenario Outline: Retrieve booking with invalid booking ID
    Given I am authenticated
    When I retrieve booking with ID "<bookingId>"
    Then the request should fail with status <statusCode> and error message "<errorMessage>"

    Examples:
      | bookingId       | statusCode | errorMessage                   |
      | negative value  | 404        | Not Found                      |
      | zero            | 404        | Failed to fetch booking: 404   |
      | invalid         | 404        | Failed to fetch booking: 404   |




