package cantina.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import cantina.model.POJO.Cliente;
import cantina.model.POJO.Venda;
import cantina.model.database.Database;
import cantina.model.database.DatabaseFactory;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Locale;
import javafx.scene.control.Alert;

public class ClienteDAO {
    
//conexão com o banco 
    private  Database database = DatabaseFactory.getDatabase("postgresql");
    private  Connection connection1 = database.conectar();

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

//inicio dos metodos CRUD
    public boolean inserir(Cliente cliente) {
        //alterar nome da tabela de clientes de acordo com o banco
        String sql = "INSERT INTO cliente(nome, email, saldo) VALUES(?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEmail());
            stmt.setDouble(3, cliente.getSaldo());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            System.out.println("Erro ao inserir Cliente. Verificar SQL");
            ex.printStackTrace();
            return false;
        }
    }

    public boolean alterar(Cliente cliente) {
        //alterar nome da tabela de clientes de acordo com o banco!
        String sql = "UPDATE cliente SET nome=?, email=? WHERE codCliente=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEmail());
            stmt.setInt(3, cliente.getCodCliente());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            System.out.println("Erro ao alterar Cliente. Verificar SQL");
            ex.printStackTrace();
            return false;
        }
    }

    public boolean remover(Cliente cliente) {
        //alterar nome da tabela de clientes de acordo com o banco!
        String sql = "DELETE FROM cliente WHERE codCliente=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, cliente.getCodCliente());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            System.err.println("Erro ao inserir Remover. Verificar SQL");
            ex.printStackTrace();
            return false;
        }
    }

    public List<Cliente> listar() {
        //alterar nome da tabela de clientes de acordo com o banco!
        String sql = "SELECT * FROM cliente";
        List<Cliente> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Cliente cliente = new Cliente();
                cliente.setCodCliente(resultado.getInt("codCliente"));
                cliente.setNome(resultado.getString("nome"));
                cliente.setEmail(resultado.getString("email"));
                cliente.setSaldo(resultado.getDouble("saldo"));
                retorno.add(cliente);
            }
        } catch (SQLException ex) {
            //System.err.println("Erro ao listar clientes");
            System.err.println("Erro ao listar Cliente. Verificar SQL");
            ex.printStackTrace();

        }
        return retorno;
    }

    public Cliente buscar(Cliente cliente) {
        //alterar nome da tabela de clientes de acordo com o banco!
        String sql = "SELECT * FROM cliente WHERE codCliente=?";
        Cliente retorno = new Cliente();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, cliente.getCodCliente());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                cliente.setNome(resultado.getString("nome"));
                cliente.setEmail(resultado.getString("email"));
                cliente.setSaldo(resultado.getDouble("saldo"));
                retorno = cliente;
            }
        } catch (SQLException ex) {
            System.err.println("Erro na busca. Verificar SQL");
            ex.printStackTrace();
        }
        return retorno;
    }

    public boolean alterarSaldoNaVenda(Double valorADepositar, int codCliente) {
        try {
            String update = "UPDATE cliente SET saldo=? WHERE codcliente=?";
            PreparedStatement updateStmt = connection.prepareStatement(update);
            updateStmt.setDouble(1, valorADepositar);
            updateStmt.setInt(2, codCliente);

            updateStmt.execute();
        } catch (SQLException ex) {
            System.err.println("Erro ao alterar saldo pela venda, com o metodo de pagamento 'Saldo em conta'");
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean inserirSaldo(Cliente cliente) {
        //pega a data do dia   
        java.util.Date uDate = new java.util.Date();

        java.sql.Date sDate = convertUtilToSql(uDate);

        try {
            //INSERT
            String insert = "INSERT INTO insercaodecredito (metododepagamento, data, valor, codcliente) VALUES (?,?,?,?)";
            PreparedStatement insertStmt = connection.prepareStatement(insert);
            insertStmt.setString(1, cliente.getMetodoDePagamento());
            insertStmt.setDate(2, (sDate));
            insertStmt.setDouble(3, (cliente.getValorDeDepositoCredito()));
            insertStmt.setInt(4, cliente.getCodCliente());

            insertStmt.execute();
            try {
                //UPDATE
                String update = "UPDATE cliente SET saldo=? WHERE codCliente=?";
                PreparedStatement updateStmt = connection.prepareStatement(update);
                updateStmt.setDouble(1, cliente.getSaldo());
                updateStmt.setInt(2, cliente.getCodCliente());

                updateStmt.execute();

                return true;
            } catch (SQLException ex) {
                System.err.println("Erro ao alterar cliente na inserção de credito. Verificar SQL");
                ex.printStackTrace();
                return false;
            }

        } catch (SQLException ex) {
            System.err.println("Erro ao inserir Saldo. Verificar SQL");
            ex.printStackTrace();

            return false;
        }

    }
    
    public List<Cliente> listarNegativados() {
        String sql = "SELECT * FROM cliente WHERE saldo < 0";
        List<Cliente> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Cliente cliente = new Cliente();
                cliente.setCodCliente(resultado.getInt("codcliente"));
                cliente.setNome(resultado.getString("nome"));
                cliente.setEmail(resultado.getString("email"));
                cliente.setSaldo(resultado.getDouble("saldo"));
                retorno.add(cliente);
            }
        } catch (SQLException ex) {
            //System.err.println("Erro ao listar clientes");
            System.err.println("Erro ao listar Cliente. Verificar SQL");
            ex.printStackTrace();

        }
        return retorno;
    }
        
    public  int contarTotalSaldoNegativo(){
        
        int ttSaldoNegativo = 0;
  
        
        try {
            String sql = "select count(saldo) from cliente where saldo < 0";
            PreparedStatement stmt = connection1.prepareStatement(sql);   
            ResultSet resultado = stmt.executeQuery();
            resultado.next(); 
            int contagem = resultado.getInt(1);
            return contagem;
        } catch (SQLException ex) {
            System.err.println("Erro ao contar saldo negativo. Verficar SQL");
            ex.printStackTrace();
        }
            return ttSaldoNegativo;
        
    }
    
    public  double contarTotalSaldoDevedor(){
        
        int ttSaldoDevedor = 0;
  
        
        try {
            String sql = "select sum(saldo) from cliente where saldo < 0";
            PreparedStatement stmt = connection1.prepareStatement(sql);   
            ResultSet resultado = stmt.executeQuery();
            resultado.next(); 
            int contagem = resultado.getInt(1);
            System.out.println(contagem + " DAO");
            return contagem;
        } catch (SQLException ex) {
            System.err.println("Erro ao contar valor total do saldo devedor. Verficar SQL");
            ex.printStackTrace();
        }
            return ttSaldoDevedor;
        
    }
    

    //CONVERSOR DE DATA JAVA.UTIL.DATE PARA JAVA.SQL.DATE
    private static java.sql.Date convertUtilToSql(java.util.Date uDate) {
        java.sql.Date sDate = new java.sql.Date(uDate.getTime());
        return sDate;
    }

}
