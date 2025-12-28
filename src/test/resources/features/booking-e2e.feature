@e2e
Feature: End-to-End Booking Lifecycle

  Scenario: Create, retrieve, update, and delete a booking
    Given a new booking is created
    When the booking is retrieved by id
    And the booking is updated
    And the booking is retrieved again
    Then the booking is deleted successfully