= PETSTORE JAVA BACKEND PROJECT =

This PetStore project shows how to do a simple REST Java backend service.

The project let you store Pets in a repository. Every pet has some attributes
and also the location where it is located, so there is a method to retrieve
all the pets that are within a box defined by two coordinates.

The repository can be a MongoDB NoSQL or a mixed solution where the searchable
attributes are stored in Solr and the rest of the attributes are stored in
MongoDB. The repository to use can be easily configured by changing a Spring
profile.

One nice feature of MongoDB is that it is "schemaless", so every stored entity
can have its own attributes. PetStore project takes advantage of this feature
and let the user store Pets with different attributes. Service transforms
every Pet into a generic format that can be easily stored in MongoDB.

The technology stack used is:

* Jersey (REST framework)
* Spring (DI framework)
* Spring-data-mongodb (MongoDB data access)
* Spring-data-solr (Solr data access)
* Jamon (monitoring tool)
* Flapdoodle Embed Mongo (embedded MongoDB used in integration tests)
* Mockito (Mocking framework)
* Byteman (Inject code in tests)
* Concordion (Behavior driven integration tests)

= LAUNCH INTEGRATION TESTS =

Just execute the "install" goal. 

$> mvn clean install

Flapdoodle plugin should download an install an embed MongoDB. Then, Maven
Tomcat plugin will deploy the project and launch Concordion test. If every
goes well, test result will be available in _service/target/condorion_ folder.

= LAUNCH EMBEDDED TOMCAT =

To run the project in an embedded Tomcat, just execute:

$> cd . ; mvn clean package && cd service && mvn \
-Dpet-service.config=file://`pwd`/src/test/resources/pet-service.properties tomcat7:run ; cd -

Service should be available at the following URL:

http://localhost:8080/service/

= TODOS =

* Add more unit tests
* Refactor Masharllers
