<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="pageFlow" scope="request" type="me.romankh.resumegenerator.web.pages.ResumeHtmlPage"/>

<div id="${pageFlow.summaryOfQualificationsSectionId}" class="resume-primary-section">
  <div class="page-header">
    <h3>
      ${pageFlow.summaryOfQualificationsSectionName}
    </h3>
  </div>

  <div>
    <div class="resume-secondary-section">
      <div class="resume-tertiary-section">
        <c:set scope="request"
               var="snippetList"
               value="${pageFlow.resume.content.summaryOfQualifications.qualifications}"/>
        <jsp:include page="snippets.jsp"/>
      </div>
    </div>
  </div>
</div>