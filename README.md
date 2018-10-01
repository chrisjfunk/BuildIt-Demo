# BuildIt-Demo Web Crawler

## How to build, test, and run
Only required dependency is Java 1.8 with JAVA_HOME set so that maven can locate it.

To Build the project including testing from the checkout directory run
**mvnw install**.  This will run all unit tests and an integration test against buildit.wiprodigital.com

when ready to run the project use the command

**mvnw spring-boot:run -Dspring-boot.run.arguments=--url=<https://buildit.wiprodigital.com/>,--wait=300,--threads=5**

### Replace the values above with your  desired parameters
**--url:**   the starting url you want to crawl.  **NOTE you must enter this url in its common form i.e with www if it usually displays that way.**  Put your url in a browser first and then copy and paste the url after the browser follows initial redirects.  See time constraint notes below.

**--wait:** the max time in seconds you want to wait.  Defaults to 300 (5 minutes) if you remove this parameter

**--threads:** the number of threads you want to run in parallel for the crawling.  Defaults to 5  **NOTE setting the thread count too high might cause connection timeout errors on http fetches so dont set too high depending on your bandwidth**

Running this app will crawl your site of choice for up N number of seconds with N number of threads.   the output will be a **siteMap.json** file in the same directory where it is run with the pages crawled.
If while running you see read timed out errors, reduce the number of threads as it is overwhelming the http connections on your machine.

## Trade-offs
1. Large sites can have hundreds of thousands of links, so one tradeoff made was
to just put a simple time liit on crawling so it just stops after x number of seconds (default 5 minutes)

2. All exceptions on loading pages (other than 404) are just treated as broken.  Brute force works and is simple but is less efficent for
things like pdf, mov links, etc.  These links are followed as though they were html pages and failure is handled gracefully but it could be smarter.
For example looking at file extension and not following internal *.pdf links.

## Improvements with more time
1. Handle connection timeout better.   Maybe retry or automatically reduce the number of active threads

2. Handle internal non html links more gracefully.    Right now they are just assumed to be broken links (not parseable as html)
and reported at the end of the app as missing internal links.  In reality they should just either not be attempted to be followed or at least marked as non html pages

3. Scrape java script code blocks and files detecting anyhting that looks like a link an internal link and trying to follow it.
ie a javascript block that says window.location.href=https://www.example.com/...

4. Right now you need to enter the starting url in the final form that does not redirect.  for example wipro.com redirects to www.wipro.com which is a different host.
This causes the app to crawl one page and mark everything as an external link.  Need to enhance to detect initial redirect on first url and
use the redirect targets as the hostname to detect internal links.

## Sample siteMap.json
Running the app creates a Json file in same directory named siteMap.json  Here is a sample output

