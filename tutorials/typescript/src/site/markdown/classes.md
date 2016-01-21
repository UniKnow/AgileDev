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

IMPORTANT: At runtime (in the generated JS) these have no significance but you will get compilation errors when used incorrectly.

In TypeScript, each member is `public` by default.

## Statics

Class members declared using the `static` keyword are going to be members of the constructor function. This means you are able to call them without instantiating a class first. Instance members on the other hand can then not be called from `static` members.

Static members can be declared using all scope modifiers, public, private and protected.    

You can have `static` members as well as `static` functions.

    class Something {
        static instances = 0;
        constructor() {
            Something.instances++;
        }
    }
    
    var s1 = new Something();
    var s2 = new Something();
    console.log(Something.instances); // 2
        
## Abstract

`abstract` members are commonly used as a means of providing a contract for some functionality a child class must provide. `abstract` classes cannot be directly instantiated. Instead the user must create some `class` that inherit from the `abstract class`.

    abstract class Animal {
        constructor(protected name: string) { }
    
        abstract makeSound(input : string) : string;
    
        move(meters) {
            alert(this.name + " moved " + meters + "m.");
        }
    }
    
    class Snake extends Animal {
        constructor(name: string) { super(name); }
    
        makeSound(input : string) : string {
            return "sssss"+input;
        }
    
        move() {
            alert("Slithering...");
            super.move(5);
        }
    }
    
## Constructor

The constructor is the function that is called when a new instance of the class is created.

## Accessors

Like most programming languages, TypeScript has getter and setter methods, also known as Accessors. These accessors are functions enabling you more control over access to an objects members. The keywords used are `get` and `set`, followed by the accessor name. The setter takes a parameter of a type and the getter has no parameter, just a return type. Accessors can be used from within other accessors.

Scope modifiers are optional, omitting them means that the accessors are public. One gotcha is that accessors for a member name must have the same scope, this means you can’t for example have a public `get` and private `set`.

    class CustomerLong extends CustomerLong
    {
      private _id: number;

      get Id(): number {
        return this._id
      }
      
      set Id( value: number ) {
        this._id = value;
      }

    }

