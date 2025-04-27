<%--@elvariable id="model" type="me.romankh.resumegenerator.web.pages.ResumeHtmlPage"--%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="${model.summaryOfQualificationsSectionId}" class="resume-primary-section">
  <div class="section-header">
    <h3>
      ${model.summaryOfQualificationsSectionName}
    </h3>
  </div>

  <div>
    <div class="resume-secondary-section">
      <div class="resume-tertiary-section">
        <c:set scope="request"
               var="snippetList"
               value="${model.resume.content.summaryOfQualifications.qualifications}"/>
        <jsp:include page="snippets.jsp"/>
      </div>
    </div>
  </div>
</div>