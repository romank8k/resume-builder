<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="pageFlow" scope="request" type="me.romankh.resumegenerator.web.pages.ResumeHtmlPage"/>

<div class="bs-docs-section">
  <div class="page-header">
    <h3 id="${pageFlow.experienceSectionId}">
      ${pageFlow.experienceSectionName}
    </h3>
  </div>

  <div>
    <c:forEach var="job" items="${pageFlow.resume.content.experience.jobs}">
      <div id="${pageFlow.generateSectionId("", job.employer, job.role)}">
        <div class="clearfix">
          <span class="pull-left"><strong>${job.employer}</strong>, ${job.department}</span>
          <span class="pull-right">${job.timespan}</span>
        </div>

        <p>
          <em>${job.role}</em>
        </p>

        <c:set scope="request"
               var="snippetList"
               value="${job.accomplishments}"/>
        <jsp:include page="snippets.jsp"/>
      </div>
    </c:forEach>
  </div>
</div>