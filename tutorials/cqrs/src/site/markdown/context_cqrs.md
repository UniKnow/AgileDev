# Context CQRS

This chapter is intended to provide some context for the main subject of this part of the tutorial: a discussion pf the Command Query Responsibility Segregation (CQRS) pattern. It is meant to explain the origins of the CQRS pattern and some of the terminology you will encounter in this tutorial and in other materials that discuss the CQRS pattern. It is important to understand that the CQRS pattern is not intended for use as the top-level architecture of your system; rather it should be applied to those modules that will gain specific benefits from the application of the pattern.

Before we dive deeper into the concepts of CQRS and its usage within a complex application, we need to introduce some of the terminology that we will use in this chapter and subsequent chapters of this tutorial. Much of this terminology comes from Domain Driven Design.

## What is Domain Driven Design

To learn more about the foundational principles of DDD, you should read [Tutorial Domain Driven Design](./../ddd/index.html).