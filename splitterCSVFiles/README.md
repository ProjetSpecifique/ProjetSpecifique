# Project splitterCSVFiles

## Description
__Java__ project to split csv files in many others smaller but it can be useful to separate a trainning set and a set to test.


## Preconditions to use
* An input folder with all csv to split must be accessible.
* An output folder must be create.

## Parameters

In the main file __SplitterCSVFiles.java__(lign 25 to 30):
* To specify a relative input folder with all csv files to split:
``` JAVA
private static String sourceFolderPath = "input";
```
* To specify a relative output folder with all csv files splitted:
``` JAVA
    private static String outputPath = "output/";
```
* To select number of output splitted files with a name, number of imageOK and number of imageNotOK
``` JAVA
    private static OutputCSVFile[] filesResultsPart = new OutputCSVFile[] {
        new OutputCSVFile("ToTrain", 100, 100),
        new OutputCSVFile("ToTest", 80, 120)
    };
 ```
