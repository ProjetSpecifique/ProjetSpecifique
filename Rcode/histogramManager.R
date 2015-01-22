source("fileManager.R")

mf <-function(x, y) merge(x, y[grepl(x$photoID[1],y$photoName),])

substrLeft <- function(x, n){ substr(x, 0, nchar(x)-n) }

generateHistograms <- function (descriptorsFolder, termsFolder) {
    # generateHistograms("../CSVFiles/", "../CSVTerms/")
    writeLines("Getting started...")

    # Open the folders and read all files
    descriptors = list.files(path=descriptorsFolder, pattern="*.csv", full.names=T, recursive=F)
    terms = list.files(path=termsFolder, pattern="*.csv", full.names=T, recursive=F)
    # Create an imbricate apply to get all possible CSV combinations between terms and descriptors
    lapply(terms, function(termCSV) {
        # Read data from term CSV
        writeLines(paste("    ", basename(termCSV)))
        termData <- read.csv(termCSV, stringsAsFactors=F, head=F, sep=";")
        # Keep only first two columns : the photo ID and its class
        colnames(termData) <- c("photoID", "class")
        termData <- termData[, c("photoID", "class")]

        # Open LaTex and HTML files for writing
        termName <- strsplit(basename(termCSV), ".", fixed=T)[[1]]
        toTest <- any(grep("*ToTest*", termName))
        folderPrefix <- "None"

        # See if it is a train or test csv
        if (toTest) {
            termName <- substrLeft(termName[1], nchar("ToTest"))   
            folderPrefix <- "./visualisation/test"
            latexFile <- file(description=paste("./visualisation/latex/", termName, ".tex", sep=""), open="wt")
            htmlFile <- file(description=paste("./visualisation/html/", termName, ".html", sep=""), open="wt")
            initFiles(latexFile, htmlFile)
        } else {
            termName <- substrLeft(termName[1], nchar("ToTrain")) 
            folderPrefix <- "./visualisation/train"
        }

        # Second level of imbrication
        lapply(descriptors, function(descriptorCSV) {
            # Read data from the descriptor CSV
            writeLines(basename(descriptorCSV))
            descriptorData <- read.csv(descriptorCSV, stringsAsFactors=F, head=F, sep=",")
            # Rename first column as photoName
            colnames(descriptorData) <- c("photoName")

            # Compute the resulting data frame
            joinedFrame <- do.call(rbind, by(termData, termData$photoID, mf, descriptorData))
            # Compute histogram limits
            ylim <- c(0.0, max(joinedFrame[,4:ncol(joinedFrame)]))
            xlim <- c(1.0, (ncol(joinedFrame)-3))
            # Shortcuts
            xvals <- 1:(ncol(joinedFrame)-3) 
            cols <- 4:ncol(joinedFrame)

            descriptorName <- strsplit(basename(descriptorCSV), ".", fixed=T)[[1]][1]
            histogramName <- paste(descriptorName, "_", termName, ".png", sep="")
            
            # Histogram for all images

            png(paste(folderPrefix, "/all/", histogramName, sep=""))
            pic1 <- plot(1, 1, ylim=ylim, xlim=xlim, type="n", ylab="Descripteur", xlab="X")
            apply(joinedFrame[, cols], 1, function(x) lines(xvals, x))
            lines(xvals, colMeans(joinedFrame[, cols]), col="#FFFF00", type="o")
            print(pic1)
            dev.off()

            # Histogram for positives
            png(paste(folderPrefix, "/positives/", histogramName, sep=""))
            pic2 <- plot(1, 1, ylim=ylim, xlim=xlim, type="n", ylab="Descripteur", xlab="X")
            apply(joinedFrame[joinedFrame[, "class"] == 1, cols], 1, function(x) lines(xvals, x))
            lines(xvals, colMeans(joinedFrame[joinedFrame[, "class"] == 1, cols]), col="#00FFFF", type="o")
            print(pic2)
            dev.off()

            # Histogram for negatives
            png(paste(folderPrefix, "/negatives/", histogramName, sep=""))
            pic3 <- plot(1, 1, ylim=ylim, xlim=xlim, type="n", ylab="Descripteur", xlab="X")
            apply(joinedFrame[joinedFrame[, "class"] == 0, cols], 1, function(x) lines(xvals, x))
            lines(xvals, colMeans(joinedFrame[joinedFrame[, "class"] == 0, cols]), col="#FF00FF", type="o")
            print(pic3)
            dev.off()

            # Write to visualisation files
            if (toTest) {
                writeToFiles(latexFile, htmlFile, descriptorName, histogramName)                
            }

        })
        
        if (toTest) {
            finFiles(latexFile, htmlFile)
            close(latexFile)
            close(htmlFile)
        }

    })
    writeLines("Done !")
}