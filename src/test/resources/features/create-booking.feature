@booking
Feature: Create Booking
  This feature allows consumers to create a new booking
  using valid booking details as defined in the API contract.

  Background:
    Given the booking service is available

  # -------------------------------------------------
  # Functional – Positive Scenario
  # -------------------------------------------------

  @booking-positive
  Scenario: Create booking with all mandatory fields valid
    When I create a booking with all required fields
    Then the booking should be created successfully

  # -------------------------------------------------
  # Functional – Duplicate Booking Validation
  # -------------------------------------------------

  @booking-negative @duplicate
  Scenario: Create booking with duplicate room id
    And a booking exists for the room
    When I create another booking for the same room
    Then the booking should fail with a client validation error
    And the error message should be "Failed to create booking"

  # -------------------------------------------------
  # Contract / Schema Validation
  # -------------------------------------------------

  @booking-contract
  Scenario: Create booking request matches the contract schema
    When I create a booking with all required fields
    Then the create booking request should match the create-booking request schema

  @booking-contract
  Scenario: Create booking response matches the contract schema
    When I create a booking with all required fields
    Then the create booking response should match the create-booking response schema

  # -------------------------------------------------
  # Functional – Mandatory Field Validation
  # -------------------------------------------------

  @booking-negative @mandatory
  Scenario Outline: Create booking with missing mandatory field
    When I create a booking with missing "<field>"
    Then the booking should fail with status 400

    Examples:
      | field        |
      | roomid       |
      | firstname    |
      | lastname     |
      | depositpaid  |
      | bookingdates |
      | email        |
      | phone        |

  # -------------------------------------------------
  # Field-level Boundary Validation
  # -------------------------------------------------

  @booking-negative @boundary-invalid @roomid
  Scenario Outline: RoomId validation – invalid values
    When I create a booking with invalid "<field>"
    Then the booking should fail with status 400

    Examples:
      | field           |
      | roomid_negative |
      | roomid_zero     |
      | roomid_string   |
      | roomid_boolean  |
      | roomid_decimal  |

  @booking-negative @boundary-invalid @firstname
  Scenario Outline: Firstname validation – invalid values
    When I create a booking with invalid "<field>"
    Then the booking should fail with status 400
    And the error messages should contain "<errorMessage>"

    Examples:
      | field               | errorMessage                  |
      | firstname_too_short | size must be between 3 and 18 |
      | firstname_too_long  | size must be between 3 and 18 |
      | firstname_blank     | size must be between 3 and 18 |

  @booking-negative @boundary-invalid @lastname
  Scenario Outline: Lastname validation – invalid values
    When I create a booking with invalid "<field>"
    Then the booking should fail with status 400
    And the error messages should contain "<errorMessage>"

    Examples:
      | field              | errorMessage                  |
      | lastname_too_short | size must be between 3 and 18 |
      | lastname_too_long  | size must be between 3 and 18 |
      | lastname_blank     | size must be between 3 and 18 |

  @booking-negative @boundary-invalid @dates
  Scenario Outline: Booking dates validation – invalid combinations
    When I create a booking with invalid "<field>"
    Then the booking should fail with a client validation error
    And the error messages should contain "Failed to create booking"

    Examples:
      | field                   |
      | invalid_date_format     |
      | checkout_before_checkin |
      | same_day_booking        |

  @booking-negative @boundary-invalid @email
  Scenario Outline: Email validation – invalid values
    When I create a booking with invalid "<field>"
    Then the booking should fail with status 400
    And the error messages should contain "<errorMessage>"

    Examples:
      | field                  | errorMessage                        |
      | email_missing_at       | must be a well-formed email address |
      | email_missing_domain   | must be a well-formed email address |
      | email_missing_username | must be a well-formed email address |

  @booking-negative @boundary-invalid @phone
  Scenario Outline: Phone validation – invalid values
    When I create a booking with invalid "<field>"
    Then the booking should fail with status 400
    And the error messages should contain "<errorMessage>"

    Examples:
      | field                | errorMessage                   |
      | phone_lessthan_11    | size must be between 11 and 21 |
      | phone_greaterthan_21 | size must be between 11 and 21 |

  @booking-positive @boundary-valid
  Scenario Outline: Accept valid boundary values
    When I create a booking with valid "<field>"
    Then the booking should be created successfully

    Examples:
      | field               |
      | firstname_length_3  |
      | firstname_length_18 |
      | lastname_length_3   |
      | lastname_length_18  |
      | phone_length_11     |
      | phone_length_21     |