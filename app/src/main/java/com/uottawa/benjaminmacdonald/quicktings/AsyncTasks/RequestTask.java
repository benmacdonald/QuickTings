package com.uottawa.benjaminmacdonald.quicktings.AsyncTasks;

import android.os.AsyncTask;

import java.net.URL;


/**
 * Created by BenjaminMacDonald on 2017-06-09.
 */

public class RequestTask extends AsyncTask<URL, Void, String> {

    protected String doInBackground(URL ... urls) {
        //TODO:: MAKE URL REQUEST

        return "test";
    }

    protected void onPostExecute(String result) {

    }
}
