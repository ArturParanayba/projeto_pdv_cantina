/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cantina.controller;

import cantina.model.POJO.Cliente;


import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author artur-paranayba
 */
public class AnchorPaneAlteracaoDeCreditoClienteController implements Initializable {

    @FXML
    private Label labelNomeClienteCrd;

    @FXML
    private Label labelSaldoAtual;

    @FXML
    private Label labelValorAposDeposito;

    @FXML
    private TextField textFieldValorDepositado;

    @FXML
    private ComboBox<String> comboBoxMtdPagamento;

    @FXML
    private Button btnAddCredito;

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnCalcular;

    public Stage dialogStage;
    public boolean btnAddCreditoClicked = false;
    public Cliente cliente;

    ObservableList<String> mtdDePagamento = FXCollections.observableArrayList("Cartão", "Dinheiro", "Transferência");

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        comboBoxMtdPagamento.setItems(mtdDePagamento);
       

    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isBtnAddCreditoClicked() {
        return btnAddCreditoClicked;
    }

    public void setBtnAddCreditoClicked(boolean btnAddCreditoClicked) {
        this.btnAddCreditoClicked = btnAddCreditoClicked;
    }

    public Cliente getCliente() {
        return cliente;
    }
    

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        this.labelNomeClienteCrd.setText(cliente.getNome());
        this.labelSaldoAtual.setText(Double.toString(cliente.getSaldo()));
        
        double valor = Double.valueOf(labelSaldoAtual.getText());
        if (valor < 0) {
            labelSaldoAtual.setTextFill(Color.RED);
        } else if (valor > 0) {
            labelSaldoAtual.setTextFill(Color.GREEN);
        }
        labelSaldoAtual.setText("R$ " + Double.toString(cliente.getSaldo()).replace(".", ","));
    }
    

    @FXML
    public void btnCalcular() {
        try {
            
            //Logica da adição do crédito
            double deposito = Double.valueOf(textFieldValorDepositado.getText().replaceAll(",", "."));
            double valorAtual = Double.valueOf(labelSaldoAtual.getText().replace(",", ".").replace("R$ ",""));
            double valorNovoSaldo = deposito + valorAtual;

            NumberFormat nf = NumberFormat.getNumberInstance();
            nf.setGroupingUsed(true);
            nf.setMinimumFractionDigits(2);
            String SaldoEmReal = nf.format(valorNovoSaldo);

            if (textFieldValorDepositado != null) {
 
                labelValorAposDeposito.setText("R$ " + (SaldoEmReal));
        
                if (valorNovoSaldo < 0) {
                    labelValorAposDeposito.setTextFill(Color.RED);
                } else if (valorNovoSaldo > 0) {
                    labelValorAposDeposito.setTextFill(Color.GREEN);
                }
            }

        } catch (NumberFormatException ex) {
            labelValorAposDeposito.setText(null);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ops!");
            alert.setHeaderText("Por favor, digite um valor para realizar o cálculo.");
            alert.show();
        }

    }

    @FXML
    public void btnAddCredito() throws ParseException {

        try {
            double saldoAposDeposito = Double.parseDouble(labelValorAposDeposito.getText().replace(",", ".").replace("R$ ", ""));
            double valorDeposito = Double.parseDouble(textFieldValorDepositado.getText().replace(",", ".").replace("R$ ", ""));
            
            //seta o valor novo saldo
            cliente.setSaldo(saldoAposDeposito); 
            //seta o valor do deposito
            cliente.setValorDepositoCredito(valorDeposito);
            //seta a string do método de pagamento
            String metodo = comboBoxMtdPagamento.getSelectionModel().getSelectedItem();
            //seta o metodo de pagamento
            cliente.setMetodoDePagamento(metodo);
   
            btnAddCreditoClicked = true;
            dialogStage.close();

        } catch (NullPointerException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ops!");
            alert.setHeaderText("Por favor, selecione o método de pagamento desta operação.");
            alert.show();
            ex.printStackTrace();
        }
    }

    @FXML
    public void btnCancelar() {

        dialogStage.close();
    }

}
