package friends;

import structures.Queue;
import structures.Stack;

import java.util.*;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null if there is no
	 *         path from p1 to p2
	 */
	
	private static ArrayList<String> findPath(Graph g,String p1, String p2, boolean[] visited,ArrayList<String> result,ArrayList<String> currWinner,int d,int[] dis,String origin) {
		boolean found = false;
		int p2Num = g.map.get(p2);
		Person person1 = g.members[g.map.get(p1)];
		Friend f = person1.first;
		Integer n = (Integer)f.fnum;
		ArrayList<String>intResult = new ArrayList<String>();
		d++;
		boolean foundShorter = false;
		while (f!=null) {
			System.out.println("About to visit "+g.members[f.fnum].name);
			n=f.fnum;
			if (dis[n]==0 && !(g.members[n].name.equals(origin))) {
				dis[n] = d;
			} else if (dis[n]!=0) {
				System.out.println(dis[n]+", "+d);
				if (dis[n]>d) {
					dis[n]=d;
					foundShorter = true;
				} else {
					f=f.next;
					continue;
				}
			}
			if (visited[n]==true && !foundShorter) {
				System.out.println("Skipping");
				f = f.next;
				continue; //skip if this friend was already visited
			}
			if (f.fnum== p2Num) {
				String name = g.members[f.fnum].name;
				intResult.add(name);
				return intResult;
			} else {
				visited[n]=true;
				String name = g.members[f.fnum].name;
				currWinner = findPath(g, name,p2,visited,result,currWinner,d,dis,origin);
				if (currWinner==null) {
					f=f.next;
				} else {
					found = true;
					System.out.println("Current person is "+p1+" .Adding "+name);
					currWinner.add(0,name);
					if (result.size()==0 ) {
						result = currWinner;
					} else if (currWinner.size()<result.size()) {
						result = currWinner;
					}
					f = f.next;
					if (f!=null) {
						System.out.println("Going to visit next friend");
					}
				}
			}
		}
		if (!found) {
			return null;
		} 
		d--;
		return result;
	}
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		ArrayList<String> currWinner = new ArrayList<String>();
		ArrayList<String> result = new ArrayList<String>();
		boolean[]visited = new boolean[g.members.length];
		int p1Num;
		if (g.map.containsKey(p1) && g.map.containsKey(p2)) {
			p1Num = g.map.get(p1);
		} else {
			return result;
		}
		visited[p1Num]=true;
		int[]dis = new int[g.members.length];
		ArrayList<String> winner = findPath(g,p1,p2,visited,result,currWinner,0,dis,p1);
		if (winner==null) {
			return result;
		}
		winner.add(0,p1);
		for (int i=0;i<visited.length;i++) {
			System.out.println(g.members[i].name+" -- "+visited[i]);
		}
		return winner;
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null if there is no student in the
	 *         given school
	 */
	
	private static Person findByName(Graph g,String name) {
		for(Person p : g.members) {
	        if(p.name.equals(name)) {
	            return p;
	        }
	    }
		return null;
	}
	
	private static boolean areFriends(Person p, int p2) {
		boolean result = false;
		Friend f = p.first;
		while (f!=null) {
			if (f.fnum==p2) {
				result=true;
				break;
			} else {
				f = f.next;
			}
		}
		return result;
	}
	private static boolean checkFriends(int num, Graph g) {
		boolean result = false;			//write this method, check all friends for connections
		return result;
	}
	private static ArrayList<Integer> checkForFriendships(int num, ArrayList<ArrayList<String>> result,Graph g) {
		ArrayList<Integer> i=new ArrayList<Integer>();
		for (int k=0;k<result.size();k++) {
			for (int j=0;j<result.get(k).size();j++) {
				Person p1 = findByName(g,result.get(k).get(j));
				if (p1!=null && (areFriends(p1,num))) {
					i.add(k);
				} else if (checkFriends(num,g)) { //check friends of num for friendships
					i.add(k);
				}
			}
		}
		return i;
	}
	private static ArrayList<ArrayList<String>> mergeLists(ArrayList<ArrayList<String>> res, ArrayList<Integer> lists, String name) {
		ArrayList<ArrayList<String>> result = res;
		ArrayList<String> base = result.get(lists.get(0));
		base.add(name);
		for (int i=1;i<lists.size();i++) {
			for (int j=0;j<result.get(lists.get(i)).size();j++) {
				base.add( result.get(lists.get(i)).get(j) );
			}
			result.remove((int) lists.get(i));
		}
		return result;
	}
	
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		ArrayList<ArrayList<String>>result = new ArrayList<ArrayList<String>>();
		ArrayList<String> currc = new ArrayList<String>();
		ArrayList<String>temp = new ArrayList<String>();
		ArrayList<Integer> n;
		boolean[] visited = new boolean[g.members.length];
		for (int i=0;i<visited.length;i++) {
			if (visited[i] || !(g.members[i].student) || !(g.members[i].school.equals(school))) continue;
			if (result.size()==0) {
				currc.add(g.members[i].name);
				System.out.println("adding first student, "+g.members[i].name);
				result.add(currc);
			}
			else {
				n = checkForFriendships(i,result,g);
				if (n.size()>0) {
					result = mergeLists(result,n,g.members[i].name);
				} else {
					temp.add(g.members[i].name);
					currc = temp;
					result.add(currc);
				}
			}
			visited[i] =true;
		}
		return result;
		
	}
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null if there are no connectors.
	 */
	private static int[] countFriends(Graph g) {
		int[] result = new int[g.members.length];
		for (int i=0;i<result.length;i++) {
			int count = 0;
			Friend f = g.members[i].first;
			while (f!=null) {
				count++;
				f=f.next;
			}
			result[i]=count;
		}
		return result;
	}
	private static int determineMaxCurNum(int[]nums) {
		int num = 0;
		for (int i=0;i<nums.length;i++) {
			if (nums[i]>num) num=nums[i];
		}
		return num;
	}

	private static boolean isConnector(Graph g,int v,int fnum,int[] back ) {
		boolean result = false;
		Person p = g.members[v];
		Friend f = p.first;
		while (f!=null) {
			if (f.fnum!=fnum && back[f.fnum]==back[fnum]) {
				result = true;
			}
			f=f.next;
		}
		return result;
	}
	private static ArrayList<String> dfs(Graph g,int v,int init,boolean[] visited, int[] dfsNums,int[]back,int currNum ,ArrayList<String> connectors) {
		visited[v] = true;
		dfsNums[v]=currNum;
		back[v]=currNum;
		System.out.println("Visiting "+g.members[v].name);
		for (Friend f = g.members[v].first;f!=null;f=f.next) {
			currNum = determineMaxCurNum(dfsNums)+1 ;
			if (!visited[f.fnum]) {
				connectors=dfs(g,f.fnum,init,visited,dfsNums,back,currNum,connectors);
				if (dfsNums[v]>back[f.fnum]) {
					back[v]=Math.min(back[f.fnum],back[v]);
				} else {
					if (v!=init) {
						if (!connectors.contains(g.members[v].name)) {
							connectors.add(g.members[v].name);
						}
//					}
					} else if (isConnector(g,v,f.fnum,back) && !connectors.contains(g.members[v].name)) {
						connectors.add(g.members[v].name);
					}
				}
			} else {
				back[v]=Math.min(back[v], dfsNums[f.fnum]);
			}
		}
		return connectors;
	}
	
	private static ArrayList<String> dfs(Graph g) {
		int n = g.members.length;
		boolean[] visited = new boolean[n];
		int[] dfsNums = new int[n];
		int[] back = new int[n];
		ArrayList<String> connectors = new ArrayList<String>();
		int initDfsNum = 1; 
		for (int v=0;v<visited.length;v++) {
			if (!visited[v]) {
				connectors = dfs(g,v,v,visited,dfsNums,back,initDfsNum,connectors);
			}
		}
		return connectors;
	}
	
	public static ArrayList<String> connectors(Graph g) {
		ArrayList<String> connectors = new ArrayList<String>();
		connectors = dfs(g);
		return connectors;
		
	}
}

