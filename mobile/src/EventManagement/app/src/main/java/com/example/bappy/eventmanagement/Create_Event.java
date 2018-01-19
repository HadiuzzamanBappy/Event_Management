package com.example.bappy.eventmanagement;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import java.util.Calendar;

public class Create_Event extends AppCompatActivity {

    EditText title,eventLength,organizedby,venue,fees,phone,about;
    TextView registrationStartdate,registrationEnddate,eventStartdate;
    Button createEvent;

    Calendar registrationStartcalender,registrationEndcalender,eventStartcalender;
    DatePickerDialog.OnDateSetListener startRegistration,endRegistration,startEvent;

    String titletext,abou,regstart,regend,eventstart,eventlen,organize,ven,phon,fee,id;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    ProgressDialog progressDialog;
    Boolean ok;

    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event_layout);

        progressDialog=new ProgressDialog(Create_Event.this);
        progressDialog.setCancelable(false);

        sharedPreferences=getSharedPreferences(getString(R.string.PREF_FILE),0);
        editor=sharedPreferences.edit();

        id=sharedPreferences.getString(getString(R.string.ID),"NO");

        title=(EditText)findViewById(R.id.eventtitle);
        registrationStartdate=(TextView) findViewById(R.id.registrationstart);
        registrationEnddate=(TextView)findViewById(R.id.registrationend);
        eventStartdate=(TextView)findViewById(R.id.eventstartdate);
        eventLength=(EditText)findViewById(R.id.eventlength);
        organizedby=(EditText)findViewById(R.id.organizedby);
        venue=(EditText)findViewById(R.id.venue);
        fees=(EditText)findViewById(R.id.fees);
        phone=(EditText)findViewById(R.id.phone);
        about=(EditText)findViewById(R.id.about);

        phone.setText("01");

        createEvent=(Button)findViewById(R.id.create);
        scrollView=(ScrollView)findViewById(R.id.scrooling);

        registrationStartcalender=Calendar.getInstance();

        startRegistration=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                registrationStartcalender.set(Calendar.YEAR,year);
                registrationStartcalender.set(Calendar.MONTH,month);
                registrationStartcalender.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                updateRegistartionStart();
            }
        };

        registrationEndcalender=Calendar.getInstance();

        endRegistration=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                registrationEndcalender.set(Calendar.YEAR,year);
                registrationEndcalender.set(Calendar.MONTH,month);
                registrationEndcalender.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                updateRegistartionEnd();
            }
        };

        eventStartcalender=Calendar.getInstance();
       startEvent=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                eventStartcalender.set(Calendar.YEAR,year);
                eventStartcalender.set(Calendar.MONTH,month);
                eventStartcalender.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                updateEventStart();
            }
        };

       createEvent.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               titletext=title.getText().toString().trim();
               if(titletext.equals("")) {
                   title.setError("Please Fill the Event Title");
                   ok = false;
               }
               else {
                   title.setError(null);
                   ok=true;
               }

               regstart=registrationStartdate.getText().toString().trim();
               if(regstart.equals("Registration Start Date")){
                   registrationStartdate.setError("Please Check The Registration start date");
                   ok = false;
               }
               else{
                   registrationStartdate.setError(null);
                   ok=true;
               }

               regend=registrationEnddate.getText().toString().trim();
               if(regend.equals("Registration End Date")){
                   registrationEnddate.setError("Please Check The Registration end date");
                   ok = false;
               }
               else{
                   registrationEnddate.setError(null);
                   ok=true;
               }

               eventstart=eventStartdate.getText().toString().trim();
               if(eventstart.equals("Event Start Date")){
                   eventStartdate.setError("Please Check The Event start date");
                   ok = false;
               }
               else{
                   eventStartdate.setError(null);
                   ok=true;
               }

               eventlen=eventLength.getText().toString().trim();
               if(eventlen.equals("")){
                   eventLength.setError("Please Check Event length");
                   ok = false;
               }
               else{
                   eventLength.setError(null);
                   ok=true;
               }

               organize=organizedby.getText().toString().trim();
               if(organize.equals("")){
                   organizedby.setError("Please Check Organizations");
                   ok = false;
               }
               else{
                   organizedby.setError(null);
                   ok=true;
               }

               ven=venue.getText().toString().trim();
               if(ven.equals("")){
                   venue.setError("Please Check The Venue");
                   ok = false;
               }
               else{
                   venue.setError(null);
                   ok=true;
               }

               phon=phone.getText().toString().trim();
               if(phone.equals(""))
               {
                   phone.setError("Please Enter Your Phone Number");
                   ok = false;
               }
               else
               {
                   if(phon.length()==11) {
                       phone.setError(null);
                       ok=true;
                   }
                   else
                   {
                       phone.setError("Your Phone Number is wrong");
                       ok = false;
                   }
               }

               fee=fees.getText().toString().trim();
               if(fee.equals("")){
                   fees.setError("Please set Fees");
                   ok = false;
               }
               else{
                   fees.setError(null);
                   ok=true;
               }

               abou=about.getText().toString().trim();
               if(abou.equals("")){
                   about.setError("Please say something about Your Event");
                   ok = false;
               }
               else{
                   about.setError(null);
                   ok=true;
               }
               if(ok && !id.equals("NO"))
               {
                   progressDialog.setMessage("Please wait....");
                   progressDialog.show();
                    new CreationBackground().execute();
               }
               else
               {
                   Snackbar.make(scrollView,"Sorry There is a Error",Snackbar.LENGTH_LONG).show();
               }
           }
       });
    }

    public void updateRegistartionStart()
    {
        String format=registrationStartcalender.get(Calendar.YEAR)+"-"+registrationStartcalender.get(Calendar.MONTH)+1
                +"-"+registrationStartcalender.get(Calendar.DAY_OF_MONTH);
        registrationStartdate.setText(format);
    }
    public void updateRegistartionEnd()
    {
        String format=registrationEndcalender.get(Calendar.YEAR)+"-"+registrationEndcalender.get(Calendar.MONTH)+1
                +"-"+registrationEndcalender.get(Calendar.DAY_OF_MONTH);
        registrationEnddate.setText(format);
    }
    public void updateEventStart()
    {
        String format=eventStartcalender.get(Calendar.YEAR)+"-"+eventStartcalender.get(Calendar.MONTH)+1
                +"-"+eventStartcalender.get(Calendar.DAY_OF_MONTH);
        eventStartdate.setText(format);
    }

    public void updateRegistrationStartCalender(View view)
    {
        new DatePickerDialog(Create_Event.this,startRegistration,registrationStartcalender.get(Calendar.YEAR)
                ,registrationStartcalender.get(Calendar.MONTH),registrationStartcalender.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void updateRegistrationEndCalender(View view)
    {
        new DatePickerDialog(Create_Event.this,endRegistration,registrationEndcalender.get(Calendar.YEAR)
                ,registrationEndcalender.get(Calendar.MONTH),registrationEndcalender.get(Calendar.DAY_OF_MONTH)).show();
    }
    public void updateEventStartCalender(View view)
    {
        new DatePickerDialog(Create_Event.this,startEvent,eventStartcalender.get(Calendar.YEAR)
                ,eventStartcalender.get(Calendar.MONTH),eventStartcalender.get(Calendar.DAY_OF_MONTH)).show();
    }

    public class CreationBackground extends AsyncTask<Void,Void,String> {

        String json_url;

        @Override
        protected void onPreExecute() {
            json_url="http://"+getString(R.string.ip_addres)+"/EventManagement/CreateEvent.php";
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
                String post= URLEncoder.encode("title","UTF-8")+"="+URLEncoder.encode(titletext,"UTF-8")
                        + "&" + URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(id,"UTF-8")
                        + "&" + URLEncoder.encode("registrationstart","UTF-8")+"="+URLEncoder.encode(regstart,"UTF-8")
                        + "&" + URLEncoder.encode("registrationend","UTF-8")+"="+URLEncoder.encode(regend,"UTF-8")
                        + "&" + URLEncoder.encode("eventstart","UTF-8")+"="+URLEncoder.encode(eventstart,"UTF-8")
                        + "&" + URLEncoder.encode("eventlength","UTF-8")+"="+URLEncoder.encode(eventlen,"UTF-8")
                        + "&" + URLEncoder.encode("organizedby","UTF-8")+"="+URLEncoder.encode(organize,"UTF-8")
                        + "&" + URLEncoder.encode("venue","UTF-8")+"="+URLEncoder.encode(ven,"UTF-8")
                        + "&" + URLEncoder.encode("phone","UTF-8")+"="+URLEncoder.encode(phon,"UTF-8")
                        + "&" + URLEncoder.encode("fees","UTF-8")+"="+URLEncoder.encode(fee,"UTF-8")
                        + "&" + URLEncoder.encode("about","UTF-8")+"="+URLEncoder.encode(abou,"UTF-8");
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
            final AlertDialog.Builder builder=new AlertDialog.Builder(Create_Event.this);

            View view= LayoutInflater.from(Create_Event.this).inflate(R.layout.custom_dialog,null);

            TextView bodymessage=(TextView)view.findViewById(R.id.message);

            ImageView closedialog=(ImageView)view.findViewById(R.id.close);

            builder.setView(view);
            if(!s.contains("Event Already Exist"))
            {
                bodymessage.setText(s);
                Button positive=(Button)view.findViewById(R.id.positive);
                Button negative=(Button)view.findViewById(R.id.negative);
                positive.setVisibility(View.GONE);
                negative.setText("OK");
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Create_Event.this,Event_Showing.class));
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
        startActivity(new Intent(Create_Event.this,Event_Showing.class));
        finish();
    }
}
