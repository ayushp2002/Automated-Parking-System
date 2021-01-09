*********************************************************************************
*************************CAR PARKING BOOKING SYSTEM******************************
*********************************************************************************
**********Author: Ayush Kumar****************************************************
*********************************************************************************

This README file contains all the instructions to install and use the application.


Installation instructions:
  1. Find the file "GenerateTable.sql" in the "./Car_Parking_Booking_System/Setup"
     folder and run it on the Oracle SQL Command line.
     
     Description:
     i. The script will create a user "JavaDev" with password "password".
    ii. It will create the tables "users", "parklot", and "booking".
   iii. It will insert some dummy data in the users table having 5 records.

  2. Find the file "GenerateParklot.java" in the "./Car_Parking_Booking_System/Setup"
     folder and compile and run it same as you'd do with a java program.

     Description:
     i. This program will insert the data of all the parking lots available.
        It will create 25 records in the parklot table created using the 
        GenerateTable.sql script.

  3. Find the file "JParkingProj.java" in the "./Car_Parking_Booking_System/src"
     folder and compile and run it same as you'd do with a java program.

     Description:
     i. This is the main application created in the Java language.