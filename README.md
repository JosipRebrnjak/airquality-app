# airquality-app
Java EE application made for job testing and refreshing my knowledge. 

TASK: 

1.Create a service that collects data about air quality monitoring networks from the URL
http://www.ekonerg.hr/iskzl/rs/mreza/list and stores it in a relational database (only data from
the Mjerna mreža grada Zagreba and Mjerna mreža Primorsko-goranske županije networks).
If a record already exists in the database, it should be updated. Track the time of the last change.

2.Create entities (EJBs) from the created tables and develop a REST service that fetches the list of stations for a given network.

3.Create a web page that displays the list of networks and fetches the list of stations for the selected network via the REST service (display only the name). Active stations should be shown in green, and inactive stations in grey.

4.Clicking on a station opens a new web page where the station’s data can be updated (only the English name and activity). Both web pages must have an identical header.

Mandatory technologies: Java, EJB, JPA, JAX-RS, SQL
Recommended (optional) technologies: CDI, Servlet, jQuery, Bootstrap, Apache FreeMarker
Solution

The solution was developed in Visual Studio Code and deployed on WildFly 38.0.1.

The backend is a Java EE REST API.
Runtime environment: OpenJDK Runtime Environment Corretto 17.0.14.7.1 (build 17.0.14+7-LTS).

## Backend Setup

1. **Database configuration:**  
   - Configure your relational database (MySQL/PostgreSQL/etc.). a 
   - Update the `persistence.xml` datasource to match your DB credentials.

2. **Add initial dataset:**  
   - When you have compiled the app and deployed it on WildFly (or before), you need to add the dataset on WildFly which will access your SQL database.  
   - This ensures that the application has the initial networks and stations available.
  
  **Example (MySQL) using JBoss CLI:**

   .\wildfly-30.0.1.Final\bin\jboss-cli.bat

   /subsystem=datasources/data-source=AirQualityDS:add(
       jndi-name=java:/jdbc/AirQualityDS,
       enabled=true,
       driver-name=mysql,
       connection-url="jdbc:mysql://<host:port>/<db_name>?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
       user-name=<username>,
       password=<password>
   )

<host:port> - host and port of database created in the first step e.g localhost:3306
<db_name> - name of the database 
<username> - user with privileges to access database
<password> - password for the user


3. **WildFly setup:**  
   - Deploy the `airquality-app.war` to WildFly 38.0.1.  
   - Make sure the datasource is available in WildFly.

4. **Swagger UI (API documentation):**  
   Available at:  
   http://<HOST:PORT>/airquality/swagger-ui/


Additional configuration for Swagger UI

To enable Swagger UI, WildFly 38.0.1 must be configured so that the OpenAPI endpoint is available.

In standalone.xml:

Add the following extension (around line 38):

<extension module="org.wildfly.extension.microprofile.openapi-smallrye"/>


Add the following subsystem (around line 455):

<subsystem xmlns="urn:wildfly:microprofile-openapi-smallrye:1.0"/>

Clients

Three different clients were developed:

1. FreeMarker Client
http://<HOST:PORT>/airquality/fm/mreze

2. React Client

Requirements:

Node.js runtime
(developed using Node.js v22.14.0 and npm v10.9.2)

Installation and run:

cd <path_to_client>/airquality-react
npm install
npm start


Address:

http://localhost:3000/

3. Simple HTML / jQuery Client

Open the following file in any browser:

./airquality-app/airquality-simple-client/index.html

   
   
