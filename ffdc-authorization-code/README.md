# Welcome

This sample client application demonstrates the implementation of the OAuth2 Authorization Code authorization grant for FusionFabric.cloud.

**To run this sample**

1. Register an application on [**Fusion**Fabric.cloud Developer Portal](https://developer.fusionfabric.cloud), and include the [Static Data for Trade Capture](https://developer.fusionfabric.cloud/api/trading-trade-capture-static-data-8faddb99-a71f-464d-9c3d-2220baacc299/docs) API. Use `http://localhost:8081/login/oauth2/code/finastra` as the reply URL.
2. Clone the current project.
3. Copy `src/main/resources/application.yml.sample` to `src/main/resources/application.yml`, open it, and enter `<%YOUR-CLIENT-ID%>`, and `<%YOUR-SECRET-KEY%>` of the application created at the step 1.   

> The values for `<%authorization-endpoint%>`, `<%token-endpoint%>`, and `<%jwk-set-uri%>` are provided by the [Discovery service](https://developer.fusionfabric.cloud/documentation/oauth2-grants#discovery-service) of the **Fusion**Fabric.cloud Developer Portal.

4. (Optional) If you want to use private key authentication, instead of the standard authentication based on secret value, follow [the steps from the documentation](https://developer.fusionfabric.cloud/ffdc-documentation/oauth2-grants.html#jwk-auth-procedure) to sign and upload a JSON Web Key to your application, and save the private RSA key in a file named **private.key**. Edit `application.yml` as follows:
+ remove or comment the line containing the secret value: 
```
finastra:
    oauth2:
        client:
#           clientSecret:"<%YOUR-SECRET-KEY%>"
```
+ set `auth.strong=True`
+ set `auth.kid="<%YOUR-KEY-ID%>"`

> Store your private key file - **private.key** - in  **src/main/resources** as **pkcs8_private.pem**, after you convert it to the **pkcs8** format:
```
openssl pkcs8 -topk8 -in private.key -nocrypt -out pkcs8_private.pem
```  

5. Run this sample app with Maven:

```sh
mvn spring-boot:run
```

6. Point your browser to http://localhost:8081. The home page of the sample application is displayed.
7. Click **Get Data**. You are redirected to the **Fusion**Fabric.cloud Developer Portal Authorization Server login page. 
8. Log into **Fusion**Fabric.cloud Developer Portal Authorization Server with one of the following credentials:

| User        | Password |
| :---------- | :------- |
| `ffdcuser1` | `123456` |
| `ffdcuser2` | `123456` |

The legal entities from the **Static Data for Trade Capture** API are displayed.

9. (Optional) Click **Remove Access Token** to delete the access token from your browser session.

> To learn how to create this sample project from scratch, follow the tutorial from the [Developer Portal Documentation](https://developer.fusionfabric.cloud/documentation/sample-client-springboot).
