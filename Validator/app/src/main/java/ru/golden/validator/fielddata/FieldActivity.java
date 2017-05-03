package ru.golden.validator.fielddata;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import ru.golden.validator.R;


public final class FieldActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String EXTRA_FIELDS = "ru.golden.validator.FIELDS";

    private ArrayList<Field> fields;
    private RecyclerView recyclerView;

    //TODO переворот

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field);

        fields = new ArrayList<>(Arrays.asList((Field[]) getIntent().getSerializableExtra(EXTRA_FIELDS)));
        for (int i = 0; i < fields.size(); ++i) {
            fields.get(i).initValue();
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
                for (int i = 0; i < fields.size(); ++i) {
                    if (!fields.get(i).setValid()) {
                        invalidCounter++;
                    }
                }
                if (invalidCounter != 0) {
                    Toast.makeText(FieldActivity.this, getString(R.string.toast_invalid_numbers) + " " + invalidCounter, Toast.LENGTH_LONG).show();
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
        }
    }
}
