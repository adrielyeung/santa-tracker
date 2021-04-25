# santa-tracker
A parcel tracking webapp, where users can log in and track their deliveries, and communicate with customer support.

Built using Maven with Spring Boot, written in Java. Connection to local Microsoft SQL Server database.

## 1. Getting started - How to run the application
Download the whole repository, ensuring especially the ```pom.xml``` file is correctly downloaded to set up dependencies.
Run the application as spring boot application, in Eclipse, set:
```
Goals: spring-boot:run
```
Navigate to ```/register``` to start by registering your account.

## 2. How to use the API
### Main production code
- ```src/main/java``` contains the Java production code.
  - ```entity``` contains all the entities involved.
  - ```controller``` contains all the logic for directing a particular URL to a view.
  - ```repository``` contains the JPA Repository to access the SQL database.
  - ```factory``` contains the code for producing the entities.
  - ```filter``` contains all the filters implemented. The no cache filter clears cache before accessing security required pages, so that browser back button cannot be used.
  - ```runner``` can be used to simulate the behaviour of the code without running the server.
### Tests
- ```src/test/java``` contains the unit tests of the ```main``` code, using JUnit 4.
### Views
- ```src/main/webapp``` contains all the views (webpages) written in JSP, the ```web.xml``` to set up the filters and the ```dispatcher-servlet```. The ```App``` contains all the pages which require security for access.

## 3. Other features to implement
Hope to:
- Complete messaging system, including notification by email about delays.
- Include embedded map for tracking where the delivery is at.
- Add demo user / admin accounts so that the app can be viewed without creating an account.
- Add page to update personal details.
