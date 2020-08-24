# Portfolio Manager Application

- This is an application for managing the portfolio of the User (With Dummy market replica)

## Basic Technologies & Approach Used

- Programming Language - Core Java 8
- SpringBoot 
- REST API
- In-memory Active MQ
- Gradle for Build
- Database - In-memory H2 database


## How to 
- Run the Application
    1. Download the application in Directory from Git Hub 
    2. Run the following command from project root (Using your Terminal or Command Prompt)
    
        ```./gradlew bootRun```
    3. The application will run on Port 8080 (Please keep port 8080 free)
    4. Now the Market is already started I am not printing the sending and receiving logs of the Market so that they will not disturb 
       the format of Console subscriber. Otherwise we can create a separate log file for the application logs. 
    5. The URL to Start the trade for user is 
        http://localhost:8080/trade.html
    6. Once we start the Trade The using above URL the POSITIONS and the Total NAV will start printing on the console 
       for the beautification of the logs I have kept them as System.out.println Statements to avoid the time and class name logging which happens if I use log4j or @Log from lambook
    7. The URL for text file printer that runs on demand and prints the latest market value for all Positions and the NAV of the whole portfolio.
        http://localhost:8080/portfolio/download.excel
    8. The URL to See the H2 database Console is 
        http://localhost:8080/h2-console/login.jsp
        It will ask for login password which is 'password' --> you can get it from the application.properties file as well
        
     
     
## Assumptions Made    

- For Call or Put Options I have kept the expire time as Constant 1 years Hence I am not taking that as Input in the Trade file 
- The Call and Put Options were a bit complex complex concepts I have tried to Implement to the best of my Knowledge. 
    Knowing this that the focus of the exercise is to check the Programming skills.
- I have made a Dummy User DOW JOE and hardcoded the User in the system for Simplicity - Otherwise we can reade it from trade file or I can take input from path
- System is running on random values so I have written test cases only for those cases where exceptions can be there for example If the Trade file is Missing 
 