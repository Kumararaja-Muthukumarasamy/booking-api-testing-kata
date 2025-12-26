@booking
Feature: Create Booking
  Creating a booking allows customers to reserve a room

  @positive-booking
  Scenario: Create a booking with valid details
    Given the booking service is available
    When I create booking with valid details
    Then the booking should be created successfully
