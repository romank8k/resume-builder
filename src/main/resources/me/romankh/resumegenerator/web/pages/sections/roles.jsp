<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="pageFlow" scope="request" type="me.romankh.resumegenerator.web.pages.ResumeHtmlPage"/>

<p>
  <c:forEach var="role" items="${rolesList}">
    <div id="${pageFlow.generateSectionId("", role.role, "")}">
    <div class="clearfix">
      <span class="pull-left">${role.department}</span>
      <span class="pull-right">${role.timespan}</span>
    </div>

    <p>
      <em>${role.role}</em>
    </p>

    <c:set scope="request"
           var="snippetList"
           value="${role.accomplishments}"/>
    <jsp:include page="snippets.jsp"/>
  </c:forEach>
</p>