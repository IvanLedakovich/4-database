# Java web application, converting text files into images and saving them into a database with a possibility of downloading them, deployable on Apache Tomcat and PostgreSQL

**The program accepts uploaded .txt files from the JSP page, processes each one in a separate thread,
reads .txt files, prints their contents in the terminal, saves them in a database, allows to view and download the uploaded files and saves them as images according
to the type and directory provided. The program uses log4j and commons-io as dependencies
as well as JUnit for testing and utilizes the Gradle build tool.**

## How to run the program:
There are 2 ways to run the application: inside the IDE and on an Apache Tomcat server.

Inside IDE (IntelliJ IDEA):

1. Install the Smart Tomcat plugin inside the IDE settings
2. Download the Apache Tomcat
3. In Run/Debug Configurations add a new one using Smart Tomcat and configure it
4. Create and write a config.properties file in accordance to the defaultConfig.properties one in the resources folder
5. Run the program

Through .war and Apache Tomcat

1. In the Gradle task menu execute the "war" task
2. Copy the .war file to the webapps folder of Apache Tomcat
3. Run the application on the Tomcat server
4. Once the .war archive has been extracted into a separate folder, 
provide your configuration file next to the defaultConfig.properties in \WEB-INF\classes
5. Rerun the Apache Tomcat server and the application is functional