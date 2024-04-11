# Invoice Project

This project is designed to generate and store invoices in different formats, including PDF, HTML, and Excel. It shows how to generate and retrieve invoices using a Spring Boot web application.

## Getting Started

### Prerequisites

- Java 11 or later
- Maven 3.6.3 or later
- MySQL 8.0 or later



## API endpoints

The following API endpoints are available:

* `POST /invoice/generate` - create a new invoice and stores locally using the json input of a specific pattern

```json
{
    "buyer": "John Doe",
    "buyerAddress": "123 Main St",
    "buyerGstin": "GSTIN123",
    "items": [
        {
            "name": "Item 1",
            "rate": 123.0,
            "quantity": "2 Nos",
            "amount": 10.0
        },
        {
            "name": "Item 2",
            "rate": 123.0,
            "quantity": "2 Nos",
            "amount": 20.0
        }
    ],
    "seller": "Acme Corp",
    "sellerAddress": "456 High St",
    "sellerGstin": "GSTIN456"
}
```
 
* `GET /invoice/download/?documentId={documentId}&displayFormat={displayFormat}` - get the document for an invoice by ID in the specified display format

## API documentation

Swagger UI is integrated into the project to provide API documentation. You can access the Swagger UI by navigating to `http://localhost:8084/swagger-ui.html` in your web browser.

## Usage

### Running the Application

1. Clone the repository to your local machine.
2. Open the project in your preferred IDE.
3. Make sure you have MySQL installed and a MySQL server running.
4. Copy the `invoice.jrxml` file to your local storage and provide its value in `application.properties` file look for property `company.reports.jrxml.path`
5. Provide directory path of your local storage value in `application.properties` file look for property `company.reports.location`, Make sure this is where you want the invoices to be generated.  
4. Modify the `application.properties` file located in the `src/main/resources` directory to include your MySQL username and password.
5. Create a MySQL schema with name as `company_documents` or add its url to `application.properties` file's property  `spring.datasource.url`.
6. Create this table in the newly created schema: 

```
CREATE TABLE `invoice` (
  `sha_key` varchar(64) NOT NULL,
  `doc_uuid` varchar(36) NOT NULL,
  `json_data` mediumtext DEFAULT NULL,
  `xls_exists` tinyint(4) DEFAULT 0,
  `xlsx_exists` tinyint(4) DEFAULT 0,
  `csv_exists` tinyint(4) DEFAULT 0,
  `html_exists` tinyint(4) DEFAULT 0,
  PRIMARY KEY (`sha_key`)
);
```
finally Build and run the application.


## Sample application properties file 

```
server.port=8084
spring.mvc.pathmatch.matching-strategy = ANT_PATH_MATCHER
#invoices will be created here
company.reports.location=D:/invoices
#path to jrxml file 
company.reports.jrxml.path=D:/invoices/invoice.jrxml
#DB config
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/company_documents?useSSL=false&useUnicode=yes&characterEncoding=UTF-8
spring.datasource.username=root
spring.datasource.password=HEHHEHEHEHEEH
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update

```
## Technology stack

The following technologies were used in this project:

* Java 11
* Spring Boot 2.5.0
* MySQL 8.0.23
* Maven 3.6.3
* JUnit 5
* Mockito 3.9.0
* Swagger 2.9.2

## Architecture
This project is built using Spring Boot and follows a RESTful API design. Any front-end framework can be used to communicate with the back-end through HTTP requests.

## Contributing
If you'd like to contribute to this project, please follow these steps:

1. Fork this repository.
2. Create a new branch with your feature or bug fix.
3. Make your changes and commit them with descriptive commit messages.
4. Push your changes to your fork.
5. Create a pull request to merge your changes into the main repository.
