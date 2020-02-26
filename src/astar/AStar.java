package astar;

/*
File originally under MIT License
Copyright (c) 2016 Hendrik Weideman
https://github.com/hjweide/a-star/

This port to Java by Yann Thierry-Mieg, based off of  :
https://github.com/yanntm/YoBot/blob/master/Astar.cpp

*/

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class AStar
{
    
    public static class Point2D {
    	public int x;
    	public int y;
		public Point2D(int x, int y) {
			this.x = x;
			this.y = y;
		}
		@Override
		public String toString() {
			return "(" + x + ", " + y + ")";
		}
    }

    static List<Point2D> AstarSearchPath(Point2D start, Point2D end, float [] weights, int width, int height)
    {	
    	int wsize = width * height;
    	int [] paths = new int[wsize];
    	List<Point2D> path = new ArrayList<Point2D>();
    	int startidx = start.x + start.y * width;
    	int endidx = end.x + end.y * width;
    	if (astar(weights, height, width, startidx, endidx, paths)) {
    		int path_idx = endidx;
    		while (path_idx != startidx) {
    			path.add(new Point2D(path_idx % width, path_idx / width));
    			path_idx = paths[path_idx];
    		}		
    		Collections.reverse(path);
    	} 
    	
    	return path;
    }

    // represents a single pixel
    private static class Node {
    	int idx;     // index in the flattened grid
    	float cost;  // cost of traversing this pixel
		public Node(int idx, float cost) {
			this.idx = idx;
			this.cost = cost;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + idx;
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Node other = (Node) obj;
			if (idx != other.idx)
				return false;
			return true;
		}
		@Override
		public String toString() {
			return "Node [idx=" + idx + ", cost=" + cost + "]";
		}
    };

    // we want the smallest cost node when we extract from PriorityQueue
    static Comparator<Node> cmpNode () {
    	return new Comparator<AStar.Node>() {
			public int compare(Node n1, Node n2) {
				return Float.compare(n1.cost,n2.cost);
			}
		}; 
    }

    // See for various grid heuristics:
    // http://theory.stanford.edu/~amitp/GameProgramming/Heuristics.html#S7
    // L_\inf norm (diagonal distance)
    static float linf_norm(int i0, int j0, int i1, int j1) {
    	return Math.max(Math.abs(i0 - i1), Math.abs(j0 - j1));
    }

    // L_1 norm (manhattan distance)
    static float l1_norm(int i0, int j0, int i1, int j1) {
    	return Math.abs(i0 - i1) + Math.abs(j0 - j1);
    }

    // weights:        flattened h x w grid of costs
    // h, w:           height and width of grid
    // start, goal:    index of start/goal in flattened grid
    // paths (output): for each node, stores previous node in path
    static boolean astar(
    	float[] weights, int h, int w,
    	int start, int goal,
    	int[] paths) {
    	
    	float INF = Float.POSITIVE_INFINITY;

    	Node start_node= new Node(start, 0f);
    	Node goal_node = new Node(goal, 0f);

    	float[] costs = new float[h * w];
    	for (int i = 0; i < h * w; ++i)
    		costs[i] = INF;
    	costs[start] = 0.f;

    	PriorityQueue<Node> nodes_to_visit = new PriorityQueue<Node>(cmpNode());
    	nodes_to_visit.add(start_node);

    	int [] nbrs = new int[4];

    	boolean solution_found = false;
    	while (!nodes_to_visit.isEmpty()) {
    		// .peek() doesn't actually remove the node
    		Node cur = nodes_to_visit.peek();

    		if (cur.equals(goal_node)) {
    			solution_found = true;
    			break;
    		}
    		// does extracts cur from the Queue
    		nodes_to_visit.poll();

    		int row = cur.idx / w;
    		int col = cur.idx % w;
    		// check bounds and find up to four neighbors: rotate from top left to bottom
    		// add more elements here to have diagonal moves.
    		nbrs[0] = (row > 0) ? cur.idx - w : -1;
    		nbrs[1] = (col + 1 < w) ? cur.idx + 1 : -1;
    		nbrs[2] = (row + 1 < h) ? cur.idx + w : -1;
    		nbrs[3] = (col > 0) ? cur.idx - 1 : -1;

    		float heuristic_cost;
    		
    		// iterate over the 4 possible moves
    		for (int i = 0; i < 4; ++i) {
    			if (nbrs[i] >= 0) {
    				// the sum of the cost so far and the cost of this move
    				// this should be multiplied by sqrt(2) for diagonal moves
    				float wei = weights[nbrs[i]];

    				float new_cost = costs[cur.idx] + wei;
    				if (new_cost < costs[nbrs[i]]) {
    					// estimate the cost to the goal based on legal moves
    					// this is manhattan, prefer linf_norm if diagonal are allowed
    					heuristic_cost = l1_norm(nbrs[i] / w, nbrs[i] % w,
    							goal / w, goal    % w);

    					// paths with lower expected cost are explored first
    					float priority = new_cost + heuristic_cost;
    					nodes_to_visit.add(new Node(nbrs[i], priority));

    					costs[nbrs[i]] = new_cost;
    					paths[nbrs[i]] = cur.idx;
    				}
    			}
    		}
    	}
    	return solution_found;
    }
}

