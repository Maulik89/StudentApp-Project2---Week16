package com.studentapp.studentinfo;

import com.studentapp.testbase.TestBase;
import constants.EndPoints;
import io.restassured.http.ContentType;
import model.StudentPojo;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Title;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import utils.TestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.hasValue;

@RunWith(SerenityRunner.class)
public class StudentCRUDTest extends TestBase {

    static String firstName = "Montu" + TestUtils.getRandomValue();
    static String lastName = "Patel" + TestUtils.getRandomValue();
    static String email = "montu" + TestUtils.getRandomValue() + "@gmail.com";
    static String programme = "rule";
    static Object studentId;

    @Title("This will create a new student")
    @Test
    public void test001() {
        List<String> courses = new ArrayList<>();
        courses.add("Public Rule");
        courses.add("Gov Rule");

        StudentPojo studentPojo = new StudentPojo();
        studentPojo.setFirstName(firstName);
        studentPojo.setLastName(lastName);
        studentPojo.setEmail(email);
        studentPojo.setProgramme(programme);
        studentPojo.setCourses(courses);
        SerenityRest.given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(studentPojo)
                .when()
                .post()
                .then().log().all().statusCode(201);
    }

    @Title("Verify if student was created")
    @Test
    public void test002() {
        String part1 = "findAll{it.lastName=='";
        String part2 = "'}.get(0)";

        HashMap<String, ?> studentMapData = SerenityRest.given()
                .log().all()
                .when()
                .get(EndPoints.GET_ALL_STUDENT)
                .then().statusCode(200).extract().path(part1 + lastName + part2);
        Assert.assertThat(studentMapData, hasValue(lastName));
        studentId = studentMapData.get("id");
        System.out.println(studentId);

    }

    @Title("Update the user and verify the updated information")
    @Test
    public void test003() {

        lastName = lastName + "pintu";

        List<String> courses = new ArrayList<>();
        courses.add("Master");
        courses.add("Bachelor");

        StudentPojo studentPojo = new StudentPojo();
        studentPojo.setFirstName(firstName);
        studentPojo.setLastName(lastName);
        studentPojo.setEmail(email);
        studentPojo.setProgramme(programme);
        studentPojo.setCourses(courses);

        SerenityRest.given()
                .log().all()
                .contentType(ContentType.JSON)
                .pathParam("studentID", studentId)
                .body(studentPojo)
                .when()
                .put(EndPoints.UPDATE_STUDENT_BY_ID)
                .then().log().all().statusCode(200);

        String part1 = "findAll{it.lastName=='";
        String part2 = "'}.get(0)";

        HashMap<String, ?> studentMapData = SerenityRest.given()
                .when()
                .get(EndPoints.GET_ALL_STUDENT)
                .then().statusCode(200).extract().path(part1 + lastName + part2);
        Assert.assertThat(studentMapData, hasValue(lastName));

    }

    @Title("Delete the student and verify if the student is deleted")
    @Test
    public void test004() {

        SerenityRest.given()
                .pathParam("studentID", studentId)
                .when()
                .delete(EndPoints.DELETE_STUDENT_BY_ID)
                .then().log().all().statusCode(204);

        SerenityRest.given()
                .pathParam("studentID", studentId)
                .when()
                .get(EndPoints.GET_SINGLE_STUDENT_BY_ID)
                .then().log().all().statusCode(404);

    }
}