package ru.golden.validator.fielddata;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;

import ru.golden.validator.R;
import ru.golden.validator.imagesworks.ImageListActivity;


public final class FieldActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String EXTRA_FIELDS = "ru.golden.validator.FIELDS";

    private ArrayList<Field> fields;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field);

        if (savedInstanceState != null && savedInstanceState.getSerializable(EXTRA_FIELDS) != null) {
            final Field[] fieldsTemp = (Field[]) savedInstanceState.getSerializable(EXTRA_FIELDS);
            if (fieldsTemp != null) {
                fields = new ArrayList<>(Arrays.asList(fieldsTemp)) ;
            } else {
                fields = new ArrayList<>(Arrays.asList((Field[]) getIntent().getSerializableExtra(EXTRA_FIELDS)));
                for (int i = 0; i < fields.size(); ++i) {
                    fields.get(i).initValue();
                }
            }
        } else {
            fields = new ArrayList<>(Arrays.asList((Field[]) getIntent().getSerializableExtra(EXTRA_FIELDS)));
            for (int i = 0; i < fields.size(); ++i) {
                fields.get(i).initValue();
            }
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new FieldListAdapter(fields));

        final Button validateButton = (Button) findViewById(R.id.button_validate);
        validateButton.setOnClickListener(this);
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case  R.id.button_validate:
                int invalidCounter = 0;
                int firstInvalid = -1;
                for (int i = 0; i < fields.size(); ++i) {
                    if (!fields.get(i).setValid()) {
                        if (firstInvalid == -1) {
                            firstInvalid = i;
                        }
                        invalidCounter++;
                    }
                }
                recyclerView.getAdapter().notifyDataSetChanged();
                if (invalidCounter != 0) {
                    recyclerView.scrollToPosition(firstInvalid);
                } else {
                    final Intent intent = new Intent(FieldActivity.this, ImageListActivity.class);
                    startActivity(intent);
                }
        }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        final Field[] field = new Field[fields.size()];
        fields.toArray(field);
        outState.putSerializable(EXTRA_FIELDS, field);
        super.onSaveInstanceState(outState);
    }
}
