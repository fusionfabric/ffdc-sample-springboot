# Welcome

This sample client application demonstrates the implementation of the OAuth2 Client Credentials authorization grant flow for FusionFabric.cloud.

**To run this sample**

1. Register an application on [**Fusion**Fabric.cloud Developer Portal](https://developer.fusionfabric.cloud), and include the **Referential Data** API. Use `*` as the reply URL.
2. Clone the current project.
3. Copy `src/main/resources/application.yml.sample` to `src/main/resources/application.yml`, open it, and enter  `<%YOUR-CLIENT-ID%>`, and `<%YOUR-SECRET-KEY%>` of the application created at the step 1.  

> The value for `<%accessTokenUri%>` is provided by the [Discovery service](https://developer.fusionfabric.cloud/documentation?workspace=FusionCreator%20Developer%20Portal&board=Home&uri=oauth2-grants.html#discovery-service) of the **Fusion**Fabric.cloud Developer Portal.
	+ `<%get-currencies-url>` - the URL of the `GET` **/currencies** endpoint of the **Referential Data** API. 

4. Run this sample app with Maven:

```sh
mvn spring-boot:run
```

5. Point your browser to http://localhost:8081. The list of currencies is retrieved from the **Referential Data** API.

> To learn how to create this sample project from scratch, follow the tutorial from [Developer Portal Documentation](https://developer.fusionfabric.cloud/documentation?workspace=FusionCreator%20Developer%20Portal&board=Home&uri=sample-client-springboot.html). 
