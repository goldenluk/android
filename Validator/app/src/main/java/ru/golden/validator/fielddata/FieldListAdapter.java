package ru.golden.validator.fielddata;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


import java.util.ArrayList;

import ru.golden.validator.R;

final class FieldListAdapter
        extends RecyclerView.Adapter<FieldListAdapter.FeedModelViewHolder> {

    private final ArrayList<Field> fields;

    FieldListAdapter(final ArrayList<Field> fields) {
        this.fields = fields;

    }

    @Override
    public FeedModelViewHolder onCreateViewHolder(final ViewGroup parent, final int type) {
        final View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.field_item, parent, false);
        return new FeedModelViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final FeedModelViewHolder holder, final int position) {
        final TextView placeholder = (TextView) holder.fieldView.findViewById(R.id.placeholder);
        placeholder.setText(fields.get(position).getPlaceholder());

        final TextInputLayout textInputLayout = (TextInputLayout) holder.fieldView.findViewById(R.id.input_url_fields);

        final EditText editText = textInputLayout.getEditText();
        if (editText != null) {
            if (fields.get(holder.getAdapterPosition()).getValue() != null) {
                editText.setText(fields.get(position).getValue());
            } else {
                editText.setText(fields.get(position).getDefaultValue());
            }
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {

                }

                @Override
                public void onTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {

                }

                @Override
                public void afterTextChanged(final Editable editable) {
                    fields.get(holder.getAdapterPosition()).setValue(editable.toString());
                }
            });
        }

        textInputLayout.setHint("Введите " + fields.get(position).getType());

        if (fields.get(holder.getAdapterPosition()).getValid() != null && !fields.get(holder.getAdapterPosition()).getValid()) {
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError(holder.fieldView.getContext().getString(R.string.hint_field_not_valid) + " " +
                    fields.get(holder.getAdapterPosition()).getType());
        } else {
            textInputLayout.setErrorEnabled(false);
        }
    }

    @Override
    public int getItemCount() {
        return fields.size();
    }

    static class FeedModelViewHolder extends RecyclerView.ViewHolder {
        private final View fieldView;

        FeedModelViewHolder(final View v) {
            super(v);
            fieldView = v;
        }
    }
}
