const puppeteer = require('puppeteer');

(async() => {
    function delay(time) {
        return new Promise(function(resolve) { 
            setTimeout(resolve, time)
        });
     }

    const url = process.env.B64URL;
    try {
        const receiver = 'antiquascarlet1697';
        const username = 'Robert Carlson'; 
        const password = 'areallydifficultpasswordtoguessthatsliterallytimpossibletohack';

        const browser = await puppeteer.launch({
            headless: true,
        });

        const page = await browser.newPage();
        await page.goto(url);
        await page.type('input[placeholder="Username"]', username);
        await page.type('input[placeholder="Password"]', password);
        await page.click('button.loginButton');
        
        console.log('Home page loaded');

        await page.waitForSelector('li.contactItem'); 
        const contactItems = await page.$$('li.contactItem');
        for (const item of contactItems) {
            const text = await page.evaluate(el => el.textContent, item);
            if (text.includes(receiver)) {
                await item.click();
                await delay(2000);

                break;
            }
        }
        console.log(`Message from ${receiver} read`);
        await browser.close();
    } catch (e) {
        console.error(e);
        process.exit(1);
    }
})();

