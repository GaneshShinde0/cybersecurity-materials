# https://bughacking.com/dvwa-ultimate-guide-first-steps-and-walkthrough/#Insecure_CAPTCHA

# SQL Blind Injection

	 1' AND sleep 
You might have noticed that this request took a while to execute. We can use Burp Suite intruder to make it even more obvious. Let’s submit and intercept the request with the sleep function. Then we can send it to the intruder, add a list of payloads that consists of numbers 1-10, add the position for inserting value to sleep() function, and run it.

Attacktpe: Sniper

	GET /vulnerabilities/sqli_blind/?id=1%27+AND+sleep%28__%29%23&Submit=Submit HTTP/1.1

We can observe that the response time increases each time we use a higher number payload.

### Medium
Just like with the previously explained DVWA SQL Injection vulnerability, the same is with the blind SQLi – one of the <select> element options can be edited with a value that consists of a blind SQLi payload – 1 AND sleep(5)#.

Even though this time this is a POST request instead of GET, we can see that injection was effective as response times are growing when increasing the payload number.

### High

With the high level, this becomes a little bit trickier, as the entered value is transferred into a cookie and then the request is made.

Attacktype: Sniper

Cookie: id=1%27+AND+sleep%28%29%23; PHPSESSIONID=somehing; security=high

But as we are able to intercept the request, we are able to modify it. One thing to consider for high blind SQL injection is that there is a random amount of sleep added. If we’ve used small values for a sleep function, we might not validate the blind SQLi.:

// Might sleep a random amount

if(rand(0,5)==3)
{
	sleep(rand(2,4));
}

## Weak Session IDs
Another vulnerability of the DVWA is Weak Session IDs. If the IDs generation is weak enough, a malicious actor does not even need complex vulnerabilt chaingin in order to gain access to the system. In case the ID creation is baased on a guessable pattern, the only thing one should do is reverse engineer it.

### Low
- devtools> storage> see that first time the session ID is equal to 1 - dvwaSession value is set to 1.
- After clicking on Generate button for the second time, we can see that ID gets the value 2,  From this we can state that the ID generation is incremental and it is easy to guess what session ID will be generated the next time.


### Medium

This time we can see that the value of dvwaSession differs each time. Even though it is higher each time, we cannot state by what number it does increment.

DVWASession Value is Equal to Current Data in the Medium Level
But if we translated this value from Epoch to human date, we will realize that this is the current date as a session ID value.

### High

With the high level, we can see that the value looks complex enough and there is no visual similarity between current and new value. Although, if we’ve guessed that this value looks like an MD5 hash, and tried to reverse engineer it, we would realize this is a hash of number 1.

High DVWA Weak Session IDs: ID is a MD5 Hash of a Number
### Impossible

For the secure, impossible, level session ID generation has no template as a hash of a random value plus word “Impossible” is calculated.

## XSS (DOM)
Cross-Site Scripting (XSS) is another injection attack. With this attack, malicious scripts are injected into the website and executed as legitimate ones. There are a few types of XSS, one of them is DOM-based XSS. This works differently than reflected or stored XSS, as DOM-based XSS happens because of the modification of a DOM environment by the client-side script.

### Low
There is a select with different values as the language options. As there is no validation we can append a script in this manner

	?default=Englishalert('DOM+XSS')

### Medium
Even though direct script evocation is not allowed in the medium DVWA XSS DOM example, there is a way to bypass it.

	/vulnerabilities/xss_d/?default=English>/option></select><img src='x' onerror='alert(1)'>

As a result script will fire:

### High

While the context of the vulnerability suggests that the exploitation will become harder, actually exploit DOM XSS on high level is as easy as on the low level. By adding # to one of the <select> values, you can execute code on the client’s side without worrying about the whitelist values.

### Impossible

As the URL content is encoded, it is not possible to exploit the vulnerability on this level.

## XSS(Reflected)

Reflected is another type of the XSS. This injection is not persistent, one of the examples of how this can be exploited is when the user is tricked to click on a malicios link

### Low
In Input Box.

<script>alert('Reflected')</script>

### Medium

<ScriPt>alert(‘Reflected XSS’)</script>

### High
<img src/onerror=alert('XSS+Reflected')>

### Impossible
A function that escapes any “illegal” characters is used.

## XSS Stored

Stored XSS is permanently stored on the website and malicious scripts can be executed every time user visits the page. For example, if a website is vulnerable and a script is injected into comments form, every users’ browser will execute it on page visit.

### Low

Stored XSS does not differ from the reflected XSS by its nature. Payloads used in the previous section would work for stored XSS.

But the task for the stored XSS DVWA vulnerability is to make a redirect to an external page. We can do this in different ways, one of them is to add an <img> element that, on error, opens a page:

	<img src="https://nonexisting.url" 
	onerror = window.open("https://www.owasp.org","xss",height=500,width=500');>

As the text field has a max size of 50 characters, we have to extend it, otherwise our payload won’t fit the field.

### Medium
But the thing is that not all fields have the same validation. The same payload will work for the Name field. Before entering the value, make sure you increased the maxlength of the field (refer to the previous section).

= window.open("http://...")

If you’ve used payload from the low security level, you will get an error:

Data too long for column 'name' at row 1.
This is related to the database column length and can be solved by using a shorter payload:

<img src = "https://nonexisting.url" onerror=window.open("https://www.owasp.org");>

### High

The high level removes symbols that the word script has. Means that our payload which we've used for low and medium levels won't be efffective 




Above will wrk

This will not load a page, but the fact is that another page will be opened. And this proves the existence of the stored XSS>

Payload from Portswigger XSS Cheat Sheet can be used for this purpose.

https://portswigger.net/web-security/cross-site-scripting/cheat-sheet

### Impossible 
Implemented htmlspecialchars() function filters all dangerous characters.

## CSP Bypass Vulnerability
CSP defines what resources can be used on the page. If the policy is set and it disallows external resources, then no external script could be loaded and executed.

### Low
For the DVWA, checking if the CSP is implemented is easy. Actually, this is the same for any case – the server responds with a Content-Security-Policy header that states what external resources are allowed.

Content-Security-Policy: script-src ‘self’ https://pastebin.com hastebin.com example.com code.jquery.com https://ssl.google-analytics.com ;

And in this case, self, pastebin, hastebin, example, code.jquery, and ssl.google-analytics are allowed.

Even though Pastebin is one of the domains allowed by the policy, it is not possible to use Pastebin for low DVWA CSP level because of the changes in Pastebin website. Even though we are able to load content from the page, it will be text, not script format.

As the ‘self’ is allowed in the CSP, we can host our own script from the DVWA server. Create a new file in the /var/www/html directory of your DVWA installation and put a single line with an alert: alert(‘CSP is working’).

Creating a JS Script for low CSP Exploitation
After this, try to load the script by submitting URL with the script – http://YOUR-INSTALLATION-IP/low-csp.js that points to the location of a file that we just uploaded.

nounce=" src="/hackable/uploads/low-csp.js"></script>

After this, script should be executed.

### High 
On this level, there is a Solve the sum button that calls a script that “calculates” the value of the hardcoded numbers and returns the sum. A GET request is sent that has a callback solveSum that returns us the result: /vulnerabilities/csp/source/jsonp.php?callback=solveSum.

What we can do is to intercept the request and instead of the solveSum, inject our own value. Let’s say we want to inject an alert:

	GET /vulnerabilities/csp/source/jsonp.php?callback=alert('We+Can+Execute+Any+Script+We+Want') HTTP/1.1

And as a result, the code will be executed:

### Impossible
Just like in the high level, JSONP is used for calling a callback function. However, this tame the function is hardcoded and there is no possibility to add custom code.

## JavaScript
While JavaScript scripts can’t be called as the vulnerabilities itself, it can definitely become an attack vector. And also, in some cases JavaScript script can give an attacker useful information that might lead to further system exploitation. DVWA JavaScript vulnerability focuses on showing what can be the potential consequences if a script contains sensitive information.

### Low
There is an easy task (at least it looks easy) - Enter word "success" and hit Submit. However, the problem you will face is the token. Token will be invalid.

	However, the purpose of the DVWA JavaScript vulnerability is to reverse engineer JavaScript code to get the needed information. Such information will help to exploit vulnerability. First step would be to locate JavaScript code. This can be done by inspecting page HTML> You will that there is a JS code included inside the <script> element. One function is really interesting for us. It consists of the logic for creating token, which we need.

	function generate_token() {
		var phrase = document.getElementById("phrase").value;
		document.getElementById("token").value = md5(rot13(phrase));
	}

Now we know that we can construct token by using ROT13 function and generating a MD5 hash.  If you are on Kali Linux, this can be done easily with the terminal only:

	echo -n 'success' | tr 'A-Aa-z' 'N-ZA-Mn-za-m' | md5sum

	As a result, token will be generated - 38581812b435834ebf84ebcc2c6424d6

Let's try to send a request, this time with the correct token, intercept request with Burp Suite and edit the token.

token = genrated token & phrase = success & send = Submit.

And BINGO. We managed to get the phrase ‘Well done!’ instead of the ‘Invalid token’.

## Medium

This time the script is minimized and it will be harder for us to read it. Try to find the script and open it (The script is called medium.js). Alternatively, after you made a request, you can go to the Debugger tab of the browser, find the script, and use the Pretty print source function.

Now we can analyze the code. We can see that setTimeout() calls a function that passes ‘XX’ to the do_elsesomething() function. The do_elsesomething() function sets token to the value of received value plus entered phrase plus ‘XX’ that is passed to the do_something() function. If this sounds complicated, take a look at the code again. I really encourage you to try to execute the function by yourself, either in the browser or IDE or even in your mind, in order to get the correct token.

Just like in the previous step, we can use Burp Suite to send the request with a found-out token. And … BINGO. The wanted Well done! is here instead of the devastating You got the phrase wrong or Invalid token phrases.

### High

Identically as with the case of medium DVWA JavaScript, we have a script , this time called high.js, After formatting it, we might see that it looks differentl.

That's because JavaScript code is obfuscated. There are a few hints on the page that tells what tools were used for obfuscating. There is also a website that will hep to deobfuscate the code.

That’s because JavaScript code is obfuscated. There are a few hints on the page that tells what tools were used for obfuscating. There is also a website that will help to deobfuscate the code – https://deobfuscatejavascript.com. We can enter the code from JavaScript file and see what we will get.

Even though the code transformed, it's still pretty hard to understand it. However, even most of the code is not beneficial for us, last lines of the code is what we neeed;

After deobfuscating we get the following code.

	(function() {
		'use strict';
		var ERROR = 'input is invalid type';
		var WINDOW = typeof window === 'object';
	
		.........................
		.........................
	
	function do_something(e) {
		for (var t = "", n = e.length - 1; n >= 0; n--) t += e[n];
		return t
	}
	function token_part_3(t, y = "ZZ") {
		document.getElementById("token").value = sha256(document.getElementById("token").value + y)
	}
	function token_part_2(e = "YY") {
		document.getElementById("token").value = sha256(e + document.getElementById("token").value)
	}
	function token_part_1(a, b) {
		document.getElementById("token").value = do_something(document.getElementById("phrase").value)
	}
	document.getElementById("phrase").value = "";
	setTimeout(function() {
		token_part_2("XX")
	}, 300);
	document.getElementById("send").addEventListener("click", token_part_3);
	token_part_1("ABCD", 44);

It contains the logic for generating token. We can reverse engineer the sequence and we can see that there are fur function in total

token_part_2
token_part_3
token_part_1
do_something

There are two ways how you can manage this task – by trying to run the code (with a small changes derived from the code investigation) and see what happens, or to analyze everything line by line, try to understand the logic and solve the task by yourself with a help of some tools (only where this is needed). For this example, we will choose the second approach.

For running the JavaScript code you can use any code editor you want. You might even use the browser. Just choose the method that you like the most. I used Atom editor for this example. If you might want to try it, there is a video how you can make JavaScript code run in the Atom editor.

What we should do next is to get the token from the page as this value is needed for the defined JS functions. After inspecting the page we can see that the token is

ecc76c19c9f3c5108773d6c3a18a6c25c9bf1131c4e250b71213274e3b2b5d08

 This token is the same for every request.

Now let's get back to the code chunk we have. So far we know that there are four functions that have to be called in a specific order. Initially it might look that the token_part_2 will be called before token_part_3. However, it is inside the setTimeout function, that will delay the call for 300 ms, An educated guess might be that token_part_2 invocation will be put on sleep and in the meanwhile, token_part_3 will execute. After that token_part_2 will be run, and lastly, token_part_1. We can run a small test and see if this guess is correct:

	setTimeout(function(){
		console.log('Function 2')
	}, 300);
	console.log('Function 3')
	console.log('Function 1')

Ok, so from this example we might guess that the real sequence is token_part_3, token_part_1, and lastly, token_part_2. But then again, this code can say nothing and might not be accurate as only the console.log() was used in this example, while the real code uses sha256 function and logic differs a little bit between functions.

Another indication tha the token_part_3 is the first function that is called, can be seen pretty easily.  If we intercept the request we can see that the the token is different:

Token is different than the initial. Reason for this is that we call the token_part_3 by clicking on the Submit button. This is the code part that proves this:

	document.getElementById("send").addEventListener("click",token_part_3);

	Moving along, logic of the three mentioned functions can be broke down into this:

- token_part_3 - Generate sha256 hash of the string that is constructed from token + "ZZ". And we already know that this value is equal to 28638d855bc00d62b33f9643eab3e43d8335ab2b308039abd8fb8bef86331b14

- token_part_1 - Passes our phrase ("Success") to the do_something function, returned string assigns to the token variable, and what the do_something function does, is ome operations with the string characters.

- token_part_2 - Function generated the hash of "XX"+value of the token.

After the token_part_3 was executed. token_part_1 sets the token to sseccus(product of the do_something). So, the hardly generated previous token value is overwritten.

And lastly, the function token_part_2 constructs a String XXsseccuss and generate a sha256 vale of it. We can do this with a simple command on kali linux: echo -n XXsseccus | sha256sum. The result of it is 7f1bfaaf829f785ba5801d5bf68c1ecaf95ce04545462c8b8f311dfc9014068a. 

Which is our final token. Set the input field value to the token, enter “success” phrase, hit Submit, and done!

## Are there any DVWA alternatives?
There are plenty of similiar applications. I already mentioned that there are more than a dozen OWASP apps. I’ve made a long list with the potential DVWA alternatives, but if you are in a hurry and you are thriving for new security challenges (I know that feeling), here are a few apps you might proceed to:

bWAPP
OWASP Mutillidae II
All of the mentioned applications are great alternatives to DVWA, as they all have OWASP Top Ten vulnerabilities implemented. It is very important to keep practicing the things you’ve learned, as there is no golden bullet for, let’s say, finding SQLi vulnerabilities. The better you know the basics, the higher chance you will be able to identify vulnerabilities in the wild. And solid grounds are very important if you are seeking a bug bounty hunter career or just trying to level up as a security tester.

<script>
  var cookies = document.cookie.split(';');
  var cookieNames = [];

  for (var i = 0; i < cookies.length; i++) {
    var cookie = cookies[i].trim().split('=');
    var cookieName = cookie[0];
    cookieNames.push(cookieName);
  }

  document.write("Cookie Names: " + cookieNames.join(", "));
</script>



<script>
  var currentUrl = document.location.href;
  document.write("Current URL: " + currentUrl);
</script>
