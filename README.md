# Java web application, converting text files into images and saving them into a database with a possibility of downloading them, deployable on Apache Tomcat and PostgreSQL

**The program accepts uploaded .txt files from the JSP page, processes each one in a separate thread,
reads .txt files, prints their contents in the terminal, saves them in a database, allows to view and download the uploaded files and saves them as images according
to the type and directory provided. The program uses log4j and commons-io as dependencies
as well as JUnit for testing and utilizes the Gradle build tool.**

## How to run the program:
There are 2 ways to run the application: inside the IDE and on an Apache Tomcat server.

Inside IDE (IntelliJ IDEA):

1. Install the Smart Tomcat plugin inside the IDE settings.
2. In Run/Debug Configurations add a new configuration using Smart Tomcat.
3. Download the Apache Tomcat and configure the parameters
4. Provide the program with a configuration file under the resources folder where output paths are stored to choose from
5. Create a database such as provided in the "dbexample" folder
6. In Run/Debug configuration provide the program with the POSTGRES_USER, POSTGRES_PASSWORD, POSTGRES_DB arguments
7. Run the program