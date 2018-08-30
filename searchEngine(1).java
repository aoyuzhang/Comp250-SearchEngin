import java.util.*;
import java.io.*;

// This class implements a google-like search engine
public class searchEngine {

    public HashMap<String, LinkedList<String> > wordIndex;    // this will contain a set of pairs (String, LinkedList of Strings)	
    public directedGraph internet;             // this is our internet graph
    
    
    
    // Constructor initializes everything to empty data structures
    // It also sets the location of the internet files
    searchEngine() {
	// Below is the directory that contains all the internet files
	htmlParsing.internetFilesLocation = "internetFiles";
	wordIndex = new HashMap<String, LinkedList<String> > ();		
	internet = new directedGraph();				
    } // end of constructor2013
    
    
    // Returns a String description of a searchEngine
    public String toString () {
	return "wordIndex:\n" + wordIndex + "\ninternet:\n" + internet;
    }
    
    
    // This does a graph traversal of the internet, starting at the given url.
    // For each new vertex seen, it updates the wordIndex, the internet graph,
    // and the set of visited vertices.
    
    void traverseInternet(String url) throws Exception {
	/* WRITE SOME CODE HERE */
		
    	
    	//get the links from the starting web page.
    	LinkedList<String> linksUrl =htmlParsing.getLinks(url);
    	
    	//get the words from the web page.
    	LinkedList<String> contentUrl = htmlParsing.getContent(url);
		
    	//Build the wordIndex.
		Iterator<String> i= contentUrl.iterator();
		while(i.hasNext())
		{
			String words =i.next(); //s contains words from the url.
		
			// Add the word to wordIndex. If the word is already present, don't add it.
			if ( !wordIndex.containsKey(words) )
				wordIndex.put( words, new LinkedList<String>() );
			// If the word does'n contain the url, add it to wordIndex.
			LinkedList<String> urls=wordIndex.get(words);
			if(!urls.contains(url)) urls.addLast(url);	
		}
    	
    	//build the Internet graph by adding all incident edges.
    	Iterator<String> j=linksUrl.iterator();
    	while(j.hasNext())
    	{
    		String links=j.next();
    		internet.addEdge(url,links);
    		
    		//set current link visited
        	internet.setVisited(url,true);
    		
    		//if the link is not visited then visit.
    		if(!internet.getVisited(links)) 
        		traverseInternet(links);
    	}
    
    	
	/* Hints
	   0) This should take about 50-70 lines of code (or less)
	   1) To parse the content of the url, call
	   htmlParsing.getContent(url), which returns a LinkedList of Strings 
	   containing all the words at the given url. Also call htmlParsing.getLinks(url).
	   and assign their results to a LinkedList of Strings.
	   2) To iterate over all elements of a LinkedList, use an Iterator,
	   as described in the text of the assignment
	   3) Refer to the description of the LinkedList methods at
	   http://docs.oracle.com/javase/6/docs/api/ .
	   You will most likely need to use the methods contains(String s), 
	   addLast(String s), iterator()
	   4) Refer to the description of the HashMap methods at
	   http://docs.oracle.com/javase/6/docs/api/ .
	   You will most likely need to use the methods containsKey(String s), 
	   get(String s), put(String s, LinkedList l).  
	*/
	
	
	
    } // end of traverseInternet
    
    
    /* This computes the pageRanks for every vertex in the internet graph.
       It will only be called after the internet graph has been constructed using 
       traverseInternet.
       Use the iterative procedure described in the text of the assignment to
       compute the pageRanks for every vertices in the graph. 
       
       This method will probably fit in about 30 lines.
    */
    void computePageRanks() {
    	
    	//get the list of all links in the graph. 
    	LinkedList<String> allLinks=internet.getVertices();
    	//set the page rank of all link to 1.
    	Iterator<String> i=allLinks.iterator();
    	while(i.hasNext())
    	{
    		String link=i.next();
    		internet.setPageRank(link,1.00);
    	}
    	//calculate page rank iterate 100 times
    	
    	for(int m=0;m<100;m++)
    	{
    		//for each vertex in the graph, calculate its pr.
    		Iterator<String> iteratelinks =allLinks.iterator();
    		while(iteratelinks.hasNext())
    		{
    			String link=iteratelinks.next();
    			LinkedList<String> inVertices= internet.getEdgesInto(link);
    			// need to invoke iterator to calculate pr.
    			double pr=0;
    			Iterator<String> j=inVertices.iterator(); //j is the iterator of invertices of current link
    			while(j.hasNext())
    			{
    				
    				String invertexofcurrver = j.next(); 
    				pr += internet.getPageRank(invertexofcurrver)/internet.getOutDegree(invertexofcurrver);
    				
    			}
    			pr=pr/2+0.5;
    			internet.setPageRank(link,pr);
    			
    			
    		}
    		
    		
    	}
    	
	/* WRITE YOUR CODE HERE */
	
    } // end of computePageRanks
    
	
    /* Returns the URL of the page with the high page-rank containing the query word
       Returns the String "" if no web site contains the query.
       This method can only be called after the computePageRanks method has been executed.
       Start by obtaining the list of URLs containing the query word. Then return the URL 
       with the highest pageRank.
       This method should take about 25 lines of code.
    */
    String getBestURL(String query) {
    	if ( !wordIndex.containsKey(query) ) 
    		return "";
    	else
    	{
    		double pgrofquerlist=0;
    		String highprlink="haha";
    		LinkedList<String> querylist = wordIndex.get(query); // querylist contain all links that contain word query.
    		Iterator<String> i=querylist.iterator();
    		while(i.hasNext())
    		{
    			String testlink=i.next(); // testlink is a link that contains word query
    			if(internet.getPageRank(testlink)>=pgrofquerlist)
    			{
    				pgrofquerlist=internet.getPageRank(testlink);
    				highprlink=testlink;
    			}
    		}
    		return highprlink+"  the page rank is "+pgrofquerlist;
    	}
    	
    	
	/* WRITE YOUR CODE HERE */

	// remove this
    } // end of getBestURL
    
    
	
    public static void main(String args[]) throws Exception{
    	System.out.println("hi");
	searchEngine mySearchEngine = new searchEngine();
	System.out.println("hi");
	// to debug your program, start with.
	//	mySearchEngine.traverseInternet("http://www.cs.mcgill.ca/~blanchem/250/a.html");
	
	// When your program is working on the small example, move on to
	mySearchEngine.traverseInternet("http://www.cs.mcgill.ca/~blanchem/250/a.html");
	System.out.println("hi");
	
	// this is just for debugging purposes. REMOVE THIS BEFORE SUBMITTING
	System.out.println("hi");
	System.out.println(mySearchEngine);
	
	mySearchEngine.computePageRanks();
	
	BufferedReader stndin = new BufferedReader(new InputStreamReader(System.in));
	String query;
	do {
	    System.out.print("Enter query: ");
	    query = stndin.readLine();
	    if ( query != null && query.length() > 0 ) {
		System.out.println("Best site = " + mySearchEngine.getBestURL(query));
	    }
	} while (query!=null && query.length()>0);				
    } // end of main
}
