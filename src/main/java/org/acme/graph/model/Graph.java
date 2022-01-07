package org.acme.graph.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.acme.graph.errors.NotFoundException;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;

/**
 * 
 * Un graphe matérialisé par une liste de sommets et d'arcs
 * 
 * @author MBorne
 *
 */
public class Graph {
	/**
	 * Liste des sommets
	 */
	private List<Vertex> vertices = new ArrayList<>();

	/**
	 * Liste des arcs
	 */
	private List<Edge> edges = new ArrayList<>();

	/**
	 * Récupération de la liste sommets
	 * 
	 * @return
	 */
	public Collection<Vertex> getVertices() {
		return vertices;
	}

	/**
	 * Recherche d'un sommet par identifiant
	 * 
	 * @param id
	 * @return
	 */
	public Vertex findVertex(String id) {
		for (Vertex vertex : vertices) {
			if (vertex.getId().equals(id)) {
				return vertex;
			}
		}
		throw new NotFoundException(String.format("Vertex '%s' not found", id));
	}

	/**
	 * Recherche d'un sommet par égalité stricte de coordonnées
	 * 
	 * @param coordinate
	 * @return
	 */
	public Vertex findVertex(Coordinate coordinate) {
		for (Vertex vertex : vertices) {
			Coordinate candidate = vertex.getCoordinate();
			if (candidate != null && candidate.equals(coordinate)) {
				return vertex;
			}
		}
		throw new NotFoundException(String.format("Vertex not found at [%s,%s]", coordinate.x, coordinate.y));
	}

	/**
	 * Récupération ou création d'un sommet en assurant l'unicité
	 * 
	 * @param graph
	 * @param coordinate
	 * @return
	 */
	public Vertex getOrCreateVertex(Coordinate coordinate) {
		Vertex vertex;
		try {
			vertex = findVertex(coordinate);
		} catch (NotFoundException e) {
			/* création d'un nouveau sommet car non trouvé */
			vertex = createVertex(coordinate, Integer.toString(getVertices().size()));
		}
		return vertex;
	}

	/**
	 * Récupération de la liste des arcs
	 * 
	 * @return
	 */
	public Collection<Edge> getEdges() {
		return edges;
	}

	/**
	 * Recherche des arcs sortant d'un sommet
	 * 
	 * @param vertex
	 * @return
	 */
	public List<Edge> getInEdges(Vertex vertex) {
		List<Edge> result = new ArrayList<>();
		for (Edge candidate : edges) {
			if (candidate.getTarget() != vertex) {
				continue;
			}
			result.add(candidate);
		}
		return result;
	}

	/**
	 * Recherche des arcs sortant d'un sommet
	 * 
	 * @param vertex
	 * @return
	 */
	public List<Edge> getOutEdges(Vertex vertex) {
		List<Edge> result = new ArrayList<>();
		for (Edge candidate : edges) {
			if (candidate.getSource() != vertex) {
				continue;
			}
			result.add(candidate);
		}
		return result;
	}

	public Vertex createVertex(Coordinate coordinate, String id){
		Vertex aVertex = new Vertex(coordinate, id);
		this.vertices.add(aVertex);
		return aVertex;
	}

	public Edge createEdge(Vertex source, Vertex target, String id, LineString geometry){
		Edge aEdge = new Edge(source,target);
		aEdge.setId(id);
		aEdge.setGeometry(geometry);
		this.edges.add(aEdge);
		return aEdge;
	}
	


}
