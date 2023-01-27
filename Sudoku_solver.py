

import argparse, time
from typing import Dict, List, Optional, Set, Tuple

# You are welcome to add constants, but do not modify the pre-existing constants

# Length of side of a Soduku board
SIDE = 9

# Length of side of "box" within a Soduku board
BOX = 3

# Domain for cells in Soduku board
DOMAIN = range(1, 10)

# Helper constant for checking a Soduku solution
SOLUTION = set(DOMAIN)


def check_solution(board: List[int], original_board: List[int]) -> bool:
    """Return True if board is a valid Sudoku solution to original_board puzzle"""
    # Original board values are maintained
    for s, o in zip(board, original_board):
        if o != 0 and s != o:
            return False
    for i in range(SIDE):
        # Valid row
        if set(board[i * SIDE : (i + 1) * SIDE]) != SOLUTION:
            return False
        # Valid column
        if set(board[i : SIDE * SIDE : SIDE]) != SOLUTION:
            return False
        # Valid Box
        box_row, box_col = (i // BOX) * BOX, (i % BOX) * BOX
        box = set()
        for r in range(box_row, box_row + BOX):
            box.update(board[r * SIDE + box_col : r * SIDE + box_col + BOX])
        if box != SOLUTION:
            return False
    return True






def backtracking_search(neighbors: List[List[int]], queue: Set[Tuple[int, int]], domains: List[List[int]], board) -> Tuple[Optional[List[int]], int]:
    """Perform backtracking search on CSP using AC3

    Args:
        neighbors (List[List[int]]): Indices of neighbors for each variable
        queue (Set[Tuple[int, int]]): Variable constraints; (x, y) indicates x must be consistent with y
        domains (List[List[int]]): Domains for each variable

    Returns:
        Tuple[Optional[List[int]], int]: Solution or None indicating no solution found and the number of recursive backtracking calls
    """
    # Track the number of recursive calls to backtrack
    recursions = 0
    
    
    def AC3(domains, undo):
        my_queue = queue.copy()

        while len(my_queue) > 0:

            xi, xj = my_queue.pop()

            if revise(domains, xi, xj, undo):
            
                if len(domains[xi]) == 0:
                    return False
            
                for xk in neighbors[xi]:
                    if xk != xj: 
                        my_queue.add((xk,xi))
    
        return True

    def revise(domains, xi, xj, undo):

        revised = False
        for val in domains[xi]:
            if domains[xj] == [val]:
                domains[xi].remove(val)
                undo.append((xi,val))
                revised = True
            
        return revised





    # Defining a function within another creates a closure that has access to variables in the 
    # enclosing scope (e.g., to neighbors, etc). To be able to reassign those variables we use
    # the 'nonlocal' specification
    def backtrack(assignment: Dict[int, int]) -> Optional[Dict[int, int]]:
        """Backtrack search recursive function

        Args:
            assignment (Dict[int, int]): Values currently assigned to variables (variable index as key)

        Returns:
            Optional[Dict[int, int]]: Valid assignment or None if assignment is inconsistent
        """
        nonlocal recursions # Enable us to reassign the recursions variable in the enclosing scope
        recursions += 1
   
        
        
        undo = []
        if not AC3(domains, undo):
            for var, val in undo:
                domains[var].append(val)
            return None
        
        asign = True
        for val in assignment.values():
            if val == 0:
                asign = False
        if asign:
            return assignment
        
        
             

        chosen = 0
        for key in assignment.keys():
            if assignment[key] == 0:
                chosen = key
                break

        for val in domains[chosen]:
            undo.append((chosen, val))

        for val in domains[chosen][:]:
            conflict = False
            for neigh in neighbors[chosen]:
                if val == assignment[neigh]:
                    conflict = True
                    break
            if conflict == False:
                assignment[chosen] = val
                domains[chosen] = [val]

                result = backtrack(assignment)

                if result != None:

                    return result 
                
                assignment[chosen] = 0

        for var, val in undo:
            domains[var].append(val)
        return None
    
    assign_vals = {}

    for i in range(len(board)):
        assign_vals[i] = board[i]


    result = backtrack(assign_vals)
    
    
    # Convert assignment dictionary to list
    if result is not None:
        result = [result[i] for i in range(SIDE * SIDE)]
    return result, recursions


def sudoku(board: List[int]) -> Tuple[Optional[List[int]], int]:
    """Solve Sudoku puzzle using backtracking search with the AC3 algorithm

    Do not change the signature of this function

    Args:
        board (List[int]): Flattened list of board in row-wise order. Cells that are not initially filled should be 0.

    Returns:
        Tuple[Optional[List[int]], int]: Solution as flattened list in row-wise order, or None, if no solution found and a count of calls to recursive backtracking function
    """
    
    domains = [[val] if val else list(DOMAIN) for val in board]
    neighbors = []
    queue = set()
    


    for i in range(len(board)):
        
        neighbors.append([])
        row = i//SIDE
        for r in range(SIDE):   #row 
            index = row*9+r
            if index != i:
                neighbors[i].append(index)
    
    for i in range(len(board)):
        column = i % SIDE
        row = i // SIDE
        for c in range(SIDE): #column
            index = column + c*SIDE
            if index != i:
                neighbors[i].append(index)



    for i in range(len(board)):
        output = []
        row = i//SIDE
        column = i % SIDE
        row_start = (row//3) * 3
        col_start = (column // 3) * 3

        for ri in range(row_start, row_start+3):
            for ci in range(col_start, col_start+3):
                index = ri*9+ci
                if index != i:
                    neighbors[i].append(index)




    for i in range(len(board)):
        for neighbor in neighbors[i]:
            
            queue.add((i,neighbor))


            


    return backtracking_search(neighbors, queue, domains, board)
















def backtracking_search_custom(neighbors: List[List[int]], queue: Set[Tuple[int, int]], domains: List[List[int]], board) -> Tuple[Optional[List[int]], int]:
    """Perform backtracking search on CSP using AC3

    Args:
        neighbors (List[List[int]]): Indices of neighbors for each variable
        queue (Set[Tuple[int, int]]): Variable constraints; (x, y) indicates x must be consistent with y
        domains (List[List[int]]): Domains for each variable

    Returns:
        Tuple[Optional[List[int]], int]: Solution or None indicating no solution found and the number of recursive backtracking calls
    """
    # Track the number of recursive calls to backtrack
    recursions = 0


    


    def forward_check(domains, xi, val, undo):
        for neighbor in neighbors[xi]:
            if val in domains[neighbor]:
                domains[neighbor].remove(val)
                undo.append((neighbor,val))
                if len(domains[neighbor]) == 0:
                    return False
        return True


    # Defining a function within another creates a closure that has access to variables in the 
    # enclosing scope (e.g., to neighbors, etc). To be able to reassign those variables we use
    # the 'nonlocal' specification
    def backtrack_custom(assignment: Dict[int, int]) -> Optional[Dict[int, int]]:
        """Backtrack search recursive function

        Args:
            assignment (Dict[int, int]): Values currently assigned to variables (variable index as key)

        Returns:
            Optional[Dict[int, int]]: Valid assignment or None if assignment is inconsistent
        """
        nonlocal recursions # Enable us to reassign the recursions variable in the enclosing scope
        recursions += 1
        
    
        asign = True
        for val in assignment.values():
            if val == 0:
                asign = False
        if asign:
            return assignment
        
        
            

        chosen = 0
        for key in assignment.keys():
            if assignment[key] == 0:
                chosen = key
                break

        for val in domains[chosen][:]:
            conflict = False
            for neigh in neighbors[chosen]:
                if val == assignment[neigh]:
                    conflict = True
                    break
            if not conflict:
                undo = []
                for v in domains[chosen]:
                    if v != val:
                        undo.append((chosen, v))

                assignment[chosen] = val
                domains[chosen] = [val]
                if forward_check(domains, chosen, val, undo):
                    result = backtrack_custom(assignment)

                    if result != None:

                        return result 

                for var, v in undo:
                    domains[var].append(v)
                assignment[chosen] = 0

        
        return None
    
    assign_vals = {}

    for i in range(len(board)):
        assign_vals[i] = board[i]


    result = backtrack_custom(assign_vals)
    
    
    # Convert assignment dictionary to list
    if result is not None:
        result = [result[i] for i in range(SIDE * SIDE)]
    return result, recursions


def my_sudoku(board: List[int]) -> Tuple[Optional[List[int]], int]:
    """Solve Sudoku puzzle using your own custom solver

    Do not change the signature of this function

    Args:
        board (List[int]): Flattened list of board in row-wise order. Cells that are not initially filled should be 0.

    Returns:
        Tuple[Optional[List[int]], int]: Solution as flattened list in row-wise order, or None, if no solution found and a count of calls to recursive backtracking function
    """
    domains = [[val] if val else list(DOMAIN) for val in board]
    neighbors = []
    queue = set()
    

    

    for i in range(len(board)):
        #neighbors[i] = []
        neighbors.append([])
        row = i//SIDE
        for r in range(SIDE):   #row 
            index = row*9+r
            if index != i:
                neighbors[i].append(index)
    
    for i in range(len(board)):
        column = i % SIDE
        row = i // SIDE
        for c in range(SIDE): #column
            index = column + c*SIDE
            if index != i:
                neighbors[i].append(index)



    for i in range(len(board)):
        output = []
        row = i//SIDE
        column = i % SIDE
        row_start = (row//3) * 3
        col_start = (column // 3) * 3

        for ri in range(row_start, row_start+3):
            for ci in range(col_start, col_start+3):
                index = ri*9+ci
                if index != i:
                    neighbors[i].append(index)




    for i in range(len(board)):
        for neighbor in neighbors[i]:
            #print(neighbor)
            queue.add((i,neighbor))

    return backtracking_search_custom(neighbors, queue, domains, board)
    
    
        
    


if __name__ == "__main__":
    # You should not need to modify any of this code
    parser = argparse.ArgumentParser(description="Run sudoku solver")
    parser.add_argument(
        "-a",
        "--algo",
        default="ac3",
        help="Algorithm (one of ac3, custom)",
    )
    parser.add_argument(
        "-l",
        "--level",
        default="easy",
        help="Difficulty level (one of easy, medium, hard)",
    )
    parser.add_argument(
        "-t",
        "--trials",
        default=1,
        type=int,
        help="Number of trials for timing",
    )
    parser.add_argument("puzzle", nargs="?", type=str, default=None)

    args = parser.parse_args()

    # fmt: off
    if args.puzzle:
        board = [int(c) for c in args.puzzle]
        if len(board) != SIDE*SIDE or set(board) > (set(DOMAIN) | { 0 }):
            raise ValueError("Invalid puzzle specification, it must be board length string with digits 0-9")
    elif args.level == "easy":
        board = [
            0,0,0,1,3,0,0,0,0,
            7,0,0,0,4,2,0,8,3,
            8,0,0,0,0,0,0,4,0,
            0,6,0,0,8,4,0,3,9,
            0,0,0,0,0,0,0,0,0,
            9,8,0,3,6,0,0,5,0,
            0,1,0,0,0,0,0,0,4,
            3,4,0,5,2,0,0,0,8,
            0,0,0,0,7,3,0,0,0,
        ]
    elif args.level == "medium":
        board = [
            0,4,0,0,9,8,0,0,5,
            0,0,0,4,0,0,6,0,8,
            0,5,0,0,0,0,0,0,0,
            7,0,1,0,0,9,0,2,0,
            0,0,0,0,8,0,0,0,0,
            0,9,0,6,0,0,3,0,1,
            0,0,0,0,0,0,0,7,0,
            6,0,2,0,0,7,0,0,0,
            3,0,0,8,4,0,0,6,0,
        ]
    elif args.level == "hard":
        board = [
            1,2,0,4,0,0,3,0,0,
            3,0,0,0,1,0,0,5,0,  
            0,0,6,0,0,0,1,0,0,  
            7,0,0,0,9,0,0,0,0,    
            0,4,0,6,0,3,0,0,0,    
            0,0,3,0,0,2,0,0,0,    
            5,0,0,0,8,0,7,0,0,    
            0,0,7,0,0,0,0,0,5,    
            0,0,0,0,0,0,0,9,8,
        ]
    else:
        raise ValueError("Unknown level")
    # fmt: on

    if args.algo == "ac3":
        solver = sudoku
    elif args.algo == "custom":
        solver = my_sudoku
    else:
        raise ValueError("Unknown algorithm type")

    times = []
    for i in range(args.trials):
        test_board = board[:] # Ensure original board is not modified
        start = time.perf_counter()
        solution, recursions = solver(test_board)
        end = time.perf_counter()
        times.append(end - start)
        if solution and not check_solution(solution, board):
            print(solution)
            raise ValueError("Invalid solution")

        if solution:
            print(f"Trial {i} solved with {recursions} recursions")
            print(solution)
        else:
            print(f"Trial {i} not solved with {recursions} recursions")

    print(
        f"Minimum time {min(times)}s, Average time {sum(times) / args.trials}s (over {args.trials} trials)"
    )
