<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="pageFlow" scope="request" type="me.romankh.resumegenerator.web.pages.ResumeHtmlPage"/>

<div id="${pageFlow.educationSectionId}" class="resume-primary-section">
  <div class="page-header">
    <h3>
      ${pageFlow.educationSectionName}
    </h3>
  </div>

  <div>
    <c:forEach var="institution" items="${pageFlow.resume.content.education.institutions}">
      <div id="${pageFlow.generateSectionId(institution.class.simpleName, institution.name, "")}"
           class="resume-secondary-section">
        <strong>${institution.name}</strong>, ${institution.location}
          <c:forEach var="degree" items="${institution.degrees}">
            <div id="${pageFlow.generateSectionId("", degree.title, "")}"
                 class="resume-tertiary-section">
              <div class="clearfix">
                <span class="pull-left">
                  <strong><em>${degree.title}</em></strong>
                </span>
                <span class="pull-right">
                  <strong>${degree.timespan}</strong>
                </span>
              </div>

              <c:set scope="request"
                     var="snippetList"
                     value="${degree.accomplishments}"/>
              <jsp:include page="snippets.jsp"/>
            </div>
          </c:forEach>
      </div>
    </c:forEach>
  </div>
</div>