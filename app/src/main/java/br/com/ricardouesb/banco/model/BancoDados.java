package br.com.ricardouesb.banco.model;

import java.util.ArrayList;
import java.util.List;

public class BancoDados {
    private static List<Client> clientes = new ArrayList<>();

    public static void addClient(Client c){
        clientes.add(c);
    }

    public static List<Client> getClientes(){
        return clientes;
    }
}
