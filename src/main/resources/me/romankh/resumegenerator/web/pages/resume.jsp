<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="pageFlow" scope="request" type="me.romankh.resumegenerator.web.pages.ResumeHtmlPage"/>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <title>
    Resume - ${pageFlow.resume.header.name}
  </title>

  <link href="static/vendor/bootstrap/css/bootstrap.css" rel="stylesheet" type="text/css">
  <link href="static/css/resume.css" rel="stylesheet" type="text/css">
  <link href="http://fonts.googleapis.com/css?family=Merriweather+Sans:400,400italic,700,700italic" rel="stylesheet" type="text/css">
</head>

<body>
<div id="resume-masthead">
  <div class="container">
    <div id="resume-masthead-header">
      <span class="pull-left">
        <c:choose>
          <c:when test="${empty pageFlow.resume.header.homepage}">
            ${pageFlow.resume.header.name}
          </c:when>
          <c:otherwise>
            <a href="${pageFlow.resume.header.homepage}">${pageFlow.resume.header.name}</a>
          </c:otherwise>
        </c:choose>
      </span>
    </div>
    <div class="clearfix"></div>

    <div id="resume-masthead-info">
      <c:if test="${pageFlow.showPersonalDataOnWeb}">
        <div>
          <span class="pull-left">
            ${pageFlow.resume.header.phone}
          </span>
          <span class="pull-right">
            ${pageFlow.resume.header.address.street},
            ${pageFlow.resume.header.address.apartment}
          </span>
        </div>
        <div class="clearfix"></div>

        <div>
          <span class="pull-left">
            ${pageFlow.resume.header.email}
          </span>
          <span class="pull-right">
            ${pageFlow.resume.header.address.city},
            ${pageFlow.resume.header.address.state}
            ${pageFlow.resume.header.address.zip}
          </span>
        </div>
        <div class="clearfix"></div>
      </c:if>
    </div>

    <div id="resume-masthead-bottom">
      <div>
        <span class="pull-left">
          <a href="${pageFlow.resumePdfPageUrl}">
            Click here for a PDF version
          </a>
        </span>
      </div>
      <div class="clearfix"></div>
    </div>
  </div>
</div>

<div class="container">
  <div class="row">
    <div class="hidden-xs col-sm-3">
      <nav id="resume-sidebar" class="hidden-print" role="navigation">
        <jsp:include page="sections/navigation.jsp"></jsp:include>
      </nav>
    </div>

    <div class="col-xs-12 col-sm-9">
      <div id="resume-content" role="main">
        <c:forEach var="section" items="${pageFlow.sections}">
          <c:choose>
            <c:when test="${section.sectionClass.simpleName eq 'Objective'}">
              <jsp:include page="sections/objective.jsp"></jsp:include>
            </c:when>
            <c:when test="${section.sectionClass.simpleName eq 'SummaryOfQualifications'}">
              <jsp:include page="sections/summaryOfQualifications.jsp"></jsp:include>
            </c:when>
            <c:when test="${section.sectionClass.simpleName eq 'Experience'}">
              <jsp:include page="sections/experience.jsp"></jsp:include>
            </c:when>
            <c:when test="${section.sectionClass.simpleName eq 'Education'}">
              <jsp:include page="sections/education.jsp"></jsp:include>
            </c:when>
            <c:when test="${section.sectionClass.simpleName eq 'Awards'}">
              <jsp:include page="sections/awards.jsp"></jsp:include>
            </c:when>
            <c:when test="${section.sectionClass.simpleName eq 'Projects'}">
              <jsp:include page="sections/projects.jsp"></jsp:include>
            </c:when>
          </c:choose>
        </c:forEach>
      </div>

      <div id="resume-footer">
        <p class="pull-right">
          Made with <a href="https://github.com/romank8k/ResumeGenerator">Resume Generator</a>
        </p>
        <p style="clear: both;"></p>
      </div>
    </div>
  </div>
</div>

<script src="/static/vendor/jquery/jquery.js"></script>
<script src="/static/vendor/bootstrap/js/bootstrap.js"></script>
<script src="/static/vendor/jquery/jquery.scrollTo.js"></script>
<script src="/static/vendor/jquery/jquery.localScroll.js"></script>
<script src="/static/js/resume.js"></script>
</body>
</html>
