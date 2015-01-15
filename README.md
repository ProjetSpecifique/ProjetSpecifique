# ProjetSpecifique

## Subject
The goal of the project is the research of solutions to recognize landscapes.
The main solution we propose is to train a learner with sets of images preselected in some categories.
Images come from [Flicker](www.flickr.com) with a database of tags describing each image.

This repository regroups projects to realize the complete workflow.



## Workflows

### Download images
* Import the clean DataBase in PostGreSQL.
* Download images from flickers by using a project from the folder __ImagesExtractors__.


### Tags sets generation
* Import the clean DataBase in PostGreSQL.
* Create the coocurrence table
``` SQL
CREATE TABLE cooccurrence AS 
SELECT im1.tag tag1, im2.tag tag2, count(im1.imageid) cooccurrence 
FROM imagetagfiltred im1, imagetagfiltred im2
WHERE im1.imageid = im2.imageid
GROUP BY im1.tag, im2.tag;
```
* Create the empty distance table
``` SQL
CREATE TABLE distancestags
(
    tag1 text NOT NULL,
    tag2 text NOT NULL,
    distance numeric
)
```
* Execute the project __DistanceTags__ to fill this table with  distance between each tags using __Jenson Shanon Divergence__
* Create a second distance table lighter (eliminate the symmetry in the distance tags matrix)
``` SQL
CREATE TABLE distancestags2 AS
        SELECT * FROM distancestags WHERE tag1 < tag2;
```
* Execute the project __generateCategoryImagesSet__ to generate a csv file for each term wanted with images ok and not.
* Separate each csv in 2 (one to train, one to test) by using project __splitterCSVFiles__.
* Finally, you can use project __imagesVisualisation__ to visualize images of a csv file in a html page.



### Generate learner models
* Compute descriptors for all images with the project __LireDescriptorsComputation__.
* TODO???


## Projects description
Projects can have it's own readme to describe how use it.
### ImageClassification
TODO???

### ImageDistance
TODO???

### ImageLearnerKNIME
TODO???

### ImagesExtractors
Folder with projects to extract and download images from __Flicker__. Each project have advantages and disadvantages (speed, language, ...)

### LearnImageTags
TODO???

### LireDescriptorsComputation
TODO???

### ProtoApplyModel
TODO???

### Rcode
TODO???

### distancesTags
__Java__ project to compute distances between tags using __Jenson Shanon Divergence__. This project can generate csv file with the matrix of distance and fill the database distance table.

### generateCategoryImagesSet
__Java__ project to generate sets of images ok and not ok for each terme you want. Sets are save in csv files.

### imagesVisualisation
__HTML, CSS, Javascript__ project to visualize images of a csv file with tag cloud. The visualization with a simple html page.

### splitterCSVFiles
__Java__ project to split csv files in many others smaller but it can be useful to separate a trainning set and a set to test. 

### wordnetSynonymeFounder
Small __Java__ project to get synonyms of a word using [Wordnet](http://wordnet.princeton.edu/) dictionnary.