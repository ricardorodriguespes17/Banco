package br.com.ricardouesb.banco.model;

import java.util.ArrayList;
import java.util.List;

public class BancoDados {
    private static List<Client> clientes = new ArrayList<>();
    private static List<Moviment> moviments = new ArrayList<>();

    public static  void addMoviment(Moviment m) {
        moviments.add(m);
    }

    public static List<Moviment> getMoviments() {
        return  moviments;
    }

    public static void addClient(Client c){
        clientes.add(c);
    }

    public static List<Client> getClientes(){
        return clientes;
    }
}
