import math
import os

import matplotlib.pyplot as plt
import pandas as pd


def returnColor(parameter):
    value = int(parameter)
    colors = ['blue', 'yellow', 'red', 'cyan', 'magenta',
              'green', 'black', 'white', 'pink', 'brown']
    return colors[value % len(colors)]


input_path = "/Users/lorenzopratesi/Documents/development/IntelliJ/K-means-hadoop/src/main/java/it/unifi/lorenzopratesi/kmeans/input"
output_path = "/Users/lorenzopratesi/Documents/development/IntelliJ/K-means-hadoop/src/main/java/it/unifi/lorenzopratesi/kmeans/output"

for file in os.listdir(input_path):
    file_content = open(input_path + "/" + file, "r")
    input_df = pd.read_csv(file_content, header=None)
    input_df = input_df.drop(input_df.columns[0], axis=1)

    x = input_df.iloc[:, 0].tolist()
    y = input_df.iloc[:, 1].tolist()

for file in os.listdir(output_path):
    if file == "part-r-00000":
        file_content = open(output_path + "/" + file, "r")
        output_df = pd.read_csv(file_content, header=None, sep=" ")
        output_df.iloc[:, 0] = output_df.iloc[:, 0].str.replace("\t", "").str[1:]
        output_df = output_df.replace(",", ".")

        centroidsX = output_df.iloc[:, 0].tolist()
        centroidsY = output_df.iloc[:, 1].tolist()

cluster = []

def compute_distance(point, center):
    d = .0
    for i in range(len(point)):
        a = float(point[i].replace(",", "."))
        b = float(center[i + 1])
        d += (a - b) * (a - b)
    return math.sqrt(d)


for i, point in input_df.iterrows():
    distances = []
    for j, center in output_df.iterrows():
        distances.append(compute_distance(center, point))

    cluster_idx = distances.index(min(distances))
    cluster.append(cluster_idx)


ax = plt.gca()

ax.scatter(x, y, s=50, c=cluster, alpha=0.5)
plt.show()
