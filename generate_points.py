from random import uniform
import sys


def generate(p, n, r, i):
    string = ""
    lParam = []
    file = open("src/main/java/it/unifi/lorenzopratesi/kmeans/input/data_set_" + str(i) + ".csv", "w")
    for i in range(p):
        lParam.append(0.0)
    for k in range(n):
        string += str(k + 1) + ','
        for e in lParam:
            e = uniform(-r, r)
            string += str(e) + ','
        string = string[:-1] + '\n'
    file.write(string)
    file.close()


if __name__ == "__main__":
    args = sys.argv
    print(args)
    numFiles = int(args[2]) // 10000
    if numFiles == 0:
        numFiles = 1

    for i in range(numFiles):
        generate(int(args[1]), int(args[2]) // numFiles, int(args[3]), i)

    print("\nFile generated in src/main/java/it/unifi/lorenzopratesi/kmeans/input/ \n\n")