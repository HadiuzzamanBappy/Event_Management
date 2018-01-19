package com.example.bappy.eventmanagement;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Event_Showing extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    RecyclerView recyclerView;
    ArrayList<Event> addevent;
    ProgressDialog progressDialog;
    EventAdapter eventAdapter;

    FloatingActionButton floatingActionButton;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String userid;

    Toolbar toolbar;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_showing_layout);

        sharedPreferences=getSharedPreferences(getString(R.string.PREF_FILE),0);
        editor=sharedPreferences.edit();

        userid=sharedPreferences.getString(getString(R.string.ID),"NO");

        floatingActionButton=(FloatingActionButton)findViewById(R.id.addevent);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog=new ProgressDialog(Event_Showing.this);
                progressDialog.setMessage("Please Wait....");
                progressDialog.setCancelable(false);
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.cancel();
                                Intent intent=new Intent(Event_Showing.this,Create_Event.class);
                                startActivity(intent);
                                finish();
                            }
                        },2000);
            }
        });

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.nav_drawar_1);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_1);
        navigationView.setNavigationItemSelectedListener(this);

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

//        recyclerView.addOnItemTouchListener(
//                new RecyclerItemClickListener(this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override public void onItemClick(View view, int position) {
//                        // do whatever
//                        int itemPosition = recyclerView.getChildLayoutPosition(view);
//                        Event event = addevent.get(itemPosition);
//                        Intent intent = new Intent(Event_Showing.this, Single_Event_Details.class);
//                        intent.putExtra("eventid", event.getId());
//                        intent.putExtra("eventname", event.getName());
//                        startActivity(intent);
//                    }
//
//                    @Override public void onLongItemClick(View view, int position) {
//                        // do whatever
//                    }
//                })
//        );

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.nav_myprofile:
                startActivity(new Intent(Event_Showing.this,Profile_Showing.class));
                break;
            case R.id.nav_myevent:
                startActivity(new Intent(Event_Showing.this,User_Event_Showing.class));
                break;
            case R.id.nav_resetpassword:
                startActivity(new Intent(Event_Showing.this,Reset_Password.class));
                break;
            case R.id.nav_share:
                Toast.makeText(Event_Showing.this, "share", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logout:
                final ProgressDialog progressDialog=new ProgressDialog(Event_Showing.this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Please Wait....");
                progressDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        editor.clear();
                        editor.commit();
                        progressDialog.cancel();
                        startActivity(new Intent(Event_Showing.this,Login.class));
                        finish();
                    }
                },3000);
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.nav_drawar_1);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.nav_drawar_1);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            final AlertDialog.Builder builder=new AlertDialog.Builder(Event_Showing.this);

            View view= LayoutInflater.from(Event_Showing.this).inflate(R.layout.custom_dialog,null);

            TextView bodymessage=(TextView)view.findViewById(R.id.message);

            ImageView closedialog=(ImageView)view.findViewById(R.id.close);

            builder.setView(view);
                bodymessage.setText("Are you sure to\nEXIT!!");
                Button positive=(Button)view.findViewById(R.id.positive);
                Button negative=(Button)view.findViewById(R.id.negative);
                negative.setText("Yes,Quit");
                positive.setText("Later");

            final AlertDialog alert=builder.create();

                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       alert.cancel();
                    }
                });
                closedialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.cancel();
                    }
                });
                alert.show();
        }
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
                final AlertDialog.Builder builder=new AlertDialog.Builder(Event_Showing.this);

                View view= LayoutInflater.from(Event_Showing.this).inflate(R.layout.custom_dialog,null);

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
            json_url="http://"+getString(R.string.ip_addres)+"/EventManagement/EventList.php";
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                //initialize the url
                URL url=new URL(json_url);
                //initialize the connection
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
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
           // Toast.makeText(Event_Showing.this, jsonFood, Toast.LENGTH_SHORT).show();
            progressDialog.cancel();
            if(result) {
                eventAdapter.notifyDataSetChanged();
            }
            else
                Toast.makeText(Event_Showing.this, "Failed...Please try again..", Toast.LENGTH_SHORT).show();
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
            Boolean regEnd=false;
            holder.name.setText(event.getName());
            holder.lastdatetext.setText(event.getLastdate());
            holder.remainingtime.setText(event.getEventStartdate());

            String present_day=new SimpleDateFormat("yyyy-M-dd hh:mm:ss").format(Calendar.getInstance().getTime());
            String registrationendDate=event.getLastdate()+" 24:00:00";

            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
            Date present_day_date= null;
            Date registrationendDatet_day_date= null;
            try {
                present_day_date = simpleDateFormat.parse(present_day);
                registrationendDatet_day_date=simpleDateFormat.parse(registrationendDate);
                if(present_day_date.getTime()>registrationendDatet_day_date.getTime())
                    regEnd=true;
                else
                    regEnd=false;
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if(regEnd) {
                holder.enter.setVisibility(View.GONE);
            }

            holder.enter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(userid.equals(event.getEventhoster()))
                        Toast.makeText(Event_Showing.this, "Sorry.You Can't because You Are the Hoster", Toast.LENGTH_LONG).show();
                    else {
                        Intent intent = new Intent(Event_Showing.this, Event_Registration.class);
                        intent.putExtra("eventid", event.getId());
                        intent.putExtra("class","1");
                        startActivity(intent);
                        finish();
                    }
                }
            });
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Event_Showing.this, Single_Event_Details.class);
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
            LinearLayout linearLayout;

            public EventHolder(View itemView) {
                super(itemView);
                name=(TextView)itemView.findViewById(R.id.eventname);
                lastdatetext=(TextView)itemView.findViewById(R.id.lastdatetime);
                remainingtime=(TextView)itemView.findViewById(R.id.eventStartdate);
                enter=(Button)itemView.findViewById(R.id.enterregistration);
                linearLayout=(LinearLayout) itemView.findViewById(R.id.textofevent);
            }
        }
    }

    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private OnItemClickListener mListener;

        public interface OnItemClickListener {
            public void onItemClick(View view, int position);

            public void onLongItemClick(View view, int position);
        }

        GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && mListener != null) {
                        mListener.onLongItemClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
                return true;
            }
            return false;
        }

        @Override public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) { }

        @Override
        public void onRequestDisallowInterceptTouchEvent (boolean disallowIntercept){}
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
