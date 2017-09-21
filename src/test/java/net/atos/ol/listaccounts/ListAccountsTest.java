package net.atos.ol.listaccounts;

import java.net.URL;

import org.arquillian.cube.DockerUrl;
import org.arquillian.cube.HostIp;
import org.arquillian.cube.CubeIp;


import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;

import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;

@RunWith(Arquillian.class)
public class ListAccountsTest {

    @HostIp
    private String ip;

    @CubeIp(containerName = "test")
    private String cip;

    @DockerUrl(containerName = "test", exposedPort = 1080)
    @ArquillianResource
    private URL url;

    private TestData testData = new TestData();

    @Test
    @RunAsClient
    @InSequence(10)
    public void testOneAccount() throws Exception {
        System.out.println("URL: "+"http://"+cip+":1080/" + "account/1/listaccounts");

        given().
                when().
                get("http://"+cip+":1080/" + "account/1/listaccounts").
                then().
                assertThat().body(equalTo(testData.getData("1")));
    }

    @Test
    @RunAsClient
    @InSequence(20)
    public void testTwoAccounts() throws Exception {

        System.out.println("URL: "+"http://"+cip+":1080/" + "account/2/listaccounts");

        given().
                when().
                get("http://"+cip+":1080/" + "account/2/listaccounts").
                then().
                assertThat().body(equalTo(testData.getData("2")));
    }
    @Test
    @RunAsClient
    @InSequence(30)
    public void testNoAccounts() throws Exception {

        System.out.println("URL: "+"http://"+cip+":1080/" + "account/0/listaccounts");

        given().
                when().
                get("http://"+cip+":1080/" + "account/0/listaccounts").
                then().
                assertThat().statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

}