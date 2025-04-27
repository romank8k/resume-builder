<%--@elvariable id="model" type="me.romankh.resumegenerator.web.pages.ResumeHtmlPage"--%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="resume-primary-section">
  <div class="section-header">
    <h3 id="${model.objectiveSectionId}">
      ${model.objectiveSectionName}
    </h3>
  </div>

  <div>
    <div class="resume-secondary-section">
      <div class="resume-tertiary-section">
        ${model.resume.content.objective.html}
      </div>
    </div>
  </div>
</div>