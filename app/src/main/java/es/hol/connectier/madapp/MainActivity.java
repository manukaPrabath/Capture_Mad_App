package es.hol.connectier.madapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView register;
    private EditText email,pass;
    private ProgressDialog loader;
    private Button loginbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Casting
        register = (TextView)findViewById(R.id.reg_btn);
        email =(EditText)findViewById(R.id.email);
        pass = (EditText)findViewById(R.id.pass);
        loginbtn=(Button)findViewById(R.id.signin_btn);

        //Progress Dialog
        loader=new ProgressDialog(this);
        loader.setMessage("Please Wait....");

        //Listeners
        loginbtn.setOnClickListener(this);
        register.setOnClickListener(this);

        //Check Authentication
        if(Shared.getInstance(this).isLoggedIn())
        {
            this.finish();
            startActivity(new Intent(getApplicationContext(),profile.class));
        }
    }

    public void Login()
    {
        final String username=email.getText().toString().trim();
        final String password=pass.getText().toString().trim();
        loader.show();
        StringRequest webrequest=new StringRequest(Request.Method.POST, "http://clayofficial.com/mad/v1/login.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loader.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if(!jsonObject.getBoolean("error"))
                     {
                         Shared.getInstance(getApplicationContext()).userLogin(jsonObject.getInt("id"),username,jsonObject.getString("email"),jsonObject.getString("name"));

                         //Nofication
                         Intent intent=new Intent();
                         PendingIntent pIntent=PendingIntent.getActivity(MainActivity.this,0,intent,0);
                         Notification noti=new Notification.Builder(MainActivity.this).setTicker("Capture").setContentTitle("Logged In").setContentText(jsonObject.getString("name")+" Logged In").setSmallIcon(R.drawable.logo).setContentIntent(pIntent).getNotification();
                         noti.flags=Notification.FLAG_AUTO_CANCEL;
                         NotificationManager nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                         nm.notify(0,noti);

                         //Redirect To Profile
                         finish();
                         startActivity(new Intent(getApplicationContext(),profile.class));
                     }
                    else
                    {
                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loader.hide();
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("username",username);
                params.put("password",password);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(webrequest);
    }

    @Override
    public void onClick(View v) {
        if (v == register){
            Intent intent = new Intent(this,register.class);
            startActivity(intent);
        }
        if( v == loginbtn)
         {
             Login();
         }
    }
}

