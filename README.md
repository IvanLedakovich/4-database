# Java web application, converting text files into images, deployable on Apache Tomcat

**The program accepts uploaded .txt files from the JSP page, processes each one in a separate thread,
reads .txt files, prints their contents in the terminal, allows to view and download the uploaded files and saves them as images according
to the type and directory provided. The program uses log4j and commons-io as dependencies
as well as JUnit for testing and utilizes the Gradle build tool.**

## How to run the program:
There are 2 ways to run the application: inside the IDE and on an Apache Tomcat server.

Inside IDE (IntelliJ IDEA):

1. Install the Smart Tomcat plugin inside the IDE settings.
2. In Run/Debug Configurations add a new configuration using Smart Tomcat.
3. Download the Apache Tomcat and configure the parameters
4. Run the program 

Through .war and Apache Tomcat

1. In the Gradle task menu execute the "war" task
2. Copy the .war file to the webapps folder of Apache Tomcat
3. Run the Apache Tomcat server and the application is running