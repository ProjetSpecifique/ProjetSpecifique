var fs = require('graceful-fs');
var request = require('request');
var async = require('async');
var gm = require('gm');

var cluster = require('cluster');
var numCPUs = require('os').cpus().length; 

//Configuration
var options = {
	nbImagesLimit: 60000,
	folderName: "images",
	folderNameNotDownloaded: "imagesNotDowloaded"
}

var uriFolder = __dirname + '/' + options.folderName;

//Connexion to postGre Database
var query = require('pg-query');
query.connectionParameters = 'postgres://utilisateur:utilisateur@localhost:5432/BDClean';

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
			return callback(err);
		}
		
		var fileStream = fs.createWriteStream(imageUri);
		fileStream.on('close', callback);
		request(imageUrl).pipe(fileStream);
	});
}


if (cluster.isMaster) {

	var nbImages = 0;
	var nbImageDownloaded = 0;
	var nbImageRemoved = 0;

	function createWorker(listImages, cbEnd){
		var worker = cluster.fork();
		//Message of a child to indicate an image download
		worker.on('message', function(msg) {
		    nbImageDownloaded++;
		    if(msg.isRemoved) nbImageRemoved++;
		    console.log(nbImageDownloaded + "/" + nbImages + " images downloaded ( " + nbImageRemoved + " removed )");
		    if(nbImageDownloaded == nbImages) cbEnd();
		});
		worker.send(listImages);
	}

	async.auto({
		listImages: function(next){
			console.log("Waiting list from DB...");
			query('SELECT id, "downloadURL" FROM "Image" WHERE NOT "isDownloaded" LIMIT ' + options.nbImagesLimit, function(err, rows){
				if(err) return next(err);
				//console.log(rows);
				nbImages = rows.length;
				next(null, rows);
			});
		},
		createFolder: function(next){
			
			//Create the folder
			if(!fs.existsSync(uriFolder)){
				fs.mkdirSync(uriFolder);
			}
			//Create the folder not Downloaded
			if(!fs.existsSync(__dirname + '/' + options.folderNameNotDownloaded)){
				fs.mkdirSync(__dirname + '/' + options.folderNameNotDownloaded);
			}
			next(null, uriFolder);
		},
		downloadImages: ['listImages', 'createFolder', function(next, res){

			console.log(nbImages + " images dispatch on " + numCPUs + " Cores");
			var nbElemPerWorker = Math.ceil(nbImages/numCPUs);
			for(var i=0; i<numCPUs-1; i++){
				createWorker(res.listImages.splice(0,nbElemPerWorker), next);
			}
			createWorker(res.listImages, next);
		}]
	}, function(err){
		if(err){
			console.log(err);
		}
		else{
			console.log('Success');
		}
	});
}
else{ //Worker

	//Message of the master with the list of images to download
	process.on('message', function(listImages) {
	    async.each(listImages, function(image, nextImage){
			if(!image.downloadURL){
				console.log("Url " + image.downloadURL + " not valid");
				return nextImage();
			}

			//name of the image = id of the image with the extension found in the url
			image.name = image.id + '.' + image.downloadURL.split('.').pop();
			image.uri = uriFolder + '/' + image.name;

			downloadAndSaveImage(image.downloadURL, image.uri, function(err){
				if(err){
					console.log("Error on image " + image.id);
					console.log(err);
					return nextImage();
				}
				//removeImageIfNotDowloadable(image, function(isRemoved){
				moveImageIfNotDowloadable(image, function(isRemoved){

					markIsDownloaded(image.id, function(err){
						cluster.worker.send({isRemoved: isRemoved});
						nextImage();
					});
					
				});
			});
		}, function(err){
			if(err) console.log(err);
			cluster.worker.disconnect();
		});
	});
}
