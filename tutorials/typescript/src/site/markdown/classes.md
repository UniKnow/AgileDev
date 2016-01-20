# Classes

Traditional JavaScript focuses on functions and prototype based inheritance as the basic means of building up reusable components.  Within TypeScript the Object-Oriented class based approach is enabled and during compilation it is transformed into javascript that works across all major browsers and platforms.

Let's take a look at a simple class based example:

    class Greeter {
        greeting : string;
        
        constructor(message : string) {
            this.greeting = message;
        }
        
        greet() {
            return "Hello, " + this.greeting;
        }
    }
    
    var greeter = new Greeter("TypeScript");
    
Within the example above we declare a new class `Greeter`. This class has 3 members, a property called `greeting`, a constructor, and a method `greet`.

When we refer to one of the members in the class we prepend it with `this.`. This denotes that it is a member access.

In the last line of the example we initiate a new instance of the `Greeter` class using `new`. This results into a call to the constructor we defined earlier, creating a new object with the `Greeter` shape, and running the constructor to initialize it.

## Inheritance

Classes in TypeScript (like other langauges) support single inheritance using the extends keyword as shown below:

    class Animal {
        name:string;
        
        constructor(theName: string) { 
            this.name = theName; 
        }
        
        move(meters: number = 0) {
            alert(this.name + " moved " + meters + "m.");
        }
    }
    
    class Snake extends Animal {
    
        constructor(name: string) { 
            super(name); 
        }
        
        move(meters = 5) {
            alert("Slithering...");
            super.move(meters);
        }
    }
        
    var snake = new Snake("Sammy the Python");
    snake.move();
    
The example above covers quite a bit of the inheritance features in TypeScript. The `extends` keyword is used to create a subclass `Snake` from the base class `Animal` and gain access to its features. It also shows how to override methods in the base class. In the example `Snake` implements a `move` method that overrides the `move` from `Animal` giving it functionality specific to the sub class.

## Access modifiers

TypeScript supports the common access modifiers that control the visibility of a member in the class:

* `public` - Available on instances everywhere.
* `protected` - Available on child classes but not on instances itself.
* `private` - Not available for access outside the class.