# invoice-service

## Description
 - Audience: Intuit Elad and Intuit employees only
 - Author: Elad Gur
 - Date: 17.4.2021 

## How to run
`./mvnw spring-boot:run` or `maven spring-boot:run`

## Example CURL requests:
- Can be found at the `scripts` folder

## Assumptions and general notes:
- I didn't managed user credentials for to ensure that only authorized users can mutate their own invoices
- I used H2 DB for development (it's comfortable for development)
- I handled common inputs but I didn't implemented validations for all input types
- All endpoints are covered with integration tests (TDD)
- Java is not my day to day programming language but I did my best to use it in the most elegant way
    - Other programming language may produce more elegant code for this exercise but I liked the type checking 
    and the resilience of java + spring boot and also it was a pleasure for me to write some java again :)
    