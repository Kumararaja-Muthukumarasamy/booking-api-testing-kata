@e2e
Feature: End-to-End Booking Flow
  This feature validates the complete lifecycle of a booking
  including creation, retrieval, update, and deletion.

  Two independent validations are covered:
  1. Functional execution of the booking lifecycle (status-based)
  2. Data-level contract consistency across APIs (expected to fail)

  # -------------------------------------------------
  # Functional – End-to-End Execution Flow
  # -------------------------------------------------
  # Validates that the booking lifecycle executes successfully
  # using HTTP status codes only.
  # -------------------------------------------------

  @e2e @functional
  Scenario: Successfully execute booking lifecycle
    Given a new booking is created
    When the booking is retrieved by id and verified successfully
    And the booking is updated with new data
    And the booking is retrieved again and confirmed that updates are reflected
    Then the booking is deleted successfully

  # -------------------------------------------------
  # Data Contract – End-to-End Consistency Validation
  # -------------------------------------------------
  # Validates data consistency across APIs using strict expectations.
  # This scenario is expected to fail due to known API inconsistencies:
  # - CREATE returns 201 instead of 200
  # - GET response structure differs from create/update payload
  # - UPDATE does not return the updated entity
  # -------------------------------------------------

  @e2e @data-contract @known-issue
  Scenario: Booking lifecycle should preserve data consistency across APIs
    Given a booking is created as per data contract
    When the booking is retrieved by id as per data contract
    And the booking is updated as per data contract
    And the booking is retrieved again as per data contract
    Then the booking is deleted successfully
