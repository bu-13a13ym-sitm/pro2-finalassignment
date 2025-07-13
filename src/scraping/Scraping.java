import java.time.*;
import java.util.*;
import java.io.*;
import java.net.*;
import java.nio.channels.*;
import java.util.regex.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;

public class Scraping {
    class tmpProduct {
        public String productName;
        public String description;
        public int price;
        public String imageURL;

        public tmpProduct(String productName, String description, int price, String imageURL) {
            this.productName = productName;
            this.description = description;
            this.price = price;
            this.imageURL = imageURL;
        }
    }

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "driver/windows/chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36");
        options.setExperimentalOption("excludeSwitches", Arrays.asList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);
        options.addArguments("--disable-blink-features=AutomationControlled");
        //options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);
        ((JavascriptExecutor) driver).executeScript(
            "Object.defineProperty(navigator, 'webdriver', {get: () => undefined})"
        );
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        WebDriver tdriver = new ChromeDriver(options);
        tdriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        try {
            tdriver.get("https://translate.google.co.jp/?sl=ja&tl=en&op=translate");
            
            driver.get("https://zozo.jp");
            WebElement ranking = driver.findElement(By.cssSelector("a[href*='/ranking/all-sales.html']"));
            ranking.click();

            ArrayList<tmpProduct> tmpProducts = new ArrayList<>();
            Thread.sleep(3000);
            if (true) throw new Exception();

            List<WebElement> top3Items = driver.findElement(By.cssSelector("ul.catalog-list")).findElements(By.cssSelector("li"));
            for (int i = 0; i < top3Items.size(); i++) {
                top3Items.get(i).findElement(By.cssSelector("a.catalog-link")).click();
                WebElement productDetails = driver.findElement(By.cssSelector("div.goodsRight")).findElement(By.cssSelector("div.p-goods-information__primary"));
                String productName = productDetails.findElement(By.cssSelector("h1.p-goods-information__heading")).getText();
                List<WebElement> priceList = productDetails.findElements(By.cssSelector("div.p-goods-information__pricer"));
                int price;
                if (!priceList.isEmpty()) {
                    String priceStr = priceList.get(0).getText();
                    price = Math.max(((Integer)(Integer.parseInt(priceStr.replaceAll("[^\\d]", "")) / 300)) * 100, 1000);
                } else {
                    String priceStr = productDetails.findElement(By.cssSelector("div.p-goods-information__proper")).findElement(By.cssSelector("span.u-text-style-strike")).getText();
                    Pattern pattern = Pattern.compile("[0-9,]+");
                    Matcher matcher = pattern.matcher(priceStr);
                    if (matcher.find()) price = Math.max(((Integer)(Integer.parseInt(matcher.group().replaceAll(",", "")) / 300)) * 100, 1000);
                }

                WebElement productImage = driver.findElement(By.cssSelector("div.swiper-slide.swiper-slide-duplicate.swiper-slide-active")).findElement(By.cssSelector("img"));
                String imageURL = productImage.getAttribute("src");

                driver.navigate().back();
                top3Items = driver.findElement(By.cssSelector("ul.catalog-list")).findElements(By.cssSelector("li"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
            tdriver.quit();
        }
    }

    public static void saveImage(String imageUrl, String fileName) throws IOException {
        File file = new File(imageUrl);
        URL url = file.toURI().toURL();
        try (InputStream in = url.openStream();
             FileOutputStream fos = new FileOutputStream(fileName)) {
            byte[] buffer = new byte[4096];
            int n;
            while ((n = in.read(buffer)) != -1) {
                fos.write(buffer, 0, n);
            }
        }
    }
}
