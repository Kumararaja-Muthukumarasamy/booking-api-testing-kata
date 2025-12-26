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
      | field     | errorMessage                       |
      | roomid    | must be greater than or equal to 1 |
      | firstname | Firstname should not be blank      |
      | lastname  | Lastname should not be blank       |
      | checkin   | must not be null                   |
      | checkout  | must not be null                   |
      | email     | Failed to create booking           |
      | phone     | Failed to create booking           |

  @booking-negative @duplicate
  Scenario: Create booking with duplicate room id
    And a booking already exists for a room
    When I create another booking for the same room
    Then the booking should fail with status 409
    And the error message should be "Failed to create booking"

  @booking-negative @boundary-invalid
  Scenario Outline: Field-level boundary validation
    When I create a booking with invalid "<field>"
    Then the booking should fail with status <statusCode>
    And the error messages should be "<errorMessage>"
    Examples:
      | field                   | statusCode | errorMessage                        |
      | roomid_negative         | 400        | must be greater than or equal to 1  |
      | firstname_too_short     | 400        | size must be between 3 and 18       |
      | firstname_too_long      | 400        | size must be between 3 and 18       |
      | lastname_too_short      | 400        | size must be between 3 and 30       |
      | lastname_too_long       | 400        | size must be between 3 and 30       |
      | invalid_date_format     | 400        | Failed to create booking            |
      | same_checkin_checkout   | 409        | Failed to create booking            |
      | checkout_before_checkin | 409        | Failed to create booking            |
      | email_without_at        | 400        | must be a well-formed email address |
      | phone_lessthan_11       | 400        | size must be between 11 and 21      |
      | phone_greaterthan_21    | 400        | size must be between 11 and 21      |

  @booking-positive @boundary-valid
  Scenario Outline: Accept valid boundary values
    When I create a booking with valid "<field>"
    Then the booking should be created successfully
    Examples:
      | field              |
      | firstname_length_3 |
      | firstname_length_18|
      | lastname_length_3 |
      | lastname_length_30 |
      | phone_length_11    |
      | phone_length_21    |