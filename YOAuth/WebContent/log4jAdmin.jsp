<%@ page language="java" contentType="text/html;charset=UTF-8" %>

<%@ page import="org.apache.log4j.Level" %>
<%@ page import="org.apache.log4j.LogManager" %>

<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="java.util.HashMap" %>

<%@ page import="java.util.Enumeration" %>
<%@ page import="java.util.Set" %>

<%@ page import="java.util.Arrays" %>
<% long beginPageLoadTime = System.currentTimeMillis();%>

<html>
<head>
    <title>Log4J Administration</title>
    <link rel="stylesheet" type="text/css" href="light-blue.css" /> 
</head>
<body onLoad="javascript:document.logFilterForm.logNameFilter.focus();">

<%
    String containsFilter = "Contains";
    String beginsWithFilter = "Begins With";

    String[] logLevels = {"debug", "info", "warn", "error", "fatal", "off"};

    String targetOperation = (String) request.getParameter("operation");

    String targetLogger = (String) request.getParameter("logger");

    String targetLogLevel = (String) request.getParameter("newLogLevel");

    String logNameFilter = (String) request.getParameter("logNameFilter");

    String logNameFilterType = (String) request.getParameter("logNameFilterType");

%>
<h1>WSX - Log4J Administration</h1>
<div id="content">
<div class="filterForm">

    <form action="log4jAdmin.jsp" name="logFilterForm">Filter Loggers:

        <input name="logNameFilter" type="text" size="50" value="<%=(logNameFilter == null ? "":logNameFilter)%>"  class="filterText"/>
        
        <input name="logNameFilterType" type="submit" value="<%=beginsWithFilter%>" class="filterButton"/>

        <input name="logNameFilterType" type="submit" value="<%=containsFilter%>" class="filterButton"/>

        <input name="logNameClear" type="button" value="Clear" class="filterButton"    onmousedown='javascript:document.logFilterForm.logNameFilter.value="";'/>
        
        <input name="logNameReset" type="reset" value="Reset" class="filterButton"/>

		<input type="hidden" name="operation" value="changeLogLevel"/>

    </form>
</div>

<table><thead>
    <tr>
        <th>Logger</th>
        <th>Parent Logger</th>
        <th>Effective Level</th>
        <th>Change Log Level To</th>
    </tr></thead>

    <%
        Enumeration loggers = LogManager.getCurrentLoggers();

        HashMap loggersMap = new HashMap(128);
        Logger rootLogger = LogManager.getRootLogger();

        if (!loggersMap.containsKey(rootLogger.getName())) {

            loggersMap.put(rootLogger.getName(), rootLogger);
        }

        while (loggers.hasMoreElements()) {
            Logger logger = (Logger) loggers.nextElement();

            if (logNameFilter == null || logNameFilter.trim().length() == 0) {

                loggersMap.put(logger.getName(), logger);
            } else if (containsFilter.equals(logNameFilterType)) {

                if (logger.getName().toUpperCase().indexOf(logNameFilter.toUpperCase()) >= 0) {

                    loggersMap.put(logger.getName(), logger);
                }

            } else {
// Either was no filter in IF, contains filter in ELSE IF, or begins with in ELSE
                if (logger.getName().startsWith(logNameFilter)) {

                    loggersMap.put(logger.getName(), logger);
                }

            }
        }
        Set loggerKeys = loggersMap.keySet();

        String[] keys = new String[loggerKeys.size()];

        keys = (String[]) loggerKeys.toArray(keys);

        Arrays.sort(keys, String.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < keys.length; i++) {

            Logger logger = (Logger) loggersMap.get(keys[i]);

// MUST CHANGE THE LOG LEVEL ON LOGGER BEFORE GENERATING THE LINKS AND THE
// CURRENT LOG LEVEL OR DISABLED LINK WON'T MATCH THE NEWLY CHANGED VALUES
            if ("changeLogLevel".equals(targetOperation) && targetLogger.equals(logger.getName())) {

                Logger selectedLogger = (Logger) loggersMap.get(targetLogger);

                selectedLogger.setLevel(Level.toLevel(targetLogLevel));
            }

            String loggerName = null;
            String loggerEffectiveLevel = null;

            String loggerParent = null;
            if (logger != null) {

                loggerName = logger.getName();
                loggerEffectiveLevel = String.valueOf(logger.getEffectiveLevel());

                loggerParent = (logger.getParent() == null ? null : logger.getParent().getName());

            }
    %>
    <tr>
        <td><%=loggerName%>
        </td>

        <td><%=loggerParent%>
        </td>
        <td><%=loggerEffectiveLevel%>

        </td>
        <td>
            <%
                for (int cnt = 0; cnt < logLevels.length; cnt++) {

                    String url = "log4jAdmin.jsp?operation=changeLogLevel&logger=" + loggerName + "&newLogLevel=" + logLevels[cnt] + "&logNameFilter=" + (logNameFilter != null ? logNameFilter : "") + "&logNameFilterType=" + (logNameFilterType != null ? logNameFilterType : "");

                    if (logger.getLevel() == Level.toLevel(logLevels[cnt]) || logger.getEffectiveLevel() == Level.toLevel(logLevels[cnt])) {

            %>
            [<%=logLevels[cnt].toUpperCase()%>]

            <%
            } else {
            %>
            <a href='<%=url%>'>[<%=logLevels[cnt]%>]</a>

            <%
                    }
                }
            %>
        </td>
    </tr>

    <%
        }
    %>
</table>
<p>
    Revision: 1.0<br/>

    Page Load Time (Millis): <%=(System.currentTimeMillis() - beginPageLoadTime)%>

</div>
</body>
</html>
