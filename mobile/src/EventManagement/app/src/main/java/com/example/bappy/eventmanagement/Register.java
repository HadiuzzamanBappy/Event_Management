package com.example.bappy.eventmanagement;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Register extends Activity {

    EditText firstname,lastname,email,city,phonenumber,password,passwordagain;
    ImageButton firstnameok,lastnameok,emailok,cityok,phonenumberok,passwordok,passwordagainok;

    Boolean firstnameindication,lastnameindiacation,emailindication,cityindication,phonenumberindication,passwordindication,passwordagainindication;

    Button SignUp;
    private ProgressDialog progressDialog;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        sharedPreferences=getSharedPreferences(getString(R.string.PREF_FILE),0);
        editor=sharedPreferences.edit();

        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        firstnameindication=false;
        lastnameindiacation=false;
        emailindication=false;
        cityindication=false;
        passwordindication=false;
        passwordagainindication=false;
        phonenumberindication=false;

        firstname=(EditText)findViewById(R.id.firstname);
        lastname=(EditText)findViewById(R.id.lastname);
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        city=(EditText)findViewById(R.id.city);
        phonenumber=(EditText)findViewById(R.id.phone);
        passwordagain=(EditText)findViewById(R.id.passwordagain);

        firstnameok=(ImageButton)findViewById(R.id.donefirstname);
        lastnameok=(ImageButton)findViewById(R.id.donelasttname);
        emailok=(ImageButton)findViewById(R.id.doneemail);
        cityok=(ImageButton)findViewById(R.id.donecity);
        passwordok=(ImageButton)findViewById(R.id.donepassword);
        passwordagainok=(ImageButton)findViewById(R.id.donepasswordagain);
        phonenumberok=(ImageButton)findViewById(R.id.donephonenumber);

        SignUp=(Button) findViewById(R.id.signup);

        firstnameok.setVisibility(View.GONE);
        lastnameok.setVisibility(View.GONE);
        emailok.setVisibility(View.GONE);
        cityok.setVisibility(View.GONE);
        passwordok.setVisibility(View.GONE);
        passwordagainok.setVisibility(View.GONE);
        phonenumberok.setVisibility(View.GONE);

        phonenumber.setText("01");

        (firstname).setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                checkfirstname(v);
            }

        });
        (lastname).setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                checklasstname(v);
            }
        });
        (phonenumber).setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                    checkphone(v);
            }
        });
        (email).setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                checkemail(v);
            }
        });
        (password).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                checkpassword(v);
            }
        });
        (city).setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                    checkcity(v);
            }
        });
        (passwordagain).setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                checkpasswordagain(v);
            }
        });

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firstnameindication && lastnameindiacation && emailindication && cityindication && phonenumberindication && passwordindication && passwordagainindication)
                {
                    progressDialog.setMessage("Registering..Please Wait......");
                    progressDialog.show();
                    new RegistrationBackground().execute();
                }
                else
                {
                    Toast.makeText(Register.this, "Fix Your Error....", Toast.LENGTH_SHORT).show();
                    checkfirstname(v);
                    checklasstname(v);
                    checkphone(v);
                    checkemail(v);
                    checkpassword(v);
                    checkcity(v);
                    checkpasswordagain(v);
                }
            }
        });
    }
    public void checkfirstname(View view)
    {
        if (firstname.getText().toString().trim().equals("")) {
            firstname.setError("Please Enter Your Firstname");
            firstnameindication = false;
            firstnameok.setVisibility(View.GONE);
        } else {
            firstname.setError(null);
            firstnameindication = true;
            firstnameok.setVisibility(View.VISIBLE);
        }
    }
    public void checklasstname(View view)
    {
        if(lastname.getText().toString().trim().equals(""))
        {
            lastname.setError("Please Enter Your Lastname");
            lastnameindiacation=false;
            lastnameok.setVisibility(View.GONE);
        }
        else
        {
            lastname.setError(null);
            lastnameindiacation=true;
            lastnameok.setVisibility(View.VISIBLE);
        }
    }
    public void checkemail(View view)
    {
        String emailstring=email.getText().toString().trim();
        if(!emailstring.equals(""))
        {
            if( android.util.Patterns.EMAIL_ADDRESS.matcher(emailstring).matches() && emailstring.contains("@gmail.com") || emailstring.contains("@yahoo.com")) {
                emailindication=true;
                emailok.setVisibility(View.VISIBLE);
                email.setError(null);
            }
            else {
                email.setError("Input correct Type email");
                emailindication=false;
                emailok.setVisibility(View.GONE);
            }
        }
        else
        {
            email.setError("Input Your email");
            emailindication=false;
            emailok.setVisibility(View.GONE);
        }
    }
    public void checkcity(View view)
    {
        if(city.getText().toString().trim().equals(""))
        {
            city.setError("Please Enter Your city");
            cityindication=false;
            cityok.setVisibility(View.GONE);
        }
        else
        {
            cityindication=true;
            cityok.setVisibility(View.VISIBLE);
            city.setError(null);
        }
    }
    public void checkphone(View view)
    {
        if(phonenumber.getText().toString().trim().equals(""))
        {
            phonenumber.setError("Please Enter Your Phone Number");
            phonenumberindication=false;
            phonenumberok.setVisibility(View.GONE);
        }
        else
        {
            if(phonenumber.getText().toString().trim().length()==11) {
                phonenumber.setError(null);
                phonenumberindication = true;
                phonenumberok.setVisibility(View.VISIBLE);
            }
            else
            {
                phonenumber.setError("Your Phone Number is wrong");
                phonenumberindication=false;
                phonenumberok.setVisibility(View.GONE);
            }
        }
    }
    public void checkpassword(View view)
    {
        String pass=password.getText().toString().trim();
        if(!pass.equals(""))
        {
            if(pass.length()<4 || pass.length()>8) {
                password.setError("Password must be between 4-8 length");
                passwordindication=false;
                passwordok.setVisibility(View.GONE);
            }
            else
            {
                passwordindication=true;
                passwordok.setVisibility(View.VISIBLE);
                password.setError(null);
            }
        }
        else
        {
            password.setError("Input Your Password");
            passwordindication=false;
            passwordok.setVisibility(View.GONE);
        }
    }
    public void checkpasswordagain(View view)
    {
        String pass=passwordagain.getText().toString().trim();
        if(!pass.equals(""))
        {
            if(pass.length()<4 || pass.length()>8) {
                passwordagain.setError("Password must be between 4-8 length");
                passwordagainindication=false;
                passwordagainok.setVisibility(View.GONE);
            }
            else
            {
                if(pass.equals(password.getText().toString().trim())) {
                    passwordagainindication = true;
                    passwordagainok.setVisibility(View.VISIBLE);
                    passwordagain.setError(null);
                }
                else
                {
                    passwordagain.setError("Password Didn't Match");
                    passwordagainindication=false;
                    passwordagainok.setVisibility(View.GONE);
                }
            }
        }
        else
        {
            passwordagain.setError("Input Your Password Again");
            passwordagainindication=false;
            passwordagainok.setVisibility(View.GONE);
        }
    }

    public class RegistrationBackground extends AsyncTask<Void,Void,String>{

        String json_url;

        @Override
        protected void onPreExecute() {
           json_url="http://"+getString(R.string.ip_addres)+"/EventManagement/Registration.php";
        }

        @Override
        protected String doInBackground(Void... strings) {
            try{
                String namesend=firstname.getText().toString().trim()+" "+lastname.getText().toString().trim();
                String emailsend=email.getText().toString().trim();
                String citysend=city.getText().toString().trim();
                String phonesend=phonenumber.getText().toString().trim();
                String passwordsend=password.getText().toString().trim();

                URL url=new URL(json_url);
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post= URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(namesend,"UTF-8")
                        + "&" + URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(emailsend,"UTF-8")
                        + "&" + URLEncoder.encode("city","UTF-8")+"="+URLEncoder.encode(citysend,"UTF-8")
                        + "&" + URLEncoder.encode("phone","UTF-8")+"="+URLEncoder.encode(phonesend,"UTF-8")
                        + "&" + URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(passwordsend,"UTF-8");
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
            final AlertDialog.Builder builder=new AlertDialog.Builder(Register.this);

            View view= LayoutInflater.from(Register.this).inflate(R.layout.custom_dialog,null);

            TextView bodymessage=(TextView)view.findViewById(R.id.message);

            ImageView closedialog=(ImageView)view.findViewById(R.id.close);

            builder.setView(view);
           if(!s.contains("Email"))
           {
               bodymessage.setText("Your Registration Successed");
               Button positive=(Button)view.findViewById(R.id.positive);
               Button negative=(Button)view.findViewById(R.id.negative);
               positive.setText("Continue");
               positive.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       editor.putString(getString(R.string.ID),s);
                       editor.putString(getString(R.string.EMAIL),email.getText().toString().trim());
                       editor.putString(getString(R.string.PASSWORD),password.getText().toString().trim());
                       editor.putString(getString(R.string.USERNAME),firstname.getText().toString().trim()+" "+lastname.getText().toString().trim());
                       editor.putString(getString(R.string.PHONE),phonenumber.getText().toString().trim());
                       editor.putString(getString(R.string.CITY),city.getText().toString().trim());
                       editor.putBoolean(getString(R.string.SAVE),true);
                       editor.commit();
                       startActivity(new Intent(Register.this,Event_Showing.class));
                       finish();
                   }
               });

               negative.setText("Login");
               negative.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       startActivity(new Intent(Register.this, Login.class));
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
}
