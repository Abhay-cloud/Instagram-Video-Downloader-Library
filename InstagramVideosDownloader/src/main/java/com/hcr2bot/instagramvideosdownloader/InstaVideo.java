package com.hcr2bot.instagramvideosdownloader;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class InstaVideo {

    public static String getVideoLink(Context context, String postUrl) {

        String replacedUrl;
        final String[] finalVideoUrl = new String[1];

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(context);

        if (TextUtils.isEmpty(postUrl)) {
            Log.e("VideoURLErrors", "Provided String is empty.");
        } else {
            if (postUrl.contains("?utm_source=ig_web_copy_link")) {
                String partToRemove = "?utm_source=ig_web_copy_link";
                replacedUrl = postUrl.replace(partToRemove, "");
            } else if (postUrl.contains("?utm_source=ig_web_button_share_sheet")) {
                String partToRemove = "?utm_source=ig_web_button_share_sheet";
                replacedUrl = postUrl.replace(partToRemove, "");
            } else if (postUrl.contains("?utm_source=share_sheet")) {
                String partToRemove = "?utm_source=share_sheet";
                replacedUrl = postUrl.replace(partToRemove, "");
            } else if (postUrl.contains("?utm_source=copy_link")) {
                String partToRemove = "?utm_source=copy_link";
                replacedUrl = postUrl.replace(partToRemove, "");
            } else {
                replacedUrl = postUrl;
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    replacedUrl + "?__a=1", null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    JSONObject Obj1 = null;
                    try {
                        Obj1 = response.getJSONObject("graphql");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JSONObject Obj2 = null;
                    try {
                        Obj2 = Obj1.getJSONObject("shortcode_media");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        finalVideoUrl[0] = Obj2.getString("video_url");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("finalURL", finalVideoUrl[0]);


                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VideoURLErrors", "Something went wrong" + error);
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();

                }
            });

            requestQueue.add(jsonObjectRequest);

        }

        return finalVideoUrl[0];

    }
}
