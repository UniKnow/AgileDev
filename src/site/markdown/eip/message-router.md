# Message Router

A message router consumes a [Message](message.md) from a [Message Channel](message-channel.md) and republish it to a different message channel depending on a set of conditions.

A key property of the message router is that it does not modify the message content. It only concerns itself with the destination of the message.