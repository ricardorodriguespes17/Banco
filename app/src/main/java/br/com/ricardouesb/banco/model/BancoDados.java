package br.com.ricardouesb.banco.model;

import java.util.ArrayList;
import java.util.List;

public class BancoDados {
    private static List<Client> clientes = new ArrayList<>();
    private static Client clientLogged;

    public static void addClient(Client c){
        clientes.add(c);
    }

    public static List<Client> getClientes(){
        return clientes;
    }

    public static void setClientLogged(Client c){
        BancoDados.clientLogged = c;
    }

    public static Client getClientLogged(){
        return clientLogged;
    }
}
