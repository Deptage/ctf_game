import React, {useEffect, useState} from 'react';
import './Investments.css'

function randomStockName(){
    var alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    var stockName = "";
    for(let i=0;i<3;i++){
        stockName+=alphabet.charAt(Math.floor(Math.random()*alphabet.length));
    }
    return stockName;
}
function generateRandomStock(){
    const stockName=randomStockName();
    const percentage = (Math.random()*4-2).toFixed(2);
    const isPositive = percentage >= 0;

    return{
        stockName,
        percentage: `${isPositive ? '+' : ''}${percentage}%`,
        isPositive
    }
}

function Investments(){
    const [stocks, setStocks] = useState([]);

    useEffect(()=> {
        const generatedStocks = Array.from({ length: 40 }, generateRandomStock);
        setStocks(generatedStocks);
    }, []);
    return (
        <section className="investments">
          <div className="scrolling-bar">
            {stocks.map((stock, index) => (
              <span key={index} className={`stock-item ${stock.isPositive ? 'positive' : 'negative'}`}>
                {stock.isPositive ? '⬆' : '⬇'} {stock.stockName} {stock.percentage}
              </span>
            ))}
          </div>
        </section>
    );
}

export default Investments;