This lab contains a DOM-based cross-site scripting vulnerability in the stock checker functionality. It uses the JavaScript document.write function, which writes data out to the page. The document.write function is called with data from location.search which you can control using the website URL. The data is enclosed within a select element.

To solve this lab, perform a cross-site scripting attack that breaks out of the select element and calls the alert function.

On the product pages, notice that the dangerous JavaScript extracts a storeId parameter from the location.search source. It then uses document.write to create a new option in the select element for the stock checker functionality.
Add a storeId query parameter to the URL and enter a random alphanumeric string as its value. Request this modified URL.
In the browser, notice that your random string is now listed as one of the options in the drop-down list.
Right-click and inspect the drop-down list to confirm that the value of your storeId parameter has been placed inside a select element.
Change the URL to include a suitable XSS payload inside the storeId parameter as follows:

product?productId=1&storeId="></select><img%20src=1%20onerror=alert(1)>

