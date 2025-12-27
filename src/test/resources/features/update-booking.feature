@update-booking
Feature: Update Booking
  Update existing booking details using booking ID

  Background:
    Given the booking service is available
    And a valid booking exists
    And I am authenticated with valid token

  @update-booking-positive
  Scenario: Update booking with valid token and valid booking ID
    When I update the booking with valid details
    Then the booking should be updated successfully
    And retrieving the booking should reflect updated details

  @update-booking-negative @auth
  Scenario Outline: Update booking with invalid token
    Given I use a "<tokenType>" token
    When I update the booking
    Then the request should fail with status 401

    Examples:
      | tokenType |
      | missing   |
      | empty     |
      | invalid   |

  @update-booking-negative @bookingid
  Scenario Outline: Update booking with invalid booking ID
    Given I am authenticated
    When I update booking with ID "<bookingId>"
    Then the request should fail with status 404

    Examples:
      | bookingId |
      | -1        |
      | 0         |
      | 999999    |