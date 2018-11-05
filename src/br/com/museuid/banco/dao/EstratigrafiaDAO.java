package br.com.museuid.banco.dao;

import br.com.museuid.model.Estratigrafia;
import br.com.museuid.util.Messenger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO responsável pela ações realizadas na base de dados referentes as Estratigrafias
 */
public class EstratigrafiaDAO extends DAO {

    public EstratigrafiaDAO() {
        super();
    }

    /**
     * Inserir Estratigrafia na base de dados
     */
    public void inserir(Estratigrafia Estratigrafia) {
        try {
            String sql = "INSERT INTO tb_Estratigrafia (formacao, grupo, descricao) VALUES (?, ?, ?) ";

            stm = conector.prepareStatement(sql);

            stm.setString(1, Estratigrafia.getFormacao());
            stm.setString(2, Estratigrafia.getGrupo());
            stm.setString(3, Estratigrafia.getDescricao());

            stm.executeUpdate();
            stm.close();

        } catch (SQLException ex) {
            Messenger.erro("Erro ao inserir na base de dados Estratigrafias! \n" + ex);
        }
    }

    /**
     * Atualizar dados Estratigrafia na base de dados
     */
    public void editar(Estratigrafia Estratigrafia) {
        try {
            String sql = "UPDATE tb_Estratigrafia SET formacao=?, grupo=?, descricao=? WHERE id_Estratigrafia=? ";

            stm = conector.prepareStatement(sql);

            stm.setString(1, Estratigrafia.getFormacao());
            stm.setString(2, Estratigrafia.getGrupo());
            stm.setString(3, Estratigrafia.getDescricao());

            stm.setInt(4, Estratigrafia.getId());

            stm.executeUpdate();
            stm.close();

        } catch (SQLException ex) {
            Messenger.erro("Erro ao atualizar na base de dados Estratigrafias! \n" + ex);
        }
    }

    /**
     * Excluir Estratigrafia da base de dados
     */
    public void excluir(int idEstratigrafia) {
        try {
            String sql = "DELETE FROM tb_Estratigrafia WHERE id_Estratigrafia = ? ";

            stm = conector.prepareStatement(sql);
            stm.setInt(1, idEstratigrafia);

            stm.execute();
            stm.close();

        } catch (SQLException ex) {
            Messenger.erro("Erro ao excluir na base de dados Estratigrafias! \n" + ex);
        }
    }

    /**
     * Consultar todas Estratigrafia cadastradas na base de dados
     */
    public List<Estratigrafia> listar() {

        List<Estratigrafia> dadosEstratigrafia = new ArrayList<>();

        try {
            String sql = "SELECT * FROM tb_Estratigrafia ";

            stm = conector.prepareStatement(sql);
            rs = stm.executeQuery(sql);

            while (rs.next()) {
                Estratigrafia Estratigrafia = new Estratigrafia(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
                dadosEstratigrafia.add(Estratigrafia);
            }

            stm.close();
            rs.close();

        } catch (SQLException ex) {
            Messenger.erro("Erro ao consultar na base de dados Estratigrafias! \n" + ex);
        }

        return dadosEstratigrafia;
    }

    /**
     * Consultar Estratigrafias cadastradas na base de dados para criação de combos de Estratigrafia
     */
    public List<Estratigrafia> combo() {

        List<Estratigrafia> dadosEstratigrafia = new ArrayList<>();

        try {
            String sql = "SELECT id_Estratigrafia, formacao FROM tb_Estratigrafia ORDER BY formacao ";

            stm = conector.prepareStatement(sql);
            rs = stm.executeQuery();

            while (rs.next()) {
                Estratigrafia Estratigrafia = new Estratigrafia(rs.getInt(1), rs.getString(2));
                dadosEstratigrafia.add(Estratigrafia);
            }

            stm.close();
            rs.close();

        } catch (SQLException ex) {
            Messenger.erro("Erro ao consultar na base de dados Estratigrafias! \n" + ex);
        }

        return dadosEstratigrafia;
    }

    /**
     * Consultar se nome da Estratigrafia já está cadastrado na base
     */
    public boolean isEstratigrafia(String formacao, int id) {
        try {
            String sql = "SELECT formacao FROM tb_Estratigrafia WHERE formacao =? AND id_Estratigrafia != ? ";

            stm = conector.prepareStatement(sql);
            stm.setString(1, formacao);
            stm.setInt(2, id);
            rs = stm.executeQuery();

            if (rs.next()) {
                return rs.getString(1).toLowerCase().trim().equals(formacao.toLowerCase().trim().toLowerCase());
            }

            stm.close();
            rs.close();

        } catch (SQLException ex) {
            Messenger.erro("Erro ao validar formação da Estratigrafia na base de dados! \n" + ex);
        }

        return false;
    }

    /**
     * Consultar total de Estratigrafias cadastradas
     */
    public int total() {
        try {
            String sql = "SELECT COUNT(*) FROM tb_Estratigrafia";

            stm = conector.prepareStatement(sql);
            rs = stm.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

            stm.close();
            rs.close();

        } catch (SQLException ex) {
            Messenger.erro("Erro ao consultar total de Estratigrafias cadastradas na base de dados! \n" + ex);
        }

        return 0;
    }
}
