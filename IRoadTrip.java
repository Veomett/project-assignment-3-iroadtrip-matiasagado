import java.util.List;
import java.util.*;
import java.io.*;

public class IRoadTrip {

    private static Map<String, String> fixedCountries;
    private static Map<String, String> countryCodes; //fill up with countries and their ID
    private static Map<String, Map<String, Integer>> distancesMap;
    private static Map<String, Integer> subDistancesMap;
    private static Map<String, Map<String, Integer>> bordersMap;
    private static Map<String, Integer> subBordersMap;

    public IRoadTrip (String [] args) {
        if (args.length != 3) {
            System.err.println("Usage: java IRoadTrip borders.txt capdist.csv state_name.tsv");
            System.exit(1);
        }

        bordersMap = new HashMap<>();
        distancesMap = new HashMap<>();
        countryCodes = new HashMap<>();
        fixedCountries = createFixedCountires();

        try {
            readCountryCodes(args[2]);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open the file " + args[2]);
            java.lang.System.exit(1);
        }

        try {
            readDistances(args[1]);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open the file " + args[1]);
            java.lang.System.exit(1);
        }


        try {
            readBorders(args[0]);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open the file " + args[0]);
            java.lang.System.exit(1);
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
                if (fixedCountries.containsKey(countryName)) {
                    countryName = fixedCountries.get(countryName);
                    countryCodes.put(country_n_Code, countryName);
                } else {
                    countryCodes.put(country_n_Code, countryName);
                }
            }
        }
    }

    public void readDistances(String filename) throws FileNotFoundException {

        File distances = new File(filename);
        Scanner myReader = new Scanner(distances);

        String header = myReader.nextLine();

        while(myReader.hasNextLine()) {

            String distanceData = myReader.nextLine();
            String[] tokens = distanceData.split(",");
            String startCountry = countryCodes.get(tokens[0]);
            String destCountry = countryCodes.get(tokens[2]);
            int distanceKM = -1;
            subDistancesMap = new HashMap<>();

            if (startCountry != null && destCountry != null) {
                distanceKM = Integer.parseInt(tokens[4]);
                if (!distancesMap.containsKey(startCountry)) {
                    subDistancesMap.put(destCountry, distanceKM);
                    distancesMap.put(startCountry, subDistancesMap);
                } else {
                    distancesMap.get(startCountry).put(destCountry, distanceKM);
                }
            }
        }
    }

    public void readBorders(String filename) throws FileNotFoundException {

        File borders = new File(filename);
        Scanner myReader = new Scanner(borders); int count =0; int count2 = 0;

        while (myReader.hasNextLine()) {

            String countryData = myReader.nextLine();
            String[] arrCountryData = countryData.split(" = ");
            int distance = -1;
            subBordersMap = new HashMap<>();

            if (arrCountryData.length == 2) {

                String countryName = arrCountryData[0];
                if (fixedCountries.containsKey(countryName)) {
                    countryName = fixedCountries.get(countryName);
                }
                String[] neighbors = arrCountryData[1].split(" km; | km, | km|km |; | [(]| [)]");;
                for (String neighbor : neighbors) {
                    String neighborName = neighbor.replaceAll("\\d| \\d|,", "");
                    if (fixedCountries.containsKey(neighborName)) {
                        neighborName = fixedCountries.get(neighborName);
                    }
                    //System.out.println(countryName + " " + neighborName);

                    if (countryCodes.containsValue(countryName) && countryCodes.containsValue(neighborName)) {
                        try {
                            distance = distancesMap.get(countryName).get(neighborName);
                        } catch(Exception e) {}
                        if (!bordersMap.containsKey(countryName)) {
                            subBordersMap.put(neighborName, distance);
                            bordersMap.put(countryName, subBordersMap);
                        } else {
                            bordersMap.get(countryName).put(neighborName, distance);
                        }
                    }
                }

            }
        }
    }


    public int getDistance (String country1, String country2) {
        // capitals distance
        return -1;
    }


    public List<String> findPath (String country1, String country2) {

        return null;
    }


    public void acceptUserInput() {

        Scanner scan = new Scanner(System.in);
        boolean valid = true;
        String firstCountry = "";
        String secondCountry = "";

        while(valid) {
            System.out.print("Enter the name of the first country (type EXIT to quit): ");
            firstCountry = scan.next();

            if (firstCountry.equalsIgnoreCase("EXIT")) {
                valid = false;
                break;
            }

            System.out.print("Enter the name of the second country (type EXIT to quit): ");
            secondCountry = scan.next();

            if (secondCountry.equalsIgnoreCase("EXIT")) {
                valid = false;
                break;
            }

            System.out.println("Route from " + firstCountry + " to " + secondCountry + ":");
            findPath(firstCountry, secondCountry);

        }
        scan.close();
    }


    public static void main(String[] args) {
        IRoadTrip a3 = new IRoadTrip(args);

        a3.acceptUserInput();
    }
}
