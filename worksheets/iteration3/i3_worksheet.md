What technical debt has been cleaned up
========================================

We were violating the single responsibility principle by having both database access and user authentication in the same class as in [here](https://code.cs.umanitoba.ca/3350-winter-2021-a03/3350-winter-2021-A03_Group2/-/blob/f00cf4086486e037bfad21f3a004b2d21d6ef554/app/src/main/java/comp3350/srsys/business/AccessUsers.java#L63). We split the classes apart into two separate classes, [AuthenticateUser.java](https://code.cs.umanitoba.ca/3350-winter-2021-a03/3350-winter-2021-A03_Group2/-/blob/3ffd7e747e6b64191bf646ef4c1c8c0eaa9291dc/app/src/main/java/comp3350/srsys/business/AuthenticateUser.java) and [AccessUsers.java](https://code.cs.umanitoba.ca/3350-winter-2021-a03/3350-winter-2021-A03_Group2/-/blob/3ffd7e747e6b64191bf646ef4c1c8c0eaa9291dc/app/src/main/java/comp3350/srsys/business/AccessUsers.java). With this, these two classes only had one reason to change each.

What technical debt did you leave?
==================================

We would've really liked to see [exception throwing handled in its own set of classes ](https://code.cs.umanitoba.ca/3350-winter-2021-a03/3350-winter-2021-A03_Group2/-/issues/77), but we ran into problems when integration testing it and couldn't finish it prior to other work needing to be done. Leaving the code as is leaves duplicate code in classes where they check for the same exceptional data. This debt would be a <u>Single Responsibility Principle violation</u> where each class will change if the exceptional behavior changes, or if there primary reason to change does change. For instance, in [AccessUsers.java](https://code.cs.umanitoba.ca/3350-winter-2021-a03/3350-winter-2021-A03_Group2/-/blob/e4bc7977432f7c828233a86bb50784f32bc12536/app/src/main/java/comp3350/srsys/business/AccessUsers.java), it will change if we change how we handle exceptional behavior or if we change how we access Users in the database.

Discuss a Feature or User Story that was cut/re-prioritized
============================================

We were hoping to complete [searching for items](https://code.cs.umanitoba.ca/3350-winter-2021-a03/3350-winter-2021-A03_Group2/-/issues/7) in this iteration, but instead prioritized completing [swap listings](https://code.cs.umanitoba.ca/3350-winter-2021-a03/3350-winter-2021-A03_Group2/-/issues/92) so our app was at least a complete prototype what it can become. It was more important for us since we decided it was one of the primary functionalities of the app, and the app wouldn't be compete without it.

Acceptance test/end-to-end
==========================

The first, and of course most difficult, automated acceptance test to write was our [create an account](https://code.cs.umanitoba.ca/3350-winter-2021-a03/3350-winter-2021-A03_Group2/-/blob/e4bc7977432f7c828233a86bb50784f32bc12536/app/src/androidTest/java/comp3350/srsys/presentation/CreateAccountAcceptanceTest.java) acceptance test. We tested creating an account starting from as fresh an install as possible.

​	The first reason the test is flaky is if the email used for testing already existed. This was handled by [deleting it](https://code.cs.umanitoba.ca/3350-winter-2021-a03/3350-winter-2021-A03_Group2/-/blob/e4bc7977432f7c828233a86bb50784f32bc12536/app/src/androidTest/java/comp3350/srsys/presentation/CreateAccountAcceptanceTest.java#L59) if it did exist and then adding it back. 

​	The second reason the test was flaky is due to difference in device screen size, and therefore the amount of an activity the keyboard hides. When [typing in the new account details](https://code.cs.umanitoba.ca/3350-winter-2021-a03/3350-winter-2021-A03_Group2/-/blob/e4bc7977432f7c828233a86bb50784f32bc12536/app/src/androidTest/java/comp3350/srsys/presentation/CreateAccountAcceptanceTest.java#L105), smaller devices kept failing because the keyboard was in the way of Espresso's attempt to click an object. To remedy this, `Espresso.closeSoftKeyboard()` was used to close the keyboard before clicking the text fields initially hidden behind the keyboard. We attempted to use the IME button on the keyboard instead since it automatically moves the focus to the next text field, but we couldn't get it to work for some reason and decided on closing the keyboard.

​	The final reason testing was flaky is because of the exception message it gives don't well define what's causing the exceptional behavior. Most of the exceptional behavior was due to layout components not being available to act upon, such as typing into a text field or clicking a button. This was fixed by using Espresso's `check()` method to verify components are available prior to trying to do some action using them such as [checking the test is on the login page](https://code.cs.umanitoba.ca/3350-winter-2021-a03/3350-winter-2021-A03_Group2/-/blob/e4bc7977432f7c828233a86bb50784f32bc12536/app/src/androidTest/java/comp3350/srsys/presentation/CreateAccountAcceptanceTest.java#L130) prior to typing in the email and password to login.

Acceptance test, untestable
===============

The challenges first faced when creating automated acceptance tests was non-descriptive layout component IDs. We had to go through the layout and rename most of the components to better describe what they were so the acceptance test became readable. The behavior that was difficult to test was clicking on a specific element in a recyclerView. Selecting the element wasn't overly difficult, but trying to get the device to scroll to the element before clicking it just wasn't working. We went over the time we timeboxed to spend on that part of the test, but discovered that our elements weren't ordered correctly. Our listing should be displayed from newest to oldest, but they were being displayed from oldest to newest. Changing that sorting made [testing that the listing was present ](https://code.cs.umanitoba.ca/3350-winter-2021-a03/3350-winter-2021-A03_Group2/-/blob/development/app/src/androidTest/java/comp3350/srsys/presentation/CreateListingAcceptanceTest.java#L115) very easy in comparison to before.

Velocity/teamwork
=================

Our ability to estimate how long something would take improved throughout the iterations. Probably due to a combination of splitting things up into smaller pieces, and better understanding what needs to be done. We were both [underestimating](https://code.cs.umanitoba.ca/3350-winter-2021-a03/3350-winter-2021-A03_Group2/-/issues/40) how long something would take and [overestimating](https://code.cs.umanitoba.ca/3350-winter-2021-a03/3350-winter-2021-A03_Group2/-/issues/26). This is almost certainly because we have never worked with some of the technologies used such as Android and HSQL, and we have never wrote down how long something will take. 

​	In comparison, in iteration 3, we have shifted from underestimating in most cases to being [nearly on target](https://code.cs.umanitoba.ca/3350-winter-2021-a03/3350-winter-2021-A03_Group2/-/issues/100) or overestimating ([1](https://code.cs.umanitoba.ca/3350-winter-2021-a03/3350-winter-2021-A03_Group2/-/issues/97) [2](https://code.cs.umanitoba.ca/3350-winter-2021-a03/3350-winter-2021-A03_Group2/-/issues/98) [3](https://code.cs.umanitoba.ca/3350-winter-2021-a03/3350-winter-2021-A03_Group2/-/issues/105)). The exception was still if we were working on a technology that was new to the group member such as [using Espresso for acceptance tests](https://code.cs.umanitoba.ca/3350-winter-2021-a03/3350-winter-2021-A03_Group2/-/issues/83), or [programming Android functionality](https://code.cs.umanitoba.ca/3350-winter-2021-a03/3350-winter-2021-A03_Group2/-/issues/88). Once we worked with the technology for a few weeks, we quickly honed in on how long it would actually take us to implement the dev task.