# Builder

Purpose of the builder pattern is to reduce the number of parameters required for the construction of an instance.

Within the builder pattern we define a static inner class `Builder`, whose job is to collect the parameters and then construct the object in one fell swoop

    public class Pizza {
      private int size;
      private boolean cheese;
      private boolean pepperoni;
      private boolean bacon;

      public static class Builder {
        //required
        private final int size;

        //optional
        private boolean cheese = false;
        private boolean pepperoni = false;
        private boolean bacon = false;

        public Builder(int size) {
          this.size = size;
        }

        public Builder cheese(boolean value) {
          cheese = value;
          return this;
        }

        public Builder pepperoni(boolean value) {
          pepperoni = value;
          return this;
        }

        public Builder bacon(boolean value) {
          bacon = value;
          return this;
        }

        public Pizza build() {
          return new Pizza(this);
        }
      }

      private Pizza(Builder builder) {
        size = builder.size;
        cheese = builder.cheese;
        pepperoni = builder.pepperoni;
        bacon = builder.bacon;
      }
    }

Note that `Pizza` is immutable and that parameter values are all in a single location. Because the `Builder`'s setter methods return the `Builder` object they can be chained:

    Pizza pizza = new Pizza.Builder(12)
                           .cheese(true)
                           .pepperoni(true)
                           .bacon(true)
                           .build();

This result in code that is easy to write and easy to understand. The `build()` method could be modified to check parameters after the `Pizza` instance has been instantiated and throw an `IllegalStateException` if an invalid value has been supplied.

## When to use the builder pattern

We all have seen constructor where each version adds a new optional parameter:

    Pizza(int size) { ... }
    Pizza(int size, boolean cheese) { ... }
    Pizza(int size, boolean cheese, boolean pepperoni) { ... }
    Pizza(int size, boolean cheese, boolean pepperoni, boolean bacon) { ... }

This is called the telescoping constructor pattern. The problem with this pattern is that once constructors are 4 or 5 parameters long it becomes difficult to remember the required order of the parameters, as well as what particular constructor you might want in a given situation.

 An alternative you have to the telescoping constructor pattern is the JavaBean pattern where you call a constructor with the mandatory parameters and then call any optional setter after:

    Pizza pizza = new Pizza(12);
    pizza.setCheese(true);
    pizza.setPepperoni(true);
    pizza.setBacon(true);

The problem here is that, because the object is created over several calls, it may be in an inconsistent state partway through its construction. This also requires a lot of extra effort to ensure thread safety.