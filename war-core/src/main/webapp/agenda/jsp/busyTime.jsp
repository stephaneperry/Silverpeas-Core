<%
response.setHeader("Cache-Control","no-store"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires",-1); //prevents caching at the proxy server
%>

<%@ page import="javax.servlet.*"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="javax.servlet.jsp.*"%>
<%@ page import="java.io.PrintWriter"%>
<%@ page import="java.io.IOException"%>
<%@ page import="java.io.FileInputStream"%>
<%@ page import="java.io.ObjectInputStream"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.beans.*"%>

<%@ page import="java.util.*"%>
<%@ page import="javax.ejb.*,java.sql.SQLException,javax.naming.*,javax.rmi.PortableRemoteObject"%>
<%@ page import="com.stratelia.webactiv.agenda.view.*"%>
<%@ page import="com.stratelia.webactiv.calendar.model.*"%>
<%@ page import="com.stratelia.webactiv.util.*"%>
<%@ page import="com.stratelia.webactiv.beans.admin.*"%>
<%@ page import="com.stratelia.webactiv.util.viewGenerator.html.*"%>
<%@ page import="com.stratelia.webactiv.util.viewGenerator.html.buttons.*"%>
<%@ page import="com.stratelia.webactiv.util.viewGenerator.html.window.*"%>
<%@ page import="com.stratelia.webactiv.util.viewGenerator.html.browseBars.*"%>
<%@ page import="com.stratelia.webactiv.util.viewGenerator.html.operationPanes.*"%>
<%@ page import="com.stratelia.webactiv.util.viewGenerator.html.frame.*"%>

<%@ include file="checkAgenda.jsp.inc" %>
<%
ResourceLocator generalMessage = GeneralPropertiesManager.getGeneralMultilang(agenda.getLanguage());

  agenda.viewByDay();

  ResourceLocator settings = agenda.getSettings();
  String action = (String) request.getParameter("Action");
  int formIndex = new Integer((String) request.getParameter("Form")).intValue();

  if (action == null) action = "View";
  if (action.equals("Next")) {
    agenda.nextDay();
  } else
  if (action.equals("Previous")) {
    agenda.previousDay();
  }
  String day = request.getParameter("Date");
  if (day != null)
	agenda.selectDay(day);
%>

<HTML>
<HEAD>
<% out.println(graphicFactory.getLookStyleSheet()); %>
<SCRIPT LANGUAGE="JAVASCRIPT" SRC="<%=javaScriptSrc%>"></SCRIPT>

<TITLE><%=generalMessage.getString("GML.popupTitle")%></TITLE>
<Script language="JavaScript">

function selectHour(hour,minute,date) 
{
  var i = 0;
  while (window.opener.document.forms[0].elements[<%=String.valueOf(formIndex + 2)%>].options[i].text != hour) {
    i++;
  }
  window.opener.document.forms[0].elements[<%=String.valueOf(formIndex + 2)%>].selectedIndex = i;
  if (window.opener.document.forms[0].elements[<%=String.valueOf(formIndex + 4)%>].options.length > i+1)
    window.opener.document.forms[0].elements[<%=String.valueOf(formIndex + 4)%>].selectedIndex = i+1;
  else
    window.opener.document.forms[0].elements[<%=String.valueOf(formIndex + 4)%>].selectedIndex = i;

  i = 0;
  while (window.opener.document.forms[0].elements[<%=String.valueOf(formIndex + 3)%>].options[i].text != minute) {
    i++;
  }
  window.opener.document.forms[0].elements[<%=String.valueOf(formIndex + 3)%>].selectedIndex = i;
  window.opener.document.forms[0].elements[<%=String.valueOf(formIndex + 5)%>].selectedIndex = i;

  window.opener.document.forms[0].elements[<%=String.valueOf(formIndex)%>].value = date;
  window.opener.document.forms[0].elements[<%=String.valueOf(formIndex + 1)%>].value = "";
  window.opener.document.forms[0].elements[<%=String.valueOf(formIndex + 6)%>].checked = false;
  window.close();
}

function gotoNext()
{
    document.busyTimeForm.Action.value = "Next";
    document.busyTimeForm.submit();
}

function gotoPrevious()
{
    document.busyTimeForm.Action.value = "Previous";
    document.busyTimeForm.submit();
}
</script>
</HEAD>
<BODY id="agenda">

<%
	Window window = graphicFactory.getWindow();

	BrowseBar browseBar = window.getBrowseBar();
	browseBar.setComponentName("Agenda");
	browseBar.setPath(agenda.getString("trouverCreneau"));

	String navigation =  "<table><tr><td width=\"12\" align=\"right\"><a href=\"javascript:onClick=gotoPrevious()\">" +
			"<img src=\"icons/topnav_l.gif\" width=\"6\" height=\"11\" border=\"0\"></a></td>" +
		        "<td align=\"center\" width=\"340\"><span class=\"txtnav\">";
	
	navigation += agenda.getString("jour"+agenda.getStartDayInWeek()) + " " + 
		    agenda.getStartDayInMonth() + " " +
		    agenda.getString("mois"+agenda.getStartMonth()) + " " +
		    agenda.getStartYear();

	navigation += "</span></td>" +
          "<td width=\"12\"><a href=\"javascript:onClick=gotoNext()\"><img src=\"icons/topnav_r.gif\" width=\"6\" height=\"11\" border=\"0\"></a></td>" +
          "<td align=\"right\">&nbsp;</td></tr></table>";

	out.println(window.printBefore());

	Frame frame=graphicFactory.getFrame();
    out.println(frame.printBefore());
%>

  <table border="0" cellpadding="0" cellspacing="0"><tr><td>&nbsp;&nbsp;</td><td>
	 <table cellpadding=2 cellspacing=1 border=0 width="300" class=line>
			  <tr>
			    <td class=intfdcolor align=center nowrap width="100%" nowrap height=24><%out.println(navigation);%></td>
			  </tr>
			</table>
			</td></tr></table>
  <%= separator %>
  <CENTER>
  <table border="0" cellpadding="0" cellspacing="0"><tr><td>&nbsp;&nbsp;</td><td>
     <table width="98%" class="intfdcolor" border="0" cellpadding="0" cellspacing="2">
      <tr> 
       <td> 
       <table border="1" class="intfdcolor4" cellpadding="2" cellspacing="1" width="100%">
        <tr>
         <td> 
          <table border="0" cellpadding="0" cellspacing="1" class="intfdcolor" width="100%">
           <tr>
            <td align=center class="intfdcolor2" rowspan="2">&nbsp;</td>
          <%
            int beginHour = new Integer(settings.getString("beginHour")).intValue();
            int endHour = new Integer(settings.getString("endHour")).intValue();
            for (int i = beginHour; i < endHour; i++) {
              out.println("<td align=center class=\"intfdcolor2\" colspan=4>" +i + "H" + "</td>");
            }
          %>
        </tr>
        <tr>
          <%
            for (int i = beginHour * 4; i < endHour * 4; i++) {
              out.print("<td class=\"intfdcolor2\">");

              String minute = String.valueOf((i & 3)*15);
              if (minute.length() < 2) minute = "0" + minute;
              String hour = String.valueOf(i >> 2);
              if (hour.length() < 2) hour = "0" + hour;
              String date = resources.getInputDate(agenda.getCurrentDay());
              out.print("<A HREF=\"javascript:onClick=selectHour('"+ hour
                +"','"+minute+"','"+date+"')\">" + minute);
              out.print("</A>");
              out.println("</td>");
            }
          %>
        </tr>
      <%
        Collection attendeesOld = agenda.getCurrentAttendees();
        JournalHeader journal = agenda.getCurrentJournalHeader();
        Collection attendees = new ArrayList();
        attendees.add(new Attendee(journal.getDelegatorId()));
        if (attendeesOld != null) {
          Iterator i = attendeesOld.iterator();
          while (i.hasNext()) {
            attendees.add(i.next());
          }
        }
        if (attendees != null) {
          Iterator i = attendees.iterator();
          while (i.hasNext()) {
            Attendee attendee = (Attendee) i.next();
            UserDetail user = agenda.getUserDetail(attendee.getUserId());
            out.println("<tr class=\"intfdcolor2\">");
            out.println("<td class=\"txtnav\" nowrap>");
            out.println(user.getLastName() + " " + user.getFirstName());
            out.println("</td>");
						
            Collection busyTime = agenda.getBusyTime(attendee.getUserId(), agenda.getCurrentDay());
            
            int h = beginHour * 4;
            int nbFree = 0;
            int nbBusy = 0;
            
            //If day in agenda's diary is a day off
            boolean dayOff = false;
            if (agenda.isHolidayDate(user.getId(), agenda.getCurrentDay()))
            {
	            dayOff = true;
	            nbBusy = endHour * 4;
            }

            JournalHeader hour = new JournalHeader("","");
            hour.setStartDate(agenda.getCurrentDay());
            while (h < endHour * 4 && !dayOff) {
              hour.setStartHour(Schedulable.quaterCountToHourString(h));
              hour.setEndHour(Schedulable.quaterCountToHourString(h+1));
              boolean isOver = false;
              Iterator b = busyTime.iterator();
              while (b.hasNext()) {
                Schedulable sched = (Schedulable) b.next();
                if (sched.isOver(hour)) isOver = true;
                //Complete day case
								if (sched.getStartHour() == null || sched.getEndHour() == null) isOver = true;
		              }
		              if (isOver && (nbFree == 0)) {
		                nbBusy++;
		              }
		              else if ( (!isOver) && (nbBusy == 0)) {
		                nbFree++;
		              }
		              else {
		                if (nbBusy != 0) {
		                  out.println("<td colspan="+nbBusy+" class=\"intfdcolor6\">&nbsp;</td>");
		                  nbBusy = 0;
		                  nbFree++;
		                }
		                else {
		                  out.println("<td colspan="+nbFree+" class=\"intfdcolor4\">&nbsp;</td>");
		                  nbFree = 0;
		                  nbBusy++;
		                }
		              }
              
              h++;
            } //end hour

            if (nbBusy != 0) {
                out.println("<td colspan="+nbBusy+" class=\"intfdcolor6\">&nbsp;</td>");
                nbBusy = 0;
            } else
            if (nbFree != 0) {
                out.println("<td colspan="+nbFree+" class=\"intfdcolor4\">&nbsp;</td>");
                nbFree = 0;
            }

            out.println("</tr>");
          }
        }
      %>
        </table>
       </td>
      </tr>
      <tr>
				      <td>
    				  				<table align=center>
																  <tr><td class="intfdcolor6outline" width=12>&nbsp;</td><td>&nbsp;<%=agenda.getString("busy")%></td></tr>
				      				</table>
											</td>								  
						</tr>
     </table>
    </td>
   </tr>
  </table>
  </td><td>&nbsp;&nbsp;</td></tr>
  </table>
</CENTER>

<%
	out.println(frame.printMiddle());
    out.println(separator);
    out.println(frame.printAfter());
	out.println(window.printAfter());
%>

<FORM NAME="busyTimeForm" ACTION="busyTime.jsp" METHOD=POST >
  <input type="hidden" name="Action">
  <input type="hidden" name="Form" value="<%=String.valueOf(formIndex)%>">
</FORM>
</BODY>
</HTML>