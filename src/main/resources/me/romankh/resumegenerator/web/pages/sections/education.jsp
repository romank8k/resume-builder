<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="pageFlow" scope="request" type="me.romankh.resumegenerator.web.pages.ResumeHtmlPage"/>

<div id="${pageFlow.educationSectionId}" class="bs-docs-section">
  <div class="page-header">
    <h3>
      ${pageFlow.educationSectionName}
    </h3>
  </div>

  <div>
    <c:forEach var="institution" items="${pageFlow.resume.content.education.institutions}">
      <div id="${pageFlow.generateSectionId(institution.class.simpleName, institution.name, "")}">
        <strong>${institution.name}</strong>, ${institution.location}
          <c:forEach var="degree" items="${institution.degrees}">
            <div id="${pageFlow.generateSectionId("", degree.title, "")}">
              <div class="clearfix">
                <span class="pull-left">${degree.title}</span>
                <span class="pull-right">${degree.timespan}</span>
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