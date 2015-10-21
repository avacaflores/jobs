package com.avacaflores.jobs;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    EditText titleEditText, descriptionEditText, contactEditText;
    private JSONObject jsonWorkPost = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        titleEditText = (EditText) findViewById(R.id.titleId);
        descriptionEditText = (EditText) findViewById(R.id.descriptionId);
        contactEditText = (EditText) findViewById(R.id.contactId);
    }

    public void addPost(View view) {

        JSONArray jsonContacts = new JSONArray();
        jsonContacts.put(contactEditText.getText());

        JSONObject jsonPost = new JSONObject();
        try {

            jsonPost.put("title", titleEditText.getText());
            jsonPost.put("description", descriptionEditText.getText());
            jsonPost.put("contacts", jsonContacts);
            jsonWorkPost.put("work_post", jsonPost);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        Log.e(LOG_TAG, "JSON " + jsonWorkPost.toString());


        NetworkOperationAsyncTaskPost asyncTaskPost = new NetworkOperationAsyncTaskPost();

        asyncTaskPost.execute();

        Intent intent = new Intent(PostActivity.this, MainActivity.class);
        startActivity(intent);

    }

    private class NetworkOperationAsyncTaskPost extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            URL url;
            HttpURLConnection connection = null;
            Uri buildUri = Uri.parse("http://dipandroid-ucb.herokuapp.com").buildUpon() // Build the URL using the Uri class
                    .appendPath("work_posts.json").build();
            try {

                url = new URL(buildUri.toString()); // Create a new URL
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept-Language", "en-us");
                connection.setUseCaches(false);
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.connect();


                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());

                byte[] outputBytes = jsonWorkPost.toString().getBytes("UTF-8");

                wr.write(outputBytes);
                wr.flush();
                wr.close();

                //Get Response
                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                return response.toString();

            } catch (Exception e) {

                e.printStackTrace();
                return null;

            } finally {

                if (connection != null) {
                    connection.disconnect();
                }
            }
        }


    }
}
