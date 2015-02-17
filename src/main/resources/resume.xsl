<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:fop="http://www.w3.org/1999/XSL/Format"
                exclude-result-prefixes="fo">
  <xsl:output method="xml" version="1.0" omit-xml-declaration="no" indent="yes"/>

  <xsl:param name="showPersonalData" as="xs:boolean" select="xs:boolean('true')"/>

  <!-- Section Header Labels -->
  <xsl:param name="objectiveLabel" select="'Objective'"/>
  <xsl:param name="summaryOfQualificationsLabel" select="'Summary of Qualifications'"/>
  <xsl:param name="projectsLabel" select="'Projects'"/>
  <xsl:param name="educationLabel" select="'Education'"/>
  <xsl:param name="awardsLabel" select="'Awards'"/>
  <xsl:param name="relevantCoursesLabel" select="'Relevant Courses'"/>
  <xsl:param name="experienceLabel" select="'Experience'"/>

  <!-- Fonts -->
  <xsl:param name="resumeHeaderFontFamily">Trebuchet MS</xsl:param>
  <xsl:param name="resumeHeaderFontSize">12pt</xsl:param>
  <xsl:param name="resumeHeaderFontWeight">normal</xsl:param>

  <xsl:param name="resumeHeaderNameFontSize">20pt</xsl:param>
  <xsl:param name="resumeHeaderHomepageFontSize">14pt</xsl:param>

  <xsl:param name="sectionHeaderFontFamily">Trebuchet MS</xsl:param>
  <xsl:param name="sectionHeaderFontSize">12pt</xsl:param>
  <xsl:param name="sectionHeaderFontWeight">bold</xsl:param>

  <xsl:param name="contentFontFamily">Georgia</xsl:param>
  <xsl:param name="contentFontSize">10pt</xsl:param>
  <xsl:param name="contentFontWeight">normal</xsl:param>

  <!-- Padding -->
  <xsl:param name="sectionContentBottomPadding" select="'10pt'"/>
  <xsl:param name="sectionSubContentBottomPadding" select="'5pt'"/>
  <xsl:param name="sectionSubSubContentBottomPadding" select="'4pt'"/>

  <!-- TODO: Add clauses to not skip other things like the education section across page boundaries. -->
  <!-- TODO: Instead of nesting blocks and accumulating padding, can you add a block just for padding, and skip the block if its the last iteration of whatever thing you’re adding. -->
  <!-- TODO: Calculate these variables dynamically. -->
  <!-- TODO: These also have the $listSpacing applied, but did not take it into account. -->
  <!--
    Section already has the $sectionSubContentBottomPadding
    and $sectionSubSubContentBottomPadding applied, so:
    $educationSectionContentBottomPadding = $sectionContentBottomPadding -
      ($sectionSubContentBottomPadding + $sectionSubSubContentBottomPadding)
  -->
  <xsl:param name="educationSectionContentBottomPadding" select="'1pt'"/>

  <!--
    $educationSectionSubContentBottomPadding =
      $sectionSubContentBottomPadding - sectionSubSubContentBottomPadding -->
  <xsl:param name="educationSectionSubContentBottomPadding" select="'1pt'"/>

  <!-- Margins -->
  <xsl:param name="listSpacing" select="'2pt'"/>
  <xsl:param name="resumeHeaderBottomMargin" select="'14pt'"/>

  <xsl:param name="bulletSymbol" select="'&#x2022;'"/>

  <xsl:variable name="lowercase" select="'abcdefghijklmnopqrstuvwxyz'" />
  <xsl:variable name="uppercase" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZ'" />

  <xsl:template match="resume">
    <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
      <fo:layout-master-set>
        <fo:simple-page-master master-name="resume"
                               page-width="215.9mm"
                               page-height="279.4mm"
                               margin-top="10mm"
                               margin-bottom="10mm"
                               margin-left="10mm"
                               margin-right="10mm">
          <fo:region-body/>
          <fo:region-before extent="20mm"/>
        </fo:simple-page-master>
      </fo:layout-master-set>

      <fo:page-sequence master-reference="resume">
        <fo:flow flow-name="xsl-region-body">
          <fo:block font-family="{$resumeHeaderFontFamily}"
                    font-size="{$resumeHeaderFontSize}"
                    font-weight="{$resumeHeaderFontWeight}"
                    space-after="{$resumeHeaderBottomMargin}">
            <fo:block font-weight="bold"
                      space-after="1mm">
              <xsl:call-template name="justifyLineTemplate">
                <xsl:with-param name="leftContent" select="header/name"/>
                <xsl:with-param name="leftAttributes">
                  font-size="<xsl:value-of select="$resumeHeaderNameFontSize"/>"
                </xsl:with-param>

                <xsl:with-param name="rightContent" select="header/homepage"/>
                <xsl:with-param name="rightAttributes">
                  font-size="<xsl:value-of select="$resumeHeaderHomepageFontSize"/>"
                </xsl:with-param>
              </xsl:call-template>
            </fo:block>

            <xsl:if test="$showPersonalData">
              <fo:block>
                <xsl:call-template name="justifyLineTemplate">
                  <xsl:with-param name="leftContent" select="header/phone"/>
                  <xsl:with-param name="leftAttributes"></xsl:with-param>

                  <xsl:with-param name="rightContent">
                    <xsl:value-of select="header/address/street"/>
                    <xsl:text>, </xsl:text>
                    <xsl:value-of select="header/address/apartment"/>
                  </xsl:with-param>
                  <xsl:with-param name="rightAttributes"></xsl:with-param>
                </xsl:call-template>
              </fo:block>

              <fo:block>
                <xsl:call-template name="justifyLineTemplate">
                  <xsl:with-param name="leftContent" select="header/email"/>
                  <xsl:with-param name="leftAttributes"></xsl:with-param>

                  <xsl:with-param name="rightContent">
                    <xsl:value-of select="header/address/city"/>
                    <xsl:text>, </xsl:text>
                    <xsl:value-of select="header/address/state"/>
                    <xsl:text> </xsl:text>
                    <xsl:value-of select="header/address/zip"/>
                  </xsl:with-param>
                  <xsl:with-param name="rightAttributes"></xsl:with-param>
                </xsl:call-template>
              </fo:block>
            </xsl:if>

            <fo:block>
              <fo:leader leader-pattern="rule" leader-length="100%"/>
            </fo:block>
          </fo:block>

          <fo:block font-family="{$contentFontFamily}"
                    font-size="{$contentFontSize}"
                    font-weight="{$contentFontWeight}">
            <fo:table table-layout="fixed" width="100%" border-collapse="separate">
              <fo:table-column column-width="20%"/>
              <fo:table-column column-width="80%"/>
              <fo:table-body>
                <xsl:apply-templates select="content"/>
              </fo:table-body>
            </fo:table>
          </fo:block>
        </fo:flow>
      </fo:page-sequence>
    </fo:root>
  </xsl:template>

  <!-- Objective -->
  <xsl:template match="objective[@include='true']">
    <fo:table-row>
      <fo:table-cell>
        <fo:block font-family="{$sectionHeaderFontFamily}"
                  font-size="{$sectionHeaderFontSize}"
                  font-weight="{$sectionHeaderFontWeight}">
          <xsl:value-of select="translate($objectiveLabel, $lowercase, $uppercase)"/>
        </fo:block>
      </fo:table-cell>

      <fo:table-cell>
        <fo:block padding-bottom="{$sectionContentBottomPadding}">
          <xsl:apply-templates/>
        </fo:block>
      </fo:table-cell>
    </fo:table-row>
  </xsl:template>

  <!-- Summary of Qualifications -->
  <xsl:template match="summary_of_qualifications[@include='true']">
    <fo:table-row>
      <fo:table-cell>
        <fo:block font-family="{$sectionHeaderFontFamily}"
                  font-size="{$sectionHeaderFontSize}"
                  font-weight="{$sectionHeaderFontWeight}">
          <xsl:value-of select="translate($summaryOfQualificationsLabel, $lowercase, $uppercase)"/>
        </fo:block>
      </fo:table-cell>

      <fo:table-cell>
        <fo:block padding-bottom="{$sectionContentBottomPadding}">
          <xsl:if test="qualifications">
            <fo:list-block provisional-distance-between-starts="4mm"
                           provisional-label-separation="0mm">
              <xsl:for-each select="qualifications/qualification[@include='true']">
                <xsl:call-template name="listItemTemplate">
                  <xsl:with-param name="bulleted" select="xs:boolean('true')"/>
                </xsl:call-template>
              </xsl:for-each>
            </fo:list-block>
          </xsl:if>
        </fo:block>
      </fo:table-cell>
    </fo:table-row>
  </xsl:template>

  <!-- Education -->
  <xsl:template match="education[@include='true']">
    <fo:table-row>
      <fo:table-cell>
        <fo:block font-family="{$sectionHeaderFontFamily}"
                  font-size="{$sectionHeaderFontSize}"
                  font-weight="{$sectionHeaderFontWeight}">
          <xsl:value-of select="translate($educationLabel, $lowercase, $uppercase)"/>
        </fo:block>
      </fo:table-cell>

      <fo:table-cell>
        <fo:block padding-bottom="{$educationSectionContentBottomPadding}">
          <xsl:for-each select="institutions/institution[@include='true']">
            <fo:block padding-bottom="{$educationSectionSubContentBottomPadding}">
              <fo:block font-weight="bold">
                <xsl:call-template name="justifyLineTemplate">
                  <xsl:with-param name="leftContent" select="name"/>
                  <xsl:with-param name="rightContent" select="location"/>
                </xsl:call-template>
              </fo:block>

              <xsl:for-each select="degrees/degree[@include='true']">
                <fo:block padding-bottom="{$sectionSubSubContentBottomPadding}">
                  <fo:block font-weight="bold">
                    <xsl:call-template name="justifyLineTemplate">
                      <xsl:with-param name="leftContent" select="title"/>
                      <xsl:with-param name="leftAttributes">
                        font-style="italic"
                      </xsl:with-param>
                      <xsl:with-param name="rightContent" select="timespan"/>
                      <xsl:with-param name="rightAttributes"/>
                    </xsl:call-template>
                  </fo:block>

                  <fo:block>
                    <xsl:apply-templates select="accomplishments">
                      <xsl:with-param name="bulleted" select="xs:boolean('true')"/>
                    </xsl:apply-templates>
                  </fo:block>
                </fo:block>
              </xsl:for-each>
            </fo:block>
          </xsl:for-each>
        </fo:block>
      </fo:table-cell>
    </fo:table-row>
  </xsl:template>

  <!-- Awards -->
  <xsl:template match="awards[@include='true']">
    <fo:table-row>
      <fo:table-cell>
        <fo:block font-family="{$sectionHeaderFontFamily}"
                  font-size="{$sectionHeaderFontSize}"
                  font-weight="{$sectionHeaderFontWeight}">
          <xsl:value-of select="translate($awardsLabel, $lowercase, $uppercase)"/>
        </fo:block>
      </fo:table-cell>

      <fo:table-cell>
        <fo:block padding-bottom="{$sectionContentBottomPadding}">
          <xsl:if test="awards">
            <xsl:for-each select="awards/award[@include='true']">
              <fo:block font-weight="bold">
                <xsl:call-template name="justifyLineTemplate">
                  <xsl:with-param name="leftContent" select="title"/>
                  <xsl:with-param name="leftAttributes"/>
                  <xsl:with-param name="rightContent" select="timespan"/>
                  <xsl:with-param name="rightAttributes"/>
                </xsl:call-template>
              </fo:block>
            </xsl:for-each>
          </xsl:if>
        </fo:block>
      </fo:table-cell>
    </fo:table-row>
  </xsl:template>

  <!-- Relevant Courses -->
  <xsl:template match="relevant_courses[@include='true']">
    <fo:table-row>
      <fo:table-cell>
        <fo:block font-family="{$sectionHeaderFontFamily}"
                  font-size="{$sectionHeaderFontSize}"
                  font-weight="{$sectionHeaderFontWeight}">
          <xsl:value-of select="translate($relevantCoursesLabel, $lowercase, $uppercase)"/>
        </fo:block>
      </fo:table-cell>

      <fo:table-cell>
        <fo:block padding-bottom="{$sectionContentBottomPadding}">
          <xsl:if test="courses">
            <fo:list-block provisional-distance-between-starts="5mm"
                           provisional-label-separation="15mm">
              <xsl:for-each select="courses/course[@include='true']">
                <xsl:call-template name="listItemTemplate">
                  <xsl:with-param name="bulleted" select="xs:boolean('true')"/>
                </xsl:call-template>
              </xsl:for-each>
            </fo:list-block>
          </xsl:if>
        </fo:block>
      </fo:table-cell>
    </fo:table-row>
  </xsl:template>

  <!-- Projects -->
  <xsl:template match="projects[@include='true']">
    <fo:table-row>
      <fo:table-cell>
        <fo:block font-family="{$sectionHeaderFontFamily}"
                  font-size="{$sectionHeaderFontSize}"
                  font-weight="{$sectionHeaderFontWeight}">
          <xsl:value-of select="translate($projectsLabel, $lowercase, $uppercase)"/>
        </fo:block>
      </fo:table-cell>

      <fo:table-cell>
        <fo:block padding-bottom="{$sectionContentBottomPadding}">
          <xsl:if test="projects">
            <fo:list-block provisional-distance-between-starts="5mm"
                           provisional-label-separation="15mm">
              <xsl:for-each select="projects/project[@include='true']">
                <xsl:call-template name="listItemTemplate">
                  <xsl:with-param name="bulleted" select="xs:boolean('true')"/>
                </xsl:call-template>
              </xsl:for-each>
            </fo:list-block>
          </xsl:if>
        </fo:block>
      </fo:table-cell>
    </fo:table-row>
  </xsl:template>

  <!-- Experience -->
  <xsl:template match="experience[@include='true']">
    <fo:table-row>
      <fo:table-cell>
        <fo:block font-family="{$sectionHeaderFontFamily}"
                  font-size="{$sectionHeaderFontSize}"
                  font-weight="{$sectionHeaderFontWeight}">
          <xsl:value-of select="translate($experienceLabel, $lowercase, $uppercase)"/>
        </fo:block>
      </fo:table-cell>

      <fo:table-cell>
        <fo:block padding-bottom="{$sectionContentBottomPadding}">
          <xsl:for-each select="jobs/job[@include='true']">
            <!-- Avoid splitting jobs across multiple pages. -->
            <fo:block page-break-inside="avoid">
              <fo:block font-weight="bold">
                <xsl:call-template name="justifyLineTemplate">
                  <xsl:with-param name="leftContent" select="employer"/>
                  <xsl:with-param name="rightContent" select="location"/>
                </xsl:call-template>
              </fo:block>

              <xsl:for-each select="roles/role[@include='true']">
                <fo:block font-weight="bold"
                          space-after="{$sectionSubSubContentBottomPadding}">
                  <xsl:call-template name="justifyLineTemplate">
                    <xsl:with-param name="leftContent" select="title"/>
                    <xsl:with-param name="leftAttributes">
                      font-style="italic"
                    </xsl:with-param>
                    <xsl:with-param name="rightContent" select="timespan"/>
                    <xsl:with-param name="rightAttributes"/>
                  </xsl:call-template>
                </fo:block>

                <fo:block>
                  <xsl:apply-templates select="accomplishments">
                    <xsl:with-param name="bulleted" select="xs:boolean('true')"/>
                  </xsl:apply-templates>
                </fo:block>

                <xsl:choose>
                  <!-- Add empty block for spacing. -->
                  <xsl:when test="position() != last()">
                    <fo:block space-after="{$sectionSubContentBottomPadding}"/>
                  </xsl:when>
                </xsl:choose>
              </xsl:for-each>

              <xsl:choose>
                <!-- Add empty block for spacing. -->
                <xsl:when test="position() != last()">
                  <fo:block space-after="{$sectionSubContentBottomPadding}"/>
                </xsl:when>
              </xsl:choose>
            </fo:block>
          </xsl:for-each>
        </fo:block>
      </fo:table-cell>
    </fo:table-row>
  </xsl:template>


  <xsl:template match="accomplishments">
    <xsl:param name="bulleted" />

    <xsl:if test="*">
      <fo:list-block provisional-distance-between-starts="5mm"
                     provisional-label-separation="15mm">
        <xsl:for-each select="accomplishment[@include='true']">
          <xsl:call-template name="listItemTemplate">
            <xsl:with-param name="bulleted" select="$bulleted"/>
          </xsl:call-template>
        </xsl:for-each>
      </fo:list-block>
    </xsl:if>
  </xsl:template>

  <xsl:template name="listItemTemplate">
    <xsl:param name="bulleted"/>

    <fo:list-item space-after="{$listSpacing}">
      <fo:list-item-label end-indent="label-end()">
        <fo:block>
          <xsl:if test="$bulleted">
            <fo:inline>
              <xsl:value-of select="$bulletSymbol"/>
            </fo:inline>
          </xsl:if>
        </fo:block>
      </fo:list-item-label>
      <fo:list-item-body start-indent="body-start()">
        <fo:block>
          <xsl:apply-templates/>
        </fo:block>
      </fo:list-item-body>
    </fo:list-item>
  </xsl:template>

  <xsl:template name="justifyLineTemplate">
    <xsl:param name="leftAttributes"/>
    <xsl:param name="leftContent"/>
    <xsl:param name="rightAttributes"/>
    <xsl:param name="rightContent"/>

    <fo:block text-align-last="justify">
      <fo:inline>
        <!-- Dynamically add any attributes to this fo:inline element. -->
        <xsl:for-each select="tokenize($leftAttributes, ',')">
          <xsl:variable name="attrName" select="substring-before(., '=')"/>
          <xsl:variable name="attrValue" select="translate(translate(substring-after(., '='), &quot;'&quot;, ''), '&quot;', '')"/>

          <xsl:attribute name="{$attrName}">
            <xsl:value-of select="$attrValue"/>
          </xsl:attribute>
        </xsl:for-each>

        <xsl:apply-templates select="$leftContent"/>
      </fo:inline>

      <!-- Fill middle of line with spaces. -->
      <fo:leader leader-pattern="space" />

      <fo:inline>
        <!-- Dynamically add any attributes to this fo:inline element. -->
        <xsl:for-each select="tokenize($rightAttributes, ',')">
          <xsl:variable name="attrName" select="substring-before(., '=')"/>
          <xsl:variable name="attrValue" select="translate(translate(substring-after(., '='), &quot;'&quot;, ''), '&quot;', '')"/>

          <xsl:attribute name="{$attrName}">
            <xsl:value-of select="$attrValue"/>
          </xsl:attribute>
        </xsl:for-each>

        <xsl:apply-templates select="$rightContent"/>
      </fo:inline>
    </fo:block>
  </xsl:template>

  <xsl:template match="a">
    <fo:basic-link external-destination="url('{@href}')"
                   color="blue">
      <xsl:apply-templates/>
    </fo:basic-link>
  </xsl:template>

  <xsl:template match="b">
    <fo:inline font-weight="bold">
      <xsl:apply-templates/>
    </fo:inline>
  </xsl:template>

  <xsl:template match="i">
    <fo:inline font-style="italic">
      <xsl:apply-templates/>
    </fo:inline>
  </xsl:template>

  <xsl:template match="u">
    <fo:inline text-decoration="underline">
      <xsl:apply-templates/>
    </fo:inline>
  </xsl:template>

  <xsl:template match="code">
    <fo:inline font-family="monospace">
      <xsl:apply-templates/>
    </fo:inline>
  </xsl:template>

  <xsl:template match="br">
    <fo:block></fo:block>
  </xsl:template>
</xsl:stylesheet>
