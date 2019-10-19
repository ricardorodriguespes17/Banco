package br.com.ricardouesb.banco.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logoutItem:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateData(){
        accountNumber.setText(clientLogged.getAccount());
        balanceText.setText("R$ " + clientLogged.getBalanceFormated());
    }

    private void logout(){
        BancoDados.setClientLogged(null);
        Intent i = new Intent(this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}
