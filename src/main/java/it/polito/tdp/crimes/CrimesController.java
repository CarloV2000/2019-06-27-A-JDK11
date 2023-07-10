/**
 * Sample Skeleton for 'Crimes.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.model.CoppiaA;
import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class CrimesController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxCategoria"
    private ComboBox<String> boxCategoria; // Value injected by FXMLLoader

    @FXML // fx:id="boxAnno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="boxArco"
    private ComboBox<String> boxArco; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	String categoria = this.boxCategoria.getValue();
    	Integer anno = this.boxAnno.getValue();
    	if(categoria == null) {
    		this.txtResult.setText("Selezionare una categoria");
    		return;
    	}
    	if(anno == null) {
    		this.txtResult.setText("Selezionare un anno");
    		return;
    	}
    	
    	String s = model.creaGrafo(categoria, anno);
    	this.txtResult.setText(s);
    	
    	String s2 = "\nArchi di peso massimo : \n";
    	List<CoppiaA>archi = new ArrayList<>(model.archiPesoMax());
    	for(CoppiaA x : archi) {
    		s2 += x.getT1()+"<->"+x.getT2()+"("+x.getPeso()+")\n";
    	}
    	this.txtResult.appendText(s2);
    	
    	for(CoppiaA x : archi) {
    		this.boxArco.getItems().add(x.getT1()+"<->"+x.getT2()+"("+x.getPeso()+")");
    	}
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	String arco = this.boxArco.getValue();
    	String type1 = arco.substring(0, arco.indexOf("<"));
    	String type2 = arco.substring(arco.indexOf(">")+1, arco.indexOf("("));
    	
    	model.calcolaPercorso(type1, type2);    	
    	Integer min = (int) model.getPesoMIN();
    	List<String>res = new ArrayList<>(model.getMigliore());
    	String s = "Percorso trovato : (peso = "+min+")\n";
    	for(String x : res) {
    		s += x+"\n";
    	}
    	this.txtResult.setText(s);
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxCategoria != null : "fx:id=\"boxCategoria\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxArco != null : "fx:id=\"boxArco\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Crimes.fxml'.";
        
    }
    
    public void setModel(Model model) {
    	this.model = model;
    	for(Integer i : model.getAllYears()) {
    		this.boxAnno.getItems().add(i);
    	}
    	for(String c : model.getAllCategories()) {
    		this.boxCategoria.getItems().add(c);
    	}
    }
}
