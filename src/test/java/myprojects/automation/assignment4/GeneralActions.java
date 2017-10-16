package myprojects.automation.assignment4;


import myprojects.automation.assignment4.model.ProductData;
import myprojects.automation.assignment4.utils.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

/**
 * Contains main script actions that may be used in scripts.
 */
public class GeneralActions {
    private WebDriver driver;
    private WebDriverWait wait;

    GeneralActions(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 30);
    }

    /**
     * Logs in to Admin Panel.
     * @param login
     * @param password
     */
    public void login(String login, String password) {
        driver.get(Properties.getBaseAdminUrl());
        driver.findElement(By.id("email")).sendKeys(login);
        driver.findElement(By.id("passwd")).sendKeys(password);
        driver.findElement(By.className("btn")).click();
    }

    public void createProduct(ProductData newProduct) {
        //opening new product page
        new Actions(driver).moveToElement(driver.findElement(By.cssSelector("#subtab-AdminCatalog"))).build().perform();
        waitForContentLoad(By.cssSelector("#subtab-AdminCatalog>ul>#subtab-AdminProducts"));
        driver.findElement(By.id("subtab-AdminProducts")).click();

        driver.findElement(By.cssSelector("#page-header-desc-configuration-add>.title")).click();

        // adding good
        driver.findElement(By.id("form_step1_name_1")).sendKeys(newProduct.getName());
        driver.findElement(By.id("form_step1_qty_0_shortcut")).sendKeys(newProduct.getQty().toString());
        driver.findElement(By.id("form_step1_price_shortcut")).sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        driver.findElement(By.id("form_step1_price_shortcut")).sendKeys(newProduct.getPrice());

        driver.findElement(By.className("switch-input")).click();
        waitForContentLoad(By.className("growl-message"));
        //check Настройки обновлены.
        waitForContentLoad(By.className("growl-message"));
        Assert.assertEquals(driver.findElement(By.className("growl-message")).getText(),
                "Настройки обновлены.", "Wrong message for product availability: ");
        driver.findElement(By.className("growl-close")).click();
        driver.findElement(By.cssSelector(".btn.btn-primary.js-btn-save")).click();
        waitForContentLoad(By.className("growl-message"));
        Assert.assertEquals(driver.findElement(By.className("growl-message")).getText(),
                "Настройки обновлены.", "Wrong message for product creation: ");
        driver.findElement(By.className("growl-close")).click();
    }

    public void checkProduct(ProductData newProduct) {
        driver.get(Properties.getBaseUrl());
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("arguments[0].scrollIntoView(true);",
                driver.findElement(By.cssSelector(".all-product-link>i")));
        waitForContentLoad(By.cssSelector(".all-product-link>i"));
        driver.findElement(By.cssSelector(".all-product-link>i")).click();
        js.executeScript("arguments[0].scrollIntoView(true);",
                driver.findElement(By.cssSelector(".next>i")));
        if (isClickable(By.cssSelector(".next>i"))) {
            driver.findElements(By.xpath(".//*[@id='js-product-list']/nav/div[2]/ul/*/a"))
                    .get(driver.findElements(By.xpath(".//*[@id='js-product-list']/nav/div[2]/ul/*/a")).size() - 2).click();
        }
        waitForContentLoad(By.linkText(newProduct.getName()));
        Assert.assertNotNull(driver.findElement(By.linkText(newProduct.getName())), "Product not found: ");
        js.executeScript("arguments[0].scrollIntoView(true);",
                driver.findElement(By.linkText(newProduct.getName())));
        driver.findElement(By.linkText(newProduct.getName())).click();


        Assert.assertEquals(driver.findElement(By.className("h1")).getText().toLowerCase(), newProduct.getName().toLowerCase());
        Assert.assertEquals(driver.findElement(By.cssSelector(".current-price>span")).getText().split(" ")[0],
                newProduct.getPrice());
        Assert.assertEquals(driver.findElement(By.cssSelector(".product-quantities>span")).getText().split(" ")[0],
                newProduct.getQty().toString());
    }

    /**
     * Waits until page loader disappears from the page
     */
    private void waitForContentLoad(By by) {
         wait.until(ExpectedConditions.elementToBeClickable(by));
    }

    private Boolean isClickable(By by) {
        Boolean isClickable;
        try {
            new WebDriverWait(driver, 2).until(ExpectedConditions.elementToBeClickable(by));
            isClickable = true;
        } catch (Exception ignored) {
            isClickable = false;
        }
        return isClickable;
    }
}
