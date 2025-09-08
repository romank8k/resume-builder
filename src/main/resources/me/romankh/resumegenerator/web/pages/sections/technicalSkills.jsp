<%--@elvariable id="model" type="me.romankh.resumegenerator.web.pages.ResumeHtmlPage"--%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="${model.technicalSkillsSectionId}" class="resume-primary-section">
  <div class="section-header">
    <h3>
      ${model.technicalSkillsSectionName}
    </h3>
  </div>

  <div>
    <div class="resume-secondary-section">
      <div class="resume-tertiary-section">
        <c:set scope="request"
               var="snippetList"
               value="${model.resume.content.technicalSkills.skills}"/>
        <jsp:include page="snippets.jsp"/>
      </div>
    </div>
  </div>
</div>