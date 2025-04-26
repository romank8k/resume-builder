<%--@elvariable id="model" type="me.romankh.resumegenerator.web.pages.ResumeHtmlPage"--%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="${model.projectsSectionId}" class="resume-primary-section">
  <div class="page-header">
    <h3>
      ${model.projectsSectionName}
    </h3>
  </div>

  <div>
    <div class="resume-secondary-section">
      <div class="resume-tertiary-section">
        <c:set scope="request"
               var="snippetList"
               value="${model.resume.content.projects.projects}"/>
        <jsp:include page="snippets.jsp"/>
      </div>
    </div>
  </div>
</div>