# Welcome

This sample client application demonstrates the implementation of the OAuth2 Client Credentials authorization grant flow for FusionFabric.cloud.

**To run this sample**

1. Register an application on [**Fusion**Fabric.cloud Developer Portal](https://developer.fusionfabric.cloud), and include the [Referential Data*](https://developer.fusionfabric.cloud/api/referential-v1-353f3933-c305-4898-88d5-cd6cd167f745/docs) API. Use `*` as the reply URL.
2. Clone the current project.
3. Copy `src/main/resources/application.yml.sample` to `src/main/resources/application.yml`, open it, and enter  `<%YOUR-CLIENT-ID%>`, and `<%YOUR-SECRET-KEY%>` of the application created at the step 1.  

> The value for `finastra.oauth2.client.accessTokenUri` is provided by the [Discovery service](https://developer.fusionfabric.cloud/documentation/oauth2-grants#discovery-service) of the **Fusion**Fabric.cloud Developer Portal.

4. (Optional) If you want to use private key authentication, instead of the standard authentication based on secret value, follow [the steps from the documentation](https://developer.fusionfabric.cloud/documentation/oauth2-grants#jwk-auth-procedure) to sign and upload a JSON Web Key to your application, and save the private RSA key in a file named **private.key**. Edit `application.yml` as follows:
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
7. (Optional) Click **Get Data** to retrieve list of currencies from the **Referential Data** API. 

 
