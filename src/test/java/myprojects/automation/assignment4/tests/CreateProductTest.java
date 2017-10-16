package myprojects.automation.assignment4.tests;

import myprojects.automation.assignment4.BaseTest;
import myprojects.automation.assignment4.model.ProductData;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CreateProductTest extends BaseTest {

    private ProductData newProduct;


    @Test(dataProvider = "getParams")
    public void createNewProduct(String login, String password) {

        actions.login(login, password);
        newProduct = ProductData.generate();
        actions.createProduct(newProduct);

    }

    @Test(dependsOnMethods = "createNewProduct")
    public void checkProductCreation() {
        actions.checkProduct(newProduct);

    }

    @DataProvider
    public Object[][] getParams() {
        return new String[][] {
                {"webinar.test@gmail.com", "Xcg7299bnSmMuRLp9ITw"}
        };
    }
}
