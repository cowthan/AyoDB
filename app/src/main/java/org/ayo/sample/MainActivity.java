package org.ayo.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.ayo.attacher.ActivityAttacher;
import org.ayo.db.sample.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_in).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ActivityAttacher.startActivity(MainActivity.this, XUtilsDBDemoActivity.class, null);
            }
        });
    }
}
