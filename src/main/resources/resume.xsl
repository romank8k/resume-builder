<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.1"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:fop="http://www.w3.org/1999/XSL/Format"
                exclude-result-prefixes="fo">
  <xsl:output method="xml" version="1.0" omit-xml-declaration="no" indent="yes"/>

  <xsl:param name="showPersonalData" select="'true'"/>

  <!-- Section Header Labels -->
  <xsl:param name="summaryOfQualificationsLabel" select="'Summary of Qualifications'"/>
  <xsl:param name="projectsLabel" select="'Projects'"/>
  <xsl:param name="educationLabel" select="'Education'"/>
  <xsl:param name="awardsLabel" select="'Awards'"/>
  <xsl:param name="relevantCoursesLabel" select="'Relevant Courses'"/>
  <xsl:param name="experienceLabel" select="'Experience'"/>

  <!-- Fonts, Padding, Margins -->
  <xsl:param name="sectionPadding" select="'2mm'"/>

<!--TODO-->
  <!--
    contentBlockTitleFont = FontFactory.getFont("Trebuchet MS", 12, Font.BOLD);
    headerInfoFont = FontFactory.getFont("Trebuchet MS", 12, Font.NORMAL);
    headerNameFont = FontFactory.getFont("Trebuchet MS", 18, Font.BOLD);
    headerHomepageFont = FontFactory.getFont("Trebuchet MS", 12, Font.ITALIC);
    bulletFont = FontFactory.getFont("Georgia", 12, Font.BOLD);
  -->
  <xsl:param name="contentFontFamily" select="'Georgia'"/>
  <xsl:param name="contentFontSize" select="'10pt'"/>


  <xsl:param name="bulletSymbol" select="'&#x2022;'"/>

  <xsl:variable name="lowercase" select="'abcdefghijklmnopqrstuvwxyz'" />
  <xsl:variable name="uppercase" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZ'" />

  <xsl:template match="resume">
    <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
      <fo:layout-master-set>
        <fo:simple-page-master master-name="resume" page-width="215.9mm" page-height="279.4mm"
                               margin-top="10mm" margin-bottom="10mm" margin-left="10mm" margin-right="10mm">
          <fo:region-body/>
          <fo:region-before extent="20mm"/>
        </fo:simple-page-master>
      </fo:layout-master-set>

      <fo:page-sequence master-reference="resume">
        <fo:flow flow-name="xsl-region-body">
          <fo:block font-family="Trebuchet MS" space-after="5mm">
            <fo:block text-align-last="justify" space-after="1mm">
              <fo:inline font-style="normal" font-weight="bold" font-size="18pt">
                <xsl:value-of select="header/name"/>
              </fo:inline>
              <fo:leader leader-pattern="space" /> <!-- Fill line with spaces -->
              <fo:inline font-style="italic" font-weight="normal">
                <xsl:value-of select="header/homepage"/>
              </fo:inline>
            </fo:block>

            <xsl:if test="$showPersonalData = 'true'">
              <fo:block text-align-last="justify">
                <fo:inline font-style="normal" font-weight="normal" font-size="12pt">
                  <xsl:value-of select="header/phone"/>
                </fo:inline>
                <fo:leader leader-pattern="space" /> <!-- Fill line with spaces -->
                <fo:inline font-style="normal" font-weight="normal">
                  <xsl:value-of select="header/address/street"/>
                  <xsl:text>, </xsl:text>
                  <xsl:value-of select="header/address/apartment"/>
                </fo:inline>
              </fo:block>

              <fo:block text-align-last="justify">
                <fo:inline font-style="normal" font-weight="normal" font-size="12pt">
                  <xsl:value-of select="header/email"/>
                </fo:inline>
                <fo:leader leader-pattern="space" /> <!-- Fill line with spaces -->
                <fo:inline font-style="normal" font-weight="normal">
                  <xsl:value-of select="header/address/city"/>
                  <xsl:text>, </xsl:text>
                  <xsl:value-of select="header/address/state"/>
                  <xsl:text> </xsl:text>
                  <xsl:value-of select="header/address/zip"/>
                </fo:inline>
              </fo:block>
            </xsl:if>

            <fo:block>
              <fo:leader leader-pattern="rule" leader-length="100%"/>
            </fo:block>
          </fo:block>

          <fo:block font-family="Georgia" font-size="10pt" border-width="1pt" border-color="red">
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

  <!-- Education -->
  <xsl:template match="education[@include='true']">
    <fo:table-row>
      <fo:table-cell>
        <fo:block font-family="Trebuchet MS" font-size="12pt" font-weight="bold" space-after="5mm">
          <xsl:value-of select="translate($educationLabel, $lowercase, $uppercase)"/>
        </fo:block>
      </fo:table-cell>

      <fo:table-cell>
        <fo:list-block provisional-distance-between-starts="4mm"
                       provisional-label-separation="0mm">
          <xsl:for-each select="institution[@include='true']">
            <fo:list-item space-after="0mm">
              <fo:list-item-label end-indent="label-end()">
                <fo:block>
                </fo:block>
              </fo:list-item-label>
              <fo:list-item-body start-indent="body-start()">
                <fo:block font-family="Georgia" font-size="10pt" font-weight="normal">
                  <fop:inline font-weight="bold">
                    <xsl:value-of select="name"/>
                  </fop:inline>
                  <xsl:text>, </xsl:text>
                  <xsl:value-of select="location"/>
                </fo:block>

                <fo:list-block provisional-distance-between-starts="5mm"
                               provisional-label-separation="15mm"
                               font-size="16pt">

                <xsl:for-each select="detail[not(@include) or @include='true']">
                    <fo:list-item space-after="0mm">
                      <fo:list-item-label end-indent="label-end()">
                        <fo:block></fo:block>
                      </fo:list-item-label>
                      <fo:list-item-body start-indent="body-start()">
                        <fo:block font-family="Georgia" font-size="10pt" font-weight="normal">
                          <xsl:apply-templates/>
                        </fo:block>
                      </fo:list-item-body>
                    </fo:list-item>
                  </xsl:for-each>
                </fo:list-block>
              </fo:list-item-body>
            </fo:list-item>
          </xsl:for-each>
        </fo:list-block>
      </fo:table-cell>
    </fo:table-row>
  </xsl:template>

  <!-- Summary of Qualifications -->
  <xsl:template match="summary_of_qualifications[@include='true']">
    <fo:table-row>
      <!--<xsl:if test="function = 'lead'">-->
          <!--<xsl:attribute name="font-weight">bold</xsl:attribute>-->
      <!--</xsl:if>-->
      <fo:table-cell>
        <fo:block font-family="Trebuchet MS" font-size="12pt" font-weight="bold" space-after="5mm">
          <xsl:value-of select="translate($summaryOfQualificationsLabel, $lowercase, $uppercase)"/>
        </fo:block>
      </fo:table-cell>

      <fo:table-cell>
        <fo:block padding-bottom="{$sectionPadding}">
          <fo:list-block provisional-distance-between-starts="4mm"
                         provisional-label-separation="0mm">
            <xsl:for-each select="qualification[@include='true']">
              <xsl:call-template name="listItemTemplate">
              </xsl:call-template>
            </xsl:for-each>
          </fo:list-block>
        </fo:block>
      </fo:table-cell>
    </fo:table-row>
  </xsl:template>

  <!-- Projects -->
  <xsl:template match="projects[@include='true']">
    <fo:table-row>
      <!--<xsl:if test="function = 'lead'">-->
        <!--<xsl:attribute name="font-weight">bold</xsl:attribute>-->
      <!--</xsl:if>-->

      <fo:table-cell>
        <fo:block font-family="Trebuchet MS" font-size="12pt" font-weight="bold" space-after="5mm">
          <xsl:value-of select="translate($projectsLabel, $lowercase, $uppercase)"/>
        </fo:block>
      </fo:table-cell>

      <fo:table-cell>
        <fo:block padding-bottom="{$sectionPadding}">
          <fo:list-block provisional-distance-between-starts="5mm"
                         provisional-label-separation="15mm"
                         font-size="16pt">
            <xsl:for-each select="project[@include='true']">
              <xsl:call-template name="listItemTemplate">
              </xsl:call-template>
            </xsl:for-each>
          </fo:list-block>
        </fo:block>
      </fo:table-cell>
    </fo:table-row>
  </xsl:template>

  <!-- Experience -->
  <xsl:template match="experience[@include='true']">
    <fo:table-row>
      <fo:table-cell>
        <fo:block font-family="Trebuchet MS" font-size="12pt" font-weight="bold" space-after="5mm">
          <xsl:value-of select="translate($experienceLabel, $lowercase, $uppercase)"/>
        </fo:block>
      </fo:table-cell>

      <fo:table-cell>
        <fo:block padding-bottom="{$sectionPadding}">
          <xsl:for-each select="job[@include='true']">
            <fo:block page-break-inside="avoid" space-after="1mm"> <!-- Avoid splitting jobs across multiple pages -->
              <fo:block text-align-last="justify">
                <fo:inline font-style="normal" font-weight="bold" font-size="10pt">
                  <xsl:value-of select="employer"/>
                </fo:inline>
                <fo:leader leader-pattern="space" /> <!-- Fill line with spaces -->
                <fo:inline font-style="normal" font-weight="normal">
                  <xsl:value-of select="timespan"/>
                </fo:inline>
              </fo:block>

              <fo:block font-style="italic" space-after="1mm">
                <xsl:value-of select="role"/>
              </fo:block>

              <fo:block>
                <xsl:apply-templates select="accomplishments"/>
              </fo:block>
            </fo:block>
          </xsl:for-each>
        </fo:block>
      </fo:table-cell>
    </fo:table-row>
  </xsl:template>

  <!-- Job -->
  <xsl:template match="accomplishments">
    <fo:list-block provisional-distance-between-starts="5mm"
                   provisional-label-separation="15mm"
                   font-size="16pt"
                   background-color="cyan">
      <xsl:for-each select="accomplishment[@include='true']">
        <xsl:call-template name="listItemTemplate"/>
      </xsl:for-each>
    </fo:list-block>
  </xsl:template>


  <xsl:template name="listItemTemplate">
    <fo:list-item space-after="0mm">
      <fo:list-item-label end-indent="label-end()">
        <fo:block font-family="Georgia" font-size="10pt" font-weight="normal">
          <fo:inline>
            <xsl:value-of select="$bulletSymbol"/>
          </fo:inline>
        </fo:block>
      </fo:list-item-label>
      <fo:list-item-body start-indent="body-start()">
        <fo:block font-family="Georgia" font-size="10pt" font-weight="normal">
          <xsl:apply-templates/>
        </fo:block>
      </fo:list-item-body>
    </fo:list-item>
  </xsl:template>

  <xsl:template match="b">
    <fo:inline font-weight="bold">
      <xsl:value-of select="."/>
    </fo:inline>
  </xsl:template>

  <xsl:template match="project">
    <xsl:value-of select="."/>
  </xsl:template>

  <xsl:template match="qualification">
    <xsl:value-of select="."/>
  </xsl:template>
</xsl:stylesheet>