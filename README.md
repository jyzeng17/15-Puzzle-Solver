# 15-Puzzle-Solver

* A Java program to solve 15-puzzle with a GUI

## Prerequisite

* Oracle/Open JDK 8

## Usage

* `$ make run`
	*(if encounter error, run `$ make clean && make` first and run again)

## Algorithm

* Searching algorithm: Bidirectional IDA\*
* Heuristic function: Manhattan distance

## Further Improvements

* Some ways to make the searching faster (not implemented yet)
	1. Program parallelism
	2. Better heuristic function, e.g., the pairwise distance
