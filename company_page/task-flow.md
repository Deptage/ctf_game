## Task flow
### Task 0: Identifying "hidden" admin panel.
First part of this challenge is dedicated to find ```/adminpanel``` endpoint. There's no hyperlink or redirect to this site from main page. Player can find it in two ways:
- by analysing the code of the frontend. In Route.js file there is list of every endpoint that this site contains
- by running a endpoint scan using, for example, **gobuster** tool.

### Task 1: Exploiting command injection.
On admin panel player can see a typical sign in form. By looking at the source code given to player, he can notice that there is no input escaping/validation and that the POST parameters that are sent via form are directly passed into the bash command. Player can take advantage of this fact to exploit a commant injection vulnerability.

Exemplary payload:
```bash
POST http://company.ctfgame.pl/admin/auth

Parameters:
{
    "login":"1",
    "password":"a ; id; ls /home/appuser; cat /home/appuser/flag.txt;  "
}
```

**Objective**: Get contents of ```/home/appuser/flag.txt```