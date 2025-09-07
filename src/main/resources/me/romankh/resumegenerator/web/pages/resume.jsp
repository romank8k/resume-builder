<%--@elvariable id="model" type="me.romankh.resumegenerator.web.pages.ResumeHtmlPage"--%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <title>
    Resume - ${model.resume.header.name}
  </title>

  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.5/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-SgOJa3DmI69IUzQ2PVdRZhwQ+dy64/BUtbMJw1MZ8t5HZApcHrRKUc4W0kG879m7" crossorigin="anonymous">
  <link href="/static/css/resume.css" rel="stylesheet" type="text/css">

  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100..900;1,100..900&display=swap" rel="stylesheet">
</head>

<body>
<div id="resume-masthead">
  <div class="container">
    <div id="resume-masthead-header">
      <span class="float-start">
        <c:choose>
          <c:when test="${empty model.resume.header.homepage}">
            ${model.resume.header.name}
          </c:when>
          <c:otherwise>
            <a href="${model.resume.header.homepage}">${model.resume.header.name}</a>
          </c:otherwise>
        </c:choose>
      </span>
    </div>
    <div class="clearfix"></div>

    <div id="resume-masthead-info">
      <c:if test="${model.showPersonalDataOnWeb}">
        <div>
          <span class="float-start">
            ${model.resume.header.phone}
          </span>
          <span class="float-end">
            ${model.resume.header.address.street}<c:if test="${not empty model.resume.header.address.street}">,</c:if>
            ${model.resume.header.address.apartment}
          </span>
        </div>
        <div class="clearfix"></div>

        <div>
          <span class="float-start">
            ${model.resume.header.email}
          </span>
          <span class="float-end">
            ${model.resume.header.address.city},
            ${model.resume.header.address.state}
            ${model.resume.header.address.zip}
          </span>
        </div>
        <div class="clearfix"></div>
      </c:if>
    </div>

    <div id="resume-masthead-bottom">
      <div>
        <span class="float-start">
          <a href="${model.resumePdfPageUrl}">
            Click here for a PDF version
          </a>
        </span>
      </div>
      <div class="clearfix"></div>
    </div>
  </div>
</div>

<div id="resume-content" class="container">
  <div class="row">
    <div class="d-none d-sm-block col-sm-3">
      <nav id="resume-sidebar" class="hidden-print" role="navigation">
        <jsp:include page="sections/navigation.jsp"></jsp:include>
      </nav>
    </div>

    <div class="col-12 col-sm-9">
      <div id="resume-content" role="main">
        <c:forEach var="section" items="${model.sections}">
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
        <p class="float-end"></p>
        <p style="clear: both;"></p>
      </div>
    </div>
  </div>
</div>

<script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.5/dist/js/bootstrap.bundle.min.js" integrity="sha384-k6d4wzSIapyDyv1kpU366/PK5hCdSbCRGRCMv+eplOQJWyd1fbcAu9OCUj5zNLiq" crossorigin="anonymous"></script>
<script src="/static/js/resume.js"></script>
</body>
</html>
