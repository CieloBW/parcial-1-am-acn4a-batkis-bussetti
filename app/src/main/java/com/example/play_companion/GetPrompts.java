package com.example.play_companion;

import android.os.AsyncTask;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetPrompts extends AsyncTask<String, Integer, String> {

    public interface AsyncResponse {
        void processFinish(String result);
    }

    public AsyncResponse delegate = null;

    @Override
    protected String doInBackground(String... strings) {
        String url = strings[0];
        String response;
        String prompt = "";
        try {
            response = run(url);
            JSONObject promptJson = new JSONObject(response);
            prompt = (String) promptJson.get("randomValue");
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
        return prompt;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (delegate != null) {
            delegate.processFinish(s);
        }
    }

    OkHttpClient client = new OkHttpClient();

    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}
