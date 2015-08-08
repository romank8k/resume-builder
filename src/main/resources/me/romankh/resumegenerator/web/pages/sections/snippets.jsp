<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="pageFlow" scope="request" type="me.romankh.resumegenerator.web.pages.ResumeHtmlPage"/>

<ul>
  <c:forEach var="snippets" items="${snippetList}">
    <li>
      <c:forEach var="snippet" items="${snippets}">${snippet}</c:forEach>
    </li>
  </c:forEach>
</ul>