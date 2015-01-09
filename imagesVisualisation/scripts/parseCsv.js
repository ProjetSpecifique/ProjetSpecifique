
/*
	Parse a csv data with separator ';'
	first column: imageId
	second column: isOK (true or false)
	others: tags

	Return an object with 3 list of images, imagesOk, imagesNotOk, imagesUnknown
*/
function parseCsv(data){
	
	if(data == null) return [];
	var listLign = data.split('\n');

	var listImage = {
		imagesOk: [],
		imagesNotOk: [],
		imagesUnknown: []
	};

	for(var i=0; i<listLign.length; i++){
		var listCols = listLign[i].split(';');

		//Remove empty columns
		for(var j=0; j<listCols.length; j++){
			if(listCols[j].trim() == ""){
				listCols.splice(j, 1);
				j--;
			}
		}
		if(listCols[0] && listCols[0] != ""){
			var image = {
				imageId : listCols[0],
				isOk: null
			};

			//Get isOk if it is here and get tags list
			if(listCols[1] && listCols[1] == "1"){
				image.isOk = true;
				image.tags = listCols.splice(2, listCols.length-2);
			}
			else if(listCols[1] && listCols[1] == "0"){
				image.isOk = false;
				image.tags = listCols.splice(2, listCols.length-2);
			}
			else{
				image.tags = listCols.splice(1, listCols.length-1);
			}

			if(image.isOk == null){
				listImage.imagesUnknown.push(image);
			}
			else if(image.isOk){
				listImage.imagesOk.push(image);
			}
			else{
				listImage.imagesNotOk.push(image);	
			}
		}
	}
	return listImage;
}