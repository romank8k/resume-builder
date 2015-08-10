<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="pageFlow" scope="request" type="me.romankh.resumegenerator.web.pages.ResumeHtmlPage"/>

<div id="${pageFlow.experienceSectionId}" class="resume-primary-section">
  <div class="page-header">
    <h3>
      ${pageFlow.experienceSectionName}
    </h3>
  </div>

  <div>
    <c:forEach var="job" items="${pageFlow.resume.content.experience.jobs}">
      <div id="${pageFlow.generateSectionId("", job.employer, "")}"
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