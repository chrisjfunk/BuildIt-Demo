# BuildIt-Demo Web Crawler

## How to build, test, and run
Only required dependency is Java 1.8 with JAVA_HOME set so that maven can locate it.

To Build the project including testing from the checkout directory run
**mvnw install**.  This will run all unit tests and an integration test against buildit.wiprodigital.com

when ready to run the project use the command

**mvnw spring-boot:run -Dspring-boot.run.arguments=--url=<https://buildit.wiprodigital.com/>**,--wait=300,--threads=5**

### Replace the values above with your  desired parameters
**--url:**   the starting url you want to crawl.  NOTE you must enter this url in its common form i.e with www. if it usaully displays that way.  Put your url in a browser first the copy and paste the url after the browser follows initial redirects.  See time constraint notes below.

**--wait:** the max time in seconds you want to wait.  defaults to 300 or 5 minutes if you remove this parameter

**--threads:** the number of threads you want to run in parallel for the crawling.  note:  setting the thread count too high might cause connection timeout errors on http fetches so dont set too high

Running this app will crawl your site of choice for up N number of seconds with N number of threads.   the output will be a siteMap.json file in the same directory where it is run with the pages crawled.
If while running you see read timed out errors, reduce the number of threads as it is overwhelming the http connections on your machine.

## Trade-offs
1. Large sites can have hundreds of thousands of links, so one tradeoff made was
to just put a simple time liit on crawling so it just stops after x number of seconds (default 5 minutes)

2. All exceptions on loading pages (other than 404) are just treated as broken.  Brute force works and is simple but is less efficent for
things like pdf, mov links, etc.  These links are followed as though they were html pages and failure is handled gracefully but it could be smarter.
For example looking at file extension and not following internal *.pdf links.

## Imporovements with more time
1. Handle connection timeout better.   Maybe retry or automatically reduce the number of active threads

2. Handle internal non html links more gracefully.    Right now they are just assumed to be broken links (not parseable as html)
and reported at the end of the app as missing internal links.  In reality they should just either not be attempted to be followed or at least marked as non html pages

3. Scrape java script code blocks and files detecting anyhting that looks like a link an internal link and trying to follow it.
ie a javascript block that says window.location.href=https://www.example.com/...

4. Right now you need to enter the starting url in the final form that does not redirect.  for example wipro.com redirects to www.wipro.com which is a different host.
This causes the app to crawl one page and mark everything as an external link.  Need to enhance to detect initial redirect on first url and
use the redirect targets as the hostname to detect internal links.
