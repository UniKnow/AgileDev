# Modules

Everyone uses `Modules` but few treat them as full fledged part of the model. Code gets broken down into all sort of categories, from aspects of the technical architecture to developers work assignments.
It is a truism that there should be low coupling between `Modules` and high cohesion within them. Explanations of coupling and cohesion tend to make them sound like technical metrics, to be judged mechanically based on the distributions of associations and interactions. Yet it isn't just code being divided into `Modules`, but concepts. There is a limit to how many things a person can think about at once (hence low coupling).

*Therefore*, choose `Modules` that tell the story of the system and contain a cohesive set of concepts. Seek low coupling in the sense of concepts that can be understood and reasoned about independently of each other. Refine the model until it partitions according to the high level domain concepts and the corresponding code is decoupled as well.
Give the `Modules` names that become part of the ubiquitous language. `Modules` and their names should refelect insight into the domain.