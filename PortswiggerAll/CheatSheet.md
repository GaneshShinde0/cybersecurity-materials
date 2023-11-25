## Lab: DOM XSS in jQuery anchor href attribute sink using location.search source
This lab contains a DOM-based cross-site scripting vulnerability in the submit feedback page. It uses the jQuery library's $ selector function to find an anchor element, and changes its href attribute using data from location.search.

To solve this lab, make the "back" link alert document.cookie

Solution:

	On the Submit feedback page, change the query parameter returnPath to / followed by a random alphanumeric string.
	Right-click and inspect the element, and observe that your random string has been placed inside an a href attribute.
	Change returnPath to:

	javascript:alert(document.cookie)
	Hit enter and click "back".

## Lab: DOM XSS in jQuery selector sink using a hashchange event
This lab contains a DOM-based cross-site scripting vulnerability on the home page. It uses jQuery's $() selector function to auto-scroll to a given post, whose title is passed via the location.hash property.

To solve the lab, deliver an exploit to the victim that calls the print() function in their browser.

Solution:

	Notice the vulnerable code on the home page using Burp or the browser's DevTools.
	From the lab banner, open the exploit server.
	In the Body section, add the following malicious iframe:

	<iframe src="https://YOUR-LAB-ID.web-security-academy.net/#" onload="this.src+='<img src=x onerror=print()>'"></iframe>
	Store the exploit, then click View exploit to confirm that the print() function is called.
	Go back to the exploit server and click Deliver to victim to solve the lab.