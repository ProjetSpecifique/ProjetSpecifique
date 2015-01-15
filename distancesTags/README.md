# Project distancesTags

## Description
__Java__ Project to compute distances between tags using __Jenson Shanon Divergence__. This project can generate csv file with the matrix of distance and fill the database distance table.

The computation can be very long. (Can be optimize by using threads and writting just the half matrix because there is symmetry)

## Preconditions to use
* Database with the cooccurrence table computed
* Database with the empty distances table create with:
``` SQL
CREATE TABLE distancestags
(
    tag1 text NOT NULL,
    tag2 text NOT NULL,
    distance numeric
)
```

## Parameters
In the main file __DistancesTags.java__:
* Database connexion information (in the main function lign 38)
``` JAVA
c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/BDImageBig", "postgres", "admin");
```
* Change output of the process by comment some lign in the main function
    * CSV file of the coocurence matrix
    ``` JAVA
        writeHistoCSV(listHisto, listTag, "tagHistograms.csv");
    ```
    * CSV file of the distances matrix
    ``` JAVA
        writeDistanceMatrixCSV(matrixDistance, listTag, "tagDistances.csv");
    ```
    * Database distance table fill
    ``` JAVA
        saveDistanceMatrixInBD(c, listTag, matrixDistance);
    ```

In the file __Distances.java__
* Multiple parameters to compute the distance between 2 tags.