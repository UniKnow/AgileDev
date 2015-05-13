# CQRS and REST

This article demonstrates an approach for building a RESTful API on top of a CQRS based system.

## CQRS prototype.

Within this article we use a simplistic inventory management model. You can create a new inventory items, rename, or de-activate them. De-activated items cannot be viewed anymore but lists of all active items can be retrieved and we can drill down into the details of each item. You can also check-on or check-out stock against these inventory items specifying the number of stock added or removed. In other words, we build up stock and then start using it.

Viewing the list of items or details of an item is achieved through queries which are synchronous. Any changes to the state are achieved using commands.

Commands and queries are stored separately and are each handled by completely different parts of the system.

TODO: Create CQRS prototype.

## Creating a REST API

The most important responsibility of the REST API is to model the underlying domain as resources and expose it with HTTP semantics. During this process we create a public domain which is composed of resources and commands.

In general you should NOT expose the underlying domain because you want to be able to change your domain objects without having to change the public facing API. Same thing the other way around, if changes are required to the public API, you don't want to change your domain as well.

**TODO: refactor rest of article since we want to use verbs as command instead of payload and command header**

### Resources

We will expose the domain via single resource at `api/InventoryItem`. Each inventory item will be sitting at `api/InventoryItem/{id}`.

|Method|URL|Payload|In/Out|Expected result|
|---|---|---|---|---|
|`POST`|`/api/InventoryItem`|`CreateInventoryItem`|IN|Create new inventory item|
|`GET`|`/api/InventoryItem`|`InventoryItemCollection`|OUT|Returns all inventory items|
|`POST`|`/api/InventoryItem/{id}`|`RenameInventoryItem`|IN|Renames an existing inventory item|
|`DELETE`|`/api/InventoryItem/{id}`|`DeactivateInventoryItem`|IN|De-activates existing inventory item|
|`POST`|`/api/InventoryItem/{id}`|`RemoveItemsFromInventory`|IN|Removes a number of items from the stock|
|`POST`|`/api/InventoryItem/{id}`|`AddItemsToInventory`|IN|Adds a number of items to the stock|
|`GET`|`/api/InventoryItem/{id}`|`InventoryItemDetail`|OUT|Returns inventory item details|

We have 2 queries `GetInventoryItems` and `GetInventoryItemDetails`. They are exposed with `GET` methods at `/api/InventoryItem/{id}`.

`GetInventoryItems` retrieves a list of names and ids as JSON or XML depending on the `accept` header. The output message is implemented by the `InventoryItemCollection`.
`GetInventoryItemDetails` returns details of a single item. This includes id, name, and currentCount.

Queries naturally map to `GET` while commands need to be mapped to `POST`, `PUT`, `DELETE`. One common approach is using RPC-style resources, for example `/api/InventoryItem/{id}/rename`. While this might be tempting, it is against REST resource oriented presentation. The command in the payload of the HTTP message should be enough to express any arbitrary action. However, relying on the body of the message has problems of its own since the body is usually delivered as a stream and buffering the body in its entirety before identifying the action is not always possible, nor wise. Here we present an approach based on level 4 (domain model) of [5LMT](http://byterot.blogspot.co.uk/2012/12/5-levels-of-media-type-rest-csds.html) where the command type is presented as a parameter of the `contentType` header.

This is required to channel the request to the appropriate handler on the service.

TODO:
* Include location header.
* Include Content-Type header [Five levels of Media Type](http://byterot.blogspot.co.uk/2012/12/5-levels-of-media-type-rest-csds.html) (5LMT)
* Include `if-unmodified-since`, `if-match` and `ETag`.

## Resource

* [Exposing CQRS through a RESTful API](http://www.infoq.com/articles/rest-api-on-cqrs)
* [m-r example in c#](https://github.com/gregoryyoung/m-r)
* [CQRS API](http://www.slideshare.net/fatmuemoo/cqrs-api)
