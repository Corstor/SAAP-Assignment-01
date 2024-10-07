# EBike Application

## Requirements and specifications

It is required to create an application that allows users to register themselves and then use one of the electric bikes that are on the environment to start a ride, moving the bike's position.

Users can registers themselves and use a bike from a remote GUI, and the administrators can create electric bikes from another remote GUI.

There should be data persistence.

Should be possible to add new functionalities available to the users dynamically.

### My specific requirements

To create persistency, it has been decided to allows the user to save its data, to load its data, and to load and change the electric bikes data.

The admin can load, save and change the electric bikes data.

The data that admin and user can change about the bikes could change (or the way to do it). <!-- TO SEE THIS POINT -->

## Architecture

The architecture chosen is a layered architecture: there will be a Presentation layer that updates itself when requested by the user, in order to stay updated with the Business layer, where there will be all the functionalities that will use the data in the Persistence layer. The Persistence layer can then save some data through the Database layer.

![C&C view of the architecture (without the Database Layer)](imgs/Uni%20-%20SAAP-Assignment-01.png)

The C&C view of the architecture without the database layer, just to see the main functions that the users and admin can do.

![C&C view of the architecture regarding the database layer](imgs/Uni%20-%20SAAP-Assignment-01-part2.png)

The part of the C&C view that shows how the database layer interact with the others (and what allows users and admin to save and load data).

An important thing to notice with this architecture is that a change on the persistance layer data does not impose a change in the database one, but instead the change is done when requested by the user/admin.