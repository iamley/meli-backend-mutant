## meli-backend-mutant

## Guidelines for lifting the project locally

1. Clone this repository.

2. Configure version 11 of Java as JDK in the IDE.

3. Run in the project directory the "mvn clean install" command, make sure the sources were generated (Target Folder).

4. Run the main class "Application" to initialize the project.

## Guidelines for consuming the API's

1. To validate if a human is a mutant, consume the service: http://3.80.153.39:8080/v1/mutant

Request example:

```json
{
    "body": {
        "dna": [
        "ATGCGA",
        "CAGTGC",
        "TTATAT",
        "AGAAGG",
        "ACCCTA",
        "TCACTG"
        ]
    }
}
```

2. To get the mutant/human ratio statistics, consume the following service: http://3.80.153.39:8080/v1/mutant/stats



   




