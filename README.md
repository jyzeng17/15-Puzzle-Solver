# 15-Puzzle-Solver

* A Java program to solve 15-puzzle with a GUI

## Prerequisite

* Oracle/Open JDK 8

## Environment

* Ubuntu 16.04 or higher version

## Usage

* `$ mkdir classes && make && make run`

## Algorithm

* Searching algorithm: Bidirectional IDA\*
* Heuristic function: current search depth + each tile's Manhattan distance

## Further Improvements

* Some ways to make the searching faster (not implemented yet)
	1. Program parallelism
	2. Better heuristic function, e.g., the pairwise distance
