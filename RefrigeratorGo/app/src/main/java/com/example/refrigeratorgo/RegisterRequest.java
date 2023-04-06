package com.example.refrigeratorgo;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://zoooz0616.dothome.co.kr/Register.php";
    private Map<String, String> map;

    public RegisterRequest(String userName, String userID, String userEmail, String userReName, String userPassword, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userName", userName);
        map.put("userID", userID);
        map.put("userEmail", userEmail);
        map.put("userReName", userReName);
        map.put("userPassword", userPassword);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
