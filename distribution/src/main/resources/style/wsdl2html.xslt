<?xml version="1.0" encoding="UTF-8"?><xsl:stylesheet version="2.0"  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">  <xsl:template match="/">    <html>      <head>        <title>WSDLDiffGenerator</title>        <link rel="stylesheet" type="text/css" href="web/a.css" />        <link href="web/jquery.treeview.css" rel="stylesheet" />        <script type="text/javascript" src="web/jquery.js" />        <script type="text/javascript" src="web/jquery.treeview.js" />        <script type="text/javascript">          $(document).ready(function(){            $("#diffs").treeview();          });        </script>      </head>      <body>        <h1>WSDL DiffGenerator Result</h1>        <h2>Compared the following documents:</h2>        <div>          <table class="s1" border="1">            <thead>              <tr>                <th>Documnet</th>                <th>TargetNamespace</th>                <th>URL</th>              </tr>            </thead>            <tbody>              <tr>                <td>WSDL-a</td>                <td>                  <xsl:value-of select="/WSDLDiff/WSDL-a/TargetNamespace" />                </td>                <td>                  <a href="{/WSDLDiff/WSDL-a/URL}">                    <xsl:value-of select="/WSDLDiff/WSDL-a/URL" />                  </a>                </td>              </tr>              <tr>                <td>WSDL-b</td>                <td>                  <xsl:value-of select="/WSDLDiff/WSDL-b/TargetNamespace" />                </td>                <td>                  <a href="{/WSDLDiff/WSDL-b/URL}">                    <xsl:value-of select="/WSDLDiff/WSDL-b/URL" />                  </a>                </td>              </tr>            </tbody>          </table>          <xsl:apply-templates select="/WSDLDiff/Diffs" />        </div>      </body>    </html>  </xsl:template>  <xsl:template match="Diffs">    <div>      <h2>Differences in WSDLs:</h2>      <xsl:if test="//@breaks = 'true'">        <div class="notice">          <img src="web/images/lightning.png"></img>          <span>The changes in this definitions invalidate the            interface.</span>        </div>      </xsl:if>      <div>        <ul id="diffs" class="treeview">          <xsl:apply-templates select="Diff" />        </ul>      </div>    </div>    <div>      <ul>        <li>          <img src="web/images/add.png"/>          Indicates that this Element has been added to the definition.        </li>        <li>          <img src="web/images/remove.png"/>          Indicates that this Element has been removed from the definition.        </li>        <li>          <img src="web/images/tick.png"/>          Indicates that the change will not influence the interface.        </li>        <li>          <img src="web/images/lightning.png"/>          Indicates that the change will invalidate the interface.        </li>      </ul>    </div>  </xsl:template>  <xsl:template match="Diff">    <ul>      <li>        <xsl:variable name="class">          <xsl:choose>            <xsl:when test="contains(Description, 'added')">add</xsl:when>            <xsl:when test="contains(Description, 'removed')">remove</xsl:when>            <xsl:when test="@safe = 'true'">safe</xsl:when>            <xsl:when test="@breaks = 'true'">breaks</xsl:when>            <xsl:otherwise></xsl:otherwise>          </xsl:choose>        </xsl:variable>        <span class="{$class}"><xsl:value-of select="Description" /></span>        <xsl:apply-templates select="Diff" />      </li>    </ul>  </xsl:template></xsl:stylesheet>