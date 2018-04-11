package com.example.pallavi.norag;

/**
 * Created by Pallavi on 12/11/2017.
 */



import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AuthorityLogin extends AppCompatActivity {
    Button bt1,bt2;
    EditText Email,Password;int c1=0;int c2=0;int ind1,ind2;int userid,roleasid;
    String baseurl;
    String e,p;String data,requestedurl;String s1;JSONObject jo;int response_data;int wait=0;SharedPreferences sp;int sessionid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authority_login);
        bt2 = (Button) findViewById(R.id.btn_login);
       // bt1 = (Button) findViewById(R.id.btn_signup);

         sp = PreferenceManager.getDefaultSharedPreferences(AuthorityLogin.this);
        sessionid = sp.getInt("authoritysessionid", -1);
       // Toast.makeText(AuthorityLogin.this,"sadasda"+sessionid,Toast.LENGTH_SHORT).show();
   if (sessionid == -1) {
            bt2 = (Button) findViewById(R.id.btn_login);
          //  bt1 = (Button) findViewById(R.id.btn_signup);


       login();

          //  onClickButtonListener();
            Log.v("hello pallavi", "hello");

        }
        else
        {
            Intent introductionpage=new Intent(AuthorityLogin.this,Introduction.class);
            //Intent intent=new Intent(Login.this,Introduction.class);
            introductionpage.putExtra("code",1);
            startActivity(introductionpage);
        }

    }

    public void login() {
      //  Toast.makeText(AuthorityLogin.this, "entered login page", Toast.LENGTH_LONG).show();
        if (wait == 0) {
            bt2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    wait = 1;
                  //  Toast.makeText(AuthorityLogin.this, "login pressed"+wait, Toast.LENGTH_LONG).show();


                    Email = (EditText) findViewById(R.id.email);
                    Password = (EditText) findViewById(R.id.password);
                    e = Email.getText().toString();
                    p = Password.getText().toString();
                    /*boolean b=Pattern.matches("[a-zA-Z0-9+._%-+]{1,256}" +
                            "@" +
                            "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" +
                            "(" +
                            "." +
                            "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" +
                            ")+",r);
                    */
                    // if(b==true)
                    //  Toast.makeText(MainActivity.this, "matches", Toast.LENGTH_LONG).show();

                    //if(b==false)
                    //{
                    //    Rollno.setError("email entered does not follow the correct format");
                    //}
                    //else
                    // {  // Toast.makeText(MainActivity.this, "does not match", Toast.LENGTH_LONG).show();
                    roleasid=sp.getInt("roleas",-1);
                    //baseurl=getString("base_url");
                    baseurl=getString(R.string.base_url);
                    if (roleasid==1)
                    {
                        requestedurl=baseurl+"proctorlogin/";
                    }
                    else if (roleasid==2)
                    {
                        requestedurl=baseurl+"facultylogin/";
                    }
                    else if (roleasid==3)
                    {
                        requestedurl=baseurl+"studentmemberlogin/";
                    }

                 //   Toast.makeText(AuthorityLogin.this, "password is" + p, Toast.LENGTH_LONG).show();
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Log.v("PALLAVI", "ENTERED NEW THREAD");
                            data = "{\"email\":\"" + e + "\",\"password\":\"" + p + "\"}";
                            Log.v("THE REQUESTED DATA IS:", data);
                            wait = 1;
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url(requestedurl)
                                    .post(RequestBody.create(okhttp3.MediaType.parse("application/json;charset=utf-8"), data))
                                    .build();

                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    AuthorityLogin.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            //Toast.makeText(AuthorityLogin.this, "Network Failure", Toast.LENGTH_SHORT).show();
                                            Snackbar sn=Snackbar.make(findViewById(R.id.coordinatorlayout),"Network Failure", Snackbar.LENGTH_LONG);
                                            sn.setActionTextColor(Color.MAGENTA);
                                            View sbView = sn.getView();
                                            sbView.setBackgroundColor(ContextCompat.getColor(AuthorityLogin.this, R.color.myblue));
                                            sn.show();
                                            wait = 0;
                                        }
                                    });
                                    Log.v("ON FAILURE", "NEW CALLBACK FAILURE");
                                }

                                @Override
                                public void onResponse(Call call, final Response response) throws IOException {
                                    Log.v("PALLAVI", "ENTERED ON RESPONSE");
                                            /*    MainActivity.this.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Email.setText(null);
                                                        Password.setText(null);
                                                    }
                                                });*/
                                    try {
                                        s1 = response.body().string();
                                    } catch (Exception e2) {
                                        e2.printStackTrace();
                                        wait = 0;
                                    }
                                    Log.v("THE JSON OBJ IS CREATED", s1);

                                  //  final String s2 = s1.substring(s1.indexOf('[') + 1, s1.length() - 1);
                                    //s1.substring(s1.indexOf('[')+1, s1.length() - 1);

                                    try {
                                        jo = new JSONObject(s1);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    AuthorityLogin.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            try {

// SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                                                //sessionid=sp.getInt("sessionid",-1);
                                                response_data = Integer.parseInt(String.valueOf(jo.getInt("return_status")));
                                                if (response_data == 1) {
                                                    //Toast.makeText(AuthorityLogin.this, "You have logged in successfully..Please wait till the page is redirected to home page", Toast.LENGTH_SHORT).show();
                                                    //Intent intent=new Intent("com.example.pallavi.chat_app.Chat");
                                                    SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(AuthorityLogin.this);
                                                    SharedPreferences.Editor ed=sp.edit();
                                                    userid=Integer.parseInt(String.valueOf(jo.getInt("sessionid")));
                                                    ed.putInt("authoritysessionid",userid);
                                                    ed.commit();
                                                    Intent intent=new Intent(AuthorityLogin.this,Introduction.class);
                                                    intent.putExtra("code",1);
                                                    startActivity(intent);
                                                    AuthorityLogin.this.finish();
                                                } else if (response_data == 2) {
                                                    //Toast.makeText(AuthorityLogin.this, "Password does not match", Toast.LENGTH_SHORT).show();
                                                    Snackbar sn=Snackbar.make(findViewById(R.id.coordinatorlayout),"Password does not match", Snackbar.LENGTH_LONG);
                                                    sn.setActionTextColor(Color.MAGENTA);
                                                    View sbView = sn.getView();
                                                    sbView.setBackgroundColor(ContextCompat.getColor(AuthorityLogin.this, R.color.myblue));
                                                    sn.show();
                                                }
                                                wait = 0;
                                            } catch (JSONException e1) {
                                                e1.printStackTrace();
                                                wait = 0;

                                            }

                                        }
                                    });
                                }
                            });

                        }
                    });
                    t.start();


                }
                //}

            });

               /* Toast.makeText(MainActivity.this,"entered Register",Toast.LENGTH_LONG).show();
                bt2.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(MainActivity.this,"clicked signup",Toast.LENGTH_LONG).show();

                                Email = (EditText) findViewById(R.id.email2);
                                Password = (EditText) findViewById(R.id.input_password2);

                                e=Email.getText().toString();
                                p=Password.getText().toString();
                                //Toast.makeText(MainActivity.this,"Usename is"+u,Toast.LENGTH_LONG).show();
                                Thread t = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        data = "{\"email\":\"" + e + "\",\"password\":\"" + p + "\"}";
                                        Log.v("THE REQUESTED DATA IS ", data);
                                        OkHttpClient client = new OkHttpClient();
                                        Request request = new Request.Builder()
                                                .url("http://www.palzone.ml/service_pallavi/login.php")
                                                .post(RequestBody.create(okhttp3.MediaType.parse("application/json;charset=utf-8"), data))
                                                .build();
                                        client.newCall(request).enqueue(new Callback() {
                                            @Override
                                            public void onFailure(Call call, final IOException e) {
                                                MainActivity.this.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Log.v("ON FAILURE ","CLIENT REQUEST NOT SENT ");
                                                        Toast.makeText(MainActivity.this, "client request not sent", Toast.LENGTH_LONG).show();
                                                    }
                                                });

                                            }

                                            @Override
                                            public void onResponse(Call call, final Response response) throws IOException {
                                                s1 = response.body().string();
                                                final String s2;
                                                s2 = s1.substring(s1.indexOf('[')+1, s1.length() - 1);
                                                try {
                                                    jo = new JSONObject(s2);
                                                } catch (JSONException e1) {
                                                    e1.printStackTrace();
                                                }

                                                Log.v("THe response is:", s1);
                                                MainActivity.this.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            int statusResponse = Integer.parseInt(String.valueOf(jo.getInt("response_data")));
                                                            if (statusResponse == 5)
                                                                Toast.makeText(MainActivity.this, "login successful", Toast.LENGTH_LONG).show();
                                                            else if (statusResponse == 6)
                                                                Toast.makeText(MainActivity.this, "password not matched", Toast.LENGTH_SHORT).show();
                                                            else if (statusResponse == 7)
                                                                Toast.makeText(MainActivity.this, "Email does not exists", Toast.LENGTH_LONG).show();
                                                            else if (statusResponse == 8)
                                                                Toast.makeText(MainActivity.this, "missing some field", Toast.LENGTH_LONG).show();

                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });

                                            }
                                        });


                                    }
                                });
                                t.start();
                            }
                        }
                );*/

        }

    }
}
