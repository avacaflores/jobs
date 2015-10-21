package com.avacaflores.jobs;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private JobPostArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<JobPost> content = new ArrayList<>();

        final ListView jobPostsListView = (ListView) findViewById(R.id.list_view);

        arrayAdapter = new JobPostArrayAdapter(this, content);
        jobPostsListView.setAdapter(arrayAdapter);


        jobPostsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this, JobDetailActivity.class);

                JobPost item = (JobPost) parent.getItemAtPosition(position);
                intent.putExtra("ID", item.getId());
                intent.putExtra("TITLE", item.getTitle());
                intent.putExtra("DESCRIPTION", item.getDescription());
                intent.putExtra("CONTACTS", item.getContacts());
                startActivity(intent);
            }
        });

        sync();

    }


    public void sync() {

        NetworkOperationAsyncTask asyncTask = new NetworkOperationAsyncTask();

        asyncTask.execute();
    }


    private class NetworkOperationAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            Uri buildUri = Uri.parse("http://dipandroid-ucb.herokuapp.com").buildUpon()
                    .appendPath("work_posts.json").build();
            try {
                URL url = new URL(buildUri.toString()); // Create a new URL

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.addRequestProperty("Content-Type", "application/json");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer buffer = new StringBuffer();

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                return buffer.toString();
            } catch (IOException ex) {
                Log.e(LOG_TAG, ex.getMessage());
            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }

                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                } catch (IOException e) {
                    Log.e(LOG_TAG, e.getMessage());
                }

            }
            return "";
        }

        @Override
        protected void onPostExecute(String json) {
            try {
                JSONArray array = new JSONArray(json);
                arrayAdapter.clear();

                for (int i = 0; i < array.length(); i++) {
                    JSONObject jobPostJSON = array.getJSONObject(i);
                    JobPost jobPost = new JobPost();

                    jobPost.setId(jobPostJSON.getInt("id"));
                    jobPost.setTitle(jobPostJSON.getString("title"));
                    jobPost.setPostDate(jobPostJSON.getString("posted_date"));
                    jobPost.setDescription(jobPostJSON.getString("description"));

                    JSONArray arrayContacts = jobPostJSON.getJSONArray("contacts");
                    String contactsString = "";

                    for (int j = 0; j < arrayContacts.length(); j++) {
                        contactsString = contactsString + arrayContacts.getString(j) + "\n";
                    }

                    jobPost.setContacts(contactsString);
                    arrayAdapter.add(jobPost);
                }

            } catch (JSONException ex) {
                Log.e(LOG_TAG, ex.getMessage());
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_sync) {

            sync();
            Log.e(LOG_TAG, "SYNC");
            return true;
        }

        if (id == R.id.action_post) {

            Intent intent = new Intent(MainActivity.this, PostActivity.class);
            startActivity(intent);
            Log.e(LOG_TAG, "POST");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
