<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="pageFlow" scope="request" type="me.romankh.resumegenerator.web.pages.ResumeHtmlPage"/>

<div id="${pageFlow.projectsSectionId}" class="resume-primary-section">
  <div class="page-header">
    <h3>
      ${pageFlow.projectsSectionName}
    </h3>
  </div>

  <div>
    <c:set scope="request"
           var="snippetList"
           value="${pageFlow.resume.content.projects.projects}"/>
    <jsp:include page="snippets.jsp"/>
  </div>
</div>