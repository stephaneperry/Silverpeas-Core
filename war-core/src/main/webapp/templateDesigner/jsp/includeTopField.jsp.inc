<%
	browseBar.setDomainName(resource.getString("templateDesigner.toolName"));
	browseBar.setComponentName(resource.getString("templateDesigner.field"));

	out.println(window.printBefore());
	out.println(frame.printBefore());
	out.println(board.printBefore());
%>
<form name="fieldForm" action="<%=m_context%>/RtemplateDesigner/jsp/<%=actionForm%>" method="POST">
<table CELLSPACING="5" WIDTH="100%">
<tr>
<td class="txtlibform" width="10" nowrap="nowrap"><%=resource.getString("GML.name")%> :</td>
<td>
	<% if (!nameDisabled) { %>
		<input type="text" name="FieldName" size="30" value="<%=name%>"/>&nbsp;<img src="<%=resource.getIcon("templateDesigner.mandatory")%>" width="5" height="5"/>
	<% } else { %>
		<%=name%><input type="hidden" name="FieldName" value="<%=name%>"/>
	<% } %>
	<input type="hidden" name="Displayer" value="<%=displayer%>"/>
</td>
</tr>
<% 
	while (languages.hasNext()) {
		String lang 	= (String) languages.next();
		String label 	= "";
		if (field != null)
			label = field.getLabel(lang);
%>
		<tr>
			<td class="txtlibform" nowrap="nowrap"><%=resource.getString("GML.label")%> (<%=lang%>) :</td><td><input type="text" name="Label_<%=lang%>" size="30" value="<%=label%>"/></td>
		</tr>
<% } %>
<tr>
<td class="txtlibform"><%=resource.getString("GML.requiredField")%> :</td><td><input type="checkbox" name="Mandatory" value="true" <%=mandatoryChecked%>/></td>
</tr>
<tr>
<td class="txtlibform"><%=resource.getString("templateDesigner.searchable")%> :</td><td><input type="checkbox" name="Searchable" value="true" <%=searchable%>/></td>
</tr>