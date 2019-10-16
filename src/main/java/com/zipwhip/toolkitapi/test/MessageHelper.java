package com.zipwhip.toolkitapi.test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class MessageHelper {

    private HashMap<String, String> sessions = new HashMap<>();

    public MessageHelper() {}

    public Response sendMessage(String from, String pw, String to, String message, String host, boolean logAll) {

        String session = getSession(from, pw, host);
        Response resp;
        if(logAll) {
            resp = given().log().all().params("session", session, "body", message, "to", to)
                    .when().post(host + "/api/v1/message/send");
        }
        else {
            resp = given().params("session", session, "body", message, "to", to)
                    .when().post(host + "/api/v1/message/send");
        }
        return resp;
    }

    public Response sendMessage(String from, String pw, String to, String message, String host) {
        return sendMessage(from, pw, to, message, host, false);
    }

    public String getSession(String account, String password, String host) {

        if (sessions.get(account) == null) {

            String url = host.replace("app", "login") + "/api/v2/user/login";
            Response r = given().contentType("application/x-www-form-urlencoded").
                    when().params("password", password, "username", account).
                    post(url);

            System.out.println(r.getBody().asString());
            sessions.put(account, new JsonPath(r.getBody().asString()).get("response"));
        }
        return sessions.get(account);
    }
}
