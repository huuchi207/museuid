package br.com.museuid.model;

public class Identificacao {

    private Catalogacao catalogacao;
    private Estratigrafia Estratigrafia;
    private Colecao colecao;
    private Local localizacao;
    private Designacao designacao;
    private Emprestimo emprestimo;

    public Identificacao(Catalogacao catalogacao, Estratigrafia Estratigrafia, Colecao colecao, Local localizacao, Designacao designacao, Emprestimo emprestimo) {
        this.catalogacao = catalogacao;
        this.Estratigrafia = Estratigrafia;
        this.colecao = colecao;
        this.localizacao = localizacao;
        this.designacao = designacao;
        this.emprestimo = emprestimo;
    }

    public Catalogacao getCatalogacao() {
        return catalogacao;
    }

    public Estratigrafia getEstratigrafia() {
        return Estratigrafia;
    }

    public Colecao getColecao() {
        return colecao;
    }

    public Local getLocalizacao() {
        return localizacao;
    }

    public Designacao getDesignacao() {
        return designacao;
    }

    public Emprestimo getEmprestimo() {
        return emprestimo;
    }

}
