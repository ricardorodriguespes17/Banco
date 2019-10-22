package br.com.ricardouesb.banco.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import br.com.ricardouesb.banco.R;
import br.com.ricardouesb.banco.model.BancoDados;
import br.com.ricardouesb.banco.model.Client;

public class MainActivity extends AppCompatActivity {
    private TextView accountNumber, balanceText, nameClient;
    private ImageButton tranferButton, depositButton;
    //Elements from alert_transfer
    private TextView balanceTextTransfer, nameAccountTranfer;
    private EditText transferValue, passwordTranfer, dataAccount;
    //Elements from alert_deposit
    private EditText depositValue;

    private Client clientLogged = BancoDados.getClientLogged();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        accountNumber = findViewById(R.id.accountNumberText);
        balanceText = findViewById(R.id.balanceAccountText);
        tranferButton = findViewById(R.id.transferButton);
        depositButton = findViewById(R.id.depositButton);
        nameClient = findViewById(R.id.nameClient);

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
        nameClient.setText(clientLogged.getName());
        accountNumber.setText(clientLogged.getAccount());
        balanceText.setText("R$ " + clientLogged.getBalanceFormated());
    }

    private void logout(){
        BancoDados.setClientLogged(null);
        Intent i = new Intent(this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

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
                    if(value <= clientLogged.getBalance()){
                        Client clientTransfer = null;

                        for(Client c : BancoDados.getClientes()){
                            if(dataOtherAccount.equals(c.getAccount()) || dataOtherAccount.equals(c.getCpf())){
                                clientTransfer = c;
                                break;
                            }
                        }

                        //Verification - Client not found
                        if(clientTransfer == null){
                            newToast("O cliente ao qual quer transferir não existe");
                            return;
                        }

                        //Verification - Clients equals
                        if(clientTransfer.equals(clientLogged)){
                            newToast("Não pode transferir para sua conta");
                            return;
                        }

                        //Transfer
                        clientTransfer.setBalance(clientTransfer.getBalance() + value);
                        clientLogged.setBalance(clientLogged.getBalance() - value);

                        //Create a toast confirmation
                        newToast("Transferência realizada com sucesso!");
                        //Update data in activity
                        updateData();
                    }else{
                        newToast("Valor menor que o saldo disponível");
                    }
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

                clientLogged.setBalance(clientLogged.getBalance() + value);
                newToast("Depósito realizado com sucesso");
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

    private void newToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
