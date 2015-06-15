# Software Design Principles

Software design principles represent a set of guidelines at the highest level. The design principles define the overall generic shape and structure of software applications. A level lower are the (architectural) patterns  which focus on a specific problem.

There are 4 primary symptoms that indicate that the design is rotten:

* *Rigidity* - Rigidity is the tendency for software to be difficult to change even in case of simple changes. Every change causes a cascade of subsequent changes in dependent modules.
* *Fragility* - Closely related to rigidity is fragility. Fragility is the tendency of the software to break (in many places) every time it is changed. Often the breakage occur in places that have no conceptual relationship with the area that was changed.
* *Immobility* - Immobility is the inability to reuse software from other projects or from parts of the same project.
* *Viscosity* - Viscosity comes in 2 forms: viscosity of the design, and viscosity of the environment. When changes in which the design is preserved are harder to employ than the hacks, then the viscosity of the design is high. Viscosity of environment comes about when the development environment is slow and inefficient. In this case developers are tempted to make changes that prevent a lot of changes, which are usually not optimal from a design point.

 These four symptoms are the signs of poor architecture. Within this part we will focus on software design principles by which we can prevent this.