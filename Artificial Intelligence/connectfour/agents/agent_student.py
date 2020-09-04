from connectfour.agents.computer_player import RandomAgent
import random
import copy

class StudentAgent(RandomAgent):
    def __init__(self, name):
        super().__init__(name)
        self.MaxDepth = 2


    def get_move(self, board):
        """
        Args:
            board: An instance of `Board` that is the current state of the board.

        Returns:
            A tuple of two integers, (row, col)
        """

        valid_moves = board.valid_moves()
        vals = []
        moves = []
        alpha = -99999
        beta = 99999

        for move in valid_moves:
            next_state = board.next_state(self.id, move[1])
            moves.append(move)
            vals.append(self.dfAlphaBeta(next_state, 1, alpha, beta))

        count_max = vals.count(max(vals))
        max_index = []

        # Centering the move for the moves with same max/min values
        if count_max > 1:
            for i in range(len(vals)):
                if vals[i] == max(vals):
                    max_index.append(i)
            bestMove = moves[max_index[int(count_max/2)]]
        else:
            bestMove = moves[vals.index(max(vals))]

        return bestMove

    def dfMiniMax(self, board, depth):
        # Goal return column with maximized scores of all possible next states
        
        if depth == self.MaxDepth:
            return self.evaluateBoardState(board)

        valid_moves = board.valid_moves()
        vals = []
        moves = []

        for move in valid_moves:
            if depth % 2 == 1:
                next_state = board.next_state(self.id % 2 + 1, move[1])
            else:
                next_state = board.next_state(self.id, move[1])
                
            moves.append(move)
            vals.append(self.dfMiniMax(next_state, depth + 1))

        count_max = vals.count(max(vals))
        count_min = vals.count(min(vals))
        max_index = []
        min_index = []

        if depth % 2 == 1:
            if count_min > 1:
                for i in range(len(vals)):
                    if vals[i] == min(vals):
                        min_index.append(i)
                bestVal = vals[min_index[int(count_min/2)]]
            else:
                bestVal = min(vals)
        else:
            if count_max>1:
                for i in range(len(vals)):
                    if vals[i] == max(vals):
                        max_index.append(i)
                bestVal = vals[max_index[int(count_max/2)]]
            else:
                bestVal = max(vals)

        return bestVal

    def dfAlphaBeta(self, board, depth, alpha, beta):
        """
        Performs alpha beta pruning to MaxDepth by cutting off expansion of moves based on alpha and beta values

        Implemented to improve efficiency for considerably large boards

        :param board: board state
        :param depth: current depth
        :param alpha: alpha cut off
        :param beta: beta cut off
        :return: optimized value
        """
        # base case
        if depth == self.MaxDepth:
            return self.evaluateBoardState(board)

        valid_moves = board.valid_moves()
        vals = []
        moves = []

        # Resolve Maximizing player or Minimizing player
        maximizinPlayer = True if depth % 2 == 0 else False

        if maximizinPlayer:
            value = -999999

            for move in valid_moves:
                if depth % 2 == 1:
                    next_state = board.next_state(self.id % 2 + 1, move[1])
                else:
                    next_state = board.next_state(self.id, move[1])
                value = max(value, self.dfAlphaBeta(next_state, depth + 1, alpha, beta))
                alpha = max(alpha, value)
                if alpha >= beta:
                    break
            return value
        else:
            value = 999999
            for move in valid_moves:
                if depth % 2 == 1:
                    next_state = board.next_state(self.id % 2 + 1, move[1])
                else:
                    next_state = board.next_state(self.id, move[1])
                value = min(value, self.dfAlphaBeta(next_state, depth + 1, alpha, beta))
                beta = min(beta , value)
                if alpha >= beta:
                    break
            
            return value

    def evaluateBoardState(self, board):
        """
        Your evaluation function should look at the current state and return a score for it. 
        As an example, the random agent provided works as follows:
            If the opponent has won this game, return -1.
            If we have won the game, return 1.
            If neither of the players has won, return a random number.
        """
        
        """
        These are the variables and functions for board objects which may be helpful when creating your Agent.
        Look into board.py for more information/descriptions of each, or to look for any other definitions which may help you.

        Board Variables:
            board.width 
            board.height
            board.last_move
            board.num_to_connect
            board.winning_zones
            board.score_array 
            board.current_player_score

        Board Functions:
            get_cell_value(row, col)
            try_move(col)
            valid_move(row, col)
            valid_moves()
            terminal(self)
            legal_moves()
            next_state(turn)
            winner()
        """

        # Winning condition check
        # if board.winner() != 0:
        #     if board.winner() == self.id:
        #         return 10000
        #     else:
        #         return -10000
        if  board.terminal() is False:
            if board.winner() == self.id:
                return 90000
            elif board.winner() == self.id % 2 + 1:
                return -90000
        else:
            return 0
        
        # Current player board value retrieval
        if self.id == 1:
            current_value = 1
            other_player_value = 2
        else:
            current_value = 2
            other_player_value = 1

        current_score = self.colScore(board, current_value, other_player_value) + self.rowScore(board, current_value, other_player_value) + self.diagScore(board, current_value, other_player_value)
        other_player_score = self.colScore(board, other_player_value, current_value) + self.rowScore(board, other_player_value, current_value) + self.diagScore(board, other_player_value, current_value)
        score = current_score - (2 * other_player_score)
        
        return score

    def colScore(self, board, current_value, other_player_value):
        """
        This funtion generates cumulative score for all the columns of the board based on possible winning zones

        :param board: board state
        :param current_value: current player id
        :param other_player_value: opponent player id
        :return: score
        """
        score = 0
        possible_block = []
        
        for i in range(board.width):
            for j in range(board.height - board.num_to_connect + 1):
                same_count = 0
                for k in range(0, board.num_to_connect):
                    if board.get_cell_value(j+k, i) != other_player_value:
                        if board.get_cell_value(j+k, i) == current_value:
                            same_count += 1
                    else:
                        same_count = 0
                        break
                    possible_block.append(same_count)

        # Assigning weights to winning zones and adding to final column score
        for val in possible_block:
            if val == 2:
                score += 10
            elif val == 3:
                if current_value == self.id:
                    score += 50
                else:
                    score += 2000

        return score

    def rowScore(self, board, current_value, other_player_value):
        """
        This funtion generates cumulative score for all the rows of the board based on possible winning zone

        :param board: board state
        :param current_value: current player id
        :param other_player_value: opponent player id
        :return: score
        """

        score = 0
        possible_block = []

        for i in range(board.height):
            for j in range(board.width-board.num_to_connect+1):
                same_count = 0
                for k in range(0, board.num_to_connect):
                    if board.get_cell_value(i,j+k) != other_player_value:
                        if board.get_cell_value(i, j+k) == current_value:
                            same_count += 1
                    else:
                        same_count = 0
                        break
                possible_block.append(same_count)
        
        # Assigning weights to winning zones and adding to final column score
        for val in possible_block:
            if val == 2:
                score += 10
            elif val == 3:
                if current_value == self.id:
                    score += 50
                else:
                    score += 2000

        return score

    def diagScore(self, board, current_value, other_player_value):
        """
        This funtion generates cumulative score for all the diagonals of the board based on possible winning zone

        :param board: board state
        :param current_value: current player id
        :param other_player_value: opponent player id
        :return: score
        """

        score = 0
        possible_block = []

        # Forward diagonal evaluation
        for i in range(board.width - board.num_to_connect + 1):
            for j in range(board.height - board.num_to_connect + 1):
                if i > 0 and j > 0:
                    continue
                k, m = j, i
                while k <= board.height - board.num_to_connect and m <= board.width - board.num_to_connect:
                    same_count = 0
                    for steps in range(0, board.num_to_connect):
                        if board.get_cell_value(k+steps, m+steps) != other_player_value:
                            if board.get_cell_value(k+steps, m+steps) == current_value:
                                same_count += 1
                        else:
                            same_count = 0
                            break
                    possible_block.append(same_count)
                    k += 1
                    m += 1

        # Reverse diagonal evaluation
        for i in range(board.width -1, board.num_to_connect, -1):
            for j in range(board.height - board.num_to_connect + 1):
                if i < board.width - 1 and j > 0:
                    continue
                k, m = j, i
                while k <= board.height - board.num_to_connect and m < board.num_to_connect:
                    same_count = 0
                    for steps in range(0, board.num_to_connect):
                        if board.get_cell_value(k+steps, m-steps) != other_player_value:
                            if board.get_cell_value(k+steps, m-steps) == current_value:
                                same_count += 1
                        else:
                            same_count = 0
                            break
                    possible_block.append(same_count)
                    k += 1
                    m -= 1

        # Assigning weights to winning zones and adding to final column score
        for val in possible_block:
            if val == 2:
                score += 10
            elif val == 3:
                if current_value == self.id:
                    score += 50
                else:
                    score += 2000
        return score
