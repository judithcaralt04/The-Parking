1. REQUIREMENTS
   - Java JDK 11 or higher
   - MySQL Server 8.0 or higher
   - IntelliJ IDEA (recommended)
   - mysql-connector-java-8.0.23.jar (included in /lib)

2. DATABASE SETUP
   - Open phpMyAdmin or MySQL Workbench
   - Create a new database (e.g. parking_db)
   - Import the file Database/parking_db.sql
   - This will create all tables automatically

3. CONFIGURATION
   - Open config.json in the project root
   - Set the following fields:
     · dbHost: "localhost"
     · dbPort: 3306
     · dbName: "parking_db"
     · dbUser: "root"
     · dbPassword: ""
     · adminPassword: password for admin login the actual is "admin1234"
     · simulatedVehicleDelay: seconds between simulations the actual is 10

4. RUN THE PROJECT
   - Open the project in IntelliJ IDEA
   - Make sure mysql-connector-java-8.0.23.jar is in the classpath
   - Run Main.java

5. LOGIN
   - Admin: username "admin" + password set in config.json
   - Regular user: register from the login screen