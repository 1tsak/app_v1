package com.oadevelopers.winbazi.utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by sanat on 4/29/2018.
 */

public class MySingleton {

    static Context context;
    private static MySingleton mInstance;
    private RequestQueue requestQueue;

    private MySingleton(Context context) {
        this.context = context;
        this.requestQueue = getRequestQueue();
    }

    public static synchronized MySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MySingleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestque(Request<T> request) {
        requestQueue.add(request);
    }
}








