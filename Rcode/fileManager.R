
# Function for writing the initial content for the LaTex and HTML files
initFiles <- function (latexFile, htmlFile) {
    head <- c("<!DOCTYPE html>",
                "<html>",
                "<head>",
                "<meta charset=\"utf-8\" />",
                "<title>Histograms</title>",
                "<link href=\"css/bootstrap.css\" rel=\"stylesheet\" type=\"text/css\"/>",
                "<link href=\"css/style.css\" rel=\"stylesheet\" type=\"text/css\"/>",
                "</head>"
        )

    body <- c("<body>",
                "<nav class=\"navbar navbar-fixed-top\" role=\"navigation\" style=\"background: cadetblue;\">",
                "    <div class=\"container\">",
                "        <div class=\"navbar-collapse collapse\">",
                "            <ul id=\"navbar\" class=\"nav navbar-nav\">",
                "                <li class=\"col-lg-6\"><h2>Training data</h2></li>",
                "                <li class=\"col-lg-6\"><h2>Test data</h2></li>",
                "            </ul>",
                "        </div>",
                "    </div>",
                "</nav>",
                "<nav class=\"navbar navbar-inverse navbar-fixed-top\" role=\"navigation\" style=\"margin-top: 60px; height: 40px\">",
                "    <div class=\"container\">",
                "        <div class=\"navbar-collapse collapse\">",
                "            <ul id=\"subnavbar\" class=\"nav navbar-nav\">",
                "                <li class=\"col-lg-2\"><h3><span class=\"glyphicon glyphicon-asterisk\"></span> All</h3></li>",
                "                <li class=\"col-lg-2\"><h3><span class=\"glyphicon glyphicon-plus\"></span> Positives</h3></li>",
                "                <li class=\"col-lg-2\"><h3><span class=\"glyphicon glyphicon-minus\"></span> Negatives</h3></li>",
                "                <li class=\"col-lg-2\"><h3><span class=\"glyphicon glyphicon-asterisk\"></span> All</h3></li>",
                "                <li class=\"col-lg-2\"><h3><span class=\"glyphicon glyphicon-plus\"></span> Positives</h3></li>",
                "                <li class=\"col-lg-2\"><h3><span class=\"glyphicon glyphicon-minus\"></span> Negatives</h3></li>",
                "            </ul>",
                "        </div>",
                "    </div>",
                "</nav>",
                "<div class=\"container\">"
        )
    writeLines(head, htmlFile)
    writeLines(body, htmlFile)

}
 
writeToFiles <- function (latexFile, htmlFile, descriptorName, histogramName) {
    imagesRow <- c("<div class=\"row\">",
            paste("    <h4>", descriptorName, "</h4>", sep=""),
            "</div>",
            "",
            "<div class=\"row\">",
            "    <div class=\"col-lg-2\">",
            paste("        <a href=\"../train/all/", histogramName, "\">", sep=""),
            paste("        <img class=\"img-thumbnail\" src=\"../train/all/", histogramName, "\">", sep=""),
            "        </a>",
            "    </div>",
            "    <div class=\"col-lg-2\">",
            paste("        <a href=\"../train/positives/", histogramName, "\">", sep=""),
            paste("        <img class=\"img-thumbnail\" src=\"../train/positives/", histogramName, "\">", sep=""),
            "        </a>",
            "    </div>",
            "    <div class=\"col-lg-2\">",
            paste("        <a href=\"../train/negatives/", histogramName, "\">", sep=""),
            paste("        <img class=\"img-thumbnail\" src=\"../train/negatives/", histogramName, "\">", sep=""),
            "        </a>",
            "    </div>",
            "",
            "    <div class=\"col-lg-2\">",
            paste("        <a href=\"../test/all/", histogramName, "\">", sep=""),
            paste("        <img class=\"img-thumbnail\" src=\"../test/all/", histogramName, "\">", sep=""),
            "        </a>",
            "    </div>",
            "    <div class=\"col-lg-2\">",
            paste("        <a href=\"../test/positives/", histogramName, "\">", sep=""),
            paste("        <img class=\"img-thumbnail\" src=\"../test/positives/", histogramName, "\">", sep=""),
            "        </a>",
            "    </div>",
            "    <div class=\"col-lg-2\">",
            paste("        <a href=\"../test/negatives/", histogramName, "\">", sep=""),
            paste("        <img class=\"img-thumbnail\" src=\"../test/negatives/", histogramName, "\">", sep=""),
            "        </a>",
            "    </div>",
            "</div>"
        )
    writeLines(imagesRow, htmlFile)
}


finFiles <- function (latexFile, htmlFile) {
    end <- c("</div>",
        "</body>",
        "</html>"
        )
    writeLines(end, htmlFile)
}