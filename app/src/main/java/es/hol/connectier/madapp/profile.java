package es.hol.connectier.madapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class profile extends AppCompatActivity implements View.OnClickListener {
    private TextView name,email,id,loc;
    private ImageView camera,logout;
    private Button getLocation,checkmem;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private static final int CAM_REQUEST=1313;
    private static final int GALLERY_REQUEST=2313;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Casting
        id=(TextView)findViewById(R.id.userid);
        name=(TextView)findViewById(R.id.name);
        email=(TextView)findViewById(R.id.email);
        loc=(TextView)findViewById(R.id.location);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        camera=(ImageView)findViewById(R.id.camera);
        logout=(ImageView)findViewById(R.id.logout);
        checkmem=(Button)findViewById(R.id.check);
        getLocation=(Button)findViewById(R.id.get_loc);

        //Listener
        camera.setOnClickListener(this);
        logout.setOnClickListener(this);
        checkmem.setOnClickListener(this);
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                 loc.setText("Longitutde : "+location.getLatitude()+"\nLatitude : "+location.getLongitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };
        configure_button();

         //Check Authentication
        if(!Shared.getInstance(this).isLoggedIn())
         {
             this.finish();
             startActivity(new Intent(getApplicationContext(),MainActivity.class));
         }

        //Set Values
        id.setText("USERID : CPT0"+ Shared.getInstance(this).getUserid());
        name.setText(Shared.getInstance(this).getFullname());
        email.setText(Shared.getInstance(this).getUserEmail());

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:configure_button(); break;
            default:break;
        }
    }
    void configure_button() {
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET},10);
            }
            return;
        }
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationManager.requestLocationUpdates("gps", 5000, 1, locationListener);
            }
        });

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CAM_REQUEST && resultCode==RESULT_OK)
        {
//            Bitmap thumbnail =(Bitmap)data.getExtras().get("data");
//            photo.setImageBitmap(thumbnail);
        }
    }



    public boolean hasCamera()
     {
         return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
     }
    public void launchcamera()
     {
         Intent i =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
         File wallpaperDirectory = new File("/sdcard/Capture/");
         wallpaperDirectory.mkdirs();
         //File pictureDirectory=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
         String pictureName = getPictureName();
         File imagefile=new File(wallpaperDirectory,pictureName);
         Uri picUri=Uri.fromFile(imagefile);
         i.putExtra(MediaStore.EXTRA_OUTPUT,picUri);
         startActivityForResult(i,CAM_REQUEST);
     }

    private String getPictureName() {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp=sdf.format(new Date());
        return "Capture_"+timestamp+".jpg";
    }



    @Override
    public void onClick(View v) {
        if(v==logout)
        {
            Shared.getInstance(this).logOut();
            finish();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
        if(v==camera)
        {
            if(hasCamera())
            {  launchcamera();  }
            else { Toast.makeText(getApplicationContext(),"Camera Not Available",Toast.LENGTH_LONG).show(); }
        }
        if(v==checkmem)
        {
            Intent gallery=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivity(gallery);
        }

    }
}
