package graph;

import java.util.*;

/**
 * An implementation of Graph using a vertices-based representation.
 * 
 * <p>This class manages a graph where each vertex holds a list of edges to its targets.
 */
public class ConcreteVerticesGraph implements Graph<String> {

    // The list of vertices in the graph
    private final List<Vertex> vertices = new ArrayList<>();

    // Abstraction function:
    //   - ConcreteVerticesGraph represents a directed graph where each vertex is uniquely labeled.
    //   - Each vertex maintains a list of edges that point to other vertices, each with a weight.
    // Representation invariant:
    //   - vertices must not be null.
    //   - Each vertex label must be unique within the graph.
    //   - Each edge must have a valid target vertex label and a non-negative weight.
    // Safety from rep exposure:
    //   - vertices list is private and not exposed directly.
    //   - Methods return copies or unmodifiable views to prevent external modification of the internal structure.

    /**
     * Adds a vertex to the graph.
     * 
     * @param vertex the label of the vertex to add
     * @return true if the vertex was added, false if it already exists
     */
    @Override
    public boolean add(String vertex) {
        if (getVertex(vertex) != null) {
            return false; // Vertex already exists
        }
        vertices.add(new Vertex(vertex));
        return true;
    }

    /**
     * Sets an edge from the source to the target with the given weight.
     * If the weight is zero, removes the edge.
     * 
     * @param source the source vertex
     * @param target the target vertex
     * @param weight the weight of the edge
     * @return the previous weight of the edge, or zero if no edge existed
     */
    @Override
    public int set(String source, String target, int weight) {
        add(source); // Ensure the source vertex exists
        add(target); // Ensure the target vertex exists

        Vertex srcVertex = getVertex(source);
        Vertex tgtVertex = getVertex(target);

        // Update the edge if it already exists
        for (Edge edge : srcVertex.edges) {
            if (edge.target.equals(target)) {
                int oldWeight = edge.weight;
                edge.weight = weight;
                return oldWeight;
            }
        }

        // Add a new edge if no such edge exists
        srcVertex.edges.add(new Edge(target, weight));
        return 0; // No previous edge
    }

    /**
     * Removes a vertex and all associated edges from the graph.
     * 
     * @param vertex the label of the vertex to remove
     * @return true if the vertex was removed, false if the vertex does not exist
     */
    @Override
    public boolean remove(String vertex) {
        Vertex vertexToRemove = getVertex(vertex);
        if (vertexToRemove == null) return false;

        // Remove the vertex from the vertices list
        vertices.remove(vertexToRemove);

        // Remove any edges pointing to this vertex
        for (Vertex v : vertices) {
            v.edges.removeIf(edge -> edge.target.equals(vertex));
        }

        return true;
    }

    /**
     * Returns the set of all vertex labels in the graph.
     * 
     * @return an unmodifiable set of all vertex labels
     */
    @Override
    public Set<String> vertices() {
        Set<String> vertexLabels = new HashSet<>();
        for (Vertex vertex : vertices) {
            vertexLabels.add(vertex.label);
        }
        return Collections.unmodifiableSet(vertexLabels);
    }

    /**
     * Returns a map of all vertices that have edges pointing to the specified target vertex.
     * 
     * @param target the target vertex label
     * @return a map of source vertex labels to edge weights
     */
    @Override
    public Map<String, Integer> sources(String target) {
        Map<String, Integer> result = new HashMap<>();
        for (Vertex vertex : vertices) {
            for (Edge edge : vertex.edges) {
                if (edge.target.equals(target)) {
                    result.put(vertex.label, edge.weight);
                }
            }
        }
        return Collections.unmodifiableMap(result);
    }

    /**
     * Returns a map of all vertices that the specified source vertex has edges to.
     * 
     * @param source the source vertex label
     * @return a map of target vertex labels to edge weights
     */
    @Override
    public Map<String, Integer> targets(String source) {
        Map<String, Integer> result = new HashMap<>();
        Vertex srcVertex = getVertex(source);
        if (srcVertex != null) {
            for (Edge edge : srcVertex.edges) {
                result.put(edge.target, edge.weight);
            }
        }
        return Collections.unmodifiableMap(result);
    }

    /**
     * Returns a string representation of the graph.
     * 
     * @return a string representation of the graph
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Vertices: ").append(vertices()).append("\nEdges: ");
        for (Vertex vertex : vertices) {
            for (Edge edge : vertex.edges) {
                sb.append("\n  ").append(vertex.label).append(" -> ").append(edge.target)
                        .append(" (weight: ").append(edge.weight).append(")");
            }
        }
        return sb.toString();
    }

    /**
     * Helper method to find a vertex by its label.
     * 
     * @param label the label of the vertex to find
     * @return the Vertex if found, or null if not
     */
    private Vertex getVertex(String label) {
        for (Vertex vertex : vertices) {
            if (vertex.label.equals(label)) {
                return vertex;
            }
        }
        return null;
    }

    /**
     * Inner class representing a vertex in the graph.
     */
    private static class Vertex {
        private final String label;
        private final List<Edge> edges;

        public Vertex(String label) {
            this.label = label;
            this.edges = new ArrayList<>();
        }
    }

    /**
     * Inner class representing an edge between two vertices.
     */
    private static class Edge {
        private final String target;
        private int weight;

        public Edge(String target, int weight) {
            this.target = target;
            this.weight = weight;
        }
    }
}
