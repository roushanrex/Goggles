package com.example.omsairam01.goggles.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.omsairam01.goggles.R;

public class Editpassword extends AppCompatActivity {

    EditText edtpsd;
    Button btnpsd;
    TextView logintxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editpassword);

        Toolbar toolbar=findViewById(R.id.tooledit);
        edtpsd=(EditText)findViewById(R.id.editText2);
        btnpsd=(Button)findViewById(R.id.button4);
        logintxt=findViewById(R.id.txtlogin);




        btnpsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              password();

            }
        });


    }

    private void password() {

        String s=edtpsd.getText().toString();
        if (s.equals("eyewear"))
        {
            Intent intent=new Intent(Editpassword.this,MainActivity.class);
            startActivity(intent);
            finish();

        }
        else
        {
            Toast.makeText(Editpassword.this, "Not  Password match",Toast.LENGTH_SHORT).show();
        }
    }
}
