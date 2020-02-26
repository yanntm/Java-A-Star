package astar;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipInputStream;

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
		
		imageDemo();
	}

	
    private static void imageDemo() {
		try {
			ZipInputStream zipStream = new ZipInputStream(new FileInputStream("maze.zip"));
			zipStream.getNextEntry();

			Scanner sc = new Scanner(zipStream);
			// skip two line
			//P3
			//# Created by GIMP version 2.10.14 PNM plug-in
			sc.nextLine();
			sc.nextLine();
			// 4002 4002
			String line = sc.nextLine();
			String [] tokens = line.split(" ");
			int width = Integer.parseInt(tokens[0]);
			int height = Integer.parseInt(tokens[1]);
			// next line is palette
			sc.nextLine();
			float [] weights = new float[width*height];
			int cur = 0;
			long time = System.currentTimeMillis();
			// one pixel per line
			while (sc.hasNextLine()) {
				int value = Integer.parseInt(sc.nextLine());
				if (value==0) {
					weights[cur++] = Float.MAX_VALUE; // black = wall
				} else {
					weights[cur++] = 1.0f; // white = walkable
				}
				// RGB so three values to a pixel
				sc.nextLine(); sc.nextLine();
			}
			System.out.println("Imported maze of size "+width+" x "+ height + " cells in "+ (System.currentTimeMillis()-time)+ " ms.");
			time = System.currentTimeMillis();
			List<Point2D> path = AStar.AstarSearchPath(new Point2D(0, 0), new Point2D(width-1,height-1), weights, width, height);
			System.out.println("Found a path of "+path.size()+ " length in "+ (System.currentTimeMillis() - time)+ " ms.");
		} catch (IOException e) {
			e.printStackTrace();
		}
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
