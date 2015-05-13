# Resource Archetypes

A REST API is composed of 4 distinct resource archetypes: document, collection, store and controller. In order to communicate a clear and clean resource model to its clients, a REST API should align each resource with only 1 of these archetypes.

Each of the archetypes will be described in the sections that follow

## Document

A document resource can be compared to an object instance or database record. A document type is the base of the other archetypes; e.g. the 3 other archetypes can be viewed as specializations of the document archetype. A document archetype typically include both fields with values and links to other related resources. A document may have child resources that represent sub concepts.

With its ability to bring many different resource types together under a single parent, it is a logical candidate for a REST APIs root resource, also known as `docroot`. The example below identifies the `docroot` for the tutorial REST APIs advertising entry point:

    http://api.tutorial.rest.org

## Collection

A collection resource is a server managed collection of resources. Clients may add new resources, however the collection is in charge whether to create a new resource or not and the collection also determines the URIs of each contained resource. The URI below shows an example of a collection:

    http://.../blog/posts

## Store

A store is a client managed resource repository. A store resource lets an client put resources in, get them out, and decide when to delete them. On their own stores do not create new resources. Instead each stored resource has a URI that was chosen by a client when it was initially put into the store.

The example below shows a example to insert a post (with ID `1234`) document in the posts store:

     PUT /posts/1234

## Controller

A controller resource models a procedural concept. Controller resources are like executable functions, with inputs and return values. A REST API relies on controller resources to perform application specific actions that cannot be logically mapped to one of the standard methods (create, retrieve, update and delete).

Controller names typically appear as the last segment in a resource URI.
