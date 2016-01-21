# Publish-Subscribe channel

The Publish-Subscribe channel delivers a copy of a particular event to each receiver.

A Publish-Subscribe channel works like this:

* It has 1 input channel that splits into multiple output channels, one for each subscriber.
* When an event is published into the channel, the publish-subscribe channel delivers a copy of the message to each of the output channels.
* Each output channel has only 1 subscriber, which is only allowed to consume a message once. In this way, each subscriber only gets the message once and consumed copies disappear from their channels.