/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cantina.controller;

import cantina.model.POJO.Cliente;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author artur-paranayba
 */
public class AnchorPaneCadastroDeClientesController implements Initializable {

    @FXML
    private Label labelClienteNome;

    @FXML
    private Label labelClienteEmail;

    @FXML
    private Label labelClienteSaldoInicial;

    @FXML
    private TextField textFieldClienteNome;

    @FXML
    private TextField textFieldClienteEmail;

    @FXML
    private TextField textFieldClienteSaldoInicial;

    @FXML
    private Button btnConfirmar;

    @FXML
    private Button btnCancelar;
    
    

    public Stage dialogStage;
    public boolean btnConfirmarClicked = false;
    public Cliente cliente;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
        public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }


    public boolean isBtnConfirmarClicked() {
        return btnConfirmarClicked;
    }

    
    public void setBtnConfirmarClicked(boolean btnConfirmarClicked) {
        this.btnConfirmarClicked = btnConfirmarClicked;
    }

    public Cliente getCliente() {
        return cliente;
    }


    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        
    }
        
    
    @FXML
    public void btnConfirmar(){
        
        cliente.setNome(textFieldClienteNome.getText());
        cliente.setEmail(textFieldClienteEmail.getText());
        //converte texto para double Double.valueOf()
        cliente.setSaldo(Double.valueOf(textFieldClienteSaldoInicial.getText().replaceAll(",", ".")));
        
        btnConfirmarClicked = true;
        dialogStage.close();
        
    }
    
    public void btnCancelar(){
        dialogStage.close();
    }
    
    
}
