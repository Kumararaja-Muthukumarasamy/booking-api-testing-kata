@booking
Feature: Create Booking
  Creating a booking allows customers to reserve a room

  Background:
    Given the booking service is available

  @positive-booking
  Scenario: Create a booking with valid details
    When I create booking with valid details
    Then the booking should be created successfully

  @booking-negative @mandatory
  Scenario Outline: Create booking with misGiven the booking service is available
    When I create a booking with missing "<field>"
    Then the booking should fail with status 400
    And the error message should be "<errorMessage>"
    Examples:
      | field       | errorMessage                       |
      | roomid      | must be greater than or equal to 1 |
      | firstname   | Firstname should not be blank      |
      | lastname    | Lastname should not be blank       |
      | checkin     | must not be null                   |
      | checkout    | must not be null                   |
      | email       | Failed to create booking           |
      | phone       | Failed to create booking           |
