<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:template match="@* | node()">
    <xsl:copy>
      <xsl:apply-templates select="@* | node()"/>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="entry/node()"/>

  <xsl:template match="entry/field" priority="1">
    <xsl:attribute name="field">
      <xsl:value-of select="text()"/>
    </xsl:attribute>
  </xsl:template>
</xsl:stylesheet>
