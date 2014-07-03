Feature: Favorites Repository

Scenario: Empty Favorites Repository
Given empty Favorites Repository
When there is nothing added to the repository
Then favorite book is null

Scenario: Going Postal added to Favorites Repository
Given empty Favorites Repository
When Going Postal is added to Favorites Repository
Then favorite book is Going Postal