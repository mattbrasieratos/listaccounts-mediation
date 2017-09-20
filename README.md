==Orchestration Layer listAccounts API Mediation Service==
This service provides an API mediation service for the Sopra listAccounts service. It exposes 1 operation:

listAccounts - return a list of the accounts for a given customer

=Build=
This project uses a Maven build, and generates a docker container called ol001-listaccounts-mediation. To build the VM use:

mvn clean package docker:build


=Test=
This project uses Arquillian Cube to instantiate a number of docker containers and run the tests against them. You will need the listaccounts stub container (ol001-listaccounts-stub) available in your docker registry.

The tests are run via Maven, using a "test" profile:

mvn -P test test


=Run=
The services are packaged in docker containers, and can be run by starting a docker container:

docker run --name ol001-listaccounts-mediation ol001-listaccounts-mediation:0.0.1-SNAPSHOT
