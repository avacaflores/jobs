package com.avacaflores.jobs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class JobDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);
        setTitle(getIntent().getStringExtra("TITLE"));

        TextView titleTextView = (TextView)findViewById(R.id.job_title_text_view);
        TextView descriptionTextView = (TextView)findViewById(R.id.job_description_text_view);
        TextView contactsTextView = (TextView)findViewById(R.id.job_contacts_text_view);

        titleTextView.setText(getIntent().getStringExtra("TITLE"));
        descriptionTextView.setText(getIntent().getStringExtra("DESCRIPTION"));
        contactsTextView.setText((getIntent().getStringExtra("CONTACTS")));
    }
}
