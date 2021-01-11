---------------------------------------------------------------------------------
# CAR PARKING BOOKING SYSTEM
---------------------------------------------------------------------------------
# Author: Ayush Kumar
---------------------------------------------------------------------------------

This README file contains all the instructions to install and use the application.

## Prerequisites:
### 1. Must have jdk-1.8.0 or above with the respective JRE installed on the system. 
 If not installed, download and install it from the oracle website or use this link given.
https://www.oracle.com/in/java/technologies/javase/javase-jdk8-downloads.html

### 2. Must have Oracle SQL XE 11g installed on the system. 
If not installed, please download and install it from the below link before starting with the program. You can follow the link below.
https://www.youwindowsworld.com/en/downloads/database/oracle/oracle-database-express-edition-11g-release-2-windows-64-bit

### 3. Must have PATH set for the JRE installed correctly. If not, follow the below steps.
1. After the installation of JDK, go to Start menu and search for "Edit the System Environment Variables" and run it with administrator privileges.
2. Click on the "Environment variables..." button on the bottom right of the open dialog.
3. Find the "Path" variable in the "System Variables" section in the bottom half of the second dialog screen and Double click it.
4. In the next dialog box, click on "New" and enter the path of the folder where JRE is installed and press enter. For eg. "C:\Program Files\Java\jre1.8.0_231".
5. Now press "OK" close all the dialog boxes.

### _4. [!IMPORTANT] Set up the JDBC connection JAR file. Follow the steps given below:_
1. Find the "ojdbc14.jar" file in the "./Setup" folder and copy it.
2. Paste it in the foder where oracle is installed. For eg. "C:\oraclexe\".
3. Go to Start menu and search for "Edit the System Environment Variables" and run it with administrator privileges.
4. Click on the "Environment variables..." button on the bottom right of the open dialog.
5. Click on the "New" button in the "System Variables" section in the bottom half of the second dialog screen.
6. Enter the "Variable Name" as "CLASSPATH" and the "Variable Vaule" as the path to the ojdbc14.jar file. For eg. "C:\oraclexe\ojdbc14.jar".
7. Now press "OK" close all the dialog boxes.

## Installation instructions:
_NOTE: If you are not sure about how to follow the below steps, they are given as windows command prompt commands at the end of this Installation section so you can follow those._
1. Find the file "CreateDBUser.sql" in the "./Setup" folder and run it on the Oracle SQL Command line.
	Description:
		1. The script will create a user "JavaDev" with password "password".
2. Find the file "GenerateTable.sql" in the "./Setup" folder and run it on the Oracle SQL Command line.
	Description:
		1. It will create the tables "users", "parklot", and "booking".
3. Find the file "GenerateParklot.java" in the "./Setup" folder and compile and run it same as you'd do with a java program.
	Description:
		1. This program will insert the data of all the parking lots available. It will create 25 records in the parklot table created using the GenerateTable.sql script.
4. Find the file "JParkingProj.java" in the "./src" folder and compile and run it same as you'd do with a java program.
	Description:
		1. This is the main application created in the Java language.
## Commands for Command prompt:
_NOTE: Please ensure that you've completed all the Prerequisites before heading to this step._
_If you still find this difficult there is a batch file which you can run to complete these commands easily. The instructions are given after this Commands section._
### Open command prompt and enter the below commands sequentially:
	```batch
	cd..
	cd..
        cd <location to the setup folder>	::For example cd C:\Project\Parking Project\Car_Parking_Booking_System\Setup
        sqlplus / as SYSDBA			::If this asks for a password, enter the password earlier entered during the Oracle XE setup
        @CreateDBUser.sql;
        sqlplus javadev
	password				::Enter this when it prompts for password. NOTE: you will not be able to see characters as you type as they are hidden
        @GenerateTable.sql;
	javac GenerateParklot.java
	java GenerateParklot
	cd..
	cd src
	javac JParkingProj.java
	java JParkingProj			::This will finally run the program
        ```
##  Batch File execution:
_NOTE: You will still have to enter some commands but lesser than the above._
1. Go to the "./Setup" folder and run the "Run.bat". 
 1. Enter "`@CreateDBUser.sql`" when it first prompts as "SQL>".
 2. Enter "`password`" when it second prompts as "Enter password:".
 3. Enter "`@GenerateTable.sql`" when it third prompts  as "SQL>".
_The program will automaticaly run after this._
