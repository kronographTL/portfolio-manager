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
    4. The URL to Start the trade for user is 
        http://localhost:8080/ui.html
    5. The URL for text file printer that runs on demand and prints the latest market value for all positions and the NAV of the whole portfolio.
        http://localhost:8080/portfolio/download.excel
        