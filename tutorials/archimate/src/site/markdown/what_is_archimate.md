# What is ArchiMate.

ArchiMate is a modeling language for describing enterprise architectures. It presents a set of elements within and relationships between architecture domains, and offers a uniform structure for describing the content of these domains.

ArchiMate is built from 3 types of elements:

1. Active elements - elements that act.
2. Behavioral elements - elements that represent behavior of active elements.
3. Passive elements - elements on which is acted by the behavioral elements.

The 3 element types, connected by relations, form some sort of sentences. A pickpocket (active element) steals (behavioral element) a wallet (passive element).

The most important elements of ArchiMate are shown below.

![concepts of ArchiMate](images/concepts-archimate.png)

## Application Component
The application component represents the 'actor' that an application in your enterprise architecture landscape is.

## Application Function
The application function stands for the behavior of the application component and shows how the application can act.

## Application Interface
The application interface stands for the route via which the application exposes itself to the business or to other applications.

## Business Interface
The business interface models the way the role interacts with others. You can think of it as a 'channel', (phone, mail, meeting, etc.).

## Business Object
A abstract element that is created or used by a business process. Think of a business object like 'a payment' or 'a bank account'. Though it is called business object, it's more like a concept.

## Business Process
A business process stands for a set of related activities together realize services or create elements. Business roles can be assigned to a business process, they perform the process, just as the application component performs the application function. A business process cannot exist without a business role.

## Business Role
The business role is an abstract sort of actor being responsible for certain behavior. Business roles can perform business processes.

## Data object
The data object is what the application function acts upon. The application function might create, read, write, update or delete the data object. As soon as the data object becomes visible to other parts of your landscape, or when it is persistent it should be modeled.

## Services
Services play a central role in the relation between domains.
Services can be of different nature and granularity, they can be provided by organizations to their customers, by applications to business processes, or by technological facilities (e.g. communication networks) to applications.

### Application Service
The application service stands for the visible behavior of the application; e.g. how the application interface can act for a client.

### Business Service
The business service represent the service the are offered by the company to others, (either inside or outside the company). A company might offer many services.

## Layers

A layered view provides a natural way to look at service oriented models. The higher layers use services that are provided by the lower layers. Within ArchiMate three main layers are distinguished:

1. The business layer offers products and services to external customers. The offered products and services are realized in the organization by business processes performed by business actors and roles.
2. The application layer supports the business layer with application services which are realized by application components.
3. The technology layer offers infrastructural services (e.g. processing, storage, communication services) needed to run applications, and realized by servers, communication hardware, and system software.

## Relations

### Access
The access relation always depicts a behavioral element accessing a passive element. The arrowhead is optional and it may depict read access or write access, (e.g. two for read/write).

### Assignment
This type of relation has multiple meaning in ArchiMate. One of the meanings is that one side (active element) performs the behavior that is represented by the behavioral element on the other side.

### Composite
The composite relation means that the element at the end with the diamond is the parent of the element on the other end, and that the child cannot exist independently from the parent.

### Realization
Realization has 2 usages within ArchiMate. It either means that the element at the end without the arrowhead is the element that creates the element at the other end. So for example, the application function realizes a application service, which is the externally usable functionality of the application.
Or it means that it realizes a particular business object.

### Used by
The used by relation means that the element at the end without the arrowhead is used by the element at the end with the arrowhead.