package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private EventsDao dao;
	private List<Integer>allYears;
	private List<String>allCategories;
	
	private Graph<String, DefaultWeightedEdge>grafo;
	private List<String>allTypes;
	private int pesoMAX;
	
	private List<String> migliore;
	private double pesoMIN;
	
	public Model() {
		this.dao = new EventsDao();
		this.allCategories = new ArrayList<>(dao.listAllCategories());
		this.allYears = new ArrayList<>(dao.listAllAnni());
		this.allTypes = new ArrayList<>();
	}
	
	public String creaGrafo(String category, Integer anno) {
		this.grafo = new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		this.allTypes = dao.listAllVertici(category, anno);
		Graphs.addAllVertices(grafo, this.allTypes);
		
		for(String x : this.allTypes) {
			for(String y : this.allTypes) {
				if(!x.equals(y)) {
					Integer peso = dao.getWeight(x, y);
					if(peso > 0) {
						Graphs.addEdge(grafo, x, y, peso);
					}
				}
			}	
		}
		
		return "Grafo creato con "+grafo.vertexSet().size()+" vertici e "+grafo.edgeSet().size()+" archi";
	}
	
	public List<CoppiaA>archiPesoMax(){
		List<CoppiaA>archiMAX = new ArrayList<>();
		this.pesoMAX = 0;
		for(DefaultWeightedEdge x : grafo.edgeSet()) {
			Integer peso = (int) grafo.getEdgeWeight(x);
			String type1 = grafo.getEdgeSource(x);
			String type2 = grafo.getEdgeTarget(x);
			if(peso > this.pesoMAX) {
				CoppiaA c = new CoppiaA(type1, type2, peso);
				archiMAX.clear();
				archiMAX.add(c);
				this.pesoMAX = peso;
			}else if(peso == this.pesoMAX) {
				CoppiaA c = new CoppiaA(type1, type2, peso);
				archiMAX.add(c);
			}
		}
		return archiMAX;
	}
	
	
	public void calcolaPercorso(String type1, String type2) {
		this.pesoMIN = 0.0;//puo essere o il numero di album o un parametro da calcolare
		this.migliore = new ArrayList<String>();
		List<String> rimanenti = new ArrayList<>(this.grafo.vertexSet());
		List<String> parziale = new ArrayList<>();
		parziale.add(type1);
		rimanenti.remove(type1);
		
		ricorsione(0, parziale, rimanenti, 0, type2);
	}

	 private void ricorsione(Integer livello, List<String> parziale, List<String> rimanenti, Integer pesoParziale, String t2){
	
	// Condizione Terminale
	if (rimanenti.isEmpty()) {
		//calcolo costo
		
		if (pesoParziale < this.pesoMIN && this.pesoMIN != 0 && parziale.size() == grafo.vertexSet().size() && parziale.get(parziale.size()-1).equals(t2)) {
			this.pesoMIN = pesoParziale;
			this.migliore = new ArrayList<>(parziale);
		}
		return;
	}
	
	
   	for (String p : rimanenti) {
   		if(!parziale.contains(p)) {
   				List<String> currentRimanenti = new ArrayList<>(rimanenti);
   				DefaultWeightedEdge e = grafo.getEdge(p, parziale.get(parziale.size()-1));
   				Integer pesoArcoAttuale = (int) grafo.getEdgeWeight(e);
				parziale.add(p);
				currentRimanenti.remove(p);
				ricorsione(livello+1, parziale, currentRimanenti, pesoParziale+pesoArcoAttuale, t2);
				parziale.remove(parziale.size()-1);
			}
		}
	
}

	public EventsDao getDao() {
		return dao;
	}

	public List<Integer> getAllYears() {
		return allYears;
	}

	public List<String> getAllCategories() {
		return allCategories;
	}

	public Graph<String, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}

	public List<String> getAllTypes() {
		return allTypes;
	}

	public int getPesoMAX() {
		return pesoMAX;
	}

	public List<String> getMigliore() {
		return migliore;
	}

	public double getPesoMIN() {
		return pesoMIN;
	}

		
	
}
