Project purpose. 
The main theme of the project was designing a learning platform, where teachers
can do their courses and manage report papers while the students can enroll in courses and even make study groups and meetings to connect
together. This project's aim was getting familiarized with databases, basic Object Oriented Design principles (by that I mean, 
really basic concepts, not even design patterns) and Java. The focus was mostly on making good structured databases and experimenting
things MySQL has to offer (triggers, events etc.) and practicing queries, but there was also a demand for a good user interface in Java.
It is very important to note that this project was made when we were still being introduced to basic concepts in Java and OOP and we didn't
know many things such as design patterns, Java Reflection, Design by Contract and so on

The main technologies used are Java and MySQL, connected with a JDBC connector.
MySQL - for persistent data storage and easy access. Some small functionalities are also implemented using triggers and events
Java - for application logic, query generation
Java SWING - for the user interface

This project incorporates a Multi Layered Architecture, having a controller, model, repository, service and view package
The controller is pretty self explainatory. It is where the classes that handle front-end events were grouped
The model is the Database model mapped in Java classes
The repository is where the query generation classes are stored. This includes many classes for different components from the model
Service is where the functionalities for every user type (SUPER-ADMIN,ADMIN,TEACHER,STUDENT) were implemented, mostly using the 
queries from repository
Finally, view contains all the visual windows for different application "pages" (for example, the courses page, the groups page etc.). It also
has an utility package, containing additional visual components (object panes, a custom calendar etc.)