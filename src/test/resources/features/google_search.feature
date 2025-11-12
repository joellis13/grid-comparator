Feature: Google Search
  As a user
  I want to search on Google
  So that I can find information

  Scenario: Basic Google search
    Given I am on the Google homepage
    When I search for "test automation"
    Then I should see the captcha challenge