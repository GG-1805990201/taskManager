# taskManager
Task Manager WebApp which has the following User Stories implemented:

User Story 1 - Create Task
An API to create a task in the database. Data points to be taken as input are as follows:
● Task Title
● Task ETA (Tentative date of completion)
○ If user doesn’t input value, default to 1 week from creation date
○ Format: dd/mm/yyyy
● Task Status (Fixed Options: Pending, In Progress, In Review and Complete)
○ If user doesn’t input value, default to be Pending

User Story 2 - Retrieve Task
An API to retrieve task details upon providing Task ID as input. Following details to be
output:
● Task Title
● Task ETA
● Task Status

User Story 3 - Retrieve All Tasks
An API to retrieve task details of all tasks. Following details to be output:
● Task ID
● Task Title
● Task ETA
● Task Status

User Story 4 - Update Task
An API to update any of the following attributes of the task by providing task ID as input:
● Task Title
● Task ETA
● Task Status

User Story 5 - Audit Trail
An API to provide details of all updates made to a task with task ID as input. Following
details to be provided:
● Task Title
● Updated Timestamp
● Updated Fields
● Old Value
● New Value

User Story 6 - Delete Task
An API to delete a task with Task ID as input.


## API Endpoints
| HTTP Verb | Endpoints | Action |
|  :---:         |     :---:      |           :---: |
| POST     | /createTask      | Post Endpoint to create task and saving to database.    |
| GET     | /getAllTasks        | Get Endpoint to fetch all tasks from DB       |
| GET     | /getTaskById       | Get Endpoint to fetch task for the given TaskId      |
| PUT     | /updateTaskById       | Put endpoint to update the existing task attributes for the given taskId.      |
| GET     | /getAuditDetails       | Get Endpoint to fetch AuditDetails for the given taskId.    |
| DELETE     | /deleteTaskById       | Delete endpoint which deletes the particular task with taskId as input.      |

## Testing
The APIs can be tested by running the spring application, by building and executing the project jar.
Below steps describe how to build the spring project.

### Building Spring Project

Ensure that all the required dependencies (JDK17, and Maven) are installed.

To build the Spring Project, execute the following command, in the same directory as pom.xml
```
mvn clean package
```
If the build succeeds, a jar file in ./target directory should be created.

> Example : taskmanager 0.0.1-SNAPSHOT.jar

If the build has succeeded, you can proceed to next step, i.e. starting the web server

### Starting a web server
To start the webserver, execute the jar, by running the following

```
java -jar taskmanager 0.0.1-SNAPSHOT.jar
```

A Tomcat Server should spin up and start listening for requests on port 8081

