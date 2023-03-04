package com.studentapp.testbase;


import constants.Path;
import io.restassured.RestAssured;
import org.junit.BeforeClass;
import utils.PropertyReader;

import static java.lang.Integer.parseInt;

/**
 * Created by bhavesh
 */
public class TestBase {
    public static PropertyReader propertyReader;

    @BeforeClass
    public static void init() {
        propertyReader = PropertyReader.getInstance();
        RestAssured.baseURI = propertyReader.getProperty("baseUrl");
        RestAssured.port = parseInt(propertyReader.getProperty("port"));
        RestAssured.basePath = Path.STUDENT; // don't need for bestBuy
        //http://localhost:8080/student
    }

}
