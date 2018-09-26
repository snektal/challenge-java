# Access Log Processor
## Rest API
This is a Spring boot application and once started it will run on following URL localhost:8080
There is a rest API that allows to execute analysis report for requirements specified in challenge.md
At minimum, API requires to provide a date in ISO format for which report will execute  

- pages by unique hits

   params: date: 2017-09-29
         
>    http://localhost:8080/api/report/unique-page-hits/2017-09-29
    
- pages by number of users  
  
   params: date: 2017-09-29
    
>    http://localhost:8080/api/report/pages-by-number-of-users/2017-09-29

   params: date: 2017-09-29 
            page: index.html
    
>    http://localhost:8080/api/report/number-of-users/2017-09-29/index.html
    
- users by unique page views

   params: date: 2017-09-29
        
>    http://localhost:8080/api/report/all-users-unique-page-views/2017-09-29

   params: date: 2017-09-29 
           userId: 04a5d9a7-0a76-47a8-abd3-9e39a1abce51
    
>   http://localhost:8080/api/report/user-unique-page-views/2017-09-29/04a5d9a7-0a76-47a8-abd3-9e39a1abce51

  
## Unit tests 
Test are invoking methods from class HttpServerAccessLogProcessor (Spring bean)
File name dependency injected in HttpServerAccessLogProcessor from property 'spruce.server.access.log.name'
in application.properties file.
Unit tests are running against testlog.csv file located under 
directory /challenge/src/test/resources/testlog.csv 
##### Note! Change this property ('spruce.server.access.log.name') to run against actual access log file
### Run tests from command line

> mvn clean install > build.log 2>&1