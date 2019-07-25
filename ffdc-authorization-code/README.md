# Welcome

This sample client application demonstrates the implementation of the OAuth2 Authorization Code authorization grant for FusionFabric.cloud.

**To run this sample**

1. Register an application on [**Fusion**Fabric.cloud Developer Portal](https://developer.fusionfabric.cloud), and include the **Static Data for Trade Capture** API. Use `http://localhost:8081/login/oauth2/code/finastra` as the reply URL.
2. Clone the current project.
3. Copy `src/main/resources/application.yml.sample` to `src/main/resources/application.yml`, open it, and enter `<%YOUR-CLIENT-ID%>`, and `<%YOUR-SECRET-KEY%>` of the application created at the step 1.   

> The values for `<%authorization-endpoint%>`, `<%token-endpoint%>`, and `<%jwk-set-uri%>` are provided by the [Discovery service](https://developer.fusionfabric.cloud/documentation?workspace=FusionCreator%20Developer%20Portal&board=Home&uri=oauth2-grants.html#discovery-service) of the **Fusion**Fabric.cloud Developer Portal.

4. Run this sample app with Maven:

```sh
mvn spring-boot:run
```

5. Point your browser to http://localhost:8081. 
6. Log into the **Fusion**Fabric.cloud Developer Portal Authorization Server with one of the following credentials:

| User        | Password |
| :---------- | :------- |
| `ffdcuser1` | `123456` |
| `ffdcuser2` | `123456` |

The home page of this sample application is displayed.

7. (Optional) Click the links in the home page to get the lists of the counterparties, legal entities and traders, from the **Static Data for Trade Capture** API.

> To learn how to create this sample project from scratch, follow the tutorial from the [Developer Portal Documentation](https://developer.fusionfabric.cloud/documentation?workspace=FusionCreator&board=Home&uri=sample-client-springboot.html). 
