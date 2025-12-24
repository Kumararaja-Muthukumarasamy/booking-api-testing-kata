@health
Feature: Booking service availability

  Scenario: Verify booking service health status
    When I check the booking service health
    Then the booking service should be up and running