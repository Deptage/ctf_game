import React from "react";
import "./WorkerSection.css"

const WorkerSection=({worker}) => {
    return(
        <div className="section">
            <div className="textContent">
                <h1>
                    {worker.name} ■ {worker.position}
                </h1>
                <h4>
                    Z nami od {worker.year} roku
                </h4>
                <p>
                    {worker.description}
                </p>
            </div>
        </div>
    )
}
export default WorkerSection;