# Helium JAVA - WORK IN PROGRESS

A reference app for using the Azure Web App for Containers service.


# Contributing

This project welcomes contributions and suggestions.  Most contributions require you to agree to a
Contributor License Agreement (CLA) declaring that you have the right to, and actually do, grant us
the rights to use your contribution. For details, visit https://cla.microsoft.com.

When you submit a pull request, a CLA-bot will automatically determine whether you need to provide
a CLA and decorate the PR appropriately (e.g., label, comment). Simply follow the instructions
provided by the bot. You will only need to do this once across all repos using our CLA.

This project has adopted the [Microsoft Open Source Code of Conduct](https://opensource.microsoft.com/codeofconduct/).
For more information see the [Code of Conduct FAQ](https://opensource.microsoft.com/codeofconduct/faq/) or
contact [opencode@microsoft.com](mailto:opencode@microsoft.com) with any additional questions or comments.

## Development

### Install dependencies

1. Clone the repository
2. Open a terminal in the local repo directory
3. [Follow CosmosDB instructions to set up KeyVault and CosmosDB](./docs/azure-infrastructure.md#create-and-setup-a-cosmosdb)
5. Run `mvn install`

#### No KeyVault

If you don't want to use KeyVault, set these environment variables:

```
export azure.cosmosdb.uri=
export azure.cosmosdb.key=
```

### Build

1. `mvn install` will compile the source files from the `src/` directory into a destination directory named `target/`.


### Test (Unit tests)

1. Create your unit test file in the `src/test/` directory.  Name your test as `*.test.java`.
2. From the project root, run `mvn test` in the terminal.
3. Output should show that your test ran.

#### Docker

```
docker run -it -p 8080:8080  helium:canary
```

#### Build

```
docker build --target=release -t helium:canary . #production
docker build --target=test -t helium:canary . #dev
```

#### Run

#### With KeyVault

##### Using Service Principal
```
docker run -it -p 8080:8080 \
  -e azure.cosmosdb.uri='https://cosmosname.documents.azure.com:443/' \
  -e CLIENT_SECRET='client_secret' \
  -e azure.keyvault.uri='https://yourkeyvault.keyvault.azure.com' \
  -e azure.keyvault.client-id='service_principal_client_id' \
  -e azure.keyvault.client-key='service_principal_password_client_password' \
  helium:canary
```

#### Without KeyVault

```
docker run -it -p 8080:8080 \
  -e azure.cosmosdb.key="your_key" \
  -e azure.cosmosdb.uri='https://cosmosname.documents.azure.com:443/' \
  helium:canary
```

## Documentation

See the docs folder in our project to get a deeper view about the project and how to set up Cosmos DB, Key Vault and other project dependencies.

- Azure Cosmos DB: https://github.com/microsoft/helium-java/docs/azure-cosmosdb.md
- Azure Key Vault: https://github.com/microsoft/helium-java/docs/azure-keyvault.md
- Deploying Helium Java in a production environment: (WIP)

### REST Endpoints

The REST endpoints are documented using the OpenAPI 3.0 standard (https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.0.md) and uses swagger-jsdoc (https://github.com/Surnet/swagger-jsdoc) to generate the swagger spec.
