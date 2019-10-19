package br.com.ricardouesb.banco.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import br.com.ricardouesb.banco.R;
import br.com.ricardouesb.banco.model.BancoDados;
import br.com.ricardouesb.banco.model.Client;

public class MainActivity extends AppCompatActivity {
    private TextView accountNumber, balanceText;
    private Client clientLogged = BancoDados.getClientLogged();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        accountNumber = findViewById(R.id.accountNumberText);
        balanceText = findViewById(R.id.balanceAccountText);

        updateData();
    }

    private void updateData(){
        accountNumber.setText(clientLogged.getAccount());
        balanceText.setText("R$ " + clientLogged.getBalanceFormated());
    }
}
