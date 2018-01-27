package com.example.bappy.eventmanagement;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

public class Login extends AppCompatActivity {

    EditText email,password;
    ImageButton emailquestion,emailok,passswordvisible;
    Button enter,signup;
    private ProgressDialog progressDialog;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TextView needHelp;
    String emailstring;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        sharedPreferences=getSharedPreferences(getString(R.string.PREF_FILE),0);
        editor=sharedPreferences.edit();

        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);

        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        emailok=(ImageButton)findViewById(R.id.doneemail);
        emailquestion=(ImageButton)findViewById(R.id.whatemail);
        passswordvisible=(ImageButton)findViewById(R.id.visiblepass);
        enter=(Button) findViewById(R.id.enter);
        signup=(Button) findViewById(R.id.signup);
        needHelp=(TextView) findViewById(R.id.needhelp);

        //email.setText("hbappy79@gmail.com");
        //password.setText("bappy");
        emailok.setVisibility(View.GONE);

        needHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder=new AlertDialog.Builder(Login.this);
                View view= LayoutInflater.from(Login.this).inflate(R.layout.custom_dialog_need_help,null);

                final EditText emailtext=(EditText) view.findViewById(R.id.emailenter);
                ImageView closedialog=(ImageView)view.findViewById(R.id.close);

                builder.setView(view);
                Button submittext=(Button)view.findViewById(R.id.submit);

                final AlertDialog alert=builder.create();
                closedialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.cancel();
                    }
                });
                submittext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        emailstring=emailtext.getText().toString().trim();
                        if(!emailstring.equals(""))
                        {
                            if( android.util.Patterns.EMAIL_ADDRESS.matcher(emailstring).matches() && emailstring.contains("@gmail.com") || emailstring.contains("@yahoo.com")) {
                                email.setError(null);
                                progressDialog.setMessage("Please Wait....");
                                progressDialog.show();
                                new Handler().postDelayed(
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                new forgotpasswordBackground().execute();
                                                alert.cancel();
                                            }
                                        },1000);
                            }
                            else {
                                email.setError("Input correct Type email");
                            }
                        }
                        else
                        {
                            email.setError("Input Your email");
                        }
                    }
                });
                alert.show();
            }
        });

        emailquestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder=new AlertDialog.Builder(Login.this);

                View view= LayoutInflater.from(Login.this).inflate(R.layout.custom_dialog,null);

                TextView bodymessage=(TextView)view.findViewById(R.id.message);
                bodymessage.setText("This is about giving email ID\nSuch as ******@gmail.com");

                ImageView closedialog=(ImageView)view.findViewById(R.id.close);

                RelativeLayout relativeLayout=(RelativeLayout)view.findViewById(R.id.buttonlayout);
                relativeLayout.setVisibility(View.GONE);

                builder.setView(view);

                final AlertDialog alert=builder.create();

                closedialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.cancel();
                    }
                });
                alert.show();
            }
        });
        (email).setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
    /* When focus is lost check that the text field
    * has valid values.
    */          if(!hasFocus)
                validateInputEmail(v);
            }
        });
        (password).setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
    /* When focus is lost check that the text field
    * has valid values.
    */
                validateInputPassword(v);
            }
        });
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailstring=email.getText().toString().trim();
                String passwordstring=password.getText().toString().trim();
                int e=0,p=0;
                if(emailstring.equals("")) {
                    email.setError("Please Enter Your email!!!!");
                    emailquestion.setVisibility(View.VISIBLE);
                    emailok.setVisibility(View.GONE);
                }
                else
                {
                    if(android.util.Patterns.EMAIL_ADDRESS.matcher(emailstring).matches()) {
                        e = 1;
                        emailquestion.setVisibility(View.GONE);
                        emailok.setVisibility(View.VISIBLE);
                    }
                    else {
                        email.setError("Invalid email!!!!");
                        emailquestion.setVisibility(View.VISIBLE);
                        emailok.setVisibility(View.GONE);
                    }
                }

                if(passwordstring.equals(""))
                    password.setError("Please Enter Your Password!!!!");
                else
                {
                   // Toast.makeText(Login.this, Integer.toString(passwordstring.length()), Toast.LENGTH_SHORT).show();
                    if(passwordstring.length()<4 || passwordstring.length()>8)
                        password.setError("Password must be between 4-8 length");
                    else
                        p=1;
                }
                if(e==1 && p==1) {
                    progressDialog.setMessage("Please Wait....");
                    progressDialog.show();

                    new Handler().postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                    new LoginBackground().execute();
                                }
                            },2000);
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Register.class));
            }
        });

        passswordvisible.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        password.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;
                    case MotionEvent.ACTION_UP:
                        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        break;
                }
                return true;
            }
        });
    }
    public void validateInputEmail(View view)
    {
        String emailstring=email.getText().toString().trim();
        if(!emailstring.equals(""))
        {
            if( android.util.Patterns.EMAIL_ADDRESS.matcher(emailstring).matches() && emailstring.contains("@gmail.com") || emailstring.contains("@yahoo.com")) {
                emailquestion.setVisibility(View.GONE);
                emailok.setVisibility(View.VISIBLE);
            }
            else {
                email.setError("Input correct Type email");
                emailquestion.setVisibility(View.VISIBLE);
                emailok.setVisibility(View.GONE);
            }
        }
        else
        {
            email.setError("Input Your email");
            emailquestion.setVisibility(View.VISIBLE);
            emailok.setVisibility(View.GONE);
        }
    }
    public void validateInputPassword(View view)
    {
        String pass=password.getText().toString().trim();
        if(!pass.equals(""))
        {
            if(pass.length()<4 || pass.length()>8)
                password.setError("Password must be between 4-8 length");
        }
    }
    public class LoginBackground extends AsyncTask<Void,Void,String> {

        String json_url;

        @Override
        protected void onPreExecute() {
            json_url="http://"+getString(R.string.ip_addres)+"/EventManagement/Login.php";
        }

        @Override
        protected String doInBackground(Void... strings) {
            try{
                String emailstring=email.getText().toString().trim();
                String passwordstring=password.getText().toString().trim();

                URL url=new URL(json_url);
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post= URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(emailstring,"UTF-8")
                        + "&" + URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(passwordstring,"UTF-8");
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
        protected void onPostExecute(String s) {
            progressDialog.cancel();
            final AlertDialog.Builder builder=new AlertDialog.Builder(Login.this);

            View view= LayoutInflater.from(Login.this).inflate(R.layout.custom_dialog,null);

            TextView bodymessage=(TextView)view.findViewById(R.id.message);

            ImageView closedialog=(ImageView)view.findViewById(R.id.close);

            builder.setView(view);
            if(s.contains("Wrong"))
            {
                bodymessage.setText(s);

                final AlertDialog alert=builder.create();

                RelativeLayout relativeLayout=(RelativeLayout)view.findViewById(R.id.buttonlayout);
                relativeLayout.setVisibility(View.GONE);

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
                editor.putString(getString(R.string.EMAIL),email.getText().toString().trim());
                editor.putString(getString(R.string.PASSWORD),password.getText().toString().trim());
                editor.putBoolean(getString(R.string.SAVE),true);
                JSONObject jsonObject= null;
                try {
                    jsonObject = new JSONObject(s);
                    JSONArray jsonArray=jsonObject.getJSONArray("Server_response");

                    int count=0;
                    String id,username,phone,city;

                    JSONObject jo=jsonArray.getJSONObject(count);
                    id=jo.getString("id");
                    username=jo.getString("username");
                    phone=jo.getString("phone");
                    city=jo.getString("city");

                    editor.putString(getString(R.string.ID),id);
                    editor.putString(getString(R.string.USERNAME),username);
                    editor.putString(getString(R.string.PHONE),phone);
                    editor.putString(getString(R.string.CITY),city);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                editor.commit();
                startActivity(new Intent(Login.this,Event_Showing.class));
                finish();
            }
        }
    }

    public class forgotpasswordBackground extends AsyncTask<Void,Void,String> {

        String json_url;

        @Override
        protected void onPreExecute() {
            json_url="http://"+getString(R.string.ip_addres)+"/EventManagement/ForgotPassword.php";
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
                String post= URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(emailstring,"UTF-8");
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
        protected void onPostExecute(String s) {
            progressDialog.cancel();
            final AlertDialog.Builder builder=new AlertDialog.Builder(Login.this);

            View view= LayoutInflater.from(Login.this).inflate(R.layout.custom_dialog,null);

            TextView bodymessage=(TextView)view.findViewById(R.id.message);

            ImageView closedialog=(ImageView)view.findViewById(R.id.close);

            builder.setView(view);
            if(!s.contains("no")) {
                new SendMailTask(Login.this).execute("hbappy79@gmail.com",
                        "1829650117", emailstring, "Recover Password",
                        "Your Password is "+s);
                bodymessage.setText("Your Password has been sent to your EMAIL ID");
            }
            else {
                bodymessage.setText("What email you have given us is not listed as a account");
            }

            final AlertDialog alert=builder.create();

            RelativeLayout relativeLayout=(RelativeLayout)view.findViewById(R.id.buttonlayout);
            relativeLayout.setVisibility(View.GONE);

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
