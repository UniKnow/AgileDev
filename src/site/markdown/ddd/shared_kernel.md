# Shared Kernel

Once separate teams are working on closely related bounded contexts they may produce systems that may not fit together.

*Therefore* agree with the teams to share some subset of the domain. Keep this kernel small. Within this kernel, include, beside the subset of the model, also the subset of code and/or the database design associated with that part of the model. This shared stuff has special status, and shouldn't be changed without consulting the other team(s).
