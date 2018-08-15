## Demo Repository for Expiring Caches based on absolute Expiration Time
The Tech Stack includes,
* Spring Cloud Config Server
* Spring Boot
* Spring Caching abstraction/annotations
* Redis Server


### Repository Consists of the following components
* config-server         - Service which serves the configuration for the cache-service and cache-scheduled-job
* config-repo           - Stores the configuration for cache-service and cache-scheduled-job
* cache-service         - Service which uses Spring Caching annotations to cache method responses 
* cache-scheduled-job   - Schedules and Expires the caches based on the expiration time configured

### Requirement
* Refresh/Update the expiration values of different caches
  used in the application at runtime without recycling the application.
* The expiration values are provided in yaml format and 
  also externalized in a git based config repo and served via Spring Cloud Config Server.

## Dependencies
*  The Components depend on the **caching-component** library as available in repo, [Caching-Component](https://github.com/gsamartian/caching-component)
*  First, download the repo and build and install the caching-component jar in your local maven repository.

   
## Getting Started
* Clone the Repository into your local machine
* First, Start the Spring Cloud Config Server as below,
* Navigate to the folder of **config-server**
  Run the command ,
  ```
  mvn spring-boot:run -Dspring.cloud.config.server.native.search-locations=<<folder for config-repo>>
  ```
  (OR) Just update **config-server/src/main/resources/application.yml** with the location of the config-repo folder
  and just run
  ```
  mvn spring-boot:run
  ```
* Next , Start the Redis Server , if not already started
* Then , navigate to **cache-service** folder path and start the **cache-service" as below,
* After that, navigate to **cache-service** folder path and start the **cache-service** as below,
	```
   mvn spring-boot:run
   ```
* Finally, start the **cache-scheduled-job** by navigating to the folder and running the below command,
```
 mvn spring-boot:run
 ```

