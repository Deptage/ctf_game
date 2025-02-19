# CTFGame

## Authors of the Game

| Name               | GitHub Profile                          | LinkedIn Profile                                                                 |
|--------------------|-----------------------------------------|----------------------------------------------------------------------------------|
| Mikołaj Płatek     | [nezquick123](https://github.com/nezquick123) | [Mikołaj Płatek](https://www.linkedin.com/in/miko%C5%82aj-p%C5%82atek-34663523b/) |
| Szymon Skowroński  | [Sazer54](https://github.com/Sazer54)         | [Szymon Skowroński](https://www.linkedin.com/in/szymon-skowro%C5%84ski-704baa26a/)|
| Jakub Brambor      | [august3sas](https://github.com/august3sas)   | [Jakub Brambor](https://www.linkedin.com/in/jakub-brambor-66a7b8295/)            |
| Zuzanna Ławniczak  | [Deptage](https://github.com/Deptage)         | [Zuzanna Ławniczak](https://www.linkedin.com/in/zuzanna-lawniczak/)               |


## Prerequisites

Before running the project, make sure you have the following requirements met:

1. **Make sure you have Docker and docker-compose installed**

2. **Add Domains to `/etc/hosts`**  
   Edit your `/etc/hosts` file to include the necessary domain mappings present in the hosts file from this repository.
    

3. **Install and Ensure `caddy` is in `PATH`**  
   Caddy is required to run this project. Download and install it from the official source:
   [Caddy Installation Guide](https://caddyserver.com/docs/install)

   Ensure `caddy` is accessible from the terminal by running:
   ```sh
   caddy version
   ```

## How to Run

Follow these steps to set up and run the project:

1. **Clone the Repository**  
   ```sh
   git clone <repository-url>
   ```

2. **Navigate to Project Directory**  
   ```sh
   cd ctfgame/
   ```

3. **Make the Script Executable**  
   ```sh
   chmod +x run_all.sh
   ```

4. **Run the Script**  
   ```sh
   ./run_all.sh
   ```
Application will be accessible at http://home.ctfgame.pl/

