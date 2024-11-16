

# Test Server API
The only current goal of `Test Server API` is to test IB's Retry-After header 
handling (status codes 429 and 503) for HTTP datasource (Auth Type: Username/Password) 
and its rule(s) against a HTTP endpoint whose behavior depends on the JSON payload passed 
in the IB rule action's body.


## Development setup

1. Make sure you have these installed and running locally:
    1. Java SDK 21
    2. Maven 3
2. Building
    1. run `mvn clean install`
3. Deployable artifact
    1. under the project root folder, the deployable jar fils is `target/test-server-api-0.0.1.jar`
    2. You can provide this .jar file to the infrastructure personnel to deploy it to a server like `bearcat.intapp.net`
4. running
    1.  run `java -jar test-server-api-0.0.1.jar`

## Using this REST server in IB
5. Please see test cases for JIRA ticket RB-9529 (https://intapp.atlassian.net/browse/RB-9529)
