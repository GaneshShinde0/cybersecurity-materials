This lab is vulnerable to password reset poisoning. The user carlos will carelessly click on any links in emails that he receives. To solve the lab, log in to Carlos's account.

You can log in to your own account using the following credentials: wiener:peter. Any emails sent to this account can be read via the email client on the exploit server.

	Go to the login page and notice the "Forgot your password?" functionality. Request a password reset for your own account.
	Go to the exploit server and open the email client. Observe that you have received an email containing a link to reset your password. Notice that the URL contains the query parameter temp-forgot-password-token.
	Click the link and observe that you are prompted to enter a new password. Reset your password to whatever you want.
	In Burp, study the HTTP history. Notice that the POST /forgot-password request is used to trigger the password reset email. This contains the username whose password is being reset as a body parameter. Send this request to Burp Repeater.
	In Burp Repeater, observe that you can change the Host header to an arbitrary value and still successfully trigger a password reset. Go back to the email server and look at the new email that you've received. Notice that the URL in the email contains your arbitrary Host header instead of the usual domain name.
	Back in Burp Repeater, change the Host header to your exploit server's domain name (YOUR-EXPLOIT-SERVER-ID.exploit-server.net) and change the username parameter to carlos. Send the request.
	Go to your exploit server and open the access log. You will see a request for GET /forgot-password with the temp-forgot-password-token parameter containing Carlos's password reset token. Make a note of this token.
	Go to your email client and copy the genuine password reset URL from your first email. Visit this URL in the browser, but replace your reset token with the one you obtained from the access log.
	Change Carlos's password to whatever you want, then log in as carlos to solve the lab.