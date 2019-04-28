package com.team100.kite_master.networking;

import android.content.Context;
import android.util.Base64;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class NetworkManager {
    private static NetworkManager instance = null;
    private String url;

    //for Volley API
    private RequestQueue requestQueue;


    private NetworkManager(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized NetworkManager getInstance(Context context) {
        if (null == instance)
            instance = new NetworkManager(context);
        return instance;
    }

    //this is so you don't need to pass context each time
    public static synchronized NetworkManager getInstance() {
        if (null == instance) {
            throw new IllegalStateException(NetworkManager.class.getSimpleName() +
                    " is not initialized, call getInstance(...) first");
        }
        return instance;
    }

    public void setUrl(String ip) {
        url = "http://" + ip + ":5000";
    }

//=========================================================================================================================

    //LOGIN
    //calls to /api/status to check if the ip is actually a kite server
    public void testIP(String ip, final VolleyListener<JSONObject> listener) {
        setUrl(ip);
        String URL = url + "/api/status";
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.getResult(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.getError(error);
                    }
                }
        );
        requestQueue.add(getRequest);
    }


    //tries to login with credentials
    public void login(final String username, final String password, final VolleyListener<JSONObject> listener) {
        String URL = url + "/api/auth/login";

        JSONObject LoginCredentials = new JSONObject();
        try {
            LoginCredentials.put("Username", username);
            LoginCredentials.put("Password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.getResult(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.getError(error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                String credentials = username + ":" + password;
                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Basic " + base64EncodedCredentials);
                return headers;
            }
        };
        requestQueue.add(postRequest);
    }

    //=========================================================================================================================

    //USERS

    public void requestUserData(String username, final VolleyListener<JSONObject> listener) {
        String URL = url + "/api/v2/users/" + username;
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.getResult(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.getError(error);
                    }
                }
        );
        requestQueue.add(getRequest);
    }

    //send put request to update password, bio, admin and mod status
    public void updatePassword(String username, String password, final VolleyListener<String> listener) {

        String URL = url + "/api/v2/users/" + username;

        JSONObject jsonBody = new JSONObject();
        try {
            if (!password.equals("")) jsonBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String requestBody = jsonBody.toString();
        StringRequest postRequest = new StringRequest(Request.Method.PUT, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listener.getResult(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.getError(error);
                    }
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                return requestBody.getBytes(StandardCharsets.UTF_8);
            }
        };

        requestQueue.add(postRequest);
    }

    //send put request to update password, bio, admin and mod status
    public void updateUser(String username, boolean isMod, boolean isAdmin, final VolleyListener<String> listener) {

        String URL = url + "/api/v2/users/" + username;

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("is_admin", isAdmin);
            jsonBody.put("is_mod", isMod);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String requestBody = jsonBody.toString();
        StringRequest postRequest = new StringRequest(Request.Method.PUT, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listener.getResult(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.getError(error);
                    }
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                return requestBody.getBytes(StandardCharsets.UTF_8);
            }
        };

        requestQueue.add(postRequest);
    }


    //delete a single user given a username
    public void deleteUser(String username, final VolleyListener<JSONObject> listener) {

        String URL = url + "/api/v2/users/" + username;
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.DELETE, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.getResult(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.getError(error);
                    }
                }
        );
        requestQueue.add(getRequest);
    }

//=========================================================================================================================

    //TOPICS

    public void requestTopics(final VolleyListener<JSONObject> listener) {
        String URL = url + "/api/v2/topics";
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.getResult(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.getError(error);
                    }
                }
        );
        requestQueue.add(getRequest);
    }


    public void addTopic(final String name, final String description, final VolleyListener<String> listener) {
        String URL = url + "/api/v2/topics";

        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("name", name);
            jsonBody.put("description", description);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBody.toString();

        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listener.getResult(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.getError(error);
                    }
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                return requestBody.getBytes(StandardCharsets.UTF_8);
            }
        };
        requestQueue.add(postRequest);
    }


    //delete a single user given a username
    public void deleteTopic(final String topicid, final VolleyListener<JSONObject> listener) {

        String URL = url + "/api/v2/topics/" + topicid;
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.DELETE, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.getResult(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.getError(error);
                    }
                }
        );
        requestQueue.add(getRequest);
    }


    //=========================================================================================================================

    //POSTS

    public void requestAllPosts(final VolleyListener<JSONObject> listener) {
        String URL = url + "/api/v2/posts";
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.getResult(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.getError(error);
                    }
                }
        );
        requestQueue.add(getRequest);
    }


    public void requestPostList(String topic, final VolleyListener<JSONObject> listener) {
        String URL = url + "/api/v2/topics/" + topic;
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.getResult(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.getError(error);
                    }
                }
        );
        requestQueue.add(getRequest);
    }


    public void requestPost(String post_id, final VolleyListener<JSONObject> listener) {
        String URL = url + "/api/v2/posts/" + post_id;
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.getResult(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.getError(error);
                    }
                }
        );
        requestQueue.add(getRequest);
    }

    public void sendPost(final String title, final String body, final String author, final String topic, final VolleyListener<String> listener) {
        String URL = url + "/api/v2/posts";

        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("title", title);
            jsonBody.put("author", author);
            jsonBody.put("topic", topic);
            jsonBody.put("body", body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBody.toString();

        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listener.getResult(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.getError(error);
                    }
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                return requestBody.getBytes(StandardCharsets.UTF_8);
            }
        };
        requestQueue.add(postRequest);
    }


    //delete a single user given a username
    public void deletePost(final String postid, final VolleyListener<JSONObject> listener) {

        String URL = url + "/api/v2/posts/" + postid;
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.DELETE, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.getResult(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.getError(error);
                    }
                }
        );
        requestQueue.add(getRequest);
    }


    //=========================================================================================================================

    //REPLIES

    public void requestReplies(final VolleyListener<JSONObject> listener) {
        String URL = url + "/api/v2/replies";
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.getResult(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.getError(error);
                    }
                }
        );
        requestQueue.add(getRequest);
    }


    public void sendReply(final String postid, final String author, final String body, final VolleyListener<String> listener) {
        String URL = url + "/api/v2/replies";

        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("post_id", postid);
            jsonBody.put("author", author);
            jsonBody.put("body", body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBody.toString();

        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listener.getResult(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.getError(error);
                    }
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                return requestBody.getBytes(StandardCharsets.UTF_8);
            }
        };
        requestQueue.add(postRequest);
    }


    //delete a single user given a username
    public void deleteReply(final String replyid, final VolleyListener<JSONObject> listener) {

        String URL = url + "/api/v2/replies/" + replyid;
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.DELETE, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.getResult(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.getError(error);
                    }
                }
        );
        requestQueue.add(getRequest);
    }





}