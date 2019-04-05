package com.example.httpcommunicationwithjwt;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static junit.framework.TestCase.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;




public class JWTUnitTest {

    private HTTPImplementation Imp;

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setup() {

        Imp = mock(HTTPImplementation.class);
    }

    @Test
    public void foo() throws JSONException {

        JSONObject obj = new JSONObject();
        obj.put("username", "fadmin");
        obj.put("post_count", 34);
        obj.put("bio", "I exist");

        when(Imp.foo()).thenReturn(obj);

        assertEquals("fadmin", Imp.foo().get("username"));
        assertEquals(34, Imp.foo().get("post_count"));
        assertEquals("I exist", Imp.foo().get("bio"));
    }

    @Test
    public void getJWTTest() {

        when(Imp.getJWT()).thenReturn("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmYWRtaW4iLCJpc19hZG1pbiI6ZmFsc2UsImlzX21vZCI6ZmFsc2UsImlhdCI6MTU1NDQ4MTIyOH0.QiYUbENoSF-9GNsV0tLfStRSClO4wtGJaF-0EhnXfME");

        assertEquals("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmYWRtaW4iLCJpc19hZG1pbiI6ZmFsc2UsImlzX21vZCI6ZmFsc2UsImlhdCI6MTU1NDQ4MTIyOH0.QiYUbENoSF-9GNsV0tLfStRSClO4wtGJaF-0EhnXfME", Imp.getJWT());
    }

    @Test
    public void JWTSuccess() throws JSONException {

        when(Imp.getUserInfo("fadmin")).thenReturn(true);
        when(Imp.setAll("fadmin", "password", "I exist", true, true)).thenReturn(true);
        when(Imp.setBio("fadmin", "I exist")).thenReturn(true);
        when(Imp.setPassword("fadmin","password")).thenReturn(true);
        when(Imp.setAdministratorStatus("fadmin", true)).thenReturn(true);
        when(Imp.setModeratorStatus("fadmin", true)).thenReturn(true);
        when(Imp.deleteUser("fadmin")).thenReturn(true);

        assertTrue(Imp.getUserInfo("fadmin"));
        assertTrue(Imp.setAll("fadmin", "password", "I exist", true, true));
        assertTrue(Imp.getUserInfo("fadmin"));
        assertTrue(Imp.getUserInfo("fadmin"));
        assertTrue(Imp.getUserInfo("fadmin"));
        assertTrue(Imp.getUserInfo("fadmin"));
        assertTrue(Imp.getUserInfo("fadmin"));


        // Plan:
        // 1. Get JWT
        // 2. Make sure that all methods that rely on JWT return true




    }


}
