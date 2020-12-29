# Modern Library Application

The Modern Library Application is a web application that allows users to login, browse and checkout books. Users can additionally add books to a wishlist and checkout multiple books from their cart. The user interface is visually appealing and easy to use:

![alt text](https://github.com/UNDERHMA/ModernLibrary/blob/main/images/home.PNG)

The Modern Library Application consists of an Angular 4 front end (available in the client folder above) and a backend built using the Spring Framework (available in the server folder above). 

On the backend (server folder), the com.revature.mand.project2 package consists of the following subpackages:
- A controllers package containing controllers that use the @RestController annotation
- A dbconfig package containing a class that configures that datasource, local session factory and transaction manager to integrate Spring ORM with Hibernate
- An entities package containing Hibernate entities used for persistence
- A repositories package containing repository classes that manage persistence
- A services package containing business logic classes that are called by the controller classes and that call the repository classes
 
 Other important files/folders within the backend include the test package that contains all unit tests, the src/main/resources folder that includes database and logging configuration files and the src/main/webapp/WEB_INF folder, which includes the application-context.xml and the web.xml configuration files. The src/main/webapp folder also contains the Angular front end project, which was build into this folder by specifying the ouputPath in the angular.json file.
 
 On the front end (client folder), the src/app folder consists of:
 - Various component folders, whcih represent different views of the application
 - An app-routing module to handle webpage navigation
 - An app.module.ts file to manage dependencies
 - An api.service.ts file to manage api calls using Rxjs and HttpClient
 - An auth.service.ts to manage authentication and authorization
 - Additionally, the login.component.ts uses Bcrpyt to hash passwords, so that passwords are stored in a more secure way.
 

Revature - 1120 Java - August
Project Two - Library Application using Angular, Hibernate, Javalin, etc

Contributors: 
Javier Gonzalez,
Andre Entrekin,
Mason Underhill

Other images:

![alt text](https://github.com/UNDERHMA/ModernLibrary/blob/main/images/books.PNG)

![alt text](https://github.com/UNDERHMA/ModernLibrary/blob/main/images/login.PNG)
