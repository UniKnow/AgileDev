# Single Responsibility Principle

This principle states that, a subsystem, class or even function, should have only 1 reason to change. The classic example is a class that has methods that deal with business rules, reports, and persisting:

    public interface Employee {
        public Money calculatePay();
        public int reportHours();
        public void save();
    }

The problem with the interface shown above is that the functions change for entirely different reasons. The `calculatePay` function will change whenever the business rules for calculating pay change. The `reportHours` function will change whenever someone wants a different way of reporting hours. The `save` function will change whenever the database scheme is changed. These 3 reasons to change make `Employee` very volatile.

Applying the SRP principle means that we have to separate the interface into components that can be deployed independently. Independent deployment means that is we deploy 1 component we do not have to redeploy any of the others.

    public interface Employee {
      public Money calculatePay();
    }

    public interface EmployeeReporter {
      public String reportHours(Employee e);
    }

    public interface EmployeeRepository {
      public void save(Employee e);
    }

The simple partitioning shown above resolves the issue. We have to note that there is still a dependency from `Employee` to the other interfaces `EmployeeReporter` and `EmployeeRepository`. So if `Employee` is changed, the other classes will likely have to be recompiled and redeployed. We could prevent this through a careful use of the [Dependency Inversion Principle](dip).
