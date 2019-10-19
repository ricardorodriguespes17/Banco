package br.com.ricardouesb.banco.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import br.com.ricardouesb.banco.R;
import br.com.ricardouesb.banco.model.Client;

public class CreateAccountActivity extends AppCompatActivity {
    private Button create, cancel;
    private EditText nameText, cpfText, passwordText, confirmPassowdText;
    private TextView errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        nameText = findViewById(R.id.nameText);
        cpfText = findViewById(R.id.cpfText);
        passwordText = findViewById(R.id.passwordText);
        confirmPassowdText = findViewById(R.id.confirmPasswordText);
        errorText = findViewById(R.id.errorTextCreateAccount);
        create = findViewById(R.id.createButton);
        cancel = findViewById(R.id.cancelButton);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }

    private void create(){
        String name = nameText.getText().toString();
        String cpf = cpfText.getText().toString();
        String password = passwordText.getText().toString();
        String confirmPassword = confirmPassowdText.getText().toString();

        if(password.equals(confirmPassword)) {
            //password is equals
            Client c = new Client(name, cpf, password);

            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }else{
            //passwords is different
            errorText.setText("Senhas não estão iguais");

        }
    }

    private void cancel(){
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }
}
