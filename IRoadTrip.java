import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import static java.lang.Integer.MAX_VALUE;

public class IRoadTrip {

    private Map<String, String> countryCodes;
    private Map<String, String> correctedCountries;
    public Graph worldMap;

    // Constructor that initializes the IRoadTrip object by reading input files
    public IRoadTrip(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage: java IRoadTrip borders.txt capdist.csv state_name.tsv");
            System.exit(1);
        }
        correctedCountries = createFixedCountires();
        countryCodes = new HashMap<>();
        worldMap = new Graph();
        try {
            readCountryCodes(args[2]);
        } catch (FileNotFoundException e) {
            handleFileNotFound(args[2]);
        }
        try {
            generateMap(args[0]);
        } catch (FileNotFoundException e) {
            handleFileNotFound(args[0]);
        }
        try {
            readDistances(args[1]);
        } catch (FileNotFoundException e) {
            handleFileNotFound(args[0]);
        }
    }

    // Handles the case when a file is not found
    private void handleFileNotFound(String filename) {
        System.out.println("Error: " + filename + "wasn't able to open.");
        System.exit(1);
    }

    // Accepts user input for country names and finds the route between them
    public void acceptUserInput() {
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.print("Enter the name of the first country (type EXIT to quit): ");
            String country1 = scan.nextLine().trim();
            if (country1.equalsIgnoreCase("EXIT")) {
                break;
            }
            if(!worldMap.matrix.containsKey(country1)) {
                System.out.println("Invalid country name. Please enter a valid country name.");
                continue;
            }
            System.out.print("Enter the name of the second country (type EXIT to quit): ");
            String country2 = scan.nextLine().trim();
            if (country2.equalsIgnoreCase("EXIT")) {
                break;
            }
            if(!worldMap.matrix.containsKey(country2)) {
                System.out.println("Invalid country name. Please enter a valid country name.");
                continue;
            }
            System.out.println("Route from " + country1 + " to " + country2 + ":");
            List<String> path = findPath(country1, country2);
            if (path == null) {
                System.out.println("No path exists");
            } else {
                for (String string : path) {
                    System.out.println(string);
                }
            }
            //System.out.println( getDistance(country1, country2) ); // works 10/10
        }
    }

    // Creates a map for fixing country name discrepancies
    private Map<String, String> createFixedCountires() {
        Map<String, String> fixedCountries = new HashMap<String, String>();

        // B
        fixedCountries.put("Bahamas, The", "Bahamas");
        fixedCountries.put("Bosnia-Herzegovina", "Bosnia and Herzegovina");
        fixedCountries.put("Botswana.", "Botswana");
        fixedCountries.put("Burkina Faso (Upper Volta)", "Burkina Faso");
        fixedCountries.put("Belarus (Byelorussia)", "Belarus");
        // C
        fixedCountries.put("Congo, Democratic Republic of (Zaire)", "Democratic Republic of the Congo");
        fixedCountries.put("Congo, Democratic Republic of the", "Democratic Republic of the Congo");
        fixedCountries.put("Congo, Republic of the", "Republic of the Congo");
        fixedCountries.put("Congo", "Republic of the Congo");
        fixedCountries.put("Cambodia (Kampuchea)", "Cambodia");
        // E
        fixedCountries.put("East Timor", "Timor-Leste");
        // G
        fixedCountries.put("German Federal Republic", "Germany");
        fixedCountries.put("Greenland).", "Greenland");
        fixedCountries.put("Gambia, The", "The Gambia");
        fixedCountries.put("Gambia", "The Gambia");
        // I
        fixedCountries.put("Italy.", "Italy");
        fixedCountries.put("Italy/Sardinia", "Italy");
        fixedCountries.put("Iran (Persia)", "Iran");
        // K
        fixedCountries.put("Korea, North", "North Korea");
        fixedCountries.put("Korea, People's Republic of", "North Korea");
        fixedCountries.put("Korea, South", "South Korea");
        fixedCountries.put("Korea, Republic of", "South Korea");
        fixedCountries.put("Kyrgyz Republic", "Kyrgyzstan");
        // M
        fixedCountries.put("Macedonia (Former Yugoslav Republic of)", "Macedonia");
        fixedCountries.put("Macedonia", "North Macedonia");
        fixedCountries.put("Macedonia (Former Yugoslav Republic of)", "North Macedonia");
        fixedCountries.put("Myanmar (Burma)", "Burma");
        // R
        fixedCountries.put("Russia (Soviet Union)", "Russia");
        // S
        fixedCountries.put("Sri Lanka (Ceylon)", "Sri Lanka");
        // T
        fixedCountries.put("Turkey (Turkiye)", "Turkey");
        fixedCountries.put("Turkey (Ottoman Empire)", "Turkey");
        fixedCountries.put("Tanzania/Tanganyika", "Tanzania");
        // U
        fixedCountries.put("US", "United States of America");
        fixedCountries.put("United States", "United States of America");
        fixedCountries.put("UK", "United Kingdom");
        fixedCountries.put("UAE", "United Arab Emirates");
        // V
        fixedCountries.put("Vietnam, Democratic Republic of", "Vietnam");
        // Y
        fixedCountries.put("Yemen (Arab Republic of Yemen)", "Yemen");
        // Z
        fixedCountries.put("Zambia.", "Zambia");
        fixedCountries.put("Zimbabwe (Rhodesia)", "Zimbabwe");

        return fixedCountries;
    }

    // Reads country codes from a file
    private void readCountryCodes(String filename) throws FileNotFoundException {
        File countryNames = new File(filename);
        Scanner myReader = new Scanner(countryNames);
        String header = myReader.nextLine(); //garbage
        while (myReader.hasNextLine()) {
            String countryData = myReader.nextLine();
            String[] tokens = countryData.split("\\t");
            String country_n_Code = tokens[0];
            String countryName = tokens[2];
            String countryDate = tokens[4];
            if (countryDate.equals("2020-12-31")) {
                if (correctedCountries.containsKey(countryName)) {
                    countryName = correctedCountries.get(countryName);
                    countryCodes.put(country_n_Code, countryName);
                } else {
                    countryCodes.put(country_n_Code, countryName);
                }
            }
        }
    }

    // Generates a map of countries and their neighbors from a file
    private void generateMap(String filename) throws FileNotFoundException {
        String countryData;
        String[] country_n_neighbors;
        String[] neighbors = new String[0];
        String countryName;
        File borders = new File(filename);
        Scanner myReader = new Scanner(borders);
        while (myReader.hasNextLine()) {
            countryData = myReader.nextLine();
            country_n_neighbors = countryData.split(" = ", 2);
            countryName = country_n_neighbors[0];
            if (correctedCountries.containsKey(countryName)) {
                countryName = correctedCountries.get(countryName);
            }
            worldMap.addVertex(countryName);
            if (country_n_neighbors.length > 1) {
                neighbors = country_n_neighbors[1].split(" km; | km|; | [(]| [)]");
            }
            if (neighbors.length != 0) {
                for (String neighbor : neighbors) {
                    String neighborName = neighbor.replaceAll("\\d| \\d|,", "");
                    if (correctedCountries.containsKey(neighborName)) {
                        neighborName = correctedCountries.get(neighborName);
                    }
                    //System.out.println(countryName + " " + neighborName);
                    worldMap.addVertex(neighborName);
                    worldMap.addEdge(countryName, neighborName, MAX_VALUE);
                }
            }
        }
    }

    // Reads distances between countries from a file
    private void readDistances(String filename) throws FileNotFoundException {

        File distances = new File(filename);
        Scanner myReader = new Scanner(distances);
        myReader.nextLine(); // skips headers
        while (myReader.hasNextLine()) {
            String distanceData = myReader.nextLine();
            String[] tokens = distanceData.split(",");
            String startCountry = countryCodes.get(tokens[0]);
            String destCountry = countryCodes.get(tokens[2]);
            int distanceKM = -1;
            if (startCountry != null && destCountry != null) {
                distanceKM = Integer.parseInt(tokens[4]);
                if (worldMap.matrix.containsKey(startCountry) && worldMap.hasEdge(startCountry, destCountry)) {
                    worldMap.matrix.get(startCountry).replace(destCountry, distanceKM);
                    worldMap.matrix.get(destCountry).replace(startCountry, distanceKM);
                }
            }
        }
    }

    // Graph class representing a map of countries and distances
    public class Graph {
        private Map<String, Map<String, Integer>> matrix = new HashMap<>(); // reference: bordersMap
        public void addVertex(String country) {
            if (!matrix.containsKey(country))
                matrix.put(country, new HashMap<>());
        }
        public void addEdge(String start, String destination, int distance) {
            matrix.get(start).put(destination, distance);
            matrix.get(destination).put(start, distance);
        }

        public boolean hasEdge(String start, String destination) {
            return ( matrix.containsKey(start) && matrix.get(start).containsKey(destination) );
        }
    }

    // Node class representing a country in the graph
    public class Node implements Comparable<Node> {
        private String country;
        private int distance;
        private String prevCountry;

        public Node(String country, int distance) {
            this.country = country;
            this.distance = distance;
            this.prevCountry = null;
        }
        public String getCountry() {
            return this.country;
        }
        public int getDistance() {
            return this.distance;
        }
        public String getPrevCountry() {
            return this.prevCountry;
        }
        public void setPrevCountry(String prevCountry) {
            this.prevCountry = prevCountry;
        }
        @Override
        public int compareTo(Node e) {
            return this.distance - e.getDistance();
        }
    }

    // Gets the distance between two countries
    public int getDistance(String country1, String country2) {
        Map<String, Integer> neighbors = worldMap.matrix.get(country1);
        return (neighbors != null && neighbors.containsKey(country2)) ? neighbors.get(country2) : -1;
    }

    // Finds the shortest path between two countries
    public List<String> findPath(String country1, String country2) {
        PriorityQueue<Node> path = initializePriorityQueue(country1);
        HashMap<String, Integer> fDistances = new HashMap<>();
        HashMap<String, String> edges = new HashMap<>();
        HashMap<String, String> vEdges = new HashMap<>();

        processNodes(path, fDistances, edges, vEdges, country2);

        return buildPath(edges, country1, country2);
    }

    // Initializes the priority queue with the root node
    private PriorityQueue<Node> initializePriorityQueue(String country1) {
        PriorityQueue<Node> path = new PriorityQueue<>();
        Node root = new Node(country1, 0);
        path.add(root);
        return path;
    }

    // Processes nodes in the graph to find the shortest path
    private void processNodes(
            PriorityQueue<Node> path,
            HashMap<String, Integer> fDistances,
            HashMap<String, String> edges,
            HashMap<String, String> vEdges,
            String country2
    ) {
        while (!path.isEmpty()) {
            Node currentNode = path.poll();
            String cNodeName = currentNode.getCountry();

            if (!fDistances.containsKey(cNodeName)) {
                fDistances.put(cNodeName, currentNode.getDistance());
                edges.put(cNodeName, currentNode.getPrevCountry());
            } else {
                continue;
            }

            Set<String> neighbors = getNeighbors(cNodeName);
            for (String neighbor : neighbors) {
                processNeighbor(
                        cNodeName,
                        neighbor,
                        currentNode,
                        fDistances,
                        edges,
                        vEdges,
                        path
                );
            }
        }
    }

    // Gets neighbors of a country in the graph
    private Set<String> getNeighbors(String cNodeName) {
        return worldMap.matrix.get(cNodeName).keySet();
    }

    // Processes a neighbor during path finding
    private void processNeighbor(
            String cNodeName,
            String neighbor,
            Node currentNode,
            HashMap<String, Integer> fDistances,
            HashMap<String, String> edges,
            HashMap<String, String> vEdges,
            PriorityQueue<Node> path
    ) {
        int neighborDistance = getDistance(cNodeName, neighbor);
        int rootDistance = neighborDistance + currentNode.getDistance();

        if (fDistances.get(neighbor) == null || rootDistance < fDistances.get(neighbor)) {
            Node neighbourCountry = new Node(neighbor, rootDistance);
            neighbourCountry.setPrevCountry(cNodeName);
            updateEdgesAndAddToPath(
                    cNodeName,
                    neighbor,
                    neighbourCountry,
                    vEdges,
                    path
            );
        }
    }

    // Updates edges and adds a neighbor to the path
    private void updateEdgesAndAddToPath(
            String cNodeName,
            String neighbor,
            Node neighbourCountry,
            HashMap<String, String> vEdges,
            PriorityQueue<Node> path
    ) {
        String edge = cNodeName + neighbor;
        String nEdge = neighbor + cNodeName;

        if (!vEdges.containsKey(edge) && !vEdges.containsKey(nEdge)) {
            vEdges.put(edge, nEdge);
            path.add(neighbourCountry);
        }
    }

    // Builds the path based on the edges
    private List<String> buildPath(HashMap<String, String> edges, String country1, String country2) {
        if (!edges.containsKey(country2)) {
            return null;
        }

        LinkedList<String> toReturn = new LinkedList<>();
        String toSearch = country2;

        while (!toSearch.equals(country1)) {
            String prevCountry = edges.get(toSearch);
            int dist = worldMap.matrix.get(toSearch).get(prevCountry);
            String toAdd = "* " + prevCountry + " --> " + toSearch + " (" + dist + " km.)";
            toReturn.addFirst(toAdd);
            toSearch = prevCountry;
        }

        return toReturn;
    }

    // Main method to create an IRoadTrip object and start user input
    public static void main(String[] args) {
        IRoadTrip a3 = new IRoadTrip(args);

        a3.acceptUserInput();
    }
}