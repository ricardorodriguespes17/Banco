package br.com.ricardouesb.banco.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import br.com.ricardouesb.banco.R;
import br.com.ricardouesb.banco.model.BancoDados;
import br.com.ricardouesb.banco.model.Client;
import br.com.ricardouesb.banco.model.Moviment;
import br.com.ricardouesb.banco.model.MovimentAdapter;

public class MainActivity extends AppCompatActivity {
    private TextView accountNumber, balanceText, nameClient;
    private LinearLayout tranferButton, depositButton, extractButton, payButton;
    private ImageView changeVisibility;
    //Elements from alert_transfer
    private TextView balanceTextTransfer, nameAccountTranfer;
    private EditText transferValue, passwordTranfer, dataAccount;
    //Elements from alert_deposit
    private EditText depositValue;
    //Elements from alert_extract
    private ListView listExtract;
    //Elements form alert_pay
    private EditText barCode;
    //SharedPreference
    SharedPreferences preferences;

    private Client clientLogged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences("user_preferences", MODE_PRIVATE);

        for(Client c : BancoDados.getClientes()){
            String clientCPF = preferences.getString("client_cpf", null);
            if(c.getCpf().equals(clientCPF)){
                clientLogged = c;
            }
        }

        accountNumber = findViewById(R.id.accountNumberText);
        balanceText = findViewById(R.id.balanceAccountText);
        tranferButton = findViewById(R.id.transferButton);
        depositButton = findViewById(R.id.depositButton);
        nameClient = findViewById(R.id.nameClient);
        changeVisibility = findViewById(R.id.change_visibility);
        extractButton = findViewById(R.id.extractButton);
        payButton = findViewById(R.id.payButton);

        tranferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transferMenu();
            }
        });

        depositButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                depositMenu();
            }
        });

        extractButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extractMenu();
            }
        });

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payMenu();
            }
        });

        changeVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeVisibility();
            }
        });

        balanceText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeVisibility();
            }
        });

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

    //Update data
    private void updateData(){
        nameClient.setText(clientLogged.getName());
        accountNumber.setText(clientLogged.getAccount());
        balanceText.setText("R$ " + clientLogged.getBalanceFormated());

        boolean visible = preferences.getBoolean("visible_balance", true);

        if(visible){
            balanceText.setBackgroundColor(Color.rgb(30, 144, 255));
            changeVisibility.setImageResource(R.drawable.visible);
        }else{
            balanceText.setBackgroundColor(Color.WHITE);
            changeVisibility.setImageResource(R.drawable.invisible);
        }
    }

    //Logout
    private void logout(){
        //Clear SharedPreferences's Client data
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear().commit();

        Intent i = new Intent(this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    //Menus
    private void transferMenu() {
        AlertDialog alertTransfer;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(buildViewTranfer());
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                //Confirm transfer
                String password = passwordTranfer.getText().toString().trim();
                String dataOtherAccount = dataAccount.getText().toString().trim();
                float value = Float.parseFloat(transferValue.getText().toString().trim());

                if(password.equals(clientLogged.getPassword())){
                    //Transfer
                    Moviment m = new Moviment(clientLogged.getCpf(), dataOtherAccount, value, "Transferência para " , false);

                    String response = m.commit();
                    newToast(response);
                    //Update data in activity
                    updateData();
                }else{
                    newToast("Senha incorreta");
                }
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                //Cancel transfer
            }
        });
        alertTransfer = builder.create();
        alertTransfer.show();
    }

    private void depositMenu() {
        AlertDialog alertDeposit;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(buildViewDeposit());
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String valueString = depositValue.getText().toString().trim();

                float value;
                try{
                    value = Float.parseFloat(valueString);
                }catch (Exception e){
                    newToast("Valor inválido");
                    return;
                }

                Moviment m = new Moviment(clientLogged.getCpf(), value, "Pagamento de depósito", true);
                String response = m.commit();
                newToast(response);
                //Update data in activity
                updateData();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDeposit = builder.create();
        alertDeposit.show();
    }

    private void extractMenu() {
        AlertDialog alertExtract;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(buildViewExtract());
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        });
        alertExtract = builder.create();
        alertExtract.show();
    }

    private void payMenu() {
        AlertDialog alertPay;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(buildViewPay());
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Moviment m = new Moviment(clientLogged.getCpf(), 50, "Conta de água", false);
                String response = m.commit();
                newToast(response);
                updateData();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        });
        alertPay = builder.create();
        alertPay.show();
    }

    //Build Menu's View
    private View buildViewTranfer() {
        LayoutInflater li = getLayoutInflater();
        View view = li.inflate(R.layout.alert_transfer, null);

        balanceTextTransfer = view.findViewById(R.id.balanceTextTransfer);
        transferValue = view.findViewById(R.id.transferValue);
        passwordTranfer = view.findViewById(R.id.passwordTransfer);
        nameAccountTranfer = view.findViewById(R.id.nameAccountTransfer);
        dataAccount = view.findViewById(R.id.dataAccount);

        balanceTextTransfer.setText("O seu saldo atual é de R$" + clientLogged.getBalanceFormated());
        dataAccount.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String dataAccountText = dataAccount.getText().toString();

                for(Client c : BancoDados.getClientes()){
                    if(dataAccountText.equals(c.getCpf()) || dataAccountText.equals(c.getAccount())){
                        nameAccountTranfer.setText(c.getName());
                        return false;
                    }
                }

                nameAccountTranfer.setText("");

                return false;
            }
        });

        return view;
    }

    private View buildViewDeposit() {
        LayoutInflater li = getLayoutInflater();
        View view = li.inflate(R.layout.alert_deposit, null);

        depositValue = view.findViewById(R.id.depositValue);

        return view;
    }

    private View buildViewExtract() {
        LayoutInflater li = getLayoutInflater();
        View view = li.inflate(R.layout.alert_extract, null);
        ArrayAdapter<Moviment> arrayAdapter = new MovimentAdapter(this);
        listExtract = view.findViewById(R.id.list_extract);
        listExtract.setAdapter(arrayAdapter);
        listExtract.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        return view;
    }

    private View buildViewPay() {
        LayoutInflater li = getLayoutInflater();
        View view = li.inflate(R.layout.alert_pay, null);
        barCode = view.findViewById(R.id.bar_code);

        return view;
    }

    //Change visibility balance
    private void changeVisibility() {
        boolean visible = preferences.getBoolean("visible_balance", true);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("visible_balance", !visible).commit();
        updateData();
    }

    //Create and show a toast
    private void newToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
