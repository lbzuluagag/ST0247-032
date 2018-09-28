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
        self.visited = []
        self.distances = np.zeros((1, 1))
        self.times = np.zeros((1, 1))
        self.vehicles = []
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

        # initialise visited
        self.visited = np.zeros(self.n)

        # create first vehicle
        self.vehicles.append(Vehicle(self.Q, self.Tmax))

    def find_closest_neighbour(self):
        tmp_closest = [-1, 0, sys.maxsize, sys.maxsize]  # [associated vehicle, target, distance, time]


        # Check all neighbours for all vehicles
        for v in self.vehicles:
            # iterate over all nodes
            for i in range(self.n):
                # Check if client, unvisited, and if neighbour is closer than previous tmp_closest. If yes, update
                if self.nodes[i][4] == "c" and self.visited[i] == 0 and self.times[v.current, i] < tmp_closest[3]:
                    print("hey")




    def plan_routes(self):
        pass


obj = CVRP_V1(sys.argv)
obj.process_input()
obj.find_closest_neighbour()