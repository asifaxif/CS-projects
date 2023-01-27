
from contextlib import nullcontext
import heapq as pq
import argparse, itertools, random
from typing import Callable, List, Optional, Sequence, Tuple





# Problem size 
BOARD_SIZE = 3

# The goal is a "blank" (0) in bottom right corner
GOAL = tuple(range(1, BOARD_SIZE**2)) + (0,)


def inversions(board: Sequence[int]) -> int:
    """Return the number of times a larger 'piece' precedes a 'smaller' piece in board"""
    return sum(
        (a > b and a != 0 and b != 0) for (a, b) in itertools.combinations(board, 2)
    )


class Node:
    def __init__(self, state: Sequence[int], parent: "Node" = None, cost=0, depth = 0, g_value = 0, h_value = 0):
        """Create Node to track particular state and associated parent and cost

        State is tracked as a "row-wise" sequence, i.e., the board (with _ as the blank)
        1 2 3
        4 5 6
        7 8 _
        is represented as (1, 2, 3, 4, 5, 6, 7, 8, 0) with the blank represented with a 0

        Args:
            state (Sequence[int]): State for this node, typically a list, e.g. [0, 1, 2, 3, 4, 5, 6, 7, 8]
            parent (Node, optional): Parent node, None indicates the root node. Defaults to None.
            cost (int, optional): Cost in moves to reach this node. Defaults to 0.
        """
        self.state = tuple(state)  # To facilitate "hashable" make state immutable
        self.parent = parent
        self.cost = cost
        self.depth = depth
        self.g_value = g_value
        self.h_value = h_value

    # GETTER METHODS FOR NODE CLASS
    
    def set_cost(self,val):
        self.cost = val
    
    def get_cost(self):
        return self.cost

    def set_depth(self,val):
        self.depth = val

    def get_depth(self):
        return self.depth

    def get_parent(self):
        return self.parent

    def get_state(self):
        return self.state

    def get_cost(self):
        return self.cost
    
    def get_gvalue(self):
        return self.g_value
    
    def set_gvalue(self, val):
        self.g_value = val
    
    def set_hvalue(self, val):
        self.h_value = val
    
    def get_hvalue(self, val):
        return self.h_value

    def is_goal(self) -> bool:
        """Return True if Node has goal state"""
        return self.state == GOAL

    def __lt__(self, nxt):
        return self.get_hvalue() < nxt.get_hvalue()
        
    # END OF GETTER METHODS FOR NODE CLASS

    def expand(self) -> List["Node"]:
        """Expand current node into possible child nodes with corresponding parent and cost"""

        
        
        children = []
        
        curr_pos = self.state.index(0)
        row1 = curr_pos//BOARD_SIZE
        col1 = curr_pos % BOARD_SIZE 
        if curr_pos - BOARD_SIZE >= 0: #for up
            next_pos = self._swap(row1,col1,row1-1,col1)
            Node1 = Node(state = next_pos, parent = self, cost = 1+self.cost)
            children.append(Node1)
        if curr_pos < BOARD_SIZE**2-BOARD_SIZE: #for down
            next_pos2 = self._swap(row1,col1,row1+1,col1)
            Node2 = Node(state = next_pos2, parent = self, cost = 1+self.cost)
            children.append(Node2)
        if curr_pos % BOARD_SIZE > 0: #for left
            next_pos3 = self._swap(row1,col1,row1,col1-1)
            Node3 = Node(state = next_pos3,parent = self, cost = 1+self.cost)
            children.append(Node3)
        if curr_pos % BOARD_SIZE < BOARD_SIZE-1: #for right
            next_pos4 = self._swap(row1,col1,row1,col1+1)
            Node4 = Node(state = next_pos4,parent = self,cost = 1+self.cost)
            children.append(Node4)

        return children

    def _swap(self, row1: int, col1: int, row2: int, col2: int) -> Sequence[int]:
        """Swap values in current state bewteen row1,col1 and row2,col2, returning new "state" to construct a Node"""
        state = list(self.state)
        state[row1 * BOARD_SIZE + col1], state[row2 * BOARD_SIZE + col2] = (
            state[row2 * BOARD_SIZE + col2],
            state[row1 * BOARD_SIZE + col1],
        )
        return state

    def __str__(self):
        return str(self.state)

    # The following methods enable Node to be used in types that use hashing (sets, dictionaries) or perform comparisons. Note
    # that the comparisons are performed exclusively on the state and ignore parent and cost values.

    def __hash__(self):
        return self.state.__hash__()

    def __eq__(self, other):
        return isinstance(other, Node) and self.state == other.state

    def __lt__(self, other):
        return self.state < other.state if self.h_value == other.h_value else self.h_value < other.h_value
        

