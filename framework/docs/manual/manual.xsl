<?xml version='1.0'?> 

<xsl:stylesheet  
       xmlns:xsl="http://www.w3.org/1999/XSL/Transform"  version="1.0"> 
	
  	<!-- This file is copied into the docbook directory. -->
	<xsl:import href="html/chunk.xsl"/> 

	<xsl:param name="html.stylesheet" select="'manual.css'"/> 

	<xsl:template name="user.header.navigation">
		<div class="header">Gravity - Manual</div>
		<div class="shadow"/>
	</xsl:template>

	<xsl:template name="user.footer.navigation">
		<div class="footer">
			<table class="table">
			<tr>
				<td><a href="http://gravity.dev.java.net/">http://gravity.dev.java.net/</a></td>
				<td class="right">DocBook styles borrowed from dynaop</td>
			</tr>
			<tr>
				<td><a href="http://myworkspace.sf.net/">http://myworkspace.sf.net/</a></td>
				<td class="right"><a href="http://dynaop.dev.java.net/">http://dynaop.dev.java.net/</a></td>
			</tr>
			</table>
		</div>
	</xsl:template>

</xsl:stylesheet>
