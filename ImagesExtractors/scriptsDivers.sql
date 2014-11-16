/*###################### Download images ######################*/

/*Add a column to know if the image is already downloaded*/
ALTER TABLE "Image"
   ADD COLUMN "isDownloaded" boolean NOT NULL DEFAULT false;

/*Update isDownloaded boolean to set true on the image with id equal something*/
UPDATE "Image"
	SET "isDownloaded" = true
	WHERE id = '1000858464';

/*Update isDownloaded boolean to put false everywhere*/
UPDATE "Image"
	SET "isDownloaded" = false
	WHERE "isDownloaded";

/*Select images id and url to download where not already download*/
SELECT id, "downloadURL"
FROM "Image"
WHERE NOT "isDownloaded"
LIMIT 10;

/*###################### Clean base ######################*/

/*DELETE imageTag associated with image not downloadable (around 312s)*/
DELETE FROM "imageTag"
WHERE imageid IN (SELECT id 
		  FROM "Image"
		  WHERE NOT "canDownloaded" OR "canDownloaded" IS NULL OR "downloadURL" IS NULL);

/*DELETE image not downloadable*/
DELETE FROM "Image"
WHERE NOT "canDownloaded" OR "canDownloaded" IS NULL OR "downloadURL" IS NULL


/*DELETE imageTag associated with tag not getty or not english (too long)
  (Gettytag has always language)*/
DELETE FROM "imageTag"
WHERE tag IN (SELECT "normText"
		FROM "Tag"
		WHERE NOT "gettytag"
		AND( "language" = 'fr' OR "language" = 'de' OR "language" = 'null')
	);

/*DELETE tag not getty or not english*/
DELETE FROM "Tag"
WHERE NOT "gettytag"
AND( "language" = 'fr' OR "language" = 'de' OR "language" = 'null')


/*DELETE image without tag associated*/
DELETE FROM "Image"
WHERE id NOT IN (SELECT DISTINCT imageid
				 FROM "imageTag");