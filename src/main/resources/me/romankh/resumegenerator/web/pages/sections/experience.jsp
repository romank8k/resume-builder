<%--@elvariable id="model" type="me.romankh.resumegenerator.web.pages.ResumeHtmlPage"--%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="${model.experienceSectionId}" class="resume-primary-section">
  <div class="page-header">
    <h3>
      ${model.experienceSectionName}
    </h3>
  </div>

  <div>
    <c:forEach var="job" items="${model.resume.content.experience.jobs}">
      <div id="${model.generateSectionId("employer-", job.employer, "")}"
           class="resume-secondary-section">
        <div class="clearfix">
          <span class="pull-left resume-employer">
            ${job.employer}
          </span>
          <span class="pull-right">
            <strong>${job.location}</strong>
          </span>
        </div>

        <c:set scope="request"
               var="rolesList"
               value="${job.roles}"/>
        <jsp:include page="roles.jsp"/>
      </div>
    </c:forEach>
  </div>
</div>