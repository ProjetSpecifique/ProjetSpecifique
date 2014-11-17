var fs = require('graceful-fs');
var request = require('request');
var async = require('async');
var gm = require('gm');


//Configuration
var options = {
	nbImagesLimit: 60000,
	folderName: "images",
	folderNameNotDownloaded: "imagesNotDowloaded"
}



//Connexion to postGre Database
var query = require('pg-query');
query.connectionParameters = 'postgres://utilisateur:utilisateur@localhost:5432/imageSourcesClean';

//Function to mark the image downloaded in DB
function markIsDownloaded(imageId, callback){
	query('UPDATE "Image" SET "isDownloaded" = true	WHERE id = \'' + imageId + '\';', callback);
}

//Function to remove the image if not downloadable
function removeImageIfNotDowloadable(image, callback){
	gm.compare(__dirname+'/imageNotFound.jpg', image.uri, function (err, isEqual, equality){
		if(err) {
			console.log(err);
			callback(false);
		}
		else{
			if(equality <= 0.00001){
				fs.unlinkSync(image.uri); //remove de image
				callback(true);
			}
			else{
				callback(false);
			}
		}
	});
}

//Function to move the image if not downloadable in an other folder
function moveImageIfNotDowloadable(image, callback){
	gm.compare(__dirname+'/imageNotFound.jpg', image.uri, function (err, isEqual, equality){
		if(err) {
			console.log(err);
			callback(false);
		}
		else{
			if(equality <= 0.00001){
				//Create the folder
				if(!fs.existsSync(__dirname + '/' + options.folderNameNotDownloaded)){
					fs.mkdirSync(__dirname + '/' + options.folderNameNotDownloaded);
				}
				fs.renameSync(image.uri, __dirname + '/' + options.folderNameNotDownloaded + '/' + image.name); //move de image
				callback(true);
			}
			else{
				callback(false);
			}
		}
	});
}

//Function to download an image and save it
function downloadAndSaveImage(imageUrl, imageUri, callback){
	request.head(imageUrl, function(err, res, body){
		if(err){
			console.log(err);
			return callback();
		}
		
		request(imageUrl).pipe(fs.createWriteStream(imageUri)).on('close', callback);
	});
}

var nbImages = 0;
var nbImageDownloaded = 0;
var nbImageRemoved = 0;

async.auto({
	listImages: function(next){
		query('SELECT id, "downloadURL" FROM "Image" WHERE NOT "isDownloaded" LIMIT ' + options.nbImagesLimit, function(err, rows){
			if(err) return next(err);
			//console.log(rows);
			nbImages = rows.length;
			next(null, rows);
		});
	},
	createFolder: function(next){
		var uriFolder = __dirname + '/' + options.folderName;
		
		//Create the folder
		if(!fs.existsSync(uriFolder)){
			fs.mkdirSync(uriFolder);
		}
		next(null, uriFolder);
	},
	downloadImages: ['listImages', 'createFolder', function(next, res){
		
		async.each(res.listImages, function(image, nextImage){
			if(!image.downloadURL){
				console.log("Url " + image.downloadURL + " not valid");
				return nextImage();
			}

			//name of the image = id of the image with the extension found in the url
			image.name = image.id + '.' + image.downloadURL.split('.').pop();
			image.uri = res.createFolder + '/' + image.name;

			downloadAndSaveImage(image.downloadURL, image.uri, function(err){
				nbImageDownloaded++;
				//removeImageIfNotDowloadable(image, function(isRemoved){
				moveImageIfNotDowloadable(image, function(isRemoved){
					if(isRemoved) nbImageRemoved++;

					markIsDownloaded(image.id, function(err){
						console.log(nbImageDownloaded + "/" + nbImages + " images downloaded ( " + nbImageRemoved + " removed )");
						nextImage();
					});
					
				});
			});
		}, next);
	}]
}, function(err){
	if(err){
		console.log(err);
	}
	else{
		console.log('Success');
	}
});
