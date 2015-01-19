<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="pageFlow" scope="request" type="me.romankh.resumegenerator.web.pages.ResumeHtmlPage"/>

<div class="bs-docs-section">
  <div class="page-header">
    <h3 id="${pageFlow.objectiveSectionId}">
      ${pageFlow.objectiveSectionName}
    </h3>
  </div>

  <div>
    <p>
      <c:forEach var="snippet" items="${pageFlow.resume.content.objective.snippets}">${pageFlow.formatTextSnippet(snippet)}</c:forEach>
    </p>
  </div>
</div>