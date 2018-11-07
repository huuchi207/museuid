package br.com.museuid.banco.controle;

import br.com.museuid.banco.dao.*;

/**
 * Classe responsável por realizar o controle dos objetos DAO que contém os
 * CRUDs e diversas operações na base de dados, filtrando a criação desses
 * objetos.
 *
 * @autor Angelica Leite
 */
public class ControleDAO {

    private static ControleDAO banco = new ControleDAO();

    private LocalDAO localDAO = new LocalDAO();
    private RelatorioDAO relatorioDAO = new RelatorioDAO();

    public static ControleDAO getBanco() {
        return banco;
    }



    public LocalDAO getLocalDAO() {
        return localDAO;
    }

    public RelatorioDAO getRelatorioDAO() {
        return relatorioDAO;
    }
}
