<%--
/*********************************************************************************
 * The contents of this file are subject to the Common Public Attribution
 * License Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.openemm.org/cpal1.html. The License is based on the Mozilla
 * Public License Version 1.1 but Sections 14 and 15 have been added to cover
 * use of software over a computer network and provide for limited attribution
 * for the Original Developer. In addition, Exhibit A has been modified to be
 * consistent with Exhibit B.
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * 
 * The Original Code is OpenEMM.
 * The Original Developer is the Initial Developer.
 * The Initial Developer of the Original Code is AGNITAS AG. All portions of
 * the code written by AGNITAS AG are Copyright (c) 2007 AGNITAS AG. All Rights
 * Reserved.
 * 
 * Contributor(s): AGNITAS AG. 
 ********************************************************************************/
 --%><%@ page language="java" import="org.agnitas.util.*, org.agnitas.web.*, java.util.*, org.agnitas.beans.*" contentType="text/html; charset=utf-8" %>
<%@ taglib uri="/WEB-INF/agnitas-taglib.tld" prefix="agn" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<agn:CheckLogon/>
<% MailingWizardForm aForm=null;
   aForm=(MailingWizardForm)session.getAttribute("mailingWizardForm");
   Mailing mailing=aForm.getMailing();
%>

<agn:Permission token="mailing.show"/>

<%
// mailing navigation:
   pageContext.setAttribute("sidemenu_active", new String("Mailings")); 
    pageContext.setAttribute("sidemenu_sub_active", new String("New_Mailing"));
    pageContext.setAttribute("agnNavigationKey", new String("MailingNew"));
    pageContext.setAttribute("agnHighlightKey", new String("New_Mailing"));
    pageContext.setAttribute("agnTitleKey", new String("Mailing")); 
    pageContext.setAttribute("agnSubtitleKey", new String("Mailing")); 
%>

<%@include file="/header.jsp"%>

<html:errors/>

<html:form action="/mwStart">

    <html:hidden property="action"/>
    
    <img src="<bean:write name="emm.layout" property="baseUrl" scope="session"/>one_pixel.gif" width="400" height="10" border="0">
    <br>
    <b><bean:message key="NewMailingMethod"/>:</b>

    <BR>
    <BR>
    <BR>
    <html:link page="<%=new String("/mailingbase.do?action=" + MailingBaseAction.ACTION_NEW) + "&mailingID=0&isTemplate=false"%>" style="color: #73A2D0;"><b><bean:message key="Normal"/>:</b> <bean:message key="NoWizard"/>.</html:link>
    <BR>
    <BR>
    <html:link page="<%=new String("/mwStart.do?action=" + MailingWizardAction.ACTION_START)%>" style="color: #73A2D0;"><b><bean:message key="Wizard"/>:</b> <bean:message key="WizardDescription"/>.</html:link>
    <BR>
    <BR>

<% // wizard navigation: %>
    <br>
    <table border="0" cellspacing="0" cellpadding="0" width="100%">
        <tr>
            <td>&nbsp;</td>
            <td align="right">
                &nbsp;
                <html:link page="<%=new String("/mailingbase.do?action=" + MailingBaseAction.ACTION_NEW) + "&mailingID=0&isTemplate=false"%>"><html:img src="button?msg=Normal" border="0"/></html:link>
                <html:image property="action_forward" value="start" src="button?msg=Wizard" border="0"/>
                &nbsp;
            </td>
        </tr>
    </table>         
</html:form>
<%@include file="/footer.jsp"%>
