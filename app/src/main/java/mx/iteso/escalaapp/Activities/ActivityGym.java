package mx.iteso.escalaapp.Activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import mx.iteso.escalaapp.R;

public class ActivityGym extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym);

        //circle image fresco
        Uri uri = new Uri.Builder().scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                .path(String.valueOf(R.drawable.ameyalli)).build();
        SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.gym_profile_picture);
        draweeView.setImageURI(uri);


    }

}
