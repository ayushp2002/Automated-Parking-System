*********************************************************************************
*************************CAR PARKING BOOKING SYSTEM******************************
*********************************************************************************
*****************************Author: Ayush Kumar*********************************
*********************************************************************************

This README file contains all the instructions to install and use the application.

Prerequisites:
  1. Must have jdk-1.8.0 or above with the respective JRE installed on the system. 
     If not installed, download and install it from the oracle website or use the
     link given below.
     #
     https://www.oracle.com/in/java/technologies/javase/javase-jdk8-downloads.html
     #

  2. Must have Oracle SQL XE 11g installed on the system. If not installed, please 
     download and install it from the below link before starting with the program.
     #
     https://www.youwindowsworld.com/en/downloads/database/oracle/oracle-database-express-edition-11g-release-2-windows-64-bit
     #

  3. Must have PATH set for the JRE installed correctly. If not, follow the below steps.

     i. After the installation of JDK, go to Start menu and search for "Edit the System
        Environment Variables" and run it with administrator privileges.
    ii. Click on the "Environment variables..." button on the bottom right of the open dialog.
   iii. Find the "Path" variable in the "System Variables" section in the bottom half of the
        second dialog screen and Double click it.
    iv. In the next dialog box, click on "New" and enter the path of the folder where
        JRE is installed and press enter. For eg. "C:\Program Files\Java\jre1.8.0_231".
     v. Now press "OK" close all the dialog boxes.

  4. [!IMPORTANT] Set up the JDBC connection JAR file. Follow the steps given below:
     
     i. Find the "ojdbc14.jar" file in the "./Setup" folder and copy it.
    ii. Paste it in the foder where oracle is installed. For eg. "C:\oraclexe\".
   iii. Go to Start menu and search for "Edit the System Environment Variables" and 
        run it with administrator privileges.
    iv. Click on the "Environment variables..." button on the bottom right of the open dialog.
     v. Click on the "New" button in the "System Variables" section in the bottom half of the
        second dialog screen.
    vi. Enter the "Variable Name" as "CLASSPATH" and the "Variable Vaule" as the path to the
        ojdbc14.jar file. For eg. "C:\oraclexe\ojdbc14.jar".
     v. Now press "OK" close all the dialog boxes.

Installation instructions:
**   NOTE: If you are not sure about how to follow the below steps, they are given as 
           windows command prompt commands at the end of this Installation section so 
 	   you can follow those.							**

  1. Find the file "CreateDBUser.sql" in the "./Setup" folder and run it on the
     Oracle SQL Command line.

     Description:
     i. The script will create a user "JavaDev" with password "password".

  2. Find the file "GenerateTable.sql" in the "./Setup" folder and run it on the
     Oracle SQL Command line.
     
     Description:
     i. It will create the tables "users", "parklot", and "booking".
**    ii. It will insert some dummy data in the users table having 5 records.	* Removed this now **

  3. Find the file "GenerateParklot.java" in the "./Setup"
     folder and compile and run it same as you'd do with a java program.

     Description:
     i. This program will insert the data of all the parking lots available.
        It will create 25 records in the parklot table created using the 
        GenerateTable.sql script.

  4. Find the file "JParkingProj.java" in the "./src"
     folder and compile and run it same as you'd do with a java program.

     Description:
     i. This is the main application created in the Java language.

  Commands for Command prompt:
**    NOTE: Please ensure that you've completed all the Prerequisites before
            heading to this step.						**

**    If you still find this difficult there is a batch file which you can run
      to complete these commands easily. The instructions are given after this
      Commands section.								**

    Open command prompt and enter the below commands sequentially:
	cd..
	cd..
        cd <location to the setup folder> ** For example cd C:\Project\Parking Project\Car_Parking_Booking_System\Setup **
        sqlplus / as SYSDBA	** If this asks for a password, enter the password earlier entered during the Oracle XE setup **
        @CreateDBUser.sql;
        sqlplus javadev
	password	** Enter this when it prompts for password. NOTE: you will not be able to see characters as you type as they are hidden **
        @GenerateTable.sql;
	javac GenerateParklot.java
	java GenerateParklot
	cd..
	cd src
	javac JParkingProj.java
	java JParkingProj	** This will finally run the program **
        
  Batch File execution:
**    NOTE: You will still have to enter some commands but lesser than the above.  **
    
    1. Go to the "./Setup" folder and run the "Run.bat". 
       i. Enter "@CreateDBUser.sql" when it first prompts as "SQL>".
      ii. Enter "password" when it second prompts as "Enter password:".
     iii. Enter "@GenerateTable.sql" when it third prompts  as "SQL>".
**    The program will automaticaly run after this.  **