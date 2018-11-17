package br.com.museuid.util;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import br.com.museuid.model.Relatorio;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * Assists in the control and management of the creation and generation of reports
 */
public class ReportUtils {

    private ReportUtils() {
    }

    /**
     * Filter the type of the report and call the corresponding function to create the graph
     */
    public static void create(AnchorPane box, String relatorio, int periodo, LocalDate data) {
        switch (relatorio) {
            case "Movimentação":
                movimentacao(box, periodo, data);
                break;
            case "Empréstimo":
                emprestimo(box, periodo, data);
                break;
            case "Visitas":
                visitas(box, periodo, data);
                break;
            case "Validação":
                validacao(box, periodo, data);
                break;
            case "Date Cataloging":
                dataCatalogacao(box, periodo, data);
                break;
            case "Search":
                procedencia(box);
                break;
            case "Dimensions":
                dimensoes(box);
                break;
            case "N ° Parties":
                numeroPartes(box);
                break;
            case "Location":
                localizacao(box);
                break;
            case "Designation":
                designacao(box);
                break;
            case "Estratigrafia":
                Estratigrafia(box);
                break;
            case "Collection":
                colecao(box);
                break;
            default:
                Messenger.alert("Informal report not found.");
        }
    }

    /**
     * Dados e configurações para geração do gráfico periodicos das datas de catalogações
     */
    private static void dataCatalogacao(AnchorPane panel, int periodo, LocalDate data) {
        if (periodo == 1) {
            boxGrafico(panel, "Data Catalogações", "", periodo, Consultas.catalogacaoDiaria(data));
        } else if (periodo == 2) {
            boxGrafico(panel, "Data Catalogações", "Dia", periodo, Consultas.catalogacaoMensal(data));
        } else if (periodo == 3) {
            boxGrafico(panel, "Data Catalogações", "Mês", periodo, Consultas.catalogacaoAnual(data));
        }
    }

    /**
     * Dados e configurações para geração do gráfico periodicos dos emprestimos
     */
    private static void emprestimo(AnchorPane panel, int periodo, LocalDate data) {
        if (periodo == 1) {
            boxGrafico(panel, "Empréstimo", "", periodo, Consultas.emprestimoDiaria(data));
        } else if (periodo == 2) {
            boxGrafico(panel, "Empréstimo", "Dia", periodo, Consultas.emprestimoMensal(data));
        } else if (periodo == 3) {
            boxGrafico(panel, "Empréstimo", "Mês", periodo, Consultas.emprestimoAnual(data));
        }
    }

    /**
     * Dados e configurações para geração do gráfico periodicos das movimentações
     */
    private static void movimentacao(AnchorPane panel, int periodo, LocalDate data) {
        if (periodo == 1) {
            boxGrafico(panel, "Movimentações", "", periodo, Consultas.movimentacaoDiaria(data));
        } else if (periodo == 2) {
            boxGrafico(panel, "Movimentações", "Dia", periodo, Consultas.movimentacaoMensal(data));
        } else if (periodo == 3) {
            boxGrafico(panel, "Movimentações", "Mês", periodo, Consultas.movimentacaoAnual(data));
        }
    }

    /**
     * Dados e configurações para geração do gráfico periodicos das validações
     */
    private static void validacao(AnchorPane panel, int periodo, LocalDate data) {
        if (periodo == 1) {
            boxGrafico(panel, "Validações", "", periodo, Consultas.validacaoDiaria(data));
        } else if (periodo == 2) {
            boxGrafico(panel, "Validações", "Dia", periodo, Consultas.validacaoMensal(data));
        } else if (periodo == 3) {
            boxGrafico(panel, "Validações", "Mês", periodo, Consultas.validacaoAnual(data));
        }
    }

    /**
     * Dados e configurações para geração do gráfico periodicos das visitas
     */
    private static void visitas(AnchorPane panel, int periodo, LocalDate data) {
        if (periodo == 1) {
            boxGrafico(panel, "Validações", "", periodo, Consultas.visitasDiaria(data));
        } else if (periodo == 2) {
            boxGrafico(panel, "Validações", "", periodo, Consultas.visitasMensal(data));
        } else if (periodo == 3) {
            boxGrafico(panel, "Validações", "Mês", periodo, Consultas.visitasAnual(data));
        }
    }

    /**
     * Dados e configurações para geração do gráfico de precêdencias do acervo
     */
    private static void procedencia(AnchorPane panel) {
        boxGrafico(panel, "Search", "", 1, Consultas.procedencia());
    }

    /**
     * Dados e configurações para geração do gráfico de acordo com as Dimensions do acervo
     */
    private static void dimensoes(AnchorPane panel) {
        boxGrafico(panel, "Dimensions", "", 1, Consultas.dimensoes());
    }

    /**
     * Dados e configurações para geração do gráfico de acordo com o número de partes do acervo
     */
    private static void numeroPartes(AnchorPane panel) {
        boxGrafico(panel, "N° de Partes", "", 1, Consultas.numeroPartes());
    }

    /**
     * Dados e configurações para geração do gráfico de
     */
    private static void localizacao(AnchorPane panel) {
        boxGrafico(panel, "Location Acervo", "", 1, Consultas.localizacao());
    }

    /**
     * Dados e configurações para geração do gráfico de designações do acervo
     */
    private static void designacao(AnchorPane panel) {
        boxGrafico(panel, "", "", 3, Consultas.designacao());
    }

    /**
     * Dados e configurações para geração do gráfico de formações estratigraficas do acervo
     */
    private static void Estratigrafia(AnchorPane panel) {
        boxGrafico(panel, "", "", 3, Consultas.Estratigrafia());
    }

    /**
     * Dados e configurações para geração do gráfico de coleções do acervo
     */
    private static void colecao(AnchorPane panel) {
        boxGrafico(panel, "", "", 3, Consultas.colecao());
    }

    /**
     * Verificar se maps contém conteudo e caso positivo montar grafico de acordo com seus dados e tipos para exibição e geração de relátorios
     */
    private static void boxGrafico(AnchorPane box, String titulo, String eixo, int periodo, Map<String, List<Relatorio>> map) {
        if (!map.isEmpty()) {
            switch (periodo) {
                case 1:
//                    boxConteudo(box, GraficoPie.criar(titulo, map));
                    break;
                case 2:
//                    boxConteudo(box, LineChartUtils.create(titulo, eixo, map));
                    break;
                case 3:
//                    boxConteudo(box, BarChartUtils.create(titulo, eixo, map));
                    break;
            }
        } else {
            graficoVazio(box);
        }

        map.clear();
    }

    /**
     * Informar quando grafico não contém conteúdo para exibição
     */
    private static void graficoVazio(AnchorPane box) {
        Label info = new Label("Dados não encontrados ou vazios para geração dos relátorios !");
        info.getStyleClass().add("conteudo-vazio");
        boxConteudo(box, info);
    }

    /**
     * Exibir gráfico no boxGrafico de conteúdos
     */
    private static void boxConteudo(AnchorPane box, Node conteudo) {
        box.getChildren().clear();
        ResizeUtils.margin(conteudo, 0);
        box.getChildren().add(conteudo);
    }
}
