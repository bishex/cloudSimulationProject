# CloudSim Plus simulation examples

This project contains various CloudSim Plus examples, including both inherited ones from CloudSim and new ones showcasing exclusive features.  
These examples are structured with meaningful names, making it easier to understand their purpose before diving into the code.  

To get started, you can check `ReducedExample.java`, which demonstrates the minimum code required to build private cloud simulations using CloudSim Plus.  
However, this approach is not reusable. After understanding the basics, it's recommended to explore `BasicFirstExample.java`,  
which follows a structured and reusable coding approach to create private cloud simulations effectively.  



## Running Examples  

There are two ways to run the examples in this project:

### 1. Using an IDE  

- Open or import the project in your preferred IDE.  
- Locate the examples inside the `org.cloudsimplus.examples` package.  
- Run any class within this package to execute a specific example.  
- To create custom simulations, add new classes inside this project.  

### 2. Using the bootstrap script  

- Navigate to the project's root directory in a terminal.  
- Run an example using the command:  
  ```bash
  sh bootstrap.sh package.ExampleClassName

