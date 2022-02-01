# FrontEnd_Automation_Challenge_2021
This project contains the code for the Front End Automation Challenge 2021.

## Challenge Instructions
Create a new automation project where you’ll need to automate the following given scenarios for the sample page (https://www.saucedemo.com/) provided:

1. Login with a valid user.
Expected: Validate the user navigates to the products page when logged in.

2. Login with an invalid user.
Expected: Validate error message is displayed.

3. Logout from the home page.
Expected: Validate the user navigates to the login page.

4. Sort products by Price (low to high).
Expected: Validate the products have been sorted by price correctly

5. Add multiple items to the shopping cart.
Expected: Validate all the items that have been added to the shopping cart.

6. Add the specific product ‘Sauce Labs Onesie’ to the shopping cart.
Expected: Validate the correct product was added to the cart.

7. Complete a purchase.
Expected: Validate the user navigates to the order confirmation page.

## Instructions to Run
[testng-parameters.xml] (/testng-parameters.xml) -> This file contains all the tests belonging the Challenge, for Chrome, Firefox and Edge browsers.
[testngtests.xml] (/testngtests.xml) -> This file contains the tests that are currently running in the CI/CD Azure Devops Pipeline
