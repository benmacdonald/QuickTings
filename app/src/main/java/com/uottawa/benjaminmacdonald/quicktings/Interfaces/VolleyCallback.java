package com.uottawa.benjaminmacdonald.quicktings.Interfaces;

import org.json.JSONObject;

/**
 * Created by BenjaminMacDonald on 2017-06-09.
 */

public interface VolleyCallback {
    void onSuccess(JSONObject result);

    void onError(String message);

}
