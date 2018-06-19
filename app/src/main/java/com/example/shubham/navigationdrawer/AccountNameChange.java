package com.example.shubham.navigationdrawer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AccountNameChange extends AppCompatActivity {

    EditText editname;
    Button OK, Cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_name_change);

        editname = (EditText)findViewById(R.id.editname);
        OK = (Button)findViewById(R.id.btnOK);
        Cancel = (Button)findViewById(R.id.btnCancel);

        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Account.class);
                intent.putExtra("Name",editname.getText().toString());
                startActivity(intent);
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
