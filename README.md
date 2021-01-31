# Tank-Battle
Dockerized JAX-RS backend and Mongo DB for a simple battle simulation.

### 3rd party software
*This is used/downloaded by the application itself, and does not need 
to be installed manually*
* [Payara](https://hub.docker.com/r/payara/server-full)
* [Mongo](https://hub.docker.com/_/mongo)
* [Swagger-UI](https://github.com/swagger-api/swagger-ui)


### To start you need...
* Zulu JDK 8.42.0.23
* Maven 3.6.3
* Docker 20.10.2

The important one here is the JDK 8, this application doesn't compile 
on Java 11 due to the moving of `javax.xml.bind`.

Make sure that your `PATH`, and `JAVA_HOME` variables are updated on your 
system to include the binaries that make `mvn` and `docker` runnable.

## Build and Run
Run the following in a console of your choice (I suggest 
[Cygwin](https://cygwin.com/install.html) for Windows, but it's just 
a preference thing):
1. `cd tank-battle`

   Navigate into our main directory, where our `pom.xml` is.    
2. `mvn clean install`
   
   Clean any previous build documents and compile our code into a
   runnable `.war` file.
3. `docker-compose up`

   Use `docker-compose` to build and deploy our images.



After that, navigate to the following address to use the Swagger UI:
http://localhost:8080/app/swagger-ui/index.html

## Current Version Information (*V1.0*)
The current version only supports one map `mapId : "1"` and two tanks
`tankId : [ "1", "2" ]`. The Game ID is returned as a UUID for the POST 
method `v1/simulate` and must be used in the `v1/score/{game_id}` method.

### Ideas for future versions
* Map

   * Generate map for session
   * Integrate damage for landmines (on treads) and decision logic 
     for AI
   * Add in difficulty level based on # of obstacles generated on 
     map
    
* Tank

   * Add new tanks with new properties:
    
      * `TANK_DAMAGE`
      * `TANK_SPEED`
      * `TANK_ARMOUR`
       
   *  Upgrade decision logic in TankAI to factor in:
    
      * Low health
      * Flanking
    
## License Information
This application runs under a GPL 3 license that is included with the code.
If this code is run and distributed however, note that the SSPL for MongoDB 
has it's own specific restrictions for redistribution as 3rd party software. 
For more information, visit 
[SSPL - Wikipedia](https://en.wikipedia.org/wiki/Server_Side_Public_License).