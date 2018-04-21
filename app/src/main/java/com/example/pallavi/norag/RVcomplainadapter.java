package com.example.pallavi.norag;

/**
 * Created by Supratim on 03-03-2018.
 */

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.maps.MapFragment;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.R.layout.simple_dropdown_item_1line;
import static android.support.v4.app.ShareCompat.getCallingActivity;

/**
 * Created by Supratim on 06/05/2017.
 */
interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
public class RVcomplainadapter extends RecyclerView.Adapter<RVcomplainadapter.PersonViewHolder> implements ItemTouchHelperAdapter{
    List<cardcomplaindata> c;
    RecyclerView rv;
    Activity main;
    PersonViewHolder pv;
    Intent mappage,viewprofilepage;
    Bundle mBundle;
    int roleid;
    String cid;
    String sid;
    String answerid,message;
    String quesid;
    String ansid,s1;
    int sessionid,s;
    int check = 0;
    boolean userSelect=false;
    String data,requesturl,baseurl;
    String res;
    JSONObject jsonObject;
    boolean wait=true;
    Intent replypage;
    Context context=null;
    RVcomplainadapter rvad;
    CoordinatorLayout cl;
    //boolean wait1=true;
    RVcomplainadapter(List asd,Activity main){
        this.c=asd;
        this.main=main;




    }
    public void addelement(cardcomplaindata complain){
        c.add(0,complain);
        notifyItemChanged(0);

    }

    public void removecomplainAt(int position) {
        c.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, c.size());
        // Snackbar sn=Snackbar.make(rv, "Complain Deleted Successfully", Snackbar.LENGTH_LONG);

