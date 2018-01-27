package com.example.bappy.eventmanagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Event_Registration extends AppCompatActivity {

    String organizationtext,paymentmethodtext,phonenotext;
    EditText organization,phoneno;
    Boolean ok;

    RadioGroup radioGroup;
    RadioButton radioButton;

    Button enter;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    ProgressDialog progressDialog;

    CoordinatorLayout coordinatorLayout;

    String userid,eventid,fromclass,pagetype,hoster,eventname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_registration_layout);

        progressDialog=new ProgressDialog(Event_Registration.this);
        progressDialog.setCancelable(false);

        sharedPreferences=getSharedPreferences(getString(R.string.PREF_FILE),0);
        editor=sharedPreferences.edit();

        userid=sharedPreferences.getString(getString(R.string.ID),"NO");
        eventid=getIntent().getStringExtra("eventid");
        fromclass=getIntent().getStringExtra("class");
        eventname=getIntent().getStringExtra("eventname");
        pagetype=getIntent().getStringExtra("pagetype");
        hoster=getIntent().getStringExtra("eventhoster");

        organization=(EditText)findViewById(R.id.organization);
        phoneno=(EditText)findViewById(R.id.contactno);
        enter=(Button)findViewById(R.id.enter);

        radioGroup=(RadioGroup)findViewById(R.id.paymentGroup);

        coordinatorLayout=(CoordinatorLayout)findViewById(R.id.registartioneventlayout);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                organizationtext=organization.getText().toString().trim();
                if(organizationtext.equals("")){
                    organization.setError("Please Check The Venue");
                    ok = false;
                }
                else{
                    organization.setError(null);
                    ok=true;
                }

                phonenotext=phoneno.getText().toString().trim();
                if(phonenotext.equals(""))
                {
                    phoneno.setError("Please Enter Your Phone Number");
                    ok = false;
                }
                else
                {
                    if(phonenotext.length()==11) {
                        phoneno.setError(null);
                        ok=true;
                    }
                    else
                    {
                        phoneno.setError("Your Phone Number is wrong");
                        ok = false;
                    }
                }
                if(ok && !userid.equals("NO"))
                {
                    progressDialog.setMessage("Please wait....");
                    progressDialog.show();
                    new EnterInEventBackground().execute();
                }
                else
                {
                    Snackbar.make(coordinatorLayout,"Sorry There is a Error",Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    public void rbclick(View view)
    {
        int paymentmethodid= radioGroup.getCheckedRadioButtonId();
        radioButton=(RadioButton)findViewById(paymentmethodid);
        paymentmethodtext=radioButton.getText().toString();
    }

    public class EnterInEventBackground extends AsyncTask<Void,Void,String> {

        String json_url;

        @Override
        protected void onPreExecute() {
            json_url="http://"+getString(R.string.ip_addres)+"/EventManagement/EventUserAdd.php";
        }

        @Override
        protected String doInBackground(Void... strings) {
            try{
                URL url=new URL(json_url);
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post= URLEncoder.encode("organization","UTF-8")+"="+URLEncoder.encode(organizationtext,"UTF-8")
                        + "&" + URLEncoder.encode("contactNo","UTF-8")+"="+URLEncoder.encode(phonenotext,"UTF-8")
                        + "&" + URLEncoder.encode("paymentmethod","UTF-8")+"="+URLEncoder.encode(paymentmethodtext,"UTF-8")
                        + "&" + URLEncoder.encode("eventid","UTF-8")+"="+URLEncoder.encode(eventid,"UTF-8")
                        + "&" + URLEncoder.encode("userid","UTF-8")+"="+URLEncoder.encode(userid,"UTF-8");
                bufferedWriter.write(post);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line=bufferedReader.readLine()) !=null)
                {
                    result+=line;
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return result.trim();
            }catch(Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(final String s) {
            progressDialog.cancel();
            final AlertDialog.Builder builder=new AlertDialog.Builder(Event_Registration.this);

            View view= LayoutInflater.from(Event_Registration.this).inflate(R.layout.custom_dialog,null);

            TextView bodymessage=(TextView)view.findViewById(R.id.message);

            ImageView closedialog=(ImageView)view.findViewById(R.id.close);

            builder.setView(view);
            if(!s.contains("You Already Registered"))
            {
                bodymessage.setText(s);
                Button positive=(Button)view.findViewById(R.id.positive);
                Button negative=(Button)view.findViewById(R.id.negative);
                positive.setVisibility(View.GONE);
                negative.setText("OK");
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Event_Registration.this,Event_Showing.class));
                        finish();
                    }
                });

                final AlertDialog alert=builder.create();

                closedialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.cancel();
                    }
                });
                alert.show();
            }
            else
            {
                RelativeLayout relativeLayout=(RelativeLayout)view.findViewById(R.id.buttonlayout);
                relativeLayout.setVisibility(View.GONE);
                bodymessage.setText(s);
                final AlertDialog alert=builder.create();

                closedialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.cancel();
                    }
                });
                alert.show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(fromclass.equals("1"))
            startActivity(new Intent(Event_Registration.this,Event_Showing.class));
        else {
            Intent intent=new Intent(Event_Registration.this, Single_Event_Details.class);
            intent.putExtra("eventid", eventid);
            intent.putExtra("eventname", eventname);
            intent.putExtra("pagetype", pagetype);
            intent.putExtra("eventhoster", hoster);
            startActivity(intent);
        }
        finish();
    }
}
