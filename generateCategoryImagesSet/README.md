# Project generateCategoryImagesSet

## Description
__Java__ project to generate sets of images ok and not ok for each terme you want. Sets are saved in csv files. Outputs CSV have the id of the image in the first column, 1 or 0 in the second column to indicate if image is OK or not OK, and all tags describing the image following in others column.

Example output csv file (the title of column will not appear in csv):

| Id of image | image is OK(1) or not OK(0) | tag1 | tag2 | tag3 | tag4 | tag5 |
|:-----------:|:---------------------------:|:----------------:|:---------------:|:-------------:|:-------------------:|:-------------------:|
| 3064673022 | 1 | a coruna | cloud | coastline | colour image |  |
| 4879541680 | 1 | beach | built structure | cloud | coastline | colour image |
| 1067386799 | 0 | animal body part | animal head | animal themes | animals in the wild | chiang mai province |
| 1068925369 | 0 | day | elevated view | no people |  |  |

Two methods to create sets are possible: __SQLRequest__ or __DistancesTags__.

### Method SQLRequest
The method __SQLRequest__ will create sets of images by using a simple SQL request with some tags specified as input.
Tags describing a term are use to select images. For instance, the term 'coastline' and tags describing: 'port' and '	boat' will use the following request to select 50 images OK:
``` SQL
SELECT imageid FROM "imagetagfiltred" WHERE tag ~ '(^|^.* )coastline($| .*$)'
UNION SELECT imageid FROM "imagetagfiltred" WHERE tag ~ '(^|^.* )port($| .*$)'
UNION SELECT imageid FROM "imagetagfiltred" WHERE tag ~ '(^|^.* )boat($| .*$)'
LIMIT 50;
```

To select image NOT OK, this method will use an other SQL request to have image having no tags in common with image OK (ignoring currents tags). For the same example, the following request will be use:
``` SQL
SELECT i.id FROM "imagefiltred" i 
WHERE NOT EXISTS (                                  --Selection images haven't same tags images OK
    SELECT NULL FROM "imagetagfiltred" 
    WHERE i.id = imageId 
    AND tag IN (
        SELECT DISTINCT tag FROM "imagetagfiltred"  --Selection tags of images OK
        WHERE imageid IN (
            SELECT imageid FROM "imagetagfiltred" WHERE tag ~ '(^|^.* )coastline($| .*$)'
            UNION SELECT imageid FROM "imagetagfiltred" WHERE tag ~ '(^|^.* )port($| .*$)'
            UNION SELECT imageid FROM "imagetagfiltred" WHERE tag ~ '(^|^.* )boat($| .*$)'
        ) EXCEPT(                                   --Except currents tags
            SELECT tag FROM "imagetagfiltred"
            GROUP BY tag
            HAVING count(imageid) > 10000
        )
    )
) LIMIT 50;
```

### Method DistancesTags
The method __DistancesTags__ will create sets of images by using a distances between tags to select similar tags and opposite tags. Images OK are found by using the same request as in method __SQLRequest__ to select image OK but using similar tags. For images NOT OK, it's the same request but with opposites tags.

### Other Method
To create new methods to select images, you can implement the interface __GeneratorImageSet__ and add the method in the factory __GeneratorImageSetFactory__.

## Preconditions to use
* An input csv file with terms to make sets of images (one per lign)
* An output folder is accessible
* With the method __SQLRequest__, you can add others tags on the lign to describe termes in the input CSV file
* With the method __DistancesTags__, be sure that the table 'distancestags2' is computed. This table contains half of the matrix of distances between tags. (see global workflow)

## Parameters

In the main file __GenerateCategoryImagesSet.java__ (lign 25 to 33):
* To specify the database connection:
``` JAVA
private static String url = "jdbc:postgresql://localhost:5432/BDImageBig";
private static String user = "utilisateur";
private static String password = "utilisateur";
```
* To specify the number of image Ok and not OK per output CSV:
``` JAVA
private static int nbImageOK = 300;
private static int nbImageNotOK = 300;
```
* To specify the name of the input CSV file with terms (and maybe tags describing if need):
``` JAVA
private static String categoriesFilePath = "categories.csv";
 ```
* To specify the output folder:
``` JAVA
private static String outputPath = "output/";
 ```
* To select the method used to create sets of images:
``` JAVA
private static GeneratorImageSetFactory.GeneratorType generatorType = GeneratorImageSetFactory.GeneratorType.DistancesTags;
 ```
 
For the method __SQLRequest__ in the file __GeneratorSQLRequest.java__ (lign 23):
* To specify the number of images with a tag to considerate this tag as current in the request to find images NOT OK:
``` JAVA
private static int minCountCurrentTags = 10000;
 ```
 
For the method __DistancesTags__ in the file __GeneratorDistancesTags.java__ (lign 24 to 27):
* To specify the number of similar and opposite tags selected for each term:
``` JAVA
private static int nbTagsSimilar = 40;
private static int nbTagsOpposite = 20;
```
* To specify the maximum distance between 2 tags to considerate them as similar:
``` JAVA
private static int distanceMaxSimilar = 1000;
```
* To specify the minimum distance between 2 tags to considerate them as opposite:
``` JAVA
private static int distanceMinOpposite = 10000;
 ```
 
