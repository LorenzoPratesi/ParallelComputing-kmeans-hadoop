# ParallelComputing-kmeans-hadoop
k-means clustering with Apache Hadoop

The enlarging volumes of information emerging by the progress of technology, makes clustering of very large scale of data a challenging task. This paper presents a parallel k-means clustering algorithms based on Apache Hadoop MapReduce model which is a simple and powerful parallel programming technique. The tests were performed on intel i7 hexa core in a pseudo-distributed mode and then in a fully distributed mode with Amazon EMR. The results show that the proposed algorithm can scale well and efficiently process large datasets on commodity hardware.

## Usage
Generate the points for the data_set with the python script [generate_points.py](https://github.com/user/repo/blob/branch/other_file.md)
(parameters, numPoints, range). 

The example below generates 5000 points where each point have 7 dimensions in range (-50, 50).
```
python generate_points.py 7 5000 50  
```

### Input dataset file
```
1,-47.10587172571255,14.943954356586303,-46.388420932704435,-44.85898057511504,14.692770405698909,8.554290548966762,-26.98274778248029
2,-32.14751308151169,-0.8911176341646865,-2.0721342259821895,24.350010927609574,-2.3325950902766976,45.28918282251456,-49.224569992743284
3,-47.591941681452674,5.468765865544512,5.8610799287109,-17.933096087609044,-30.09825602820945,35.48023571874454,9.63601787232966
4,48.44711307991986,-46.263696455513006,35.22224682081695,17.704512714415443,21.400883598731284,-17.60257644610158,-7.2307839109048615
5,-34.95815828915639,-1.7145665578178964,-44.719500095063545,35.051300048996254,-0.045665580754914004,13.08567798073446,-38.20550263636838
6,-46.186264882326725,12.786944713661526,-1.1674725772879526,-31.604073729884497,-42.84667412761787,1.0108232599687383,29.826072036677147
...
...
```


Start kmeans clustering with hadoop by typing this command:

```
hadoop jar KMeansHadoop.jar PointsKMeans -i <IN> -o <OUT> -d <DIM> -k <CLUSTERS> [-e <EPS>]

Options description: 
	[-i] stands for the input folder.
	[-o] stands for the output folder.
	[-d] stands for the number of dimensions of each data point 
	[-k] stands for the number of clusters
        [-e] stands for the tollerance of significant decimals to consider when comparing floats
```  

Running the program will create in the output folder a file containing the coordinated of each centroid
### Output result file
```
0	-25.474,17.934,-9.333,-7.991,-1.379,21.269,-4.432
1	-17.7,-24.262,-12.361,1.163,5.73,-22.346,1.535
2	12.235,-22.147,19.466,3.453,-5.037,22.458,-2.773
3	3.215,18.707,23.649,8.259,-1.9,-21.397,-1.662
4	26.664,9.637,-25.983,-5.845,-0.004,3.174,8.597
```

