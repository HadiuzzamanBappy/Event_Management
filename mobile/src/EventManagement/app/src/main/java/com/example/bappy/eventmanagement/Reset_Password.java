package com.example.bappy.eventmanagement;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Reset_Password extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String id,savePassword;
    EditText oldpass,newpass,newpassagain;
    Button reset;
    String oldpasstext,newpasstext,newpassagaintext;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password_layout);

        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait...");

        sharedPreferences=getSharedPreferences(getString(R.string.PREF_FILE),0);
        id=sharedPreferences.getString(getString(R.string.ID),"");
        savePassword=sharedPreferences.getString(getString(R.string.PASSWORD),"");

        oldpass=(EditText)findViewById(R.id.oldPassword);
        newpass=(EditText)findViewById(R.id.newPassword);
        newpassagain=(EditText)findViewById(R.id.newPasswordagain);

        reset=(Button)findViewById(R.id.resetpass);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldpasstext=oldpass.getText().toString();
                newpasstext=newpass.getText().toString();
                newpassagaintext=newpassagain.getText().toString();
                if(oldpasstext.equals(savePassword))
                {
                    if(!newpasstext.equals(newpassagaintext))
                    {
                        newpass.setError("Password Missmatch");
                        newpassagain.setError("Password Missmatch");
                    }
                    else
                    {
                        newpass.setError(null);
                        newpassagain.setError(null);
                        progressDialog.show();
                        new PasswordResetBackgroud().execute();
                    }
                }
                else
                {
                    oldpass.setError("Old Password Didn't Match");
                    if(!newpasstext.equals(newpassagaintext))
                    {
                        newpass.setError("Password Missmatch");
                        newpassagain.setError("Password Missmatch");
                    }
                    else
                    {
                        newpass.setError(null);
                        newpassagain.setError(null);
                    }
                }
            }
        });
    }
    public class PasswordResetBackgroud extends AsyncTask<Void,Void,String> {

        String json_url;

        @Override
        protected void onPreExecute() {
            json_url="http://"+getString(R.string.ip_addres)+"/EventManagement/ResetPassword.php";
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
                String post= URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(id,"UTF-8")
                        + "&" + URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(newpasstext,"UTF-8");
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
            Toast.makeText(Reset_Password.this, s, Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
