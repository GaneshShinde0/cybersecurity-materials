User ID controlled by request parameter
APPRENTICE

LAB
Not solved
This lab has a horizontal privilege escalation vulnerability on the user account page.

To solve the lab, obtain the API key for the user carlos and submit it as the solution.

You can log in to your own account using the following credentials: wiener:peter

ACCESS THE LAB
 Solution
Log in using the supplied credentials and go to your account page.
Note that the URL contains your username in the "id" parameter.
Send the request to Burp Repeater.
Change the "id" parameter to carlos.
Retrieve and submit the API key for carlos.
