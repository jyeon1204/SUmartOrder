package com.example.audtj.sumarto;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {

    final static private String URL = "https://audtjddbfl.cafe24.com/MemberRegister.php";
    private Map<String, String> parameters;

    public RegisterRequest(String userID, String userPassword, String userMail, String userNickname, Response.Listener<String> listener ){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID",userID);
        parameters.put("userPassword",userPassword);
        parameters.put("userMail",userMail);
        parameters.put("userNickname",userNickname);
    }

    @Override
    public Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}