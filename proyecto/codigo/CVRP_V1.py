import sys
import numpy as np
import math


class Vehicle:
    def __init__(self, Q, Tmax):
        self.current = 0
        self.route = [[0, 0.0]]
        self.rem_energy = Q
        self.rem_time = Tmax
        self.time2base = 0.0
        self.closest_station = 0

class CVRP_V1:
    def __init__(self, args):
        # Read input fields
        self.lines = []
        self.n = 0
        self.m = 0
        self.u = 0
        self.breaks = 0
        self.r = 0.0
        self.speed = 0.0
        self.Tmax = 0.0
        self.Smax = 0.0
        self.st_customer = 0.0
        self.Q = 0.0
        self.nodes = []
        # Calculated/Generated input fields
        self.visited = []                       # a list that indicates with 1 and 0 if a node has been visited
        self.closest_station = []               # a list that stores the closest station for each node
        self.distances = np.zeros((1, 1))       # a matrix that contains the distances between all nodes
        self.times = np.zeros((1, 1))           # a matric that contains the times between all nodes
        self.vehicles = []                      # a list of all vehicles used
        # Output field
        self.routes = []

        # automatically read input
        self.read_input(args)

    def read_input(self, args):

        if len(args) != 2:
            sys.exit("ERROR: Invalid number of arguments")

        try:
            filename = args[1]
            file = open(filename, "r")
            self.lines = file.readlines()
        except:
            sys.exit("ERROR: Could not read file")

    def process_input(self):
        # read n
        tmp = self.lines[0].split(" ")
        self.n = int(tmp[len(tmp) - 1])
        self.lines.remove(self.lines[0])
        # read m
        tmp = self.lines[0].split(" ")
        self.m = int(tmp[len(tmp) - 1])
        self.lines.remove(self.lines[0])
        # read u
        tmp = self.lines[0].split(" ")
        self.u = int(tmp[len(tmp) - 1])
        self.lines.remove(self.lines[0])
        # read breaks
        tmp = self.lines[0].split(" ")
        self.breaks = int(tmp[len(tmp) - 1])
        self.lines.remove(self.lines[0])
        # read r
        tmp = self.lines[0].split(" ")
        self.r = float(tmp[len(tmp) - 1])
        self.lines.remove(self.lines[0])
        # read speed
        tmp = self.lines[0].split(" ")
        self.speed = float(tmp[len(tmp) - 1])
        self.lines.remove(self.lines[0])
        # read Tmax
        tmp = self.lines[0].split(" ")
        self.Tmax = float(tmp[len(tmp) - 1])
        self.lines.remove(self.lines[0])
        # read Smax
        tmp = self.lines[0].split(" ")
        self.Smax = float(tmp[len(tmp) - 1])
        self.lines.remove(self.lines[0])
        # read st_customer
        tmp = self.lines[0].split(" ")
        self.st_customer = float(tmp[len(tmp) - 1])
        self.lines.remove(self.lines[0])
        # read Q
        tmp = self.lines[0].split(" ")
        self.Q = float(tmp[len(tmp) - 1])
        self.lines.remove(self.lines[0])
        # remove unnecessary lines
        self.lines.remove(self.lines[0])
        self.lines.remove(self.lines[0])
        self.lines.remove(self.lines[0])
        # read all nodes and store them in their respective field
        while not self.lines[0] == "\n":
            tmp = self.lines[0].split(" ")
            num = int(tmp[0])
            name = tmp[1]
            x = float(tmp[2])
            y = float(tmp[3])
            type = tmp[4]
            type_s = int(tmp[5])

            self.nodes.append([num, name, x, y, type, type_s])
            self.lines.remove(self.lines[0])
        # create distance and time matrix
        xs = np.zeros(self.n)
        ys = np.zeros(self.n)
        self.distances = np.zeros((self.n, self.n))
        self.times = np.zeros((self.n, self.n))

        for i in range(self.n):
            xs[self.nodes[i][0]] = self.nodes[i][2]
            ys[self.nodes[i][0]] = self.nodes[i][3]

        for i in range(self.n):
            for j in range(i + 1):
                distance = math.sqrt((xs[i] - xs[j]) ** 2) + math.sqrt((ys[i] - ys[j]) ** 2)
                self.distances[i, j] = distance
                self.distances[j, i] = distance
                self.times[i, j] = distance / self.speed
                self.times[j, i] = distance / self.speed

            self.distances[i, i] = sys.maxsize
            self.times[i, i] = sys.maxsize

        # initialise visited and closest station
        self.visited = [0] * self.n
        self.closest_station = [0] * self.n

        # initialize first vehicle
        self.vehicles.append(Vehicle(self.Q, self.Tmax))

        # find closest station for every node

        for node in self.nodes:
            if node[4] == "c":
                for i in range(self.n):
                    if self.nodes[i][4] == "s" and self.distances[node[0], i] < self.distances[node[0], self.closest_station[node[0]]]:
                        self.closest_station[node[0]] = i

    # Checks if a vehicle could safely go to a proposed node and still have enough time and energy to get to the depot
    def can_go(self, vehicle, client):
        # Check if closest charging station would still be in reach
        tmp_energy = vehicle.rem_energy - self.distances[vehicle.current, client] - self.distances[client, self.closest_station[client]]
        if tmp_energy < 0:
            return False
        # Check if base could still be reached with updated energy from client node
        # If yes, check if time would be sufficient
        # If no, check if time would be sufficient taking time to next charging station and charging time into account

    def find_closest_neighbour(self):
        tmp_closest = [-1, 0, sys.maxsize, sys.maxsize]  # [associated vehicle, target, distance, time]

        # Check all neighbours for all vehicles
        for v in self.vehicles:
            # iterate over all nodes (i represents the target node)
            for i in range(self.n):
                # Check if client, unvisited, and if neighbour is closer than previous tmp_closest. If yes, update
                if self.nodes[i][4] == "c" and self.visited[i] == 0 and self.times[v.current, i] < tmp_closest[3]:
                    tmp_closest = [v, i, self.distances[v.current, i], self.times[v.current, i]]

        # Check if corresponding vehicle can go to closest client within its time and battery constrains


    def plan_routes(self):
        pass


obj = CVRP_V1(sys.argv)
obj.process_input()
obj.find_closest_neighbour()