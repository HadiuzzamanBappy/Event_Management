package com.example.bappy.eventmanagement;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class Event_Member_Showing extends AppCompatActivity {

    String eventid,eventname;
    ProgressDialog progressDialog;

    ArrayList<EventMemberJava> addmember;
    private RecyclerView recyclerView;

    EventMemberAdapter eventMemberAdapter;
    private FloatingActionButton floatingActionButton;

    android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_member_showing_layout);

        toolbar=(Toolbar)findViewById(R.id.eventmembertoolbar);
        setSupportActionBar(toolbar);

        eventid=getIntent().getStringExtra("eventid");
        eventname=getIntent().getStringExtra("eventname");

        toolbar.setTitle(eventname);

        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        addmember=new ArrayList<>();

        new MemberBackgroundTask().execute();


        recyclerView = (RecyclerView) findViewById(R.id.eventmemberlist);
        //initialize the foodadapter
        eventMemberAdapter = new EventMemberAdapter(addmember);
        //seeting adapter in the listview
        recyclerView.setAdapter(eventMemberAdapter);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        floatingActionButton=(FloatingActionButton)findViewById(R.id.eventdescriptionclick);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                Intent intent=new Intent(Event_Member_Showing.this,Single_Event_Details.class);
                                intent.putExtra("eventid", eventid);
                                intent.putExtra("eventname", eventname);
                                intent.putExtra("pagetype", "2");
                                startActivity(intent);
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
                final AlertDialog.Builder builder=new AlertDialog.Builder(Event_Member_Showing.this);

                View view= LayoutInflater.from(Event_Member_Showing.this).inflate(R.layout.custom_dialog,null);

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

    class EventMemberAdapter extends RecyclerView.Adapter<EventMemberAdapter.EventHolder>
    {
        ArrayList<EventMemberJava> list=new ArrayList();

        EventMemberAdapter(ArrayList<EventMemberJava> list1)
        {
            list=list1;
        }


        @Override
        public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.member_holder_layout,null);
            return new EventHolder(view);
        }

        @Override
        public void onBindViewHolder(final EventHolder holder, int position) {
            final EventMemberJava event=list.get(position);
            holder.name.setText(event.getName());
            holder.email.setText("Email : "+event.getEmail());
            holder.organization.setText("Organization : "+event.getOrganization());
            holder.phone.setText("Phone No : "+event.getPhone());
            holder.city.setText("City : "+event.getCity());
            holder.paymethod.setText("Pay Method : "+event.getPaymethod());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class EventHolder extends RecyclerView.ViewHolder
        {
            TextView name,email,organization,phone,city,paymethod;

            public EventHolder(View itemView) {
                super(itemView);
                name=(TextView)itemView.findViewById(R.id.membername);
                email=(TextView)itemView.findViewById(R.id.memberemail);
                organization=(TextView)itemView.findViewById(R.id.memberorganization);
                phone=(TextView) itemView.findViewById(R.id.memberphone);
                city=(TextView)itemView.findViewById(R.id.membercity);
                paymethod=(TextView) itemView.findViewById(R.id.memberpaymethod);
            }
        }
    }

    class MemberBackgroundTask extends AsyncTask<Void,Void,Boolean> {
        String json_url;
        String JSON_STRING,resul;

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Performing.Please Wait....");
            progressDialog.show();
            json_url = "http://" + getString(R.string.ip_addres) + "/EventManagement/UserEventMember.php";
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputstream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedwritter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                String postdata = URLEncoder.encode("event_id", "UTF-8") + "=" + URLEncoder.encode(eventid, "UTF-8");
                bufferedwritter.write(postdata);
                bufferedwritter.flush();
                bufferedwritter.close();
                outputstream.close();
                InputStream inputstream = httpURLConnection.getInputStream();
                BufferedReader bufferdreader = new BufferedReader(new InputStreamReader(inputstream, "iso-8859-1"));
                resul = "";
                String line = "";
                while ((line = bufferdreader.readLine()) != null) {
                    resul += line;
                }
                bufferdreader.close();
                inputstream.close();
                httpURLConnection.disconnect();

                //returning the string as a result
                String jsonFood= resul.trim();

                //set json string json object
                JSONObject jsonObject=new JSONObject(jsonFood);
                //initialize array from json object
                JSONArray jsonArray=jsonObject.getJSONArray("Server_response");

                int count=0;
                String name,email,organization,phone,city,paymethod;
                while(count<jsonArray.length())
                {
                    //get json object from count length
                    JSONObject jo=jsonArray.getJSONObject(count);
                    name=jo.getString("name");
                    email=jo.getString("email");
                    organization=jo.getString("organization");
                    phone=jo.getString("phone");
                    city=jo.getString("city");
                    paymethod=jo.getString("paymethod");
                    //making it as a java object
                    EventMemberJava event=new EventMemberJava(name,email,organization,phone,city,paymethod);
                    //adding to the adapter
                    addmember.add(event);
                    count++;
                }

                return true;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean result) {
           // Toast.makeText(Event_Member_Showing.this, resul, Toast.LENGTH_SHORT).show();
            progressDialog.cancel();
            if(result) {
                eventMemberAdapter.notifyDataSetChanged();
            }
            else
                Toast.makeText(Event_Member_Showing.this, "Failed...Please try again..", Toast.LENGTH_SHORT).show();
        }
    }

    class EventMemberJava{
        String name,email,organization,phone,city,paymethod;

        public EventMemberJava(String name, String email, String organization, String phone, String city, String paymethod) {
            this.name = name;
            this.email = email;
            this.organization = organization;
            this.phone = phone;
            this.city = city;
            this.paymethod = paymethod;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getOrganization() {
            return organization;
        }

        public void setOrganization(String organization) {
            this.organization = organization;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getPaymethod() {
            return paymethod;
        }

        public void setPaymethod(String paymethod) {
            this.paymethod = paymethod;
        }
    }
}
