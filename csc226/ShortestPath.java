//package shorteshpath;
/* ShortestPath.java
   CSC 226 - Spring 2017
      
   This template includes some testing code to help verify the implementation.
   To interactively provide test inputs, run the program with
	java ShortestPath
	
   To conveniently test the algorithm with a large input, create a text file
   containing one or more test graphs (in the format described below) and run
   the program with
	java ShortestPath file.txt
   where file.txt is replaced by the name of the text file.
   
   The input consists of a series of graphs in the following format:
   
    <number of vertices>
	<adjacency matrix row 1>
	...
	<adjacency matrix row n>
	
   Entry A[i][j] of the adjacency matrix gives the weight of the edge from 
   vertex i to vertex j (if A[i][j] is 0, then the edge does not exist).
   Note that since the graph is undirected, it is assumed that A[i][j]
   is always equal to A[j][i].
	
   An input file can contain an unlimited number of graphs; each will be 
   processed separately.


   B. Bird - 08/02/2014
*/


import java.util.Scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.PriorityQueue;


//Do not change the name of the ShortestPath class
public class ShortestPath{

	/* ShortestPath(G)
		Given an adjacency matrix for graph G, return the total weight
		of a minimum weight path from vertex 0 to vertex 1.
		
		If G[i][j] == 0, there is no edge between vertex i and vertex j
		If G[i][j] > 0, there is an edge between vertices i and j, and the
		value of G[i][j] gives the weight of the edge.
		No entries of G will be negative.
	*/
	public static int numVerts;
	public static int totalWeight;
	public static Node m;
	static int ShortestPath(int[][] G,int source){
		numVerts = G.length;
        Node[] nodes = new Node[numVerts];
		PriorityQueue<Node> PQ = new PriorityQueue<>(numVerts);        
        for(int i=0;i<numVerts;i++){
            if(i==0)nodes[i] = new Node(i, 0);
            else nodes[i] = new Node(i, 99999);
                PQ.add(nodes[i]);
        }
                
        Node current;
        while(nodes[source].visited==false){
            current = PQ.poll();         
			for(int i=0;i<numVerts;i++){
				if(G[current.id][i]!=0 && nodes[i].visited == false){
					PQ.remove(nodes[i]);
					nodes[i].relax(current, G[current.id][i]);
					PQ.add(nodes[i]);
				}
			}
            current.visited = true;
        }
		
		
		totalWeight = nodes[source].distance;
		m = nodes[source];	
		return totalWeight;	
	}
	
	static void PrintPaths(int source){
		Node x = m;		
		int[] path = new int[numVerts];
		int number = 0;
		for (int i=0; i<path.length;i++){
			path[i]=-1;
		}
		int j = 0;
		while (x.parent!=null){
			path[j] = x.parent.id;
			x = x.parent;
			j++;
		}
		for (int i=0; i<path.length;i++){
			if (path[i]!=-1){
				number++;
			}
		}
		int[] path1 = new int[number];
		for (int i=0; i<path1.length;i++){
			path1[i]=path[i];
		}
		
		System.out.printf("The path from 0 to %d is: ",source); 
		for (int i = path1.length-1 ;i >=0;i--){
			System.out.print(path1[i]);
			System.out.printf(" --> ");
			
		}
		System.out.print(source);
		System.out.printf(" and the total distance is : %d ",totalWeight);
		System.out.println();
    }
	
	/* main()
	   Contains code to test the ShortestPath function. You may modify the
	   testing code if needed, but nothing in this function will be considered
	   during marking, and the testing process used for marking will not
	   execute any of the code below.
	*/
	public static void main(String[] args) throws FileNotFoundException{
		Scanner s;
		if (args.length > 0){
			try{
				s = new Scanner(new File(args[0]));
			} catch(java.io.FileNotFoundException e){
				System.out.printf("Unable to open %s\n",args[0]);
				return;
			}
			System.out.printf("Reading input values from %s.\n",args[0]);
		}else{
			s = new Scanner(System.in);
			System.out.printf("Reading input values from stdin.\n");
		}
		
		int graphNum = 0;
		double totalTimeSeconds = 0;
		
		//Read graphs until EOF is encountered (or an error occurs)
		while(true){
			graphNum++;
			if(graphNum != 1 && !s.hasNextInt())
				break;
			System.out.printf("Reading graph %d\n",graphNum);
			int n = s.nextInt();
			int[][] G = new int[n][n];
			int valuesRead = 0;
			for (int i = 0; i < n && s.hasNextInt(); i++){
				for (int j = 0; j < n && s.hasNextInt(); j++){
					G[i][j] = s.nextInt();
					valuesRead++;
				}
			}
			if (valuesRead < n*n){
				System.out.printf("Adjacency matrix for graph %d contains too few values.\n",graphNum);
				break;
			}
			long startTime = System.currentTimeMillis();
			int num = G.length;
			for(int m =0; m<num;m++){	
				ShortestPath(G, m);
				PrintPaths(m);      
			}
			long endTime = System.currentTimeMillis();
			totalTimeSeconds += (endTime-startTime)/1000.0;
			
			//System.out.printf("Graph %d: Minimum weight of a 0-1 path is %d\n",graphNum,totalWeight);
		}
		graphNum--;
		System.out.printf("Processed %d graph%s.\nAverage Time (seconds): %.2f\n",graphNum,(graphNum != 1)?"s":"",(graphNum>0)?totalTimeSeconds/graphNum:0);
	}
}

class Node implements Comparable<Node>{
    public int distance, id;
    public Node parent;
	public Node next;
    public boolean visited;
    
    public Node(int id, int dist){
        this.id = id;
        this.distance = dist;
        parent = null;
		next = null;
        visited = false;
    }

    @Override
    public int compareTo(Node t) {   
        
        if(distance==t.distance) return 0;
        else if (distance<t.distance) return -1;
        else return 1;
    }

    void relax(Node t, int edgeWeight) {
        if(distance> (t.distance+edgeWeight)){
            distance = t.distance+edgeWeight;
            parent = t;
        }
    }
}

