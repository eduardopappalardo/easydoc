package davidsolutions.easydoc.servico.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import davidsolutions.caixaferramentas.dao.GerenciadorConexao;
import davidsolutions.easydoc.servico.constante.Configuracao;

public class GerenciadorConexaoTeste implements GerenciadorConexao {

    public Connection obterConexao() {
        try {
            DriverManager.registerDriver(new org.h2.Driver());
            return DriverManager.getConnection(Configuracao.URL_CONEXAO_BANCO.obterValor());
        } catch (SQLException excecao) {
            throw new RuntimeException(excecao);
        }
    }
}