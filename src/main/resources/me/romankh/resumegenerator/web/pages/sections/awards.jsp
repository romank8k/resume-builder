<%--@elvariable id="model" type="me.romankh.resumegenerator.web.pages.ResumeHtmlPage"--%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="resume-primary-section">
  <div class="page-header">
    <h3 id="${model.awardsSectionId}">
      ${model.awardsSectionName}
    </h3>
  </div>

  <div>
    <div class="resume-secondary-section">
      <div class="resume-tertiary-section">
        <c:forEach var="award" items="${model.resume.content.awards.awards}">
          <div class="clearfix">
            <span class="pull-left">${award.title}</span>
            <span class="pull-right">${award.timespan}</span>
          </div>
        </c:forEach>
      </div>
    </div>
  </div>
</div>