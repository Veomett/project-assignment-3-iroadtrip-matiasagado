import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import static java.lang.Integer.MAX_VALUE;

public class IRoadTrip {

    private Map<String, String> countryCodes;
    private Map<String, String> correctedCountries;
    public Graph worldMap;

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
            generateMap(args[0]); // for reference: readBorders()
        } catch (FileNotFoundException e) {
            handleFileNotFound(args[0]);
        }

        try {
            readDistances(args[1]); // for reference: readBorders()
        } catch (FileNotFoundException e) {
            handleFileNotFound(args[0]);
        }

    }

    private void handleFileNotFound(String filename) {
        System.out.println("Error: " + filename + "wasn't able to open.");
        System.exit(1);
    }

    public void acceptUserInput() {

        Scanner scan = new Scanner(System.in);

        while (true) {

            System.out.print("Enter the name if the first country (type EXIT to quit): ");
            String country1 = scan.nextLine().trim();

            if (country1.equalsIgnoreCase("EXIT")) {
                break;
            }

            System.out.print("Enter the name if the first country (type EXIT to quit): ");
            String country2 = scan.nextLine().trim();

            if (country2.equalsIgnoreCase("EXIT")) {
                break;
            }

            System.out.println("Route from " + country1 + " to " + country2 + ":");
        }
    }

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
                //System.out.println(country_n_Code + " | " + countryName + " | " + countryDate);
            }
        }
    }

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
                    worldMap.replaceEdge(startCountry,destCountry,distanceKM);
                }
            }

        }
    }

    public class Graph {

        private Map<String, Map<String, Integer>> matrix = new HashMap<>(); // reference: bordersMap

        public void addVertex(String country) {
            if (! matrix.containsKey(country))
                matrix.put(country, new HashMap<>());
        }
        public void addEdge(String start, String destination, int distance) {
            matrix.get(start).put(destination, distance);
            matrix.get(destination).put(start, distance);
        }

        public boolean hasEdge(String start, String destination) {
            return ( matrix.containsKey(start) && matrix.get(start).containsKey(destination) );
        }

        public void replaceEdge(String start, String destination, int distance) {
            matrix.get(start).replace(destination, distance);
            matrix.get(destination).replace(start, distance);
        }
    }

    public static class Node implements Comparable<Node>{
        String country;
        Integer distance;
        Node(String c, Integer d){
            country = c;
            distance = d;
        }
        @Override
        public int compareTo(Node n1)
        {
            return this.distance - n1.distance;
        }
    }

    public static void main(String[] args) {
        IRoadTrip a3 = new IRoadTrip(args);

        a3.acceptUserInput();
    }
}