# Consumer-based testing

Consumer based testing is counter intuitive as it relies on the consumer writing tests for the producer. When writing contract tests, a consumer writes a test against a service it uses to confirm that the service contract satisfies the consumer needs.

By doing this we enable a neat trick in the deployment pipeline. After a service passes its internal build and QA process, all services and consumers go through a unified integration test stage. It could be triggered by the consumer changing tests, or the producer committing changes to the service. During this stage each consumer runs its tests against the new version of the changed service. Any failure prevents the new version from progressing in the pipeline.

Now for example, if we would have a `Order` Service that depends on the `Product` and `Billing` service, a new build of `Order` would trigger the execution of its consumer based tests against the latest version of `Product` and `Billing` service to pass the integration test stage.

![Example pipeline consumer based testing](images/example-pipeline-consumer-based-testing.png)

Triggering only tests that are associated with a particular change can get tricky, however you can go a long way by simply running all contract tests each time a new service is deployed to the integration test stage within the pipeline.

![Sample test run consumer based test](images/sample-test-run-consumer-based-test.png)

This would mean that the consumer based tests of billing would be included (gray arrow), which aren't relevant to the change that was introduced.

Assuming that all the tests pass, we have a set of services that have been proven to work together. We can record the set of them working together by creating a Deployable Artifact Set (DAS). This DAS can become the single deployable artifact for higher stages within the deployment pipeline, or it can become a compatibility reference.

![Info](images/info-icon.png) The DAS could become the input for creating a container that could be deployed to other test environments.

## See also

* [Service connector] - Since all services are accessed through a service connector the consumer based tests to verify whether the service satisfies the needs of the consumer will be included in the service connector.

