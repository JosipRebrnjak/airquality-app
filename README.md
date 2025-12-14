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

API Documentation (Swagger)

Swagger UI is available for API documentation:

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

   
   
