from random import randint

class MineField:
	grid_chars = ["-","F","?","*"," ", "x"]
	#hidden, flagged, maybe, mine, empty, revealed
	mines = []
	flag_count = 0
	cleared_cells = 0

	def __init__(self, w=0, h=0, minecount=0, hchar="-", vchar="|"):
		# default size is 8x8
		if w == 0:
			w = 8
		if h == 0:
			h = w
		if minecount == 0: #default amount of mines is 16% of spaces
			minecount = ((w*h)*0.16//1) #floor division by 1 gets rid of everything after the decimal
		self.width = w
		self.height = h
		self.hbar_char = hchar
		self.edge = vchar
		self.mine_ammount = int(minecount)
		self.field = [[0 for i in range(self.width)] for j in range(self.height)]
		self.hidden = [[True for i in range(self.width)] for j in range(self.height)]
		self.display = [[0 for i in range(self.width)] for j in range(self.height)]
		self.hbar = "   |_"
		for i in range(w):
			self.hbar += f"{i}_"
		self.hbar += "|"
		self.initField()

	def initField(self):
		#Generate new mine positions
		for i in range(self.mine_ammount):
			done = False
			while not done:
				newmine = [randint(0,self.width-1), randint(0,self.height-1)]
				if newmine not in self.mines:
					done = True
					self.mines.append(newmine)
		#Set adjacent mine counts in the field 
		for mine in self.mines:
			for i in range(-1, 2):
				for j in range(-1, 2):
					if i==0 and j==0:
						continue
					elif mine[1]+i >= 0 and mine[1]+i < self.height and mine[0]+j >= 0 and mine[0]+j < self.width:
						self.field[mine[1]+i][mine[0]+j]+=1

	def printField(self):
		print(self.hbar) #Print the top row of the board (not the field)
		for i_ind,i in enumerate(self.field):
			print(f"{i_ind:3d}{self.edge}", end=" ") #print the left-most edge of the board
			for j_ind,j in enumerate(i):
				if self.hidden[i_ind][j_ind]:
					print(self.grid_chars[self.display[i_ind][j_ind]], end=" ")
				else:
					if j == 0:
						print(" ", end=" ")
					else:
						print(j, end=" ")
			print(f"{self.edge}{i_ind}") #print right-most edge of the board
		print(self.hbar) #print bottom row of the board

	def printRawField(self):
		for i in self.field:
			for j in i:
				print(f"{j:2d}", end=" ")
			print()

	def revealMines(self):
		for i in self.mines:
			self.display[i[1]][i[0]] = 3

	def printMineList(self):
		for i in self.mines:
			print(i)

	def getSurroundingCells(self, x, y):
		#[[x+i,y+j] for i in range(-1,2) for j in range(-1,2) if [i,j] != [0,0] and x+i<6 and y+j<6 and [x,y][0]>0 and [x,y][1]>0]
		res = []
		for i in range(-1,2):
			for j in range(-1,2):
				if i == 0 and j == 0: #ignore original cell
					continue
				if y+i >= 0 and y+i < self.height and x+j >= 0 and x+j < self.width:
					res.append([x+j, y+i])
		return res

	def click(self, x, y):
		if self.display[y][x] > 0: #Cant clear a cell if it is flagged
			return
		if [x,y] in self.mines: #Lose if clearing mined cell
			return
		if self.hidden[y][x]:
			self.cleared_cells += 1
			self.hidden[y][x] = False
		if self.field[y][x] == 0: #cells with no adjacent mines reveal all empty cells
			for cell in self.getSurroundingCells(x,y):
				if self.hidden[cell[1]][cell[0]]:
					self.click(cell[0], cell[1])

	def flag(self, x, y):
		if self.display[y][x] == 0:
			self.flag_count += 1
		self.display[y][x]+=1
		if self.display[y][x]>2:
			self.display[y][x]=0
			self.flag_count -= 1

	def countAdjacentMines(self, x, y):
		count = 0
		for i in range(-1,2):
			for j in range(-1,2):
				if [x+j, y+i] in self.mines:
					count+=1
		return count

################################

status_arr = ["what?", "You Win!", "You Lose!"]

print()
print()
print()
print("Welcome to PySweeper! A minesweeper clone for the Python CLI.")
print("To play, enter a set of coordinates and an action to take in the following format.")
print("\tx y action")
print("Available actions are 'f' to set a flag and 'x' to clear a cell.")
print("For old-time Windows Minesweeper players, 'f' is equivalent to a right-click and 'x' is equivalent to a left-click")
print("For example: to clear the cell 2 units across and 5 units down enter the following command")
print("\t2 5 x")
print("To place a flag in cell 8 units across and 3 units down enter the following command")
print("\t8 3 f")
print("Flags prevent you from accidentally clearing a cell where you know there is a mine and mark the cell visually.")
print("Have fun!")
print()
f1 = MineField(9)
#f1.printMineList()
# f1.revealMines()
f1.printField()
# f1.printRawField()

end = False
status = 0 #0 = neutral, 1 = won, 2 = lost

while not end:
	flg = False
	response = ""
	while not flg:
		response = input("x y action: ")
		response = response.split()
		x = int(response[0])
		y = int(response[1])
		if len(response) != 3:
			print("Invalid entry: there should be exactly 3 arguments")
			continue
		elif x < 0 or x >= f1.width or y < 0 or y>= f1.height:
			print("Invalid entry: value out of range")
			continue
		else:
			flg = True
	if response[2] == "f":
		f1.flag(x, y)
	elif response[2] == "x":
		f1.click(x, y)
	else:
		print(f"Action '{response[2]}' unrecognized. Valid actions are 'f' (set flag) and 'x' (clear cell).")
	
	if f1.display[y][x] > 0 and response[2] == "x":
		print("Cannot clear flagged cell! Use the flag action on it again to remove the flag.")
	elif [x,y] in f1.mines and response[2] == "x":
		end = True
		status = 2
		f1.revealMines()
	if (f1.width*f1.height) - f1.cleared_cells == f1.mine_ammount:
		end = True
		status = 1
	# f1.printRawField()
	print("\n"*5)
	print(f"Cells cleared: {f1.cleared_cells}/{f1.width*f1.height}    Mines found: {f1.flag_count}/{f1.mine_ammount}")
	f1.printField()

print(status_arr[status])
