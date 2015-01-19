<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="pageFlow" scope="request" type="me.romankh.resumegenerator.web.pages.ResumeHtmlPage"/>

<ul class="nav bs-sidenav">
  <c:forEach var="section" items="${pageFlow.sections}">
    <li class="">
      <a href="#${section.sectionId}">${section.sectionName}</a>

      <c:if test="${not empty section.subSections}">
        <ul class="nav">
          <c:forEach var="subSection" items="${section.subSections}">
            <li class="">
              <a href="#${subSection.sectionId}">${subSection.sectionName}</a>
            </li>
          </c:forEach>
        </ul>
      </c:if>
    </li>
  </c:forEach>
</ul>