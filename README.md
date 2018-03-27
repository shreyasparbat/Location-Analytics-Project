# Location Analytics Project

A web application built by me and my team for our IS203 Software Engineering Module. This app can be used by any valid user to obtain diverse statistics of the locations of people inside a selected building. 

Users are able to select the type of analytics services (e.g., the heat map that shows how many students are located on the floor map, the three most populated seminar rooms, and the current locations of your group members) and the app shows them the corresponding information in a user-friendly way.



## Directory Structure

```dire
|-app                // App related sources, files & documentation
  |--javadocs
  |--sql
  |--src
  |--web
    |--WEB-INF
      |--lib
|- documents         // Diagrams and other documentations
|- metrics			// Bug metrics and Task metrics
  |- bm.xlxs		        
  |- tm.xlxs 	    
|- presentations    // Presentation slides
|- testing          // Test plan, Test cases, Test results
|- minutes
```



## Functionalities Overview

We were successfully able to complete the following functionality requirements of the web app (set forth by teaching team): -

1. **Login**
   - A user should be able to log in with their email ID and password. The email ID refers to the part before the @ sign of the email(<email ID>@<sch>.smu.edu.sg). You will be provided the list of valid users and passwords. There is no need to provide any form of user management (creation, deletion, change password etc.)
   - The reports are only accessible after logging in.
2. **Bootstrap**
   - The administrator can bootstrap the SLOCA system with location & demographics data.
3. **Heat map**
   - A user can see the crowd density of any floor of the SIS building on any given day and time.
4. **Basic Location Reports**
   - A user can see the following stats (for the SIS building) for any given day and time:
     - The breakdown of Year 2013/2014/2015/2016/2017 students, genders, and schools
     - The top K popular places (K should be selectable at an integer from 1 to 10)
     - The top K people who have been in the same location as a specified person (K = 1 to 10)
     - The top K next places that users visit after visiting a particular place
5. **Automatic Group Identification**
   - A user can see the list of groups (location of group and composition) at a particular timing (e.g., user 1 and 5 are in a group at location A, 2, 4, 6 are a group at location C).
     - Groups are automatically detected by SLOCA based on the users' location traces.
6. **Dual Interfaces: UI and Web Services (JSON)**
   - UI: You will need to provide a UI for users to log in and use the features specified above (except for bootstrap -- see below).
     - Note: the UI components (for all requirements including heatmap) are left to your complete discretion. It is okay to output the results in nicely formatted tables etc. However, if you are able to make the output look nicer (using maps, graphs etc.), please do so! You can download a copy of the SIS floor maps [here](https://drive.google.com/open?id=0B_WUUg0734w9RkpEWkdnc2pvTEE).
     - See the section titled "libraries and external code" for links to maps of SIS as well as graphics libraries that can help you display the crowd density (and other features) in nice ways (e.g., overlaying the information on a floor map of the SIS building and easy to use graphing features)
   - Web Services: You will also need to provide JSON APIs that allow all system functionalities to be queried programmatically. See below for the full JSON requirements



More information about the project requirements can be found on the [IS203 Wiki page](https://wiki.smu.edu.sg/is203/index.php?title=Project-for-2017&oldid=2233).