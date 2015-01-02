
/*
Calculate ne number of occurence of each tag from the list of images
Return a list of tag with text and weight
 */
function calculateTagsStats(listImages){
    var tagsObj = {};
    listImages.forEach(function(image){
    	image.tags.forEach(function(tag){
    		if(typeof tagsObj[tag] == 'undefined'){
    			tagsObj[tag] = 1;
    		}
    		else{
    			tagsObj[tag]++;
    		}
    	});
    });
    
    var listTag = Object.keys(tagsObj);
    listTag = listTag.map(function(tag){
    	return {
    			text: tag,
    			weight: tagsObj[tag]
    	}
    });

    return listTag;

}