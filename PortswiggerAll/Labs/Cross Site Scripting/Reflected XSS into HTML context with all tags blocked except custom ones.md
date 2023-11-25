This lab blocks all HTML tags except custom ones.

To solve the lab, perform a cross-site scripting attack that injects a custom tag and automatically alerts document.cookie.

	Go to the exploit server and paste the following code, replacing YOUR-LAB-ID with your lab ID:

	<script>
	location = 'https://YOUR-LAB-ID.web-security-academy.net/?search=%3Cxss+id%3Dx+onfocus%3Dalert%28document.cookie%29%20tabindex=1%3E#x';
	</script>
	Click "Store" and "Deliver exploit to victim".
	This injection creates a custom tag with the ID x, which contains an onfocus event handler that triggers the alert function. The hash at the end of the URL focuses on this element as soon as the page is loaded, causing the alert payload to be called.


In the provided script:

	html
	location = 'https://0a8b00380479d219818812b1006d004e.web-security-academy.net/?search=%3Cxss+id%3Dx+onfocus%3Dalert%28document.cookie%29%20tabindex=1%3E#x';
	Save to grepper
	The %3E#x at the end of the URL is URL-encoded and represents specific characters. Let's break it down:

	%3E: This is the URL-encoded representation of the greater-than sign (>). When decoded, it becomes >.

	#x: This is a fragment identifier in a URL. The fragment identifier is the part of a URL that follows the "#" symbol. It is often used to refer to a specific section within a webpage.

	Putting it together:

	%3E#x means > followed by the fragment identifier #x.
	So, the complete URL after decoding will look like this:

	html
	https://0a8b00380479d219818812b1006d004e.web-security-academy.net/?search=<xss id=x onfocus=alert(document.cookie) tabindex=1>#x
	Save to grepper
	In the context of a URL, the #x fragment identifier doesn't affect the server-side processing of the request; instead, it might be used by client-side scripts on the webpage to scroll to a specific section identified by the element with the id of 'x'. The main payload for the potential Cross-Site Scripting (XSS) vulnerability is in the search parameter.