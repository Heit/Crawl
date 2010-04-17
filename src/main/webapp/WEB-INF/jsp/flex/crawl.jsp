<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C/DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>KSU diploma work</title>
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/AC_OETags.js"></script>
    <link href="<%= request.getContextPath() %>/css/main.css" type="text/css" rel="stylesheet"/>
    <meta content="text/html; charset=UTF-8" http-equiv="content-type"/>
</head>
<body>
<div id="container">

    <p>
       <i>Наименование.</i> Инструмент для организации полнотекстового поиска на ресурсах сети Internet.
    </p>

    <p>
        Вашему вниманию представлен прототип встраиваемой полнотекстовой системы поиска, в разделе
        "Параметры индексирования" сначала необходимо указать адрес сетевого ресурса, который должен быть
        проиндексирован
    </p>
    <script language="JavaScript" type="text/javascript">
        <!--
        // -----------------------------------------------------------------------------
        // Globals
        // Major version of Flash required
        var requiredMajorVersion = 9;
        // Minor version of Flash required
        var requiredMinorVersion = 0;
        // Minor version of Flash required
        var requiredRevision = 124;
        // -----------------------------------------------------------------------------
        // -->
    </script>

    <script language="JavaScript" type="text/javascript">
        <!--
        // Version check for the Flash Player that has the ability to start Player Product Install (6.0r65)
        var hasProductInstall = DetectFlashVer(6, 0, 65);

        // Version check based upon the values defined in globals
        var hasRequestedVersion = DetectFlashVer(requiredMajorVersion, requiredMinorVersion, requiredRevision);

        if (hasProductInstall && !hasRequestedVersion) {
            // DO NOT MODIFY THE FOLLOWING FOUR LINES
            // Location visited after installation is complete if installation is required
            var MMPlayerType = (isIE == true) ? "ActiveX" : "PlugIn";
            var MMredirectURL = window.location;
            document.title = document.title.slice(0, 47) + " - Flash Player Installation";
            var MMdoctitle = document.title;

            AC_FL_RunContent(
                    "src", "playerProductInstall",
                    "FlashVars", "MMredirectURL=" + MMredirectURL + '&MMplayerType=' + MMPlayerType + '&MMdoctitle=' + MMdoctitle + "",
                    "width", "800",
                    "height", "600",
                    "align", "middle",
                    "id", "crawl",
                    "quality", "high",
                    "bgcolor", "#ffffff",
                    "name", "crawl",
                    "allowScriptAccess", "sameDomain",
                    "type", "application/x-shockwave-flash",
                    "pluginspage", "http://www.adobe.com/go/getflashplayer"
                    );
        } else if (hasRequestedVersion) {
            // if we've detected an acceptable version
            // embed the Flash Content SWF when all tests are passed
            AC_FL_RunContent(
                    "src", "crawl",
                    "width", "800",
                    "height", "600",
                    "align", "middle",
                    "id", "crawl",
                    "quality", "high",
                    "bgcolor", "#ffffff",
                    "name", "search",
                    "allowScriptAccess", "sameDomain",
                    "type", "application/x-shockwave-flash",
                    "pluginspage", "http://www.adobe.com/go/getflashplayer"
                    );
        } else {  // flash is too old or we can't detect the plugin
            var alternateContent = 'Alternate HTML content should be placed here. '
                    + 'This content requires the Adobe Flash Player. '
                    + '<a href=http://www.adobe.com/go/getflash/>Get Flash</a>';
            document.write(alternateContent);  // insert non-flash content
        }
        // -->
    </script>
    <noscript>
        <object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553560000"
                id="crawl" width="800" height="600"
                codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
            <param name="movie" value="crawl.swf"/>
            <param name="quality" value="high"/>
            <param name="bgcolor" value="#ffffff"/>
            <param name="allowScriptAccess" value="sameDomain"/>
            <embed src="crawl.swf" quality="high" bgcolor="#ffffff"
                   width="800" height="600" name="search" align="middle"
                   play="true"
                   loop="false"
                   quality="high"
                   allowScriptAccess="sameDomain"
                   type="application/x-shockwave-flash"
                   pluginspage="http://www.adobe.com/go/getflashplayer">
            </embed>
        </object>
    </noscript>
</div>
</body>
