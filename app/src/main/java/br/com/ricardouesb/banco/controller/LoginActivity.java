package br.com.ricardouesb.banco.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import br.com.ricardouesb.banco.R;
import br.com.ricardouesb.banco.model.BancoDados;
import br.com.ricardouesb.banco.model.Client;

public class LoginActivity extends AppCompatActivity {
    private Button createAccount, enter;
    private EditText dataText, passwordText;
    private TextView errorText;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = getSharedPreferences("user_preferences", MODE_PRIVATE);

        if(BancoDados.getClientes().isEmpty()){
            Client c1 = new Client("Ricardo Rodrigues Neto", "1234", "123456");
            c1.setAccount("1111-1");
            Client c2 = new Client("Thiago Alves Viana", "9876", "123456");
            c2.setAccount("2222-2");
        }

        if(preferences.contains("client_cpf")){
            String clientCPF = preferences.getString("client_cpf", null);
            for(Client c : BancoDados.getClientes()){
                if(c.getCpf().equals(clientCPF)){
                    loginAccept(c);
                    return;
                }
            }
        }

        dataText = findViewById(R.id.dataText);
        passwordText = findViewById(R.id.passwordTextLogin);
        errorText = findViewById(R.id.errorTextLogin);
        createAccount = findViewById(R.id.createAccountButton);
        enter = findViewById(R.id.enterButton);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    private void login(){
        String dataTyped = dataText.getText().toString();
        String passwordTyped = passwordText.getText().toString();

        for(Client c : BancoDados.getClientes()){
            if(c.getAccount().equals(dataTyped) || c.getCpf().equals(dataTyped)){
                if(c.getPassword().equals(passwordTyped)){
                    loginAccept(c);
                    return;
                }else{
                    //incorret password
                    errorText.setText("Senha incorreta");
                    return;
                }
            }
        }

        //user not found
        errorText.setText("Usuário não encontrado");

    }

    private void loginAccept(Client client){
        //Save client with SharedPreferences
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("client_cpf", client.getCpf()).commit();

        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    private void createAccount(){
        Intent i = new Intent(this, CreateAccountActivity.class);
        startActivity(i);
    }
}
