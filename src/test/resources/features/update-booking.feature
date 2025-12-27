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

  @update-booking-negative @mandatory
  Scenario Outline: Update booking with missing mandatory field
    When I update the booking with missing "<field>"
    Then the update request should fail with status 400
    And the error messages should be "<errorMessage>"

    Examples:
      | field     | errorMessage                  |
      | firstname | Firstname should not be blank |
      | checkin   | must not be null              |

  @update-booking-negative @boundary-invalid
  Scenario Outline: Field-level boundary validation on update
    When I update the booking with invalid "<field>"
    Then the update request should fail with status <statusCode>
    And the error messages should be "<errorMessage>"

    Examples:
      | field                   | statusCode | errorMessage                        |
      | firstname_too_short     | 400        | size must be between 3 and 18       |
      | firstname_too_long      | 400        | size must be between 3 and 18       |
      | lastname_too_short      | 400        | size must be between 3 and 30       |
      | lastname_too_long       | 400        | size must be between 3 and 30       |
      | invalid_date_format     | 400        | Failed to update booking            |
      | same_checkin_checkout   | 409        | Failed to update booking            |
      | checkout_before_checkin | 409        | Failed to update booking            |
      | email_without_at        | 400        | must be a well-formed email address |
      | phone_lessthan_11       | 400        | size must be between 11 and 21      |
      | phone_greaterthan_21    | 400        | size must be between 11 and 21      |

  @update-booking-positive @boundary-valid
  Scenario Outline: Accept valid boundary values on update
    When I update the booking with valid "<field>"
    Then the booking should be updated successfully

    Examples:
      | field              |
      | firstname_length_3 |
      | firstname_length_18|
      | lastname_length_3  |
      | lastname_length_30 |
      | phone_length_11    |
      | phone_length_21    |

  @update-booking-negative @auth
  Scenario Outline: Update booking with invalid or missing token
    Given I use a "<tokenType>" token
    When I update the booking with valid details
    Then the update request should fail with status <statusCode> and error message "<errorMessage>"

    Examples:
      | tokenType | statusCode | errorMessage             |
      | missing   | 401        | Authentication required  |
      | empty     | 403        | Failed to fetch booking: 403 |
      | invalid   | 403        | Failed to fetch booking: 403 |