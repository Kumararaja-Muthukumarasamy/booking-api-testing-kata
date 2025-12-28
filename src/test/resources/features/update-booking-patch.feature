@patch-booking
Feature: Patch Booking
  Partially update existing booking details using booking ID

  Background:
    Given the booking service is available
    And a valid booking exists for patch
    And I am authenticated with valid token for patch

  @patch-booking-positive
  Scenario: Patch booking with valid token and valid booking ID
    When I patch the booking with partial details
    Then the booking should be partially updated successfully
    And retrieving the booking should reflect patched details

  @patch-booking-positive @partial
  Scenario Outline: Patch booking with only one field
    When I patch the booking with valid "<field>"
    Then the booking should be partially updated successfully
    And retrieving the booking should reflect patched details

    Examples:
      | field       |
      | firstname   |
      | lastname    |
      | depositpaid |

  @patch-booking-negative @boundary-invalid
  Scenario Outline: Field-level boundary validation on patch
    When I patch the booking with invalid "<field>"
    Then the patch request should fail with status <statusCode> and error message "<errorMessage>"

    Examples:
      | field               | statusCode | errorMessage                  |
      | firstname_too_short | 400        | size must be between 3 and 18 |
      | firstname_too_long  | 400        | size must be between 3 and 18 |
      | lastname_too_short  | 400        | size must be between 3 and 30 |
      | lastname_too_long   | 400        | size must be between 3 and 30 |

  @patch-booking-positive @boundary-valid
  Scenario Outline: Accept valid boundary values on patch
    When I patch the booking with valid "<field>"
    Then the booking should be partially updated successfully

    Examples:
      | field               |
      | firstname_length_3  |
      | firstname_length_18 |
      | lastname_length_3   |
      | lastname_length_30  |

  @patch-booking-negative @auth
  Scenario Outline: Patch booking with invalid or missing token
    Given I use a "<tokenType>" token
    When I patch the booking with valid details using "<tokenType>" token
    Then the patch request should fail with status <statusCode> and error message "<errorMessage>"

    Examples:
      | tokenType | statusCode | errorMessage   |
      | missing   | 401        | Unauthorized   |
      | empty     | 401        | Unauthorized   |
      | invalid   | 401        | Unauthorized   |