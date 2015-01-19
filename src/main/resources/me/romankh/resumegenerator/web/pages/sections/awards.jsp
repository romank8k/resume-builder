<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="pageFlow" scope="request" type="me.romankh.resumegenerator.web.pages.ResumeHtmlPage"/>

<div class="bs-docs-section">
  <div class="page-header">
    <h3 id="${pageFlow.awardsSectionId}">
      ${pageFlow.awardsSectionName}
    </h3>
  </div>

  <div>
    <c:forEach var="award" items="${pageFlow.resume.content.awards.awards}">
      <div class="clearfix">
        <span class="pull-left">${award.title}</span>
        <span class="pull-right">${award.timespan}</span>
      </div>
    </c:forEach>
  </div>
</div>