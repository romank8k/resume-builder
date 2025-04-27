<%--@elvariable id="model" type="me.romankh.resumegenerator.web.pages.ResumeHtmlPage"--%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<ul class="nav">
  <c:forEach var="section" items="${model.sections}">
    <li class="">
      <a href="#${section.sectionId}">${section.sectionName}</a>

      <c:if test="${not empty section.subSections}">
        <ul class="nav">
          <c:forEach var="subSection" items="${section.subSections}">
            <li class="">
              <a href="#${subSection.sectionId}">${subSection.sectionName}</a>

              <c:if test="${not empty subSection.subSections}">
                <ul class="nav">
                  <c:forEach var="subSubSection" items="${subSection.subSections}">
                    <li class="">
                      <a href="#${subSubSection.sectionId}">${subSubSection.sectionName}</a>
                    </li>
                  </c:forEach>
                </ul>
              </c:if>
            </li>
          </c:forEach>
        </ul>
      </c:if>
    </li>
  </c:forEach>
</ul>