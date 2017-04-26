package ru.golden.flickrviewer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

import ru.golden.flickrviewer.network.GetImagesService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button button = (Button) findViewById(R.id.refr);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                startService(new Intent(MainActivity.this, GetImagesService.class));
            }
        });

    }
}
