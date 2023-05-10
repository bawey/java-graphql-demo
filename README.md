# java-graphql-demo

A project demonstrating the process of integrating GraphQL functionality into a Spring / Jersey web application

The idea: demonstrate top-level patterns without going too deep into individual aspects like advanced GQL features, serialization, de-serialization etc.

## Generate bare-bones structure

Follow [Jersey doc](https://eclipse-ee4j.github.io/jersey.github.io/documentation/latest/getting-started.html#new-from-archetype) 
to create a demo app

```bash
mvn archetype:generate -DarchetypeArtifactId=jersey-quickstart-grizzly2 \
-DarchetypeGroupId=org.glassfish.jersey.archetypes -DinteractiveMode=false \
-DgroupId=com.github.bawey -DartifactId=jersey-spring-graphql-demo -Dpackage=com.github.bawey.graphqldemo \
-DarchetypeVersion=2.39.1
```

## Integrate Spring with Jersey

https://www.baeldung.com/jersey-rest-api-with-spring
https://github.com/eclipse-ee4j/jersey/blob/2.39.1/examples/helloworld-spring-webapp/src/main/webapp/WEB-INF/web.xml

## Create a schema file
## Setup class generator
## Add a simple GQL endpoint
## Add a graphql service, following the convention: document, operationName, parameters
## Configure 
## Setup GQL configuration
- fetchers, loaders, what not

## Integrate the plugin to generate the server types

## Configure the plugin to generate some client sources
- should be generated under the test sources somehow
- test sources should spin-up an instance of Grizzly or whatever