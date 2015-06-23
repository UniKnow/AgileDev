# Timeouts

By defining timeouts we can specify how long to wait before a downstream system can be regarded as down. When the timeout is chosen too long you can slow down the whole system. Timeout too quickly, and you might have considered a call that might have worked as failed. Having no timeouts at all, and a downstream system being down could hang your whole system.

Put timeouts on all downstream calls and log when timeouts occur, and if required change them accordingly.