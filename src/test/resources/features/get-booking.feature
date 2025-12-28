@get-booking
Feature: Get Booking by ID
  Retrieve booking details using booking ID with authentication

  Background:
    Given the booking service is available

  @get-booking-positive @contract
  Scenario: Retrieve booking with valid token and valid booking ID
    Given a booking exists
    And I am authenticated with valid token for get booking
    When I retrieve the booking by ID
    Then the booking details should be returned successfully
    And the get booking response should match the schema

  @get-booking-negative @auth @contract
  Scenario Outline: Unauthorized or forbidden access to booking
    Given a booking exists
    And I use a "<tokenType>" token
    When I retrieve the booking by ID
    Then the request should fail with status <statusCode> and error message "<errorMessage>"

    Examples:
      | tokenType | statusCode | errorMessage            |
      | missing   | 401        | Authentication required |
      | empty     | 401        | Authentication required |
      | invalid   | 401        | Authentication required |

  @get-booking-negative @bookingid
  Scenario Outline: Retrieve booking with invalid booking ID
    And I am authenticated with valid token for get booking
    When I retrieve booking with ID "<bookingId>"
    Then the request should fail with status <statusCode> and error message "<errorMessage>"

    Examples:
      | bookingId      | statusCode | errorMessage                 |
      | negative value | 404        | Not Found                    |
      | zero           | 404        | Failed to fetch booking: 404 |
      | invalid        | 404        | Failed to fetch booking: 404 |