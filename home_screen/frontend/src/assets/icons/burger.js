import React from "react";
import './burger.css';
const Burger = ({color, onClick}) => {
    return(
        <div class="burgerContainer" onClick={onClick}>
            <div class="bar1" style={{backgroundColor: color}}></div>
            <div class="bar2" style={{backgroundColor: color}}></div>
            <div class="bar3" style={{backgroundColor: color}}></div>
        </div>
    );
}
export default Burger;