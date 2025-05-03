<%--@elvariable id="model" type="me.romankh.resumegenerator.web.pages.ResumeHtmlPage"--%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="${model.experienceSectionId}" class="resume-primary-section">
  <div class="section-header">
    <h3>
      ${model.experienceSectionName}
    </h3>
  </div>

  <div>
    <c:forEach var="job" items="${model.resume.content.experience.jobs}">
      <div id="${model.generateSectionId("employer", job.employer)}"
           class="resume-secondary-section">
        <div class="clearfix">
          <span class="float-start resume-employer">
            ${job.employer}
          </span>
          <span class="float-end">
            <strong>${job.location}</strong>
          </span>
        </div>

        <c:forEach var="role" items="${job.roles}">
          <div id="${model.generateSectionId("role", role.title, job.employer)}"
               class="resume-tertiary-section">
            <div class="clearfix">
              <span class="float-start">
                <strong><em>${role.title}</em></strong>
              </span>
              <span class="float-end">
                <strong>${role.timespan}</strong>
              </span>
            </div>

            <c:set scope="request"
                   var="snippetList"
                   value="${role.accomplishments}"/>
            <jsp:include page="snippets.jsp"/>
          </div>
        </c:forEach>
      </div>
    </c:forEach>
  </div>
</div>