# Usage JUnitBDD

*NOTE:* Only methods which are annotated with `@Scenario` will be executed during test!

    /**
     * Example of unit test, enrich with BDD functionality
     */
    @RunWith(BDDRunner.class)
    @Story(value = "Example JUnit BDD story", report = "html")
    @Narrative(as = "new Developer ",
        iWant = "to have an example that shows all the possibilities of JUnitBDD ",
        soThat = "I can apply them within my own project.")
    public class ExampleStoryTest {

        public ExampleStoryTest() {
        }

        /**
         * Example Scenario
         */
        @Scenario("Example of scenario with all possible steps")
        public void testScenario() {
            Given("Some pre-conditions");
            And("Additional given");
            When("Executing certain behavior");
            Then("Expected");
        }

        /**
         * Example Scenario
         */
        @Scenario(value = "Example of pending scenario", pending = true)
        public void testPendingScenario() {
            Given("Some pre-conditions");
            And("Additional given");
            When("Executing certain behavior");
            Then("Expected");
        }
    }