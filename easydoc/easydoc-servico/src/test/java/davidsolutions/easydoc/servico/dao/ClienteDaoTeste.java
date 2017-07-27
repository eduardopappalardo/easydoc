package davidsolutions.easydoc.servico.dao;

import java.sql.SQLException;

import org.hsqldb.util.DatabaseManagerSwing;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import davidsolutions.caixaferramentas.conversao.ConversorException;
import davidsolutions.caixaferramentas.dao.DaoException;
import davidsolutions.caixaferramentas.reflection.ReflectionException;
import davidsolutions.easydoc.servico.entidade.Cliente;

public class ClienteDaoTeste extends DaoTeste {

    private ClienteDao clienteDao;

    private Cliente cliente;

    @Before
    public void configurar() throws DaoException, SQLException {
        this.clienteDao = new ClienteDao(super.conexao);
        this.cliente = new Cliente();
        this.cliente.setNome("Teste");
    }

    @Test
    public void adicionar() throws DaoException, SQLException, ReflectionException, ConversorException {
        this.clienteDao.adicionar(this.cliente);
    }

    @After
    public void abrirConsoleH2() {
        DatabaseManagerSwing
                .main(new String[] { "--url", "jdbc:h2:mem:EasyDoc", "--user", "sa", "--password", "" });
    }
}