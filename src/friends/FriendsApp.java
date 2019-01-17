package friends;
//import structures.Queue;
import java.io.*;
import java.util.*;
 public class FriendsApp {
	 static Scanner stdin = new Scanner(System.in);
	 public static void main(String[] args) throws IOException { 
		 Scanner sc = new Scanner(new File("graph1.txt"));
		 Graph graph = new Graph(sc);
		 ArrayList <String> path = Friends.shortestChain(graph,"notika", "katia");
		 System.out.println("Path is " +path);
		 ArrayList<ArrayList<String>> cliques = Friends.cliques(graph, "rutgers");
		 ArrayList<ArrayList<String>> cliques1 = Friends.cliques(graph, "penn state");
		 ArrayList<ArrayList<String>> cliques2 = Friends.cliques(graph, "ucla");
		 ArrayList<ArrayList<String>> cliques3 = Friends.cliques(graph, "cornell");

		 System.out.println(cliques);
		 System.out.println(cliques1);
		 System.out.println(cliques2);
		 System.out.println(cliques3);

		 ArrayList<String> connectors = Friends.connectors(graph);
		 System.out.println("Connectors are :"+connectors);
	 }
 }