This lab makes an assumption about the privilege level of the user based on the HTTP Host header.

To solve the lab, access the admin panel and delete the user carlos.

	Send the GET / request that received a 200 response to Burp Repeater. Notice that you can change the Host header to an arbitrary value and still successfully access the home page.
	Browse to /robots.txt and observe that there is an admin panel at /admin.
	Try and browse to /admin. You do not have access, but notice the error message, which reveals that the panel can be accessed by local users.
	Send the GET /admin request to Burp Repeater.
	In Burp Repeater, change the Host header to localhost and send the request. Observe that you have now successfully accessed the admin panel, which provides the option to delete different users.
	Change the request line to GET /admin/delete?username=carlos and send the request to delete carlos to solve the lab.