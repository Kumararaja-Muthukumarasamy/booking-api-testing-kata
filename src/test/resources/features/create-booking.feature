@booking
Feature: Create Booking
  A booking can be created for an available room

  @positive-booking
  Scenario: Create a booking with valid details
    When I create booking with valid details
    Then the booking should be created successfully
