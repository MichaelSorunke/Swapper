Iteration 1 Worksheet - Group 2 - Swapper
=====================

Adding a feature 
-----------------

One feature we added to our project was account management (https://code.cs.umanitoba.ca/3350-winter-2021-a03/3350-winter-2021-A03_Group2/-/issues/5). 
Our user stories were Create an Account (https://code.cs.umanitoba.ca/3350-winter-2021-a03/3350-winter-2021-A03_Group2/-/issues/17) 
and Password Reset (https://code.cs.umanitoba.ca/3350-winter-2021-a03/3350-winter-2021-A03_Group2/-/issues/18). 
Associated Tests invloved Adding the user to the database in UserPersistenceFDBTest 
(https://code.cs.umanitoba.ca/3350-winter-2021-a03/3350-winter-2021-A03_Group2/-/blob/master/app/src/test/java/comp3350/srsys/persistence/database/UserPersistenceFDBTest.java), 
and testing user related functions in AccessUsersTests (https://code.cs.umanitoba.ca/3350-winter-2021-a03/3350-winter-2021-A03_Group2/-/blob/master/app/src/test/java/comp3350/srsys/business/AccessUsersTest.java). 
The merge commit that completed the feature was when we merged the UI into the master branch (https://code.cs.umanitoba.ca/3350-winter-2021-a03/3350-winter-2021-A03_Group2/-/commit/db53394e2c3d8218d549de244907da50fda71e0d).

To tackle this feature we decided that we should split up UI and the logic programming between the group. 
We had to start with creating DSO's and a fake database. Once that was done we created ways in which to insert a created account into
the user database. We were able to test that shortly afterwards but at the same time the UI was being worked on seperately.
We ran into some problems with communicating to the logic layer from the UI but figured that out while writing the tests linked above.
We finished with merging into master and fixing code that was in the UI that should be in the logic layer.

Exceptional code 
----------------

On line 45 of AccessUsersTest (https://code.cs.umanitoba.ca/3350-winter-2021-a03/3350-winter-2021-A03_Group2/-/blob/master/app/src/test/java/comp3350/srsys/business/AccessUsersTest.java)
we test to see if an exception will be thrown when we try to make a new user using an email that is already in use.

What happens is we create two users that share the same email. Emails need to be unique in our application and so when we try to insert
both users, it should throw our exception.  

Branching 
----------

Our branching strategy (https://code.cs.umanitoba.ca/3350-winter-2021-a03/3350-winter-2021-A03_Group2/-/blob/master/BranchingStrategy.md)

Screenshot of our branching strategy (https://code.cs.umanitoba.ca/3350-winter-2021-a03/3350-winter-2021-A03_Group2/-/blob/master/BranchingStrategyInAction.jpg)

SOLID
-----

Link to created issue (https://code.cs.umanitoba.ca/3350-winter-2021-a03/youtilities-comp3350-a03-group3/-/issues/38)

Link to the code we refer to [Single Responsibility Principle Violation](SinglresponsibiltyViolation.jpg)

Agile Planning 
-------------- 

Originally we wanted the user to be able to browse the current listings this iteration 
as well as be able to display pictures in their listing. 
However, as we were working we ran into problems in other areas such as the communication between the UI, Business and Database layers.
These issues were fundamental to the project and as such had to be taken care of first.
For this reason we concluded that there would not be enough time to produce 
a polished version of browsing listings and displaying pictures for iteration 1 and pushed it to iteration 2.