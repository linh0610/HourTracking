# My Personal Project

## A paid time and work tracking app for employee

- This app for tracking shifts of an employee, and you can also create an account,
  change your shift, add your current wage, and it will calculate the wage by-weekly
  and also deducting tax from the wage.
- Mostly employees and manager will use it
- This project interest me because usually you will the tracking app and tax app separately, but it was a hassle everytime to check two different place.


### User stories:
- As a user I want to be able to add an account
- As a user I want to clock in and clock out.
- As a user I want to input my wage per hour into the memory of the app.
- As a user I want to choose my shift.
- As a user I want to be able to choose to quit and save the application.
- As a user I want to have an option to view my hours worked from that I have saved in file.
- As a user I want to be able to choose my own start and end time of my shift.
- As a user I want to see a timer to see how long I have worked after clocking in.
- As a user I want to toggle dark mode and light mode
- As a user I want to have a DVD Screensaver after clocking in
### Phase 4: Task 2
Saved accounts to ./data/accounts.json

Logged events:

Sun Apr 07 21:17:17 PDT 2024: Event log cleared.

Sun Apr 07 21:17:30 PDT 2024: Shift set at 2024-04-18 Start: 21:17, End: 21:17 for account: linh

Sun Apr 07 21:17:30 PDT 2024: Account added: linh

Sun Apr 07 21:17:55 PDT 2024: Wage set at 12.0 for account: linh

### Phase 4: Task 3
If I have more time to do this project, 
I would first split the user interface logic 
into multiple class to enhance the maintainability as 
of right now, every logic is being packed into one class 
which makes it hard to debug. For example, sign in, log in and should be in another class, the frame should be in another class, clock in and clock out function should be in a different class.
I would also change some
duplicated code in the class to make the structure more neatly.