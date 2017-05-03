package ru.golden.validator.imagesworks;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import ru.golden.validator.R;


public final class FullImageActivity extends AppCompatActivity {
    private static final String EXTRA_STRING_URL = "ru.golden.validator.URL";

    private ScaleGestureDetector scaleGestureDetector;
    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        Toast.makeText(this, R.string.toast_pinch, Toast.LENGTH_LONG).show();

        final String url = getIntent().getStringExtra(EXTRA_STRING_URL);

        imageView = (ImageView)findViewById(R.id.full_image);
        scaleGestureDetector = new ScaleGestureDetector(this,new ScaleListener());


        Picasso.with(FullImageActivity.this)
                .load(url.replace("http", "https"))
                .placeholder(R.drawable.ic_yoda_1)
                .error(R.drawable.ic_fail)
                .into(imageView);

    }

    @Override
    public boolean onTouchEvent(final MotionEvent ev) {
        scaleGestureDetector.onTouchEvent(ev);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.
            SimpleOnScaleGestureListener {

        float factor = 1;

        @Override
        public boolean onScale(final ScaleGestureDetector detector) {
            final float scaleFactor = detector.getScaleFactor() - 1;
            factor += scaleFactor;
            imageView.setScaleX(factor);
            imageView.setScaleY(factor);
            return true;
        }
    }
}
