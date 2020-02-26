package astar;

import java.util.List;

import astar.AStar.Point2D;

public class Main {
	public static void main(String[] args) {
		// We start in top left and must go to bottom right
		char[][] sample1 =  
			{{'.', '.', '.', '#', '.', '.'}
			,{'.', '.', '#', '#', '#', '#'}
			,{'#', '.', '#', '.', '.', '.'}
			,{'#', '.', '#', '.', '#', '.'}
			,{'.', '.', '.', '.', '#', '.'}
			,{'.', '.', '.', '.', '#', '.'}};
		System.out.println(shortestPathLength(sample1));
		char[][] sample2 =  
			{{'.', '#', '.', '.', '.', '#', '.', '.', '.', '#', '.', '.', '.'}
			,{'.', '#', '.', '#', '.', '#', '.', '#', '.', '#', '.', '#', '.'}
			,{'.', '#', '.', '#', '.', '#', '.', '#', '.', '#', '.', '#', '.'}
			,{'.', '#', '.', '#', '.', '#', '.', '#', '.', '#', '.', '#', '.'}
			,{'.', '#', '.', '#', '.', '#', '.', '#', '.', '#', '.', '#', '.'}
			,{'.', '#', '.', '#', '.', '#', '.', '#', '.', '#', '.', '#', '.'}
			,{'.', '.', '.', '#', '.', '.', '.', '#', '.', '.', '.', '#', '.'}};		
		System.out.println(shortestPathLength(sample2));
	}

	static float [] computeWeightMap(char [][] grid) {
    	int width = grid.length;
    	int height = grid[0].length;
    	float [] weights = new float[width*height];
    	for (int x = 0; x < width; x++) {
    		for (int y = 0; y < height; y++) {
    			// A simple grid has weight 1 for empty cells and MAX for impassable
    			float w = grid[x][y]=='.' ? 1.0f : Float.MAX_VALUE;
    			weights[x + y * width] = w;
    		}
    	}
    	return weights;
    }

	
	public static int shortestPathLength(char[][] grid) {
    	// convert input data grid to a float[] weights
		float[] weights = computeWeightMap(grid);
    	int width = grid.length;
    	int height = grid[0].length;
    	// obtain the solution : a list of points.
    	List<Point2D> path = AStar.AstarSearchPath(new Point2D(0, 0), new Point2D(grid.length-1,grid[0].length -1), weights, width, height);
		// return the length of a shortest path. The path itself is not shown.
		return path.size();
	}
}
