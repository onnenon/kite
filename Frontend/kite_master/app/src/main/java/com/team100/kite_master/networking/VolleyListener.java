package com.team100.kite_master.networking;

import com.android.volley.VolleyError;

public interface VolleyListener<T>
{
    public void getResult(T object);
    public void getError(VolleyError err);
}