<%--@elvariable id="model" type="me.romankh.resumegenerator.web.pages.ResumeHtmlPage"--%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:forEach var="role" items="${rolesList}">
  <div class="resume-tertiary-section">
    <div class="clearfix">
      <span class="pull-left">
        <strong><em>${role.title}</em></strong>
      </span>
      <span class="pull-right">
        <strong>${role.timespan}</strong>
      </span>
    </div>

    <c:set scope="request"
           var="snippetList"
           value="${role.accomplishments}"/>
    <jsp:include page="snippets.jsp"/>
  </div>
</c:forEach>
