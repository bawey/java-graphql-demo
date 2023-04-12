# java-graphql-demo

A project demonstrating the process of integrating GraphQL functionality into a Spring / Jersey web application

## Generate bare-bones structure

Follow [Jersey doc](https://eclipse-ee4j.github.io/jersey.github.io/documentation/latest/getting-started.html#new-from-archetype) 
to create a demo app

```bash
mvn archetype:generate -DarchetypeArtifactId=jersey-quickstart-grizzly2 \
-DarchetypeGroupId=org.glassfish.jersey.archetypes -DinteractiveMode=false \
-DgroupId=com.github.bawey -DartifactId=jersey-spring-graphql-demo -Dpackage=com.github.bawey.graphqldemo \
-DarchetypeVersion=2.39.1
```

