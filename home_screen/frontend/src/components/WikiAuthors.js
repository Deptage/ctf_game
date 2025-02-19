import React from "react";
import '../styles/WikiAuthors.css';

const Authors = () =>{
    return(
        <div className="authorsPanel">
            <h2 className="authors-text">Authors:</h2>
            <br></br>
            <h3 className="authors-text"><a className="authors-text" href="https://www.linkedin.com/in/miko%C5%82aj-p%C5%82atek-34663523b/" target="_blank">Mikołaj Płatek</a></h3>
            <h3 className="authors-text"><a className="authors-text" href="https://www.linkedin.com/in/zuzanna-%C5%82awniczak/" target="_blank">Zuzanna Ławniczak</a></h3>
            <h3 className="authors-text"><a className="authors-text" href="https://www.linkedin.com/in/szymon-skowro%C5%84ski-704baa26a/" target="_blank">Szymon Skowroński</a></h3>
            <h3 className="authors-text"><a className="authors-text" href="https://www.linkedin.com/in/jakub-brambor-66a7b8295/" target="_blank">Jakub Brambor</a></h3>
            

        </div>
    );
}
export default Authors;