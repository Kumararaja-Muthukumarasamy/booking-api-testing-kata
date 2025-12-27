@delete-booking
Feature: Delete booking

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
    Then the delete request should fail with status <statusCode>

    Examples:
      | id   | statusCode |
      | 0    | 500        |
      | -1   | 500        |
      | 9999 | 500        |
      | null | 500        |

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