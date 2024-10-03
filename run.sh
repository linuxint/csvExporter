#!/bin/bash
java -cp "lib/*:sql-to-csv.jar" com.example.sqltocsv.CsvExporterApplication --spring.config.location=file:/path/to/application.properties
