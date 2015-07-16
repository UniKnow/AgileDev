#Database schema versioning

The database is a critical part of your application. If you deploy version 2.0 of your application against version 1.0 of your database you get a broken application. That's why your database should always be under source control together with your application code.

## Versioning database with Liquibase

See [Automatic DB migration for Java web apps with Liquibase](http://www.operatornew.com/2012/11/automatic-db-migration-for-java-web.html)