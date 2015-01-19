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

  <link href="vendor/bootstrap/css/bootstrap.css" rel="stylesheet">
  <link href="css/resume.css" rel="stylesheet">
</head>

<body>

<a href="https://github.com/rkhmelichek/ResumeGenerator">
  <img style="position: absolute; top: 0; right: 0; border: 0;"
       src="https://s3.amazonaws.com/github/ribbons/forkme_right_green_007200.png"
       alt="Fork me on GitHub">
</a>

<div class="container">
  <div class="row">
    <div class="col-md-3">
      <div class="bs-sidebar hidden-print affix" role="complementary">
        <jsp:include page="sections/navigation.jsp"></jsp:include>
      </div>
    </div>

    <div class="col-md-9" role="main">
      <a href="${pageFlow.resumePdfPageUrl}">
        Click here for a PDF version
      </a>

      <jsp:include page="sections/header.jsp"></jsp:include>

      <c:forEach var="section" items="${pageFlow.sections}">
        <c:choose>
          <c:when test="${section.sectionClass.simpleName eq 'Objective'}">
            <jsp:include page="sections/objective.jsp"></jsp:include>
          </c:when>
          <c:when test="${section.sectionClass.simpleName eq 'SummaryOfQualifications'}">
            <jsp:include page="sections/summaryOfQualifications.jsp"></jsp:include>
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
          <c:when test="${section.sectionClass.simpleName eq 'Experience'}">
            <jsp:include page="sections/experience.jsp"></jsp:include>
          </c:when>
        </c:choose>
      </c:forEach>
    </div>
  </div>
</div>

<script src="vendor/jquery/jquery.js"></script>
<script src="vendor/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript">
  var $window = $(window)
  var $body = $(document.body)

  var navHeight = $('.navbar').outerHeight(true) + 10;
  //console.log(navHeight);
  navHeight = 10;

  $body.scrollspy({
    target: '.bs-sidebar',
    offset: navHeight
  });

  $window.on('load', function () {
    $body.scrollspy('refresh')
  });

  $('.bs-docs-container [href=#]').click(function (e) {
    e.preventDefault();
  });

  $body.on('activate.bs.scrollspy', function () {
  });
</script>
</body>
</html>