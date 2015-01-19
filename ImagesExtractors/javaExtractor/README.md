## Java Extractor

This project made in __JAVA__ helps the user to download all images from a __Postgres database__ using url from Flicker's images.

In order to use properly this program, you need to configurate some parameters as fellow :

* Make sure that the table __Image__ has a column named __isDownloaded__. Otherwise the program will fail. 

* Specify the follow where imlages are going to be saved
``` JAVA
Cprivate static String folder = "getty_images/";
```
* Specify the limit of images that are going to be downloaded by the program 
``` JAVA
private static int number = 100;
```
* Change the follow parameters by your's postgres parameters
** Postgres server and database informations
``` JAVA
String url = "jdbc:postgresql://localhost:5432/imageSources";
```
** Postgres username
``` JAVA
String user = "utilisateur";
```
** Postgres password
``` JAVA
String password = "utilisateur";
```

Sometimes the dowloaded image is broken because the image itself is not anymore available in __Filcker__.