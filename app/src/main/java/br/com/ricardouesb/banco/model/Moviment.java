package br.com.ricardouesb.banco.model;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Moviment {
    private String clientCpf;
    private String otherAccount;
    private float value;
    private String title;
    private boolean type;  //TRUE = 'input money', FALSE = 'out money'
    private Date date;


    public Moviment(String clientCpf, String otherAccount, float value, String title, boolean type) {
        this.clientCpf = clientCpf;
        this.otherAccount = otherAccount;
        this.value = value;
        this.title = title;
        this.type = type;
        date = new Date();
    }

    public Moviment(String clientCpf, float value, String title, boolean type) {
        this.clientCpf = clientCpf;
        this.value = value;
        this.title = title;
        this.type = type;
        date = new Date();
    }

    public String commit(){
        Client client = null, otherClient = null;
        for(Client c : BancoDados.getClientes()){
            if(c.getCpf().equals(clientCpf)){
                client = c;
            }
            if(c.getCpf().equals(otherAccount) || c.getAccount().equals(otherAccount)){
                otherClient = c;
            }
        }

        if(client.equals(null)){
            return "Cliente não encontado";
        }

        if(type){
            client.setBalance(client.getBalance() + value);
        }else{
            if(value > client.getBalance()){
                return "Saldo não disponível";
            }

            client.setBalance(client.getBalance() - value);

            if(otherClient != null){
                if(otherClient.equals(client)){
                    return "Erro! Conta remetente e destino iguais";
                }
                title = "Transferência para " + otherClient.getName();
                otherClient.setBalance(otherClient.getBalance() + value);
            }
        }

        BancoDados.addMoviment(this);

        return "Sucesso!";
    }

    public String getClientCpf() {
        return clientCpf;
    }

    public void setClientCpf(String clientCpf) {
        this.clientCpf = clientCpf;
    }

    public String getOtherAccount() {
        return otherAccount;
    }

    public void setOtherAccount(String otherAccount) {
        this.otherAccount = otherAccount;
    }

    public float getValue() {
        return value;
    }

    public String getValueFormated() {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(value);
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean getType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public String getDateFormated(){
        SimpleDateFormat formatDate = new SimpleDateFormat("HH:mm:ss, dd/MM/yyyy");
        return formatDate.format(date);
    }

    public void setData(Date date) {
        this.date = date;
    }
}
