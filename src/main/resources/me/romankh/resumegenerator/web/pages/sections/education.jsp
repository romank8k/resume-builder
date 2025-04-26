<%--@elvariable id="model" type="me.romankh.resumegenerator.web.pages.ResumeHtmlPage"--%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="${model.educationSectionId}" class="resume-primary-section">
  <div class="page-header">
    <h3>
      ${model.educationSectionName}
    </h3>
  </div>

  <div>
    <c:forEach var="institution" items="${model.resume.content.education.institutions}">
      <div id="${model.generateSectionId("institution-", institution.name, "")}"
           class="resume-secondary-section">
        <div class="clearfix">
          <span class="pull-left resume-institution">
              ${institution.name}
          </span>
          <span class="pull-right">
            <strong>${institution.location}</strong>
          </span>
        </div>

        <c:forEach var="degree" items="${institution.degrees}">
          <div id="${model.generateSectionId("", degree.title, "")}"
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