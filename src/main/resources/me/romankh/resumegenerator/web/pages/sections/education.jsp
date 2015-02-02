<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="pageFlow" scope="request" type="me.romankh.resumegenerator.web.pages.ResumeHtmlPage"/>

<div id="${pageFlow.educationSectionId}" class="bs-docs-section">
  <div class="page-header">
    <h3>
      ${pageFlow.educationSectionName}
    </h3>
  </div>

  <div>
    <c:forEach var="institution" items="${pageFlow.resume.content.education.institutions}">
      <div id="${pageFlow.generateSectionId(institution.class.simpleName, institution.name, "")}">
        <strong>${institution.name}</strong>, ${institution.location}
        <ul>
          <c:forEach var="detail" items="${institution.details}">
            <li>${detail}</li>
          </c:forEach>
        </ul>
      </div>
    </c:forEach>
  </div>
</div>