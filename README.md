# Java-A-Star

A basic Java implementation of A-star or A-* search algorithm

The intention is to be a small standalone Java implementation that is easy to adapt to new use cases.

This implementation is also meant to be *fast* and *memory efficient* and it *can* actually scale pretty well.

The input of the algorithm is :
* a "weight" grid, giving the cost of entering a given cell. For a basic application, set cost `1.0f` for empty (pathable) cells and `MAX_FLOAT` for walls and other obstacles. 
* a start and target positions, our goal is to find the minimal cost path from start to target.

The output is an ordered list of positions, representing one shortest path from start to target.

The assumptions are that :
* moves are vertical or horizontal, their cost is simply the cost of entering the cell. If you want "realistic" diagonal movements, add the `4` new adjacent positions in the main loop, and remember
 to make their entry cost $sqrt(2)$ more expensive.
* because of this assumption, the basic approximation used for heuristic distance estimation and cutoff is the "manhattan" distance.

The implementation uses :
* A `java.util.PriorityQueue` is the important data structure used to efficiently deal with the choice of next explored node.
* During the main loop, all data is flattened to a one dimensional array, hence positions are simply a single integer. 
Updates therefore do a lot of modulo width or integer divide by width, i.e. recompute (row,grid) from this index.
* The output of the low-level procedure is a reversed list of node indexes, from goal to start. A wrapper is offered to expose this as a list of `Point2D`.

# Example usage

The example main shows how to to produce the initial `float [] weights` by supposing we have a screen of pixels
 given as grid of characters where `.` is passable and `#` is a wall.

The code is more general than the use of it done in the main ; in particular we only consider walls or empty spaces in the grid when the algorithm supports arbitrary costs.

## Acknowledgements

This project is under MIT License, use it as you will.

Originally based on project : https://github.com/hjweide/a-star/ Copyright (c) 2016 Hendrik Weideman 

This port to Java by Yann Thierry-Mieg (c) (2020) based off of https://github.com/yanntm/YoBot/blob/master/Astar.cpp.
