package com.example.bappy.eventmanagement;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Single_Event_Details extends AppCompatActivity {

    String eventid,eventname;

    private ProgressDialog progressDialog;

    FloatingActionButton floatingActionButton;

    TextView hosternametext,hosteremailtext,StartRegistrationDatetext,EndRegistrationDatetext,EventStartDatetext,
            OrganizedBytext,Venuetext,ContactNotext,EventhLengthtext,Feestext,abouttext,remaintext,registrationopen;

    String title,hostername,hosteremail,StartRegistrationDate,EndRegistrationDate,EventStartDate,OrganizedBy,Venue,
            ContactNo,EventhLength,Fees,about;

    android.support.v7.widget.Toolbar toolbar;

    String daytext, secondtext, minutetext, hourtext,pagetype,hoster=null,userid;
    Boolean eventStartOk,eventEndOk,regEnd;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_event_details_layout);

        sharedPreferences=getSharedPreferences(getString(R.string.PREF_FILE),0);
        userid=sharedPreferences.getString(getString(R.string.ID),"NO");

        eventid=getIntent().getStringExtra("eventid");
        eventname=getIntent().getStringExtra("eventname");
        pagetype=getIntent().getStringExtra("pagetype");

        if(pagetype.equals("1"))
            hoster=getIntent().getStringExtra("eventhoster");

        toolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.singleeventactionbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(eventname);

        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait....");
        progressDialog.show();

        new SingleEventDetailsBackground().execute();

        floatingActionButton=(FloatingActionButton)findViewById(R.id.enterevent);
        hosternametext=(TextView)findViewById(R.id.hosternameview);
        hosteremailtext=(TextView)findViewById(R.id.hosteremailview);
        StartRegistrationDatetext=(TextView)findViewById(R.id.registrationStartdateview);
        EndRegistrationDatetext=(TextView)findViewById(R.id.registrationEnddateview);
        EventStartDatetext=(TextView)findViewById(R.id.eventStartdateview);
        EventhLengthtext=(TextView)findViewById(R.id.eventLengthview);
        OrganizedBytext=(TextView)findViewById(R.id.organizedByview);
        Venuetext=(TextView)findViewById(R.id.venueview);
        ContactNotext=(TextView)findViewById(R.id.phoneNoview);
        Feestext=(TextView)findViewById(R.id.feesView);
        abouttext=(TextView)findViewById(R.id.aboutview);

        remaintext=(TextView)findViewById(R.id.remainingtimeheader);
        registrationopen=(TextView)findViewById(R.id.registrationenabler);

        if(pagetype.equals("2"))
            floatingActionButton.setVisibility(View.GONE);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pagetype.equals("1") && !hoster.equals(null) && hoster.equals(userid))
                    Toast.makeText(Single_Event_Details.this, "Sorry, You Are The Creator", Toast.LENGTH_SHORT).show();
                else{
                    final ProgressDialog progressDialog = new ProgressDialog(Single_Event_Details.this);
                    progressDialog.setMessage("Please Wait....");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    new Handler().postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.cancel();
                                    Intent intent = new Intent(Single_Event_Details.this, Event_Registration.class);
                                    intent.putExtra("eventid", eventid);
                                    intent.putExtra("class", "2");
                                    intent.putExtra("eventname", eventname);
                                    intent.putExtra("pagetype", pagetype);
                                    intent.putExtra("eventhoster", hoster);
                                    startActivity(intent);
                                    finish();
                                }
                            }, 2000);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.option_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.about:
                final AlertDialog.Builder builder=new AlertDialog.Builder(Single_Event_Details.this);

                View view= LayoutInflater.from(Single_Event_Details.this).inflate(R.layout.custom_dialog,null);

                TextView bodymessage=(TextView)view.findViewById(R.id.message);

                ImageView closedialog=(ImageView)view.findViewById(R.id.close);

                builder.setView(view);
                bodymessage.setText("This is about Event Managing.\nWe all know that evnt creating become an usual purpose of " +
                        "our daily life.\n\nBy this application one can create event and can see how many member registered in their " +
                        "event.\n\nThe Admin can see member details also.\n :) :)");
                RelativeLayout relativeLayout=(RelativeLayout)view.findViewById(R.id.buttonlayout);
                relativeLayout.setVisibility(View.GONE);
                final AlertDialog alert=builder.create();

                closedialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.cancel();
                    }
                });
                alert.show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    public class SingleEventDetailsBackground extends AsyncTask<Void,Void,Boolean> {

        String json_url,result;

        @Override
        protected void onPreExecute() {
            json_url="http://"+getString(R.string.ip_addres)+"/EventManagement/SingleEventDetails.php";
        }

        @Override
        protected Boolean doInBackground(Void... strings) {
            try{
                URL url=new URL(json_url);
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post= URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(eventid,"UTF-8");
                bufferedWriter.write(post);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                result="";
                String line="";
                while((line=bufferedReader.readLine()) !=null)
                {
                    result+=line;
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray=jsonObject.getJSONArray("Server_response");

                    int count=0;

                    JSONObject jo=jsonArray.getJSONObject(count);

                    title=jo.getString("title");
                    hostername=jo.getString("hostername");
                    hosteremail=jo.getString("hosteremail");
                    StartRegistrationDate=jo.getString("StartRegistrationDate");
                    EndRegistrationDate=jo.getString("EndRegistrationDate");
                    EventStartDate=jo.getString("EventStartDate");
                    OrganizedBy=jo.getString("OrganizedBy");
                    Venue=jo.getString("Venue");
                    ContactNo=jo.getString("ContactNo");
                    EventhLength=jo.getString("EventhLength");
                    Fees=jo.getString("Fees");
                    about=jo.getString("about");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return true;
            }catch(Exception e)
            {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean s) {
            //Toast.makeText(Single_Event_Details.this, result+title, Toast.LENGTH_SHORT).show();

            progressDialog.cancel();
            if(!s)
                Toast.makeText(Single_Event_Details.this, "Try Again", Toast.LENGTH_SHORT).show();
            else {
                setTheName();
            }
        }
    }

    public void setTheName()
    {
        hosternametext.setText("Event Hoster: "+hostername);
        hosteremailtext.setText("Event Hoster Email: "+hosteremail);
        StartRegistrationDatetext.setText("Registration Date Start: "+StartRegistrationDate);
        EndRegistrationDatetext.setText("Registration Date End: "+EndRegistrationDate);
        EventStartDatetext.setText("Event Date Start: "+EventStartDate);
        EventhLengthtext.setText("Event Length: "+EventhLength);
        OrganizedBytext.setText("Organized By: "+OrganizedBy);
        Venuetext.setText("Venue: "+Venue);
        ContactNotext.setText("For Any Details: "+ContactNo);
        Feestext.setText("Initial Fees: "+Fees);
        abouttext.setText(" ** "+title+": "+about);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                String present_day=new SimpleDateFormat("yyyy-M-dd hh:mm:ss").format(Calendar.getInstance().getTime());
                String target_day=EventStartDate+" 24:00:00";
                String registrationendDate=EndRegistrationDatetext+" 24:00:00";

                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-M-dd hh:mm:ss");

                try{
                    Date present_day_date=simpleDateFormat.parse(present_day);
                    Date target_day_date=simpleDateFormat.parse(target_day);
                    Date registrationendDatet_day_date=simpleDateFormat.parse(target_day);
                    if(present_day_date.getTime()>registrationendDatet_day_date.getTime())
                        regEnd=true;
                    else
                        regEnd=false;
                    if(present_day_date.getTime()>target_day_date.getTime()){
                        eventStartOk=true;
                        if(present_day_date.getTime()>(target_day_date.getTime()+(Integer.parseInt(EventhLength)*3600000*24)))
                            eventEndOk=true;
                        else
                            eventEndOk=false;
                    }
                    else {
                        eventStartOk=false;
                        long time = target_day_date.getTime() - present_day_date.getTime();
                        long second = time / 1000 % 60;
                        long minute = time / (1000 * 60) % 60;
                        long hours = time / (1000 * 60 * 60) % 24;
                        long day = time / (1000 * 60 * 60 * 24);
                        if (second < 9)
                            secondtext = ":0" + Long.toString(second);
                        else
                            secondtext = ":" + Long.toString(second);
                        if (minute < 9)
                            minutetext = ":0" + Long.toString(minute);
                        else
                            minutetext = ":" + Long.toString(minute);
                        if (hours < 9)
                            hourtext = " d 0" + Long.toString(hours);
                        else
                            hourtext = " d " + Long.toString(hours);
                        if (day < 9)
                            daytext = "0" + Long.toString(day);
                        else
                            daytext =Long.toString(day);
                    }
                }catch (Exception e){
                    System.out.print("Didnt work"+e);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!eventStartOk)
                            remaintext.setText(daytext+hourtext+minutetext+secondtext+" To Go");
                        else
                        {
                            if(eventEndOk)
                                remaintext.setText("Event Ended.");
                            else
                                remaintext.setText("Event Started.");
                        }
                        if(regEnd) {
                            registrationopen.setText("Registration Closed");
                            floatingActionButton.setVisibility(View.GONE);
                        }
                        else
                            registrationopen.setText("Registration Open Still");
                    }
                });
            }
        }, 0, 1000);
    }
}
