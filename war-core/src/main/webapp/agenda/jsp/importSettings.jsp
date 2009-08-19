<%
response.setHeader("Cache-Control","no-store"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires",-1); //prevents caching at the proxy server
%>

<%@ page import="java.io.PrintWriter"%>
<%@ page import="java.io.IOException"%>
<%@ page import="java.io.FileInputStream"%>
<%@ page import="java.io.ObjectInputStream"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.beans.*"%>
<%@ page import="java.util.*"%>
<%@ page import="javax.ejb.*,java.sql.SQLException,javax.naming.*,javax.rmi.PortableRemoteObject"%>

<%@ page import="com.stratelia.webactiv.beans.admin.*"%>
<%@ page import="com.stratelia.webactiv.agenda.view.*"%>
<%@ page import="com.stratelia.webactiv.calendar.model.*"%>
<%@ page import="com.stratelia.webactiv.util.*"%>
<%@ page import="com.stratelia.webactiv.util.viewGenerator.html.window.*"%>
<%@ page import="com.stratelia.webactiv.util.viewGenerator.html.frame.Frame"%>
<%@ page import="com.stratelia.webactiv.util.viewGenerator.html.*"%>
<%@ page import="com.stratelia.webactiv.util.viewGenerator.html.buttons.*"%>
<%@ page import="com.stratelia.webactiv.util.viewGenerator.html.buttonPanes.*"%>
<%@ page import="com.stratelia.webactiv.util.viewGenerator.html.browseBars.*"%>
<%@ page import="com.stratelia.silverpeas.peasCore.URLManager"%>
<%@ page import="com.stratelia.webactiv.agenda.model.*"%>

<%@ include file="checkAgenda.jsp.inc" %>

<%
	CalendarImportSettings importSettings = (CalendarImportSettings) request.getAttribute("ImportSettings");
	String action = "updateImportSettings";
	if (importSettings == null) {
		action = "saveImportSettings";
		importSettings = new CalendarImportSettings();
		importSettings.setHostName( request.getRemoteHost() );
	}

  	ResourceLocator settings = agenda.getSettings();
	Window window = graphicFactory.getWindow();
	BrowseBar browseBar = window.getBrowseBar();
	browseBar.setComponentName(agenda.getString("agenda"));
	browseBar.setPath(agenda.getString("agenda.outlookImportOptions"));
	Frame frame = graphicFactory.getFrame();
	
    ButtonPane buttonPane = graphicFactory.getButtonPane();
    buttonPane.addButton((Button) graphicFactory.getFormButton(agenda.getString("valider"), "javascript:onClick=document.importSettingsForm.submit();", false));
    buttonPane.addButton((Button) graphicFactory.getFormButton(agenda.getString("annuler"), "Main", false));

%>

<HTML>
<HEAD>
<%= graphicFactory.getLookStyleSheet() %>
<TITLE></TITLE>

<SCRIPT language="Javascript">
function updateHostName()
{
	document.importSettingsForm.hostName.value = '<%=request.getRemoteHost()%>';
}
</SCRIPT>

</HEAD>

<BODY id="agenda">

<%= window.printBefore() %>
<%= frame.printBefore() %>
<%= board.printBefore() %>

<form Method="POST" name="importSettingsForm" action="<%=action%>">
	<table border="0" cellspacing="0" cellpadding="5" width="100%">

	<%-- Synchro type --%>
    <tr>
        <td align="left" valign="baseline" class="txtlibform" nowrap="nowrap"><%=agenda.getString("agenda.synchroType")%> :</td>
        <td align="left" valign="middle" width="100%">
        <%
		if ("yes".equals(settings.getString("importOutlookCalendarAvailable")))
		{  %>
			<input name="synchroType" type="radio" value="<%=CalendarImportSettings.TYPE_OUTLOOK_IMPORT%>" <%= ( importSettings.getSynchroType() == CalendarImportSettings.TYPE_OUTLOOK_IMPORT ) ? "checked" : ""%> >&nbsp;<%=agenda.getString("agenda.outlookSynchro")%><br>
		<%
		} 
       
		if ("yes".equals(settings.getString("importNotesCalendarAvailable")))
		{  %>
			<input name="synchroType" type="radio" value="<%=CalendarImportSettings.TYPE_NOTES_IMPORT%>" <%= ( importSettings.getSynchroType() == CalendarImportSettings.TYPE_NOTES_IMPORT ) ? "checked" : ""%> >&nbsp;<%=agenda.getString("agenda.notesSynchro")%><br>
		<%
		} %>
 			<input name="synchroType" type="radio" value="<%=CalendarImportSettings.TYPE_NO_IMPORT%>" <%= ( importSettings.getSynchroType() == CalendarImportSettings.TYPE_NO_IMPORT ) ? "checked" : ""%> >&nbsp;<%=agenda.getString("agenda.noSynchro")%>
        </td>
    </tr>

	<%-- Synchro delay --%>
    <tr>
        <td align="left" valign="baseline" class="txtlibform" nowrap="nowrap"><%=agenda.getString("agenda.synchroDelay")%> :</td>
        <td align="left" valign="baseline">
        	<input name="synchroDelay" type="text" size="3" maxlength="3" value="<%=importSettings.getSynchroDelay()%>">&nbsp;&nbsp; <%=agenda.getString("agenda.minutes")%>
        </td>
    </tr>

	<%-- Synchro hostName --%>
    <tr>
        <td align="left" valign="baseline" class="txtlibform" nowrap="nowrap"><%=agenda.getString("agenda.hostName")%> :</td>
        <td align="left" valign="baseline">
        	<input name="hostName" type="text" size="20" maxlength="50" value="<%=importSettings.getHostName()%>">&nbsp;&nbsp; <input type="button" value="<%=request.getRemoteHost()%>" onClick="updateHostName()">
        </td>
    </tr>

  </table>
<%= board.printAfter() %>
<BR><center>
<%= buttonPane.print() %>
</center>
</form>
<%= frame.printAfter() %>
<%= window.printAfter() %>

</BODY>
</HTML>