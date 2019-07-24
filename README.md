unit-springboot-sample
======================

Sample application using the [unit](https://github.com/patientsknowbest/unit) 
library and spring boot together. 

Demonstrates an application with several units: 
* `HelloController` is a spring `RestController`, as well as a `Unit`
* `SampleHttpApiResource` is a `Unit` wrapper around an HTTP API client. 
* `MutableConfig` is a mutable configuration store, holding some runtime-mutable config for `SampleHttpApiResource`
* `HealthCheckController` provides an endpoint to display the system state in DOT graph format.

Import the project into Intellij (or IDE of your choice supporting Spring Boot)
and run the `Application` class.

The application will run an http server on port 8080. 
Access /healthcheck to see the graph of all units & their state. 
Post to /setconfig to change the configuration value, which updates 
the configuration & causes dependents to restart.  


