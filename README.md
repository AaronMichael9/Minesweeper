# Minesweeper
(yes, I'm collaborating with myself on this project, my git accounts are a bit jumbled)
This project has 3 parts:
A. Fully functional Minesweeper game with user interface. Currently it is hard coded to 15 by 30 with 40 bombs, but it should work with any (reasonable) specifications.
B. An interface that allows various AI's to "play" Minesweeper. The program will create an extension of
	the Analyzer class and repeatedly give it the board state, then ask it for the next move.
C. A challenge. Try to construct the best algorithm for playing Minesweeper
   My original intent was to measure success as wins out of 1000, but as of 6/6/2018 none of my algorithms have won a single game.
   As an alternative the system also tracks the average number of tiles revealed per game.
   (As a side note, this measurement is more consistent than I expected. 450 tiles, 1000 games at a time, it usually differs by less than 10)
   Currently the best is 300 tiles per game.
   update: the initial shortcomings of the algorithms was due to a bug that was causing my solvers to only see half the board. After fixing that my best algorithm
   achieves approximately 80% win rate, but averaging less than 350 tiles per game.
