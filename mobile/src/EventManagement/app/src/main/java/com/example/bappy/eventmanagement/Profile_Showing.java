package com.example.bappy.eventmanagement;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Profile_Showing extends AppCompatActivity {

    TextView profname,profemail,profphone,profcity;
    String profnametext,profemailtext,profphonetext,profcitytext;
    Button showok;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_showing_layout);

        sharedPreferences=getSharedPreferences(getString(R.string.PREF_FILE),0);

        profnametext=sharedPreferences.getString(getString(R.string.USERNAME),"");
        profemailtext=sharedPreferences.getString(getString(R.string.EMAIL),"");
        profphonetext=sharedPreferences.getString(getString(R.string.PHONE),"");
        profcitytext=sharedPreferences.getString(getString(R.string.CITY),"");

        profname=(TextView)findViewById(R.id.profilename);
        profemail=(TextView)findViewById(R.id.profileemail);
        profphone=(TextView)findViewById(R.id.profilephone);
        profcity=(TextView)findViewById(R.id.profilecity);
        showok=(Button)findViewById(R.id.buttonok);

        profname.setText(profnametext);
        profemail.setText(profemailtext);
        profphone.setText(profphonetext);
        profcity.setText(profcitytext);

        showok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
