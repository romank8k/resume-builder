<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="pageFlow" scope="request" type="me.romankh.resumegenerator.web.pages.ResumeHtmlPage"/>

<div class="bs-docs-section">
  <div class="page-header">
    <div id="js-header">
      <div>
        <span class="h3 pull-left">${pageFlow.resume.header.name}</span>
        <span class="pull-right">
          <a href="${pageFlow.resume.header.homepage}">${pageFlow.resume.header.homepage}</a>
        </span>
      </div>
      <div class="clearfix"></div>
      <c:if test="${pageFlow.showPersonalDataOnWeb}">
        <div>
          <span class="pull-left">${pageFlow.resume.header.phone}</span>
          <span class="pull-right">
            ${pageFlow.resume.header.address.street},
            ${pageFlow.resume.header.address.apartment}
          </span>
        </div>
        <div class="clearfix"></div>
        <div>
          <span class="pull-left">${pageFlow.resume.header.email}</span>
          <span class="pull-right">
            ${pageFlow.resume.header.address.city},
            ${pageFlow.resume.header.address.state}
            ${pageFlow.resume.header.address.zip}
          </span>
        </div>
        <div class="clearfix"></div>
      </c:if>
    </div>
  </div>
</div>