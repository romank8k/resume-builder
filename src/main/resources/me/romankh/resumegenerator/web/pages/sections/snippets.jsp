<%--@elvariable id="model" type="me.romankh.resumegenerator.web.pages.ResumeHtmlPage"--%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<ul>
  <c:forEach var="snippet" items="${snippetList}">
    <li>
        ${snippet}
    </li>
  </c:forEach>
</ul>