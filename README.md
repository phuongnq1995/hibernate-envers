# Hibernate Envers Auditing Example

## Overview

This project demonstrates how to use Hibernate Envers for auditing entity changes in a Spring Boot application. Hibernate Envers automatically tracks and stores historical changes (revisions) of your entities, allowing you to query and restore previous states.

## How Hibernate Envers Works

- **Revision Tracking**: Envers creates audit tables for your entities and stores a new record for each change (insert, update, delete).
- **Revision Entities**: Each revision is associated with a revision entity that stores metadata (such as timestamp, user, or module).
- **Audit Queries**: You can query the history of an entity using Envers APIs.

## Default Behavior

By default, Envers:
- Audits all changes to entities annotated with `@Audited`.
- Stores revision metadata in a revision table (e.g., `REVINFO`).
- Uses the default revision entity and listener.

## Customizing Envers Behavior

You can customize Envers to fit your auditing requirements:

### 1. Custom Revision Entity
- Define a custom revision entity by extending `@RevisionEntity`.
- Add custom fields (e.g., user, module) to track extra metadata.

### 2. Custom Revision Listener
- Implement `RevisionListener` to populate custom fields.
- Example: `CustomTrackingRevisionListener.java` sets additional audit info like the current module or user.

### 3. Aspect-Oriented Module Tracking
- Use an aspect (e.g., `ModuleAspect.java`) to capture contextual information (such as the current module) and store it for auditing.

### 4. Configuration
- Configure Envers in your `application.yml` or `application.properties`.
- Example settings: enabling/disabling auditing, customizing table names, etc.

### 5. Querying History
- Use Envers APIs to query the history of an entity.
- Example: `HistoryService.java` queries the history of a specific entity.
```text
=========================================
Rev Id: 102, timestamp: 1749479410390 by userId: 40daa935-7927-4429-9dbb-2ff191f7f2d9
================ Details ===================
Store, changed properties: [masterAddress, languages, deliveryAddress, name]
=========================================
```

## Example Customization Flow
1. Implement a custom revision entity and listener.
2. Register the listener in your configuration.
3. Use aspects or interceptors to set contextual audit data.
4. Annotate entities with `@Audited` to enable auditing.

## References
- [Hibernate Envers Documentation](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#envers)
- [Spring Boot + Hibernate Envers Example](https://www.baeldung.com/hibernate-envers)

---

Feel free to explore the source code for examples of custom revision listeners and module tracking aspects.

## Development

Update your local database connection in `application.yml` or create your own `application-local.yml` file to override
settings for development.

During development it is recommended to use the profile `local`. In IntelliJ `-Dspring.profiles.active=local` can be
added in the VM options of the Run Configuration after enabling this property in "Modify options".

After starting the application it is accessible under `localhost:8080`.

## Build

The application can be built using the following command:

```
mvnw clean package
```

Start your application with the following command - here with the profile `production`:

```
java -Dspring.profiles.active=production -jar ./target/hibernate-envers-0.0.1-SNAPSHOT.jar
```

If required, a Docker image can be created with the Spring Boot plugin. Add `SPRING_PROFILES_ACTIVE=production` as
environment variable when running the container.

```
mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=org.phuongnq/hibernate-envers
```

## Further readings

* [Maven docs](https://maven.apache.org/guides/index.html)  
* [Spring Boot reference](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)  
* [Spring Data JPA reference](https://docs.spring.io/spring-data/jpa/reference/jpa.html)
* [Thymeleaf docs](https://www.thymeleaf.org/documentation.html)  
* [Bootstrap docs](https://getbootstrap.com/docs/5.3/getting-started/introduction/)  
* [Learn Spring Boot with Thymeleaf](https://www.wimdeblauwe.com/books/taming-thymeleaf/)  
