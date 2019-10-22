package br.com.ricardouesb.banco.model;

import java.text.DecimalFormat;

public class Client {
    private String name;
    private String cpf;
    private String account;
    private String password;
    private float balance;

    public Client(String name, String cpf, String password) {
        this.name = name;
        this.cpf = cpf;
        this.account = (int) Math.floor(Math.random() * 10000)  + "-" + (int) Math.floor(Math.random() * 10);
        this.password = password;
        this.balance = 0;
        BancoDados.addClient(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public float getBalance() {
        return balance;
    }

    public String getBalanceFormated() {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(balance);
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }
}
