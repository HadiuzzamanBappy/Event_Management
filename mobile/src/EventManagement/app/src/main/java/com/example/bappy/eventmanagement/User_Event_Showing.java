package com.example.bappy.eventmanagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import android.widget.Button;
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

public class User_Event_Showing extends AppCompatActivity {

    RecyclerView recyclerView;

    ArrayList<Event> addevent;

    ProgressDialog progressDialog;

    EventAdapter eventAdapter;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String userid;

    android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_event_showing_layout);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        sharedPreferences=getSharedPreferences(getString(R.string.PREF_FILE),0);
        editor=sharedPreferences.edit();

        userid=sharedPreferences.getString(getString(R.string.ID),"NO");

        progressDialog=new ProgressDialog(this);
        //progressDialog.setTitle("Attention");
        progressDialog.setCancelable(false);

        addevent = new ArrayList<>();
        new BackgroundEventDetails().execute();

        recyclerView = (RecyclerView) findViewById(R.id.eventlist);
        //initialize the foodadapter
        eventAdapter = new EventAdapter(addevent);
        //seeting adapter in the listview
        recyclerView.setAdapter(eventAdapter);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

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
                final AlertDialog.Builder builder=new AlertDialog.Builder(User_Event_Showing.this);

                View view= LayoutInflater.from(User_Event_Showing.this).inflate(R.layout.custom_dialog,null);

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

    class BackgroundEventDetails extends AsyncTask<Void,Void,Boolean>
    {

        String json_url;
        String JSON_STRING;
        String jsonFood;

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading.Please Wait....");
            progressDialog.show();
            //making a link to php file for reading data from database
            json_url="http://"+getString(R.string.ip_addres)+"/EventManagement/UserEventList.php";
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                URL url=new URL(json_url);
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post= URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(userid,"UTF-8");
                bufferedWriter.write(post);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                //getting input to the program from database
                InputStream inputStream=httpURLConnection.getInputStream();
                //reading data from database through php file
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                //initialize a string-builder
                StringBuilder stringBuilder=new StringBuilder();
                while((JSON_STRING=bufferedReader.readLine())!=null){
                    //adding string to the string builder
                    stringBuilder.append(JSON_STRING+"\n");
                }
                //closing all connection
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                //returning the string as a result
                jsonFood= stringBuilder.toString().trim();

                //set json string json object
                JSONObject jsonObject=new JSONObject(jsonFood);
                //initialize array from json object
                JSONArray jsonArray=jsonObject.getJSONArray("Server_response");

                int count=0;
                String id,name,lastdate,eventStartdate,eventhoster,EventhLength;
                while(count<jsonArray.length())
                {
                    //get json object from count length
                    JSONObject jo=jsonArray.getJSONObject(count);
                    id=jo.getString("id");
                    name=jo.getString("name");
                    lastdate=jo.getString("lastdate");
                    eventStartdate=jo.getString("eventStartdate");
                    eventhoster=jo.getString("eventhoster");
                    EventhLength=jo.getString("EventhLength");
                    //making it as a java object
                    Event event=new Event(id,name,lastdate,eventStartdate,eventhoster,EventhLength);
                    //adding to the adapter
                    addevent.add(event);
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
            progressDialog.cancel();
            if(result) {
                eventAdapter.notifyDataSetChanged();
            }
            else
                Toast.makeText(User_Event_Showing.this, "Failed...Please try again..", Toast.LENGTH_SHORT).show();
        }
    }

    class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventHolder>
    {
        ArrayList<Event> list=new ArrayList();

        EventAdapter(ArrayList<Event> list1)
        {
            list=list1;
        }


        @Override
        public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.event_single_layout,null);
            return new EventHolder(view);
        }

        @Override
        public void onBindViewHolder(final EventHolder holder, int position) {
            final Event event=list.get(position);
            holder.name.setText(event.getName());
            holder.lastdatetext.setText(event.getLastdate());
            holder.remainingtime.setText(event.getEventStartdate());

            holder.enter.setText("Member");
            holder.enter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        Intent intent = new Intent(User_Event_Showing.this, Event_Member_Showing.class);
                        intent.putExtra("eventid", event.getId());
                        intent.putExtra("eventname", event.getName());
                        startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class EventHolder extends RecyclerView.ViewHolder
        {
            TextView name,lastdatetext,remainingtime;
            Button enter;

            public EventHolder(View itemView) {
                super(itemView);
                name=(TextView)itemView.findViewById(R.id.eventname);
                lastdatetext=(TextView)itemView.findViewById(R.id.lastdatetime);
                remainingtime=(TextView)itemView.findViewById(R.id.eventStartdate);
                enter=(Button)itemView.findViewById(R.id.enterregistration);
            }
        }
    }

    class Event{
        String id,name,lastdate,eventStartdate,eventhoster,eventhLength;

        public Event(String id, String name, String lastdate, String eventStartdate, String eventhoster, String eventhLength) {
            this.id = id;
            this.name = name;
            this.lastdate = lastdate;
            this.eventStartdate = eventStartdate;
            this.eventhoster = eventhoster;
            this.eventhLength = eventhLength;
        }

        public String getEventhlength() {
            return eventhLength;
        }

        public void setEventhlength(String eventhLength) {
            this. eventhLength = eventhLength;
        }

        public String getEventhoster() {
            return eventhoster;
        }

        public void setEventhoster(String eventhoster) {
            this.eventhoster = eventhoster;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLastdate() {
            return lastdate;
        }

        public void setLastdate(String lastdate) {
            this.lastdate = lastdate;
        }

        public String getEventStartdate() {
            return eventStartdate;
        }

        public void setEventStartdate(String eventStartdate) {
            this.eventStartdate = eventStartdate;
        }
    }
}