def bfs(initial_board: Sequence[int], max_depth=12) -> Tuple[Optional[Node], int]:
    """Perform breadth-first search to find 8-squares solution

    Args:
        initial_board (Sequence[int]): Starting board
        max_depth (int, optional): Maximum moves to search. Defaults to 12.

    Returns:
        Tuple[Optional[Node], int]: Tuple of solution Node (or None if no solution found) and number of unique nodes explored
    """
    
    
    
    Node1 = Node(state = initial_board)
    reached = { Node1}
    frontier = []
    frontier.append(Node1)
    


    if Node1.is_goal():
        return Node1,0

    while len(frontier) != 0:
        cur_node = frontier.pop(0)
        
        unique_nodes = len(reached)
        if cur_node.get_cost() > max_depth:
            return None,unique_nodes
        elif cur_node.is_goal():
            return cur_node,unique_nodes
        else:
            nodes_list = cur_node.expand()
            for successor in nodes_list:
                
                if(successor not in reached):
                    reached.add(successor)
                    frontier.append(successor)

    
    return None, 0




def manhattan_distance(node: Node) -> int:
    """Compute manhattan distance f(node), i.e., g(node) + h(node)"""
    
    node_state = node.get_state()
    dist = node.cost
    for i in range(1,BOARD_SIZE**2):
        a,b = (node_state.index(i), GOAL.index(i))
        dist += abs(a % BOARD_SIZE - b % BOARD_SIZE) + abs(a // BOARD_SIZE - b // BOARD_SIZE) 
    return dist
    


def alt_manhattan_distance(node: Node) -> int:
    curr_state = node.get_state()
    dist=0
    for i in range(BOARD_SIZE):
        for j in range(BOARD_SIZE):
            tile_index = i*BOARD_SIZE + j
            tile_val = curr_state[tile_index]
            if tile_val != GOAL[tile_index]:
                goal_index = GOAL.index(tile_val)
                goal_i = goal_index // BOARD_SIZE
                goal_j = goal_index & BOARD_SIZE 
                dist += abs(i -goal_i) + abs(j - goal_j)

    return dist




def custom_heuristic(node: Node) -> int:
     node_state = node.get_state()
     total_conflict = 0
     for i in range(0,BOARD_SIZE):
        row_conflict = 0
        for j in range(0,BOARD_SIZE):
            tile_conflict = 0
            val = node_state[i*BOARD_SIZE + j]
            if val == 0:
                continue
            if check_row(i, val):
                k = j+1
                while(k<BOARD_SIZE):
                    next_val = node_state[i*BOARD_SIZE+k]
                    if(check_row(i, next_val)):
                        if(val>next_val):
                            tile_conflict += 1
                    k+=1
                if(tile_conflict<row_conflict):
                    row_conflict = tile_conflict
                    if row_conflict== (BOARD_SIZE-1):
                        break
        total_conflict+=row_conflict

     for j in range(0,BOARD_SIZE):
        col_conflict = 0
        for i in range(0,BOARD_SIZE):
            tile_col_conflict=0
            val = node_state[i*BOARD_SIZE+j]
            if(val == 0):
                continue
            if(check_col(j, val)):
                k = i+1
                while(k<BOARD_SIZE):
                    next_val = node_state[k*BOARD_SIZE+j]
                    if(check_col(j, next_val)):
                        if(val>next_val):
                            tile_col_conflict +=1
                    k+=1

                if(tile_col_conflict>col_conflict):
                    col_conflict = tile_col_conflict
                    if(col_conflict==(BOARD_SIZE-1)):
                        break
        total_conflict+=col_conflict

     return manhattan_distance(node) + total_conflict*2
                
        
                
def check_row(i, val):
    if i == (val / (BOARD_SIZE - 1)):
        return True
    elif i == (val / BOARD_SIZE):
        return True
    else:
        return False

def check_col(j, val):
    if ((val - (j+1)) % BOARD_SIZE) == 0:  
        return True
    return False      


def astar(
    initial_board: Sequence[int],
    max_depth=12,
    heuristic: Callable[[Node], int] = manhattan_distance,
) -> Tuple[Optional[Node], int]:
    """Perform astar search to find 8-squares solution

    Args:
        initial_board (Sequence[int]): Starting board
        max_depth (int, optional): Maximum moves to search. Defaults to 12.
        heuristic (_Callable[[Node], int], optional): Heuristic function. Defaults to manhattan_distance.

    Returns:
        Tuple[Optional[Node], int]: Tuple of solution Node (or None if no solution found) and number of unique nodes explored
    """
    

    Node1 = Node(state = initial_board)

    if(Node1.is_goal()):
        return Node1,0
    
    Frontier = []
    Reached = {Node1.state : Node1}

    
    Node1.set_hvalue(heuristic(Node1))

    pq.heappush(Frontier, Node1)
    
    unique_nodes = 0

    while len(Frontier) != 0:
        Node2 = pq.heappop(Frontier)
        
        unique_nodes = len(Reached)
        if Node2.get_cost() > max_depth:
            return None, unique_nodes
        elif Node2.is_goal():
            return Node2, unique_nodes
        else:
            
            nodes_list = Node2.expand()
            for Successor in nodes_list:
                
                Successor.set_hvalue(heuristic(Successor))

                if Successor.state not in Reached:
                    pq.heappush(Frontier, Successor)
                    Reached[Successor.state] = Successor
                elif Reached[Successor.state].cost > Successor.cost:
                    pq.heappush(Frontier, Successor)
                    Reached[Successor.state] = Successor
                    

    return None,0

if __name__ == "__main__":

    
    parser = argparse.ArgumentParser(
        description="Run search algorithms in random inputs"
    )
    parser.add_argument(
        "-a",
        "--algo",
        default="bfs",
        help="Algorithm (one of bfs, astar, astar_custom)",
    )
    parser.add_argument(
        "-i",
        "--iter",
        type=int,
        default=1000,
        help="Number of iterations",
    )
    parser.add_argument(
        "-s",
        "--state",
        type=str,
        default=None,
        help="Execute a single iteration using this board configuration specified as a string, e.g., 123456780",
    )

    args = parser.parse_args()

    num_solutions = 0
    num_cost = 0
    num_nodes = 0

    if args.algo == "bfs":
        algo = bfs
    elif args.algo == "astar":
        algo = astar
    elif args.algo == "astar_custom":
        algo = lambda board: astar(board, heuristic=custom_heuristic)
    else:
        raise ValueError("Unknown algorithm type")

    if args.state is None:
        iterations = args.iter
        while iterations > 0:
            init_state = list(range(BOARD_SIZE**2))
            random.shuffle(init_state)

            # A problem is only solvable if the parity of the initial state matches that
            # of the goal.
            if inversions(init_state) % 2 != inversions(GOAL) % 2:
                continue

            solution, nodes = algo(init_state)
            if solution:
                num_solutions += 1
                num_cost += solution.cost
                num_nodes += nodes

            iterations -= 1
    else:
        # Attempt single input state
        solution, nodes = algo([int(s) for s in args.state])
        if solution:
            num_solutions = 1
            num_cost = solution.cost
            num_nodes = nodes

    if num_solutions:
        print(
            "Iterations:",
            args.iter,
            "Solutions:",
            num_solutions,
            "Average moves:",
            num_cost / num_solutions,
            "Average nodes:",
            num_nodes / num_solutions,
        )
    else:
        print("Iterations:", args.iter, "Solutions: 0")
