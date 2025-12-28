@delete-booking
Feature: Delete booking

  Background:
    Given the booking service for DeleteBooking is available

  @delete-positive
  Scenario: Delete booking with valid ID and token
    Given a valid booking exists for delete
    And I am authenticated with valid token for delete
    When I delete the booking
    Then the booking should be deleted successfully

  @delete-negative @id
  Scenario Outline: Delete booking with valid token and invalid id
    Given I am authenticated with valid token for delete
    When I delete the booking using id "<id>"
    Then the delete request should fail with status <statusCode> and error message "<errorMessage>"

    Examples:
      | id   | statusCode | errorMessage             |
      | 0    | 500        | Failed to delete booking |
      | -1   | 500        | Failed to delete booking |
      | 9999 | 500        | Failed to delete booking |
      | null | 500        | Failed to delete booking |

  @delete-negative @delete-no-id
  Scenario: Delete booking without ID
    Given I am authenticated with valid token for delete
    When I send delete request without id
    Then the delete request should fail with status 405

  @delete-negative @auth
  Scenario Outline: Delete booking with invalid or missing token
    Given a valid booking exists for delete
    And I use a "<tokenType>" token for delete
    When I delete the booking using token
    Then the delete request should fail with status <statusCode> and error message "<errorMessage>"

    Examples:
      | tokenType | statusCode | errorMessage             |
      | invalid   | 500        | Failed to delete booking |
      | empty     | 500        | Failed to delete booking |
      | missing   | 401        | Authentication required  |