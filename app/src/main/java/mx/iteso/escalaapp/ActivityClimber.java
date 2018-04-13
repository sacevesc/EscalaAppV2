package mx.iteso.escalaapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;


public class ActivityClimber extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_climber);
        //circle image fresco
        Uri uri = new Uri.Builder().scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                .path(String.valueOf(R.drawable.sebas_perfil)).build();
        SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.climber_profile_picture);
        draweeView.setImageURI(uri);
    }
}
