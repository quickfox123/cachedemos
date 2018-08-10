## Repository Created with code to reproduce the issue when using
* Spring Cloud Config Server
* Spring Boot
* Spring Caching abstraction/annotations with Redis Cache Server

### Requirement is the refresh/update the expiration values of different caches used in the application at runtime without recycling the application.
* The expiration values are provided in yaml format and 
* externalied in a git based config repo   and served via Spring Cloud Config Server.

# When using @RefreshScope the CacheManager bean is not getting refreshed.

Also, not sure if the updated expiration would be applicable for existing keys as well
as new keys added in the future via this Cache Manager.
