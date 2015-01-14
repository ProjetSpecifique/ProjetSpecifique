
/*
Parameters
*/
var nbImagePerlign = 10;
var imagesPath = "images/";


/*
	Function call when the input file change to load the CSV file
*/
function loadCsv(){
	loadFile('fileInputCsv', 'csv', function(data){
		
		//Load images from csv
		var images = parseCsv(data);

		//Display table of images
		displayTableImage(images.imagesOk, 'tableImageOk', 'imageOk');
		displayTableImage(images.imagesNotOk, 'tableImageNotOk', 'imageNotOk');

		//Display cloud of tags
		displayTagCloud(images.imagesOk, "tagCloudOk");
		displayTagCloud(images.imagesNotOk, "tagCloudNotOk");
	});
}

function displayTableImage(listImage, idTable, classImage){
	//Display table of images
	var table = $('#' + idTable);
	var tableLign = null;

	table.empty();
	var lignId = 0;
	for(var i=0; i<listImage.length; i++){
		if(i%nbImagePerlign == 0){
			lignId++;
			table.append("<tr id='lign_" + idTable + "_" + lignId + "' class='tableLign'></tr>");
			tableLign = $('#lign_' + idTable + "_" + lignId);
		}
		tableLign.append("<td class='" + classImage + "'>" +
							"<span title='" + listImage[i].tags + "'><img id='"+listImage[i].imageId+"' class='image' src='" + imagesPath + listImage[i].imageId + ".jpg' "+
							"onmouseover=document.getElementById('preview').src='"+imagesPath + listImage[i].imageId + ".jpg'; " +
							/*"onmouseout=document.getElementById('preview').src='Preview.png';>" +*/
							/*"<p class='idImage'>" + listImage[i].imageId + "</p>*/"</span>" + 
						 "</td>");
	}
}

function displayTagCloud(listImage, idTagCloud){
	var tagCloud = $('#' + idTagCloud);
	tagCloud.empty();
	tagCloud.jQCloud(calculateTagsStats(listImage));
}
/*
	Load a file from the input element with id 'fileInputId'
	If ext not null, check if the extension is 'ext'
	When the file is loaded, cb is call with text data from the file
	exemple:
	loadFile('fileInputCsv', 'csv', function(data){
		console.log(data);
	});
*/
function loadFile(fileInputId, ext, cb){
	if (!window.File || !window.FileReader || !window.FileList || !window.Blob) {
		alert('The File APIs are not fully supported in this browser.');
		return;
	}	
	input = document.getElementById(fileInputId);
	if (!input) {
		alert("Um, couldn't find the fileinput element.");
	}
	else if (!input.files) {
		alert("This browser doesn't seem to support the `files` property of file inputs.");
	}
	else {
		file = input.files[0];
		if(ext && !file.name.lastIndexOf(ext)===file.name.length-3){
			alert("Not " + ext);
			return;
		}
		fr = new FileReader();
		fr.onload = function(){
			cb(fr.result);
		}
		fr.readAsText(file); 
	}
}