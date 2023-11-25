This lab contains a debug page that discloses sensitive information about the application. To solve the lab, obtain and submit the SECRET_KEY environment variable.

	With Burp running, browse to the home page.
	Go to the "Target" > "Site Map" tab. Right-click on the top-level entry for the lab and select "Engagement tools" > "Find comments". Notice that the home page contains an HTML comment that contains a link called "Debug". This points to /cgi-bin/phpinfo.php.
	In the site map, right-click on the entry for /cgi-bin/phpinfo.php and select "Send to Repeater".
	In Burp Repeater, send the request to retrieve the file. Notice that it reveals various debugging information, including the SECRET_KEY environment variable.
	Go back to the lab, click "Submit solution", and enter the SECRET_KEY to solve the lab.

Or 
1. Launch Home;
2. View Page Source.
3. Look for any information on page or comment.
4. See if you can get anything related to debug.
5. launch debug page.
6. See for SECRET_KEY.