```json
{
  "host" : "buildit.wiprodigital.com",
  "pages" : [ {
    "inboundLinkCount" : 8,
    "url" : "https://buildit.wiprodigital.com/",
    "externalLinks" : [ {
      "url" : "https://medium.com/buildit",
      "description" : "Medium Monogram Logo Blog"
    }, {
      "url" : "https://medium.com/buildit/",
      "description" : "blog"
    }, {
      "url" : "https://www.instagram.com/buildit_tech",
      "description" : "Instagram Logo"
    }, {
      "url" : "https://twitter.com/buildit_tech",
      "description" : "Twitter Logo"
    }, {
      "url" : "https://www.linkedin.com/company/buildit.",
      "description" : "LinkedIn Logo"
    }, {
      "url" : "https://github.com/buildit",
      "description" : "Github Logo"
    }, {
      "url" : "https://wiprodigital.com/privacy-policy",
      "description" : "Privacy policy"
    } ],
    "mediaLinks" : [ {
      "url" : "https://cdnjs.cloudflare.com/ajax/libs/scrollReveal.js/3.4.0/scrollreveal.min.js",
      "description" : "script"
    }, {
      "url" : "https://cdnjs.cloudflare.com/ajax/libs/gsap/1.20.4/TweenLite.min.js",
      "description" : "script"
    }, {
      "url" : "https://cdnjs.cloudflare.com/ajax/libs/gsap/1.20.4/easing/EasePack.min.js",
      "description" : "script"
    }, {
      "url" : "https://buildit.wiprodigital.com/scripts/bundle.min.js",
      "description" : "script"
    }, {
      "url" : "https://www.googletagmanager.com/gtag/js?id=UA-78138413-2",
      "description" : "script"
    } ],
    "description" : "buildit @ wipro digital",
    "internalLinks" : [ {
      "url" : "https://buildit.wiprodigital.com/about/",
      "description" : "About us"
    }, {
      "url" : "https://buildit.wiprodigital.com/careers/",
      "description" : "Careers"
    }, {
      "url" : "https://buildit.wiprodigital.com/locations/",
      "description" : "Locations"
    }, {
      "url" : "https://buildit.wiprodigital.com/about/",
      "description" : "About us"
    }, {
      "url" : "https://buildit.wiprodigital.com/",
      "description" : "Buildit Logo"
    } ]
  }, {
    "inboundLinkCount" : 4,
    "url" : "https://buildit.wiprodigital.com/careers/",
    "externalLinks" : [ {
      "url" : "https://medium.com/buildit",
      "description" : "Medium Monogram Logo Blog"
    }, {
      "url" : "https://jobs.smartrecruiters.com/ni/WiproDigital/615cb3da-adcd-45a1-ba5e-2410fdaa78bd?trid=2f4a2735-7172-45dd-aca7-63618dca8aff",
      "description" : "Senior Backend Engineer"
    }, {
      "url" : "https://jobs.smartrecruiters.com/ni/WiproDigital/5158dc2a-a6be-4f9a-9f2b-fc2b59cce4cd?trid=2f4a2735-7172-45dd-aca7-63618dca8aff",
      "description" : "Platform Engineer"
    }, {
      "url" : "https://jobs.smartrecruiters.com/ni/WiproDigital/356c70f8-8865-42f8-b4fb-c7cd90bdaa9f?trid=2f4a2735-7172-45dd-aca7-63618dca8aff",
      "description" : "API Engineer"
    }, {
      "url" : "https://jobs.smartrecruiters.com/ni/WiproDigital/231e5892-a5c5-47c6-87d8-41a8817acaa8?trid=2f4a2735-7172-45dd-aca7-63618dca8aff",
      "description" : "DevOps Engineer"
    }, {
      "url" : "https://jobs.smartrecruiters.com/ni/WiproDigital/fb6bd73b-c9a6-4bec-a65d-b87f79de15b3?trid=2f4a2735-7172-45dd-aca7-63618dca8aff",
      "description" : "Front End Engineer"
    }, {
      "url" : "https://jobs.smartrecruiters.com/ni/WiproDigital/260cfd53-7622-4dcb-aeb2-ea2cf686581d?trid=2f4a2735-7172-45dd-aca7-63618dca8aff",
      "description" : "Senior Java Developer"
    }, {
      "url" : "https://jobs.smartrecruiters.com/ni/WiproDigital/f53071b2-0bfe-452d-a054-1c7da8431463?trid=2f4a2735-7172-45dd-aca7-63618dca8aff",
      "description" : "Java Developer"
    }, {
      "url" : "https://jobs.smartrecruiters.com/ni/WiproDigital/c520605f-4fa9-4b08-ab9e-c13def027434?trid=2f4a2735-7172-45dd-aca7-63618dca8aff",
      "description" : "Platform Engineer"
    }, {
      "url" : "https://jobs.smartrecruiters.com/ni/WiproDigital/cba2b346-5037-42e6-9c88-9c80f62ade83?trid=2f4a2735-7172-45dd-aca7-63618dca8aff",
      "description" : "Full Stack Engineer"
    }, {
      "url" : "https://jobs.smartrecruiters.com/ni/WiproDigital/920ec1aa-f789-4850-b17f-22fb36981355?trid=2f4a2735-7172-45dd-aca7-63618dca8aff",
      "description" : "Platform Engineer"
    }, {
      "url" : "https://jobs.smartrecruiters.com/ni/WiproDigital/e8173bfa-e72b-4607-be4c-69a3bbf8ce56?trid=2f4a2735-7172-45dd-aca7-63618dca8aff",
      "description" : "Front End Engineer"
    }, {
      "url" : "https://jobs.smartrecruiters.com/ni/WiproDigital/61e0c747-0365-4661-bc83-9dc85da0bc3b?trid=2f4a2735-7172-45dd-aca7-63618dca8aff",
      "description" : "Full Stack Engineer"
    }, {
      "url" : "https://jobs.smartrecruiters.com/ni/WiproDigital/cf21856a-5e11-4763-b87d-c80bb92f9a0f?trid=2f4a2735-7172-45dd-aca7-63618dca8aff",
      "description" : "Lead Platform Engineer (with German)"
    }, {
      "url" : "https://jobs.smartrecruiters.com/ni/WiproDigital/62a144b2-f1d6-42b7-af97-30c8d84c2578?trid=2f4a2735-7172-45dd-aca7-63618dca8aff",
      "description" : "Lead Front End Engineer (with German)"
    }, {
      "url" : "https://jobs.smartrecruiters.com/ni/WiproDigital/40186d1e-890f-40ba-8e80-eebb3ed818ab?trid=2f4a2735-7172-45dd-aca7-63618dca8aff",
      "description" : "Senior Java Developer"
    }, {
      "url" : "https://jobs.smartrecruiters.com/ni/WiproDigital/85ffe24b-19bd-46f2-8912-5f3f3effa51b?trid=2f4a2735-7172-45dd-aca7-63618dca8aff",
      "description" : "Java Developer"
    }, {
      "url" : "https://jobs.smartrecruiters.com/ni/WiproDigital/1deffe8a-2fd9-4851-81ea-b8b10e473f9b?trid=2f4a2735-7172-45dd-aca7-63618dca8aff",
      "description" : "Platform Engineer"
    }, {
      "url" : "https://jobs.smartrecruiters.com/ni/WiproDigital/5d2a910a-3f6d-4f20-9b15-91b3841dfdbb?trid=2f4a2735-7172-45dd-aca7-63618dca8aff",
      "description" : "Full Stack Engineer"
    }, {
      "url" : "https://jobs.smartrecruiters.com/ni/WiproDigital/23d588a6-1ba1-4075-bc46-90988278f253?trid=2f4a2735-7172-45dd-aca7-63618dca8aff",
      "description" : "Front End Engineer"
    }, {
      "url" : "https://jobs.smartrecruiters.com/ni/WiproDigital/411cbd22-a1b3-4e94-a346-7a5f170aed10?trid=2f4a2735-7172-45dd-aca7-63618dca8aff",
      "description" : "Platform Engineer"
    }, {
      "url" : "https://jobs.smartrecruiters.com/ni/WiproDigital/d3f58dcc-799f-40ae-962f-2ff101d0057a?trid=2f4a2735-7172-45dd-aca7-63618dca8aff",
      "description" : "Full Stack Engineer"
    }, {
      "url" : "https://jobs.smartrecruiters.com/ni/WiproDigital/ac3f9686-ea56-4e5c-a06c-9a9a73b0b532?trid=2f4a2735-7172-45dd-aca7-63618dca8aff",
      "description" : "Front End Engineer"
    }, {
      "url" : "https://jobs.smartrecruiters.com/ni/WiproDigital/edcf71c1-aff7-41b1-96ac-579d0b771470?trid=2f4a2735-7172-45dd-aca7-63618dca8aff",
      "description" : "Lead Platform Engineer (with German)"
    }, {
      "url" : "https://jobs.smartrecruiters.com/ni/WiproDigital/e73bd1a5-eaf9-4719-8dbc-5c1b1ef85c76?trid=2f4a2735-7172-45dd-aca7-63618dca8aff",
      "description" : "Lead Front End Engineer (with German)"
    }, {
      "url" : "https://jobs.smartrecruiters.com/ni/WiproDigital/1a5b7328-1435-4ce9-a8e2-d40fe05a7818?trid=2f4a2735-7172-45dd-aca7-63618dca8aff",
      "description" : "Full Stack Engineer - Permanent"
    }, {
      "url" : "https://jobs.smartrecruiters.com/ni/WiproDigital/a27a09ae-d389-4011-96b8-045990ca1b65?trid=2f4a2735-7172-45dd-aca7-63618dca8aff",
      "description" : "Platform Engineer"
    }, {
      "url" : "https://jobs.smartrecruiters.com/ni/WiproDigital/46d2ee84-903d-4872-98fa-f69357fb79ae?trid=2f4a2735-7172-45dd-aca7-63618dca8aff",
      "description" : "Front End Engineer"
    }, {
      "url" : "https://jobs.smartrecruiters.com/ni/WiproDigital/f701558c-6805-4808-8316-669816d2097a?trid=2f4a2735-7172-45dd-aca7-63618dca8aff",
      "description" : "Platform Engineer"
    }, {
      "url" : "https://jobs.smartrecruiters.com/ni/WiproDigital/51e135dc-1f5d-424f-bae1-dc4a20a4a250?trid=2f4a2735-7172-45dd-aca7-63618dca8aff",
      "description" : "Front End Engineer"
    }, {
      "url" : "https://jobs.smartrecruiters.com/ni/WiproDigital/b44f1c0f-bb0f-4666-8742-9c24d0f6528a?trid=2f4a2735-7172-45dd-aca7-63618dca8aff",
      "description" : "Java developer"
    }, {
      "url" : "https://jobs.smartrecruiters.com/ni/WiproDigital/03269fbb-1bf5-43c4-9cde-101ad69d199f?trid=2f4a2735-7172-45dd-aca7-63618dca8aff",
      "description" : "Front End Engineer"
    }, {
      "url" : "https://jobs.smartrecruiters.com/ni/WiproDigital/3ecf481a-c488-46f1-8f4d-61bd9776dafe?trid=2f4a2735-7172-45dd-aca7-63618dca8aff",
      "description" : "Senior Java Developer"
    }, {
      "url" : "https://jobs.smartrecruiters.com/ni/WiproDigital/5530f1c5-d2d9-41b8-98de-81a5698e03d2?trid=2f4a2735-7172-45dd-aca7-63618dca8aff",
      "description" : "Platform Engineer"
    }, {
      "url" : "https://jobs.smartrecruiters.com/ni/WiproDigital/04d61118-ba97-4ded-98c9-c39ccc7c646e?trid=2f4a2735-7172-45dd-aca7-63618dca8aff",
      "description" : "Senior Java developer - contract"
    }, {
      "url" : "https://jobs.smartrecruiters.com/ni/WiproDigital/1b4b0582-7de7-4fee-af47-436fbb446646?trid=2f4a2735-7172-45dd-aca7-63618dca8aff",
      "description" : "Senior Java Developer"
    }, {
      "url" : "https://jobs.smartrecruiters.com/ni/WiproDigital/42e70b0a-87e1-408b-8ee0-a664558944a6?trid=2f4a2735-7172-45dd-aca7-63618dca8aff",
      "description" : "Java developer"
    }, {
      "url" : "https://jobs.smartrecruiters.com/ni/WiproDigital/fa88abb8-69e7-491c-a3f8-1f215e51e8ee?trid=2f4a2735-7172-45dd-aca7-63618dca8aff",
      "description" : "Platform Engineer"
    }, {
      "url" : "https://jobs.smartrecruiters.com/ni/WiproDigital/3984d011-ed73-413a-9a8d-5ae93bb70a99?trid=2f4a2735-7172-45dd-aca7-63618dca8aff",
      "description" : "Front End Engineer"
    }, {
      "url" : "https://www.instagram.com/buildit_tech",
      "description" : "Instagram Logo"
    }, {
      "url" : "https://twitter.com/buildit_tech",
      "description" : "Twitter Logo"
    }, {
      "url" : "https://www.linkedin.com/company/buildit.",
      "description" : "LinkedIn Logo"
    }, {
      "url" : "https://github.com/buildit",
      "description" : "Github Logo"
    }, {
      "url" : "https://wiprodigital.com/privacy-policy",
      "description" : "Privacy policy"
    } ],
    "mediaLinks" : [ {
      "url" : "https://cdnjs.cloudflare.com/ajax/libs/scrollReveal.js/3.4.0/scrollreveal.min.js",
      "description" : "script"
    }, {
      "url" : "https://buildit.wiprodigital.com/scripts/bundle.min.js",
      "description" : "script"
    }, {
      "url" : "https://www.googletagmanager.com/gtag/js?id=UA-78138413-2",
      "description" : "script"
    } ],
    "description" : "Careers | buildit @ wipro digital",
    "internalLinks" : [ {
      "url" : "https://buildit.wiprodigital.com/",
      "description" : "buildit @ wipro digital"
    }, {
      "url" : "https://buildit.wiprodigital.com/about/",
      "description" : "About us"
    }, {
      "url" : "https://buildit.wiprodigital.com/locations/",
      "description" : "Locations"
    }, {
      "url" : "https://buildit.wiprodigital.com/careers/#the-hiring-process",
      "description" : "Our hiring process"
    }, {
      "url" : "https://buildit.wiprodigital.com/",
      "description" : "Buildit Logo"
    } ]
  }, {
    "inboundLinkCount" : 4,
    "url" : "https://buildit.wiprodigital.com/about/",
    "externalLinks" : [ {
      "url" : "https://medium.com/buildit",
      "description" : "Medium Monogram Logo Blog"
    }, {
      "url" : "https://medium.com/buildit/technology/home",
      "description" : "technologies"
    }, {
      "url" : "https://medium.com/buildit/design-systems/home",
      "description" : "Design Systems"
    }, {
      "url" : "https://medium.com/buildit/org-change/home",
      "description" : "continuous improvement and systems thinking"
    }, {
      "url" : "https://www.wipro.com/",
      "description" : "Wipro"
    }, {
      "url" : "https://wiprodigital.com/",
      "description" : ""
    }, {
      "url" : "https://designit.com",
      "description" : ""
    }, {
      "url" : "https://www.instagram.com/buildit_tech",
      "description" : "Instagram Logo"
    }, {
      "url" : "https://twitter.com/buildit_tech",
      "description" : "Twitter Logo"
    }, {
      "url" : "https://www.linkedin.com/company/buildit.",
      "description" : "LinkedIn Logo"
    }, {
      "url" : "https://github.com/buildit",
      "description" : "Github Logo"
    }, {
      "url" : "https://wiprodigital.com/privacy-policy",
      "description" : "Privacy policy"
    } ],
    "mediaLinks" : [ {
      "url" : "https://buildit.wiprodigital.com/images/spaceman.svg",
      "description" : "img"
    }, {
      "url" : "https://buildit.wiprodigital.com/images/cube.svg",
      "description" : "img"
    }, {
      "url" : "https://buildit.wiprodigital.com/images/bull.svg",
      "description" : "img"
    }, {
      "url" : "https://buildit.wiprodigital.com/images/r2d2.svg",
      "description" : "img"
    }, {
      "url" : "https://cdnjs.cloudflare.com/ajax/libs/scrollReveal.js/3.4.0/scrollreveal.min.js",
      "description" : "script"
    }, {
      "url" : "https://buildit.wiprodigital.com/scripts/bundle.min.js",
      "description" : "script"
    }, {
      "url" : "https://www.googletagmanager.com/gtag/js?id=UA-78138413-2",
      "description" : "script"
    } ],
    "description" : "About us | buildit @ wipro digital",
    "internalLinks" : [ {
      "url" : "https://buildit.wiprodigital.com/",
      "description" : "buildit @ wipro digital"
    }, {
      "url" : "https://buildit.wiprodigital.com/careers/",
      "description" : "Careers"
    }, {
      "url" : "https://buildit.wiprodigital.com/locations/",
      "description" : "Locations"
    }, {
      "url" : "https://buildit.wiprodigital.com/",
      "description" : "Buildit Logo"
    } ]
  }, {
    "inboundLinkCount" : 3,
    "url" : "https://buildit.wiprodigital.com/locations/",
    "externalLinks" : [ {
      "url" : "https://medium.com/buildit",
      "description" : "Medium Monogram Logo Blog"
    }, {
      "url" : "https://www.google.com/maps/place/Wipro+Limited/@12.838566,77.6592042,16z/data=!4m5!3m4!1s0x0:0x1d0caf77fe02554f!8m2!3d12.8384852!4d77.6571581?hl=en-US",
      "description" : ""
    }, {
      "url" : "https://www.google.co.uk/maps/place/5445+Legacy+Dr+%23300,+Plano,+TX+75024,+USA/@33.0785339,-96.8124283,16z/data=!4m5!3m4!1s0x864c3ccc06dd4f19:0x4de03bea3977bc1!8m2!3d33.0782823!4d-96.8082226",
      "description" : ""
    }, {
      "url" : "https://www.google.com/maps?ll=39.752562,-105.002314&z=16&t=m&hl=en-US&gl=GB&mapclient=embed&q=1550+Wewatta+St+Denver,+CO+80202+USA",
      "description" : ""
    }, {
      "url" : "https://www.google.com/maps?ll=53.330506,-6.228374&z=16&t=m&hl=en-US&gl=GB&mapclient=embed&cid=3233598717796492272",
      "description" : ""
    }, {
      "url" : "https://www.google.com/maps/place/2+Castle+Terrace,+Edinburgh+EH1+2EL/@55.9486962,-3.2066846,19z/data=!3m1!4b1!4m5!3m4!1s0x4887c79842eb1771:0xaa89ab9f8cf5e689!8m2!3d55.9486955!4d-3.2061374",
      "description" : ""
    }, {
      "url" : "https://www.google.com/maps/place/Alchemia+Ferrum+Tower/@54.3985837,18.5747376,17z/data=!3m1!4b1!4m5!3m4!1s0x46fd752864e2eab3:0x80d3c9302c7fad51!8m2!3d54.3985837!4d18.5769263",
      "description" : ""
    }, {
      "url" : "https://www.google.com/maps/place/2+Finsbury+Ave,+London+EC2M+2PA/@51.5201019,-0.0871868,17z/data=!3m1!4b1!4m5!3m4!1s0x48761cadd0fdb387:0xa8fcbacd368e31b6!8m2!3d51.5201019!4d-0.0849981",
      "description" : ""
    }, {
      "url" : "https://www.google.com/maps/place/77+Sands+St,+Brooklyn,+NY+11201,+USA/@40.700331,-73.987327,17z/data=!3m1!4b1!4m5!3m4!1s0x89c25a346b0a6f41:0xe60dd10638023226!8m2!3d40.7003578!4d-73.9873581",
      "description" : ""
    }, {
      "url" : "https://www.google.com/maps/place/Aleje+Jerozolimskie+123A,+00-001+Warszawa,+Poland/@52.2249301,20.9888819,17z/data=!3m1!4b1!4m5!3m4!1s0x471ecc912c4600c1:0xcf1c1af486cc53d1!8m2!3d52.2249301!4d20.9910706",
      "description" : ""
    }, {
      "url" : "https://www.instagram.com/buildit_tech",
      "description" : "Instagram Logo"
    }, {
      "url" : "https://twitter.com/buildit_tech",
      "description" : "Twitter Logo"
    }, {
      "url" : "https://www.linkedin.com/company/buildit.",
      "description" : "LinkedIn Logo"
    }, {
      "url" : "https://github.com/buildit",
      "description" : "Github Logo"
    }, {
      "url" : "https://wiprodigital.com/privacy-policy",
      "description" : "Privacy policy"
    } ],
    "mediaLinks" : [ {
      "url" : "https://maps.googleapis.com/maps/api/staticmap?format=jpg&key=AIzaSyAa-P3u_B9zTs_DJ_dXRK5og7r3_n7vlT0&maptype=roadmap&scale=2&size=425x300&markers=12.837073620840986,77.65719560673462&zoom=15",
      "description" : "img"
    }, {
      "url" : "https://maps.googleapis.com/maps/api/staticmap?format=jpg&key=AIzaSyAa-P3u_B9zTs_DJ_dXRK5og7r3_n7vlT0&maptype=roadmap&scale=2&size=425x300&markers=33.0782823,-96.8104113&zoom=14",
      "description" : "img"
    }, {
      "url" : "https://maps.googleapis.com/maps/api/staticmap?format=jpg&key=AIzaSyAa-P3u_B9zTs_DJ_dXRK5og7r3_n7vlT0&maptype=roadmap&scale=2&size=425x300&markers=39.75239475433594,-105.00245422823357&zoom=16",
      "description" : "img"
    }, {
      "url" : "https://maps.googleapis.com/maps/api/staticmap?format=jpg&key=AIzaSyAa-P3u_B9zTs_DJ_dXRK5og7r3_n7vlT0&maptype=roadmap&scale=2&size=425x300&markers=53.33044103386714,-6.228172585833819&zoom=15",
      "description" : "img"
    }, {
      "url" : "https://maps.googleapis.com/maps/api/staticmap?format=jpg&key=AIzaSyAa-P3u_B9zTs_DJ_dXRK5og7r3_n7vlT0&maptype=roadmap&scale=2&size=425x300&markers=55.9486487,-3.2061902&zoom=17",
      "description" : "img"
    }, {
      "url" : "https://maps.googleapis.com/maps/api/staticmap?format=jpg&key=AIzaSyAa-P3u_B9zTs_DJ_dXRK5og7r3_n7vlT0&maptype=roadmap&scale=2&size=425x300&markers=54.39851249592157,18.57698661508039&zoom=15",
      "description" : "img"
    }, {
      "url" : "https://maps.googleapis.com/maps/api/staticmap?format=jpg&key=AIzaSyAa-P3u_B9zTs_DJ_dXRK5og7r3_n7vlT0&maptype=roadmap&scale=2&size=425x300&markers=51.51993868691731,-0.08485094876959921&zoom=17",
      "description" : "img"
    }, {
      "url" : "https://maps.googleapis.com/maps/api/staticmap?format=jpg&key=AIzaSyAa-P3u_B9zTs_DJ_dXRK5og7r3_n7vlT0&maptype=roadmap&scale=2&size=425x300&markers=40.70033451610208,-73.98736217524858&zoom=17",
      "description" : "img"
    }, {
      "url" : "https://maps.googleapis.com/maps/api/staticmap?format=jpg&key=AIzaSyAa-P3u_B9zTs_DJ_dXRK5og7r3_n7vlT0&maptype=roadmap&scale=2&size=425x300&markers=52.22498157391693,20.991161204874516&zoom=15",
      "description" : "img"
    }, {
      "url" : "https://cdnjs.cloudflare.com/ajax/libs/scrollReveal.js/3.4.0/scrollreveal.min.js",
      "description" : "script"
    }, {
      "url" : "https://buildit.wiprodigital.com/scripts/bundle.min.js",
      "description" : "script"
    }, {
      "url" : "https://www.googletagmanager.com/gtag/js?id=UA-78138413-2",
      "description" : "script"
    } ],
    "description" : "Locations | buildit @ wipro digital",
    "internalLinks" : [ {
      "url" : "https://buildit.wiprodigital.com/",
      "description" : "buildit @ wipro digital"
    }, {
      "url" : "https://buildit.wiprodigital.com/about/",
      "description" : "About us"
    }, {
      "url" : "https://buildit.wiprodigital.com/careers/",
      "description" : "Careers"
    }, {
      "url" : "https://buildit.wiprodigital.com/",
      "description" : "Buildit Logo"
    } ]
  } ]
}
```
