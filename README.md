# Java Apache POI Example 📚

This project demonstrates how to use Apache POI to read and process Excel spreadsheets in a Spring
Boot application.

## Project Structure

- `/controller/BookController.java`: Contains the REST controller for importing and exporting books.
- `/service/BookService.java`: Contains the service logic for importing and exporting books from an
  Excel file.
- `/spreadsheet/**`: Contains classes for processing Excel spreadsheets, such as `SpreadSheetReader`
  and `SpreadSheetWriter`.

## Technologies Used
<div style="display: flex; gap: "10px">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java" /> 
  <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="Spring Boot" /> 
  <img src="https://img.shields.io/badge/Apache_POI-A8B9CC?style=for-the-badge&logo=apache&logoColor=white" alt="Apache POI" /> 
  <img src="https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white" alt="Maven" />
<div/>
  
## Features

[//]: # (Copilot, write about the features of the project here, such as the ability to import a excel file, ttransformit it into a DTO validating the fiels, using own java or for example, jakarta validation, and export files by temporary file technique or in memory)

- Import and transform Excel data into a DTO object.
- Validate the Excel file format before processing it.
- Use Jakarta Validation to validate the DTO imported from the Excel file.
- Export books to an Excel file using the temporary file technique or in-memory processing.
