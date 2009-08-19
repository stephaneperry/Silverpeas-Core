<%@ page errorPage="../../admin/jsp/errorpage.jsp"%>
<%@ include file="checkVersion.jsp"%>
<%@ page import="com.stratelia.webactiv.util.viewGenerator.html.browseBars.BrowseBar" %>
<%@ page import="com.stratelia.webactiv.util.viewGenerator.html.buttonPanes.ButtonPane" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/viewGenerator.tld" prefix="view"%>
<%
  // declaration of labels !!!
  ResourceLocator messages = new ResourceLocator("com.stratelia.silverpeas.versioningPeas.multilang.versioning", m_MainSessionCtrl.getFavoriteLanguage());
  request.setAttribute("resources", messages);

  String versionTypeLabel = messages.getString("typeOfVersion");
  String commentsLabel = messages.getString("comments");
  String okLabel = messages.getString("ok");
  String nokLabel = messages.getString("cancel");
  String[] radioButtonLabel = { messages.getString("public"), messages.getString("archive") };
  pageContext.setAttribute("radios", radioButtonLabel);
%>

<html>
<title></title>
<view:looknfeel />
<head>
<script>

</script>
</head>
<body>
<fmt:setLocale value="${sessionScope['SilverSessionController'].favoriteLanguage}" />
<view:setBundle bundle="${requestScope.resources.resourceBundle}" />
      <form name="addForm" action="<c:url value="/RVersioningPeas/${param.ComponentId}/saveOnline" />" method="POST">
      <input type="hidden" name="radio" value="0"/>
      <input type="hidden" name="action" value="checkin"/>
      <input type="hidden" name="publicationId" value="<c:out value="${param.Id}" />" />
      <input type="hidden" name="componentId" value="<c:out value="${param.ComponentId}" />" />
      <input type="hidden" name="spaceId" value="<c:out value="${param.SpaceId}" />" />
      <input type="hidden" name="documentId" value="<c:out value="${param.documentId}" />" />
      <table CELLPADDING="5" CELLSPACING="0" BORDER="0" WIDTH="100%">
        <tr>
          <td class="txtlibform"><%=versionTypeLabel%> :</td>
          <td align="left" valign="baseline">
            <c:forEach items="${pageScope.radios}" var="radio" varStatus="status">
              <input type="radio" name="versionType" <c:if test="${status.index == 0}">checked</c:if> onClick="javascript:document.addForm.radio.value=<c:out value="${status.index}" />;" /><c:out value="${radio}" />
            </c:forEach>
          </td>
        </tr>
        <tr>
          <td class="txtlibform" valign="top"><%=commentsLabel%> :</td>
          <td align=left valign="baseline"><textarea name="comments" rows="5" cols="60"></textarea></td>
        </tr>
      </table>
      </form>
    <%
      ButtonPane buttonPane = gef.getButtonPane();
      buttonPane.addButton(gef.getFormButton(okLabel, "javascript:document.addForm.submit();", false));
      buttonPane.addButton(gef.getFormButton(nokLabel,"javascript:parent.document.forms[0].action.value='checkin';parent.document.forms[0].submit();", false));
      out.println("<br><center>");
      out.println(buttonPane.print());
      out.println("</center>");
    %>
</body>
</html>