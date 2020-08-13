package sych.homework.helloworld;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddItemActivity extends AppCompatActivity {

    private EditText mNameEditText;
    private EditText mPriceEditText;
    private Button mAddButton;

    private String mName;
    private String mPrice;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        mNameEditText = findViewById(R.id.name_edittext);
        mNameEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(
                    final CharSequence charSequence,
                    final int i,
                    final int i1,
                    final int i2
            ) {

            }

            @Override
            public void onTextChanged(
                    final CharSequence charSequence,
                    final int i,
                    final int i1,
                    final int i2
            ) {

            }

            @Override
            public void afterTextChanged(final Editable editable) {
                mName = editable.toString();
                checkEditTextHasText();
            }
        });
        mPriceEditText = findViewById(R.id.price_edittext);
        mPriceEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(
                    final CharSequence charSequence,
                    final int i,
                    final int i1,
                    final int i2
            ) {

            }

            @Override
            public void onTextChanged(
                    final CharSequence charSequence,
                    final int i,
                    final int i1,
                    final int i2
            ) {

            }

            @Override
            public void afterTextChanged(final Editable editable) {
                mPrice = editable.toString();
                checkEditTextHasText();
            }
        });

        mAddButton = findViewById(R.id.add_button);
        mAddButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {

                if (!TextUtils.isEmpty(mName) && !TextUtils.isEmpty(mPrice)) {
                    setResult(
                            RESULT_OK,
                            new Intent().putExtra("name", mName).putExtra("price", mPrice));
                    finish();
                }
            }
        });
    }

    public void checkEditTextHasText() {
        mAddButton.setEnabled(!TextUtils.isEmpty(mName) && !TextUtils.isEmpty(mPrice));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
    }
}