        // sn.show();

    }



    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_complain, parent, false);
        check=0;
        PersonViewHolder pvh = new PersonViewHolder(v);

        return pvh;




    }

    @Override
    public void onBindViewHolder(final PersonViewHolder holder, final int position) {

        pv=holder;
        baseurl=main.getString(R.string.base_url);
        SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(main);
        roleid=sp.getInt("role",-1);
        if (roleid==2)
        {
            holder.deletebtn.setVisibility(View.GONE);
            sessionid=sp.getInt("authoritysessionid",-1);
        }
        if (roleid==1)
        {
            holder.mapbtn.setVisibility(View.GONE);
            holder.sharebtn.setVisibility(View.GONE);
            holder.spinnervote.setVisibility(View.GONE);
            holder.spinnerstatus.setVisibility(View.GONE);
            holder.sharetv.setVisibility(View.GONE);
            holder.openmaptv.setVisibility(View.GONE);
            holder.votetv.setVisibility(View.GONE);
            holder.votemystatus.setVisibility(View.GONE);
            holder.myvotetv.setVisibility(View.GONE);
         //   holder.votebtn.setVisibility(View.GONE);

            sessionid=sp.getInt("studentsessionid",-1);

        }
        holder.cid.setText(String.valueOf(c.get(position).cid));
        holder.complainusername.setText(String.valueOf(c.get(position).student_name));
        holder.complain.setText(c.get(position).complain_txt);
        holder.status.setText("Status: "+c.get(position).status);
        holder.votemystatus.setText(c.get(position).myvote+" star given");
        holder.totalvotetv.setText(String.valueOf(c.get(position).totalvote));
        holder.severity.setText(c.get(position).severity_of_punishment);
        check=0;
        holder.mapbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Check","Pressed Map button");
                //android.support.v4.app.Fragment fragment= new MapsActivity();

                //FragmentManager fragmentManager = fragment.getActivity().getSupportFragmentManager();
                //android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


                //fragmentTransaction.replace(R.id.complain, fragment);
                //fragmentTransaction.addToBackStack(null);
                //fragmentTransaction.commit();
                Intent mappage=new Intent(main,MapsActivity.class);
                mappage.putExtra("latitude", c.get(position).latitude);
                mappage.putExtra("longitude",c.get(position).longitude);
                main.startActivity(mappage);
                main.finish();



            }
        });
        holder.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newcid=c.get(position).cid;
                // int newcid=Integer.valueOf(holder.cid.getText().toString());
                data = "{\"cid\":\"" + newcid + "\",\"sessionid\":\"" + sessionid + "\"}";
                Log.v("Requested data is",data);
                requesturl=baseurl+"deletecomplain/";
                Thread th=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client=new OkHttpClient();
                        okhttp3.Request request = new okhttp3.Request.Builder()
                                .url(requesturl)
                                .post(RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), data))
                                .build();
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                main.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(main,"Network Failure",Toast.LENGTH_SHORT);
                                        //  cl=(CoordinatorLayout)rv.findViewById(R.id.coordinatorlayout);

                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                s1=response.body().string();
                                main.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.v("s1",s1);

                                        try {
                                            jsonObject =new JSONObject(s1);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                        try {

                                            message=jsonObject.getString("message");

                                            //Toast.makeText(main,message,Toast.LENGTH_SHORT).show();
                                            //cl=(CoordinatorLayout)rv.findViewById(R.id.coordinatorlayout);
                                            Snackbar sn=Snackbar.make(main.findViewById(R.id.coordinatorlayout), message, Snackbar.LENGTH_LONG);
                                            sn.setActionTextColor(Color.MAGENTA);
                                            View sbView = sn.getView();
                                            sbView.setBackgroundColor(ContextCompat.getColor(main, R.color.myblue));
                                            sn.show();
                                            removecomplainAt(position);
                                            //int unlikeqbtn=0;

                                            //cd.add(i,new carddata(userpic2,id,quserid,ques,questionaskname,nooflike,noofunlike,noofanswers,statuslike,statusunlike,sessionuserid,follow,editqbtn,likeqbtn,unlikeqbtn));
                                            //rvad.addelement(new cardcomplaindata(cid,sid,severity_of_punishment,student_name,mobile_no,g_mobile_no,complain_txt,attachment,date,status,totalvote,myvote));
                                            //     c.add(0,new cardcomplaindata(cid,sid,severity_of_punishment,student_name,mobile_no,g_mobile_no,complain_txt,attachment,date,status,totalvote,myvote));

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        });

                    }
                });
                th.start();
            }
        });
        holder.spinnerstatus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                userSelect=true;
                return false;
            }
        });

        holder.spinnerstatus.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (userSelect)
                {
                    String newstatus = holder.spinnerstatus.getAdapter().getItem(position).toString();
                    // String newstatus=holder.spinnerstatus.getOnItemSelectedListener().toString();
                    //String newstatus="";
                    int cid=c.get(position).cid;
                    Log.v("Status",newstatus);

                    data = "{\"cid\":\"" + cid + "\",\"newstatus\":\"" + newstatus + "\"}";
                    requesturl=baseurl+"updatecomplainstatus/";
                    if (newstatus!=null){
                        Thread th=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Log.v("Thread","Thread is getting called");
                                //  wait=false;
                                OkHttpClient client=new OkHttpClient();
                                okhttp3.Request request = new okhttp3.Request.Builder()
                                        .url(requesturl)
                                        .post(RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), data))
                                        .build();

                                client.newCall(request).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        main.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //Toast.makeText(main,"Network Failure",Toast.LENGTH_SHORT).show();
                                                // cl=(CoordinatorLayout)rv.findViewById(R.id.coordinatorlayout);
                                                Snackbar sn=Snackbar.make(main.findViewById(R.id.coordinatorlayout), "Network Failure", Snackbar.LENGTH_LONG);
                                                sn.setActionTextColor(Color.MAGENTA);
                                                View sbView = sn.getView();
                                                sbView.setBackgroundColor(ContextCompat.getColor(main, R.color.myblue));
                                                sn.show();

                                                //     wait=true;
                                            }
                                        });
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {

                                        s1=response.body().string();
                                        main.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Log.v("s1",s1);

                                                try {
                                                    jsonObject =new JSONObject(s1);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }


                                                try {
                                                    int cid=jsonObject.getInt("cid");
                                                    String status=jsonObject.getString("status");

                                                    holder.status.setText("Status: "+status);
                                                    //int unlikeqbtn=0;
                                                    Snackbar sn=Snackbar.make(main.findViewById(R.id.coordinatorlayout), "Status Successfully updated", Snackbar.LENGTH_LONG);
                                                    sn.setActionTextColor(Color.MAGENTA);
                                                    View sbView = sn.getView();
                                                    sbView.setBackgroundColor(ContextCompat.getColor(main, R.color.myblue));
                                                    sn.show();
                                                    //cd.add(i,new carddata(userpic2,id,quserid,ques,questionaskname,nooflike,noofunlike,noofanswers,statuslike,statusunlike,sessionuserid,follow,editqbtn,likeqbtn,unlikeqbtn));
                                                    //rvad.addelement(new cardcomplaindata(cid,sid,severity_of_punishment,student_name,mobile_no,g_mobile_no,complain_txt,attachment,date,status,totalvote,myvote));
                                                    // c.add(0,new cardcomplaindata(cid,sid,severity_of_punishment,student_name,mobile_no,g_mobile_no,complain_txt,attachment,date,status,totalvote,myvote));
                                                    //   wait=true;
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    // wait=true;
                                                }
                                                //rvad=new RVcomplainadapter(c,main);
                                                //notifyItemRangeChanged(0,c.size());
                                                //  rv.setAdapter(rvad);

                                                // Toast.makeText(main,"Successful in deleting answer",Toast.LENGTH_LONG).show();
                                                //removeanswerAt(position);
                                            }
                                        });
                                    }
                                });

                            }
                        });
                        th.start();
                    }
                }


            }


            public void onNothingSelected(AdapterView<?> parent) {
                //      holder.spinnerstatus.setSelection(0);

                Log.v("Kuch","kuch");

            }
        });

        holder.spinnervote.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String newvoting = holder.spinnervote.getAdapter().getItem(position).toString();
                int cid=c.get(position).cid;
                SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(main);
                int sessionid=sp.getInt("authoritysessionid",-1);
                Log.v("Voting",newvoting);
                data = "{\"cid\":\"" + cid + "\",\"voting\":\"" + newvoting + "\",\"sessionid\":\"" + sessionid + "\"}";
                requesturl=baseurl+"addvotingcomplain/";
             //   if (newvoting!=null && ++check>1) {
                    Thread th=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Log.v("Thread","Thread is getting called");
                            OkHttpClient client=new OkHttpClient();
                            okhttp3.Request request = new okhttp3.Request.Builder()
                                    .url(requesturl)
                                    .post(RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), data))
                                    .build();
                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    main.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(main,"error",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    s1=response.body().string();
                                    main.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.v("s1",s1);
                                            try {
                                                jsonObject =new JSONObject(s1);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            try {
                                                int cid=jsonObject.getInt("cid");
                                                String status=jsonObject.getString("voting");
                                                holder.votemystatus.setText(status+" star given");
                                                Snackbar sn=Snackbar.make(main.findViewById(R.id.coordinatorlayout), "Vote Successfully Given", Snackbar.LENGTH_LONG);
                                                sn.setActionTextColor(Color.MAGENTA);
                                                View sbView = sn.getView();
                                                sbView.setBackgroundColor(ContextCompat.getColor(main, R.color.myblue));
                                                sn.show();
                                                //int unlikeqbtn=0;
                                                //cd.add(i,new carddata(userpic2,id,quserid,ques,questionaskname,nooflike,noofunlike,noofanswers,statuslike,statusunlike,sessionuserid,follow,editqbtn,likeqbtn,unlikeqbtn));
                                                //rvad.addelement(new cardcomplaindata(cid,sid,severity_of_punishment,student_name,mobile_no,g_mobile_no,complain_txt,attachment,date,status,totalvote,myvote));
                                                // c.add(0,new cardcomplaindata(cid,sid,severity_of_punishment,student_name,mobile_no,g_mobile_no,complain_txt,attachment,date,status,totalvote,myvote));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            //rvad=new RVcomplainadapter(c,main);
                                            //notifyItemRangeChanged(0,c.size());
                                            //  rv.setAdapter(rvad);
                                            // Toast.makeText(main,"Successful in deleting answer",Toast.LENGTH_LONG).show();
                                            //removeanswerAt(position);
                                        }
                                    });
                                }
                            });
                        }
                    });
                    th.start();
                //}
            }

            public void onNothingSelected(AdapterView<?> parent) {
                //      holder.spinnerstatus.setSelection(0);
            }
        });

        holder.sharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newcid=c.get(position).cid;
                //SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(main);
                //sessionid=sp.getInt("author")
                data="{\"cid\":\""+newcid+"\"}";
                requesturl=baseurl+"sharecomplain/";

                Thread th=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client=new OkHttpClient();
                        okhttp3.Request request = new okhttp3.Request.Builder()
                                .url(requesturl)
                                .post(RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), data))
                                .build();

                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                main.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Toast.makeText(main,"Network Failure",Toast.LENGTH_LONG).show();
                                        //cl=(CoordinatorLayout)rv.findViewById(R.id.coordinatorlayout);
                                        Snackbar sn=Snackbar.make(main.findViewById(R.id.coordinatorlayout), "Network Failure", Snackbar.LENGTH_LONG);
                                        sn.setActionTextColor(Color.MAGENTA);
                                        View sbView = sn.getView();
                                        sbView.setBackgroundColor(ContextCompat.getColor(main, R.color.myblue));
                                        sn.show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {

                                s1=response.body().string();
                                main.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.v("s1",s1);

                                        try {
                                            jsonObject =new JSONObject(s1);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                        try {
                                            int cid=jsonObject.getInt("cid");
                                            int sid=jsonObject.getInt("sid");
                                            String severity_of_punishment=jsonObject.getString("severity_of_punishment");
                                            String student_name=jsonObject.getString("student_name");
                                            String mobile_no=jsonObject.getString("mobile_no");
                                            String g_mobile_no=jsonObject.getString("g_mobile_no");
                                            String complain_txt=jsonObject.getString("complain_txt");
                                            String attachment=jsonObject.getString("attachment");
                                            String date=jsonObject.getString("date");
                                            String status=jsonObject.getString("status");
                                            int totalvote=jsonObject.getInt("totalvotes");
                                            String myvote=jsonObject.getString("myvote");
                                            float latitude=Float.parseFloat(jsonObject.getString("latitude"));
                                            float longitude=Float.parseFloat(jsonObject.getString("longitude"));

                                            //int unlikeqbtn=0;

                                            //cd.add(i,new carddata(userpic2,id,quserid,ques,questionaskname,nooflike,noofunlike,noofanswers,statuslike,statusunlike,sessionuserid,follow,editqbtn,likeqbtn,unlikeqbtn));
                                            //rvad.addelement(new cardcomplaindata(cid,sid,severity_of_punishment,student_name,mobile_no,g_mobile_no,complain_txt,attachment,date,status,totalvote,myvote));
                                            c.add(0,new cardcomplaindata(latitude,longitude,cid,sid,severity_of_punishment,student_name,mobile_no,g_mobile_no,complain_txt,attachment,date,status,totalvote,myvote));

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        Snackbar sn=Snackbar.make(main.findViewById(R.id.coordinatorlayout), "Complain Share Successfully", Snackbar.LENGTH_LONG);
                                        sn.setActionTextColor(Color.MAGENTA);
                                        View sbView = sn.getView();
                                        sbView.setBackgroundColor(ContextCompat.getColor(main, R.color.myblue));
                                        sn.show();
                                        rvad=new RVcomplainadapter(c,main);
                                        notifyItemRangeChanged(0,c.size());
                                        //  rv.setAdapter(rvad);

                                        // Toast.makeText(main,"Successful in deleting answer",Toast.LENGTH_LONG).show();
                                        //removeanswerAt(position);
                                    }
                                });
                            }
                        });

                    }
                });
                th.start();

            }
        });
        /*
        holder.answerusername.setText(c.get(position).answerusername);
        holder.answer.loadDataWithBaseURL("", c.get(position).answer, "text/html", "UTF-8", "");
        // holder.question.loadUrl((c.get(position).ques));
        final String path="https://www.palzone.cf/services";
        Picasso.with(main).load(path + c.get(position).answeruserpic).into(holder.userpic);
        holder.noofanswerlike.setText(String.valueOf(c.get(position).noofanswerlike));
        holder.noofanswerunlike.setText(String.valueOf(c.get(position).noofanswerunlike));
        holder.noofreplies.setText(String.valueOf(c.get(position).noofreplies));
        //holder.qid.setText(String.valueOf(c.get(position).id));
        holder.answerlikebtn=c.get(position).answerlikeabtn;
        holder.answerunlikebtn=c.get(position).answerunlikeabtn;
        holder.quesid=String.valueOf(c.get(position).qid);
        holder.answerid=String.valueOf(c.get(position).answerid);
        SharedPreferences sharedPref= PreferenceManager.getDefaultSharedPreferences(main);
        sessionid=sharedPref.getInt("sessionuserid", -1);
        s=sessionid;
        int answeruserid=c.get(position).qansweruserid;
       */
        /* if(sessionid!=answeruserid)
        {
            holder.delabtn.setVisibility(View.GONE);
        }
        else
        {
            holder.delabtn.setVisibility(View.VISIBLE);
        }
        */




    }
    @Override
    public int getItemCount() {
        return c.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(c, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(c, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        c.remove(position);
        notifyItemRemoved(position);
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;

        //TextView noofanswer;
        ImageView personPhoto;
        TextView complainusername,sharetv,votetv,openmaptv,votemystatus,totalvotetv,severity,myvotetv;
        //Spinner spinnervote;
        //Spinner spinnerstatus;
        TextView status;
        MaterialBetterSpinner spinnerstatus,spinnervote;
        TextView complain;
        ImageButton sharebtn,mapbtn,deletebtn,votebtn;
        TextView cid;
        LinearLayout ll;
        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cav);

            // personPhoto=(ImageView)itemView.findViewById(R.id.complainimageButton);
            complainusername=(TextView)itemView.findViewById(R.id.complainusername);
            //spinnerstatus=(Spinner)itemView.findViewById(R.id.spinnerstatus);
            spinnervote=(MaterialBetterSpinner)itemView.findViewById(R.id.spinnervote);
            String[] votelist = {"1","2","3","4","5"};
            ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(main,android.R.layout.simple_dropdown_item_1line, votelist);
            spinnervote.setAdapter(arrayAdapter1);

            status=(TextView)itemView.findViewById(R.id.statustxt);
            complain=(TextView)itemView.findViewById(R.id.complaintxt);
            sharebtn=(ImageButton)itemView.findViewById(R.id.sharebtn);
            mapbtn=(ImageButton)itemView.findViewById(R.id.mapbtn);
            deletebtn=(ImageButton)itemView.findViewById(R.id.deletecomplain);
            cid=(TextView)itemView.findViewById(R.id.cid);
            sharetv=(TextView)itemView.findViewById(R.id.sharetv);
            openmaptv=(TextView)itemView.findViewById(R.id.openmaptv);
            votetv=(TextView)itemView.findViewById(R.id.votetv);
               spinnerstatus= (MaterialBetterSpinner)itemView.findViewById(R.id.spinnerstatus);
            String[] SPINNERLIST = {"In Progress", "In DC", "Action taken"};
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(main,
                    simple_dropdown_item_1line, SPINNERLIST);
            spinnerstatus.setAdapter(arrayAdapter);
            votemystatus=(TextView)itemView.findViewById(R.id.votemystatus);
            totalvotetv=(TextView)itemView.findViewById(R.id.totalvotetv);
            severity=(TextView)itemView.findViewById(R.id.severity);
           // votebtn=(ImageButton)itemView.findViewById(R.id.votebtn);
            myvotetv=(TextView) itemView.findViewById(R.id.myvotetv);
            //ll=(LinearLayout)itemView.findViewById(R.id.authoritylayout);
            //qid=(TextView)itemView.findViewById(R.id.qid);
            // answerbutton=(ImageButton)itemView.findViewById(R.id.answerButton);


            //quesid= Integer.parseInt(qid.getText().toString());
            //Log.v("The id of question is", String.valueOf(quesid));


        }
    }


}