# Value Objects

*Many objects have no conceptual identity. These objects describe some characteristic of a thing.*

Tracking the identity of `Entities` is essential, but attaching identity to other objects can hurt system performance, add analytical work, and muddle the model by making all object look the same. Software design is a constant battle with complexity. We must make distinctions so that special handling is applied only where necessary.
However if we think of this category of objects as just the absence of identity , we haven't dded much to our toolbox or vocabulary. In fact, these objects have characteristics of their own, and their own significance to the model. These are the objects that describe things.

*Therefore*, when you care only about the attributes of an element of the model, classify it as a `Value Object`. Make it express the meaning of the attributes it conveys and give it related functionality. Treat the `Value Object` as immutable. Don't give it any identity and avoid the design complexities necessary to maintain `Entities`.