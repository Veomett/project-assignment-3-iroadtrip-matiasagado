# CS 245 (Fall 2023) - Assignment 3 - IRoadTrip

# Table of Contents
- Overview
- How It Works
- Classes and Components
- Configuration
- Features
- Results
- Acknowledgements

# Overview

IRoadTrip is a Java-based program designed to find the shortest path between two countries based on their borders and distances. This tool leverages input files containing information about countries, their borders, and distances to create a graph. Users can then input two country names, and the program calculates and displays the optimal route along with corresponding distances.

# How It Works

1. Graph Creation: Reads input files to generate a graph representing countries and their neighboring relationships.
2. User Input: Accepts user input for two country names.
3. Pathfinding Algorithm: Utilizes Dijkstra's algorithm to find the shortest path between the specified countries.
4. Results Display: Presents the route and distances to the user.

# Classes and Components

#### IRoadTrip

The main class orchestrating the entire program.

- Constructor: Reads and processes input files, initializes essential components.
- User Input Handling: Takes user input for country names, validates input, and displays results.
  
#### Graph
A class representing the graph structure of countries and distances.

- Graph Structure: Represents the map of countries and their distances.
- Vertex and Edge Operations: Provides methods to add vertices, edges, and check for edge existence.

#### Node
A class representing a country node in the graph.
  
- Country Node: Represents a country in the graph, including information about its distance and the previous country in the path.

# Configuration
To run the program, execute the following command in the terminal:

    java IRoadTrip borders.txt capdist.csv state_name.tsv
    
- borders.txt: File containing information about countries and their neighbors.
- capdist.csv: File containing distances between countries.
- state_name.tsv: File containing country codes and names.
  
# Features

#### Fixing Country Names

- The program includes a mechanism for fixing discrepancies in country names. 
- A map (correctedCountries) is used to standardize country names by replacing variations with a common name.
  
#### File Handling
- The program handles file-related exceptions gracefully, displaying an error message and exiting if a file cannot be opened.
  
#### Graph Representation
- The countries and their connections are represented using a graph data structure (Graph class).
- Distances between countries are stored in the adjacency matrix of the graph.
  
#### Shortest Path Algorithm

- The program utilizes Dijkstra's algorithm to find the shortest path between two countries.
- The algorithm processes nodes in the graph, updating distances and paths as it explores the graph.

# Results

findPath:

Enter the name of the first country (type EXIT to quit): Canada

Enter the name of the second country (type EXIT to quit): Chile

Route from Canada to Chile:

* Canada --> United States of America (731 km.)
* United States of America --> Mexico (3024 km.)
* Mexico --> Guatemala (1065 km.)
* Guatemala --> Honduras (345 km.)
* Honduras --> Nicaragua (247 km.)
* Nicaragua --> Costa Rica (337 km.)
* Costa Rica --> Panama (523 km.)
* Panama --> Colombia (760 km.)
* Colombia --> Peru (1880 km.)
* Peru --> Chile (2425 km.)

# Acknowledgments

This program was developed with inspiration from educational sources:

1. [Dijsktra's Algorithm](https://www.youtube.com/watch?v=BuvKtCh0SKk)
2. [Graph Data Structure](https://www.youtube.com/watch?v=a1Z1GmKzcPs)
3. [File Handling in Java](https://www.youtube.com/watch?v=XB4MIexjvY0)

These sources provide valuable insights into the underlying concepts of the program.
