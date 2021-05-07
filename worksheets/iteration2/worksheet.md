Iteration 2 Worksheet - Group 2 - Swapper
=====================





Paying off technical debt
-----------------

1. This technical debt is from fixing a SOLID violation. We needed to move some authentication from a domain object to the business layer. 
To pay it off we moved the authentication into a class called AccessUsers that is in our logic layer.
You can see it in this [commit](https://code.cs.umanitoba.ca/3350-winter-2021-a03/3350-winter-2021-A03_Group2/-/commit/a22dd13220d9c5fda79305e7c5fbe2779a5f8260) the changes we made.
This type of debt was Prudent Inadvertent. We had just learnt about SOLID the week of the due date and missed going back to check our code. We now know how we should have done it 
and in the future will try to place similar methods in better places.

2. This technical debt is from fixin a bug where after logging out of the application you would get logged in again if you pressed the back button on the phone.
To pay it off we had to write code that would broadcast that the user has logged out. So any page that would require the user to be logged in would send them back to the log in page.
you can see it in this [commit](https://code.cs.umanitoba.ca/3350-winter-2021-a03/3350-winter-2021-A03_Group2/-/commit/6deff3abedcd75284f005d2c6cf39af99b2b8dbf)
This type of debt was Prudent Inadvertent. We had not thought to test if that could happen but now that we know it can, we are able to code other pages that require the user to be logged in similarly.

## SOLID
[Group 2's Integration Segregation Violation](https://code.cs.umanitoba.ca/3350-winter-2021-a01/group-2/-/issues/42)




## Retrospective

We tried to work on all of what we said we could improve on. But we didn't follow exactly what we planned to do. For example we wanted to review GitHub Flow.
We fell into under estimating the time we needed to code and so we did make better branches, commits and we have better used merge requests. However it may not follow exactly what
GitHub Flow says to do. For example we make merge requests earlier than we should. However doing this kept us all caught up with any changes happening.

An example of us making good commits can be found [here](https://code.cs.umanitoba.ca/3350-winter-2021-a03/3350-winter-2021-A03_Group2/-/merge_requests/54/commits?commit_id=d796755cadbfbe3ef2b871aba5cb497809feb38f)

This [link](https://code.cs.umanitoba.ca/3350-winter-2021-a03/3350-winter-2021-A03_Group2/-/network/master?utf8=%E2%9C%93&extended_sha1=) will show the branches that branch off of master 

## Design Patterns

We used a singleton [when we access the database path](https://code.cs.umanitoba.ca/3350-winter-2021-a03/3350-winter-2021-A03_Group2/-/merge_requests/51/diffs?commit_id=726b74394305ff92917d6951ca65cbc49375d2fa). We make sure that there is only one database path. We don't need multiple.

The singleton design pattern can be read about [here](https://refactoring.guru/design-patterns/singleton). 

## Iteration 1 Feedback Fixes

[Log back in after logging out](https://code.cs.umanitoba.ca/3350-winter-2021-a03/3350-winter-2021-A03_Group2/-/issues/66)

The issue present was users could log back in simply by pressing the back button. This is definitely a security concern and simply wrong. It's a security issue because anyone with access to their unlocked device can tamper with the user's account, listings, and anything else that the user can normally access. The user expects to need to log back in to access their account once they log out which isn't how it worked at the time.

Looking into the problem, this is a common thing across android apps and the code to handle the problem is required in each activity class where being logged in is required to access the that activity. 

To start, we tested and created a solution for a single page through using android's `BroadCastReceiver` functionality. When the user logs out, the system broadcasts that they're logging out and any page that requires being logged in will redirect the user to the log in page. Since we wanted this available to almost all our activities, it was extracted from the original class and its own class and interface were create `Logout.java` and `ILogout.java` bf4391a4239f2ed994cd3aca51d3a6c40167b8ea. Once extracted into its own class, it was only a matter of creating an instance of it in each activity class and calling the `ensureUserLoggedIn()` method. 

