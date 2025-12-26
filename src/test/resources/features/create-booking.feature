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
  Scenario Outline: Create booking with missing mandatory field
    When I create a booking with missing "<field>"
    Then the booking should fail with status 400
    And the error messages should be "<errorMessage>"
    Examples:
      | field       | errorMessage                       |
      | roomid      | must be greater than or equal to 1 |
      | firstname   | Firstname should not be blank      |
      | lastname    | Lastname should not be blank       |
      | checkin     | must not be null                   |
      | checkout    | must not be null                   |
      | email       | Failed to create booking           |
      | phone       | Failed to create booking           |

  @booking-negative @duplicate
  Scenario: Create booking with duplicate room id
    And a booking already exists for a room
    When I create another booking for the same room
    Then the booking should fail with status 409
    And the error message should be "Failed to create booking"