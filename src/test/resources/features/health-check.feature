@health
Feature: Booking Service Health Check
  This feature verifies whether the booking service is up and running

  Scenario: Verify booking service health status
    Given the booking service configuration is available
    When I check the booking service health
    Then the booking service should be up and running