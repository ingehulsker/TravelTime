<%-- 
    Document   : routeinfo
    Created on : 17-apr-2014, 23:21:00
    Author     : Inge
--%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Actuele reistijden informatie</title>
        <script type="text/javascript">
            function scheduleRefresh()
            {
                window.setTimeout(refresh, 60000);
            }
            
            function refresh()
            {
                location.href = '<%=request.getContextPath()%>/routeInfo.do?useraction=displayRouteInfo';
            }
        </script>
    </head>
    <body onload="scheduleRefresh();" style="font-family: Verdana, sans-serif; background-color: #dcf0f8;">
        <div style="text-align: center; width: 100%;">
            <table style="text-align: center; width: 100%; border: 2px solid #04597e;">
                <tr>
                    <td style="text-align: center; width: 200px;"><html:img src="/car.png" alt="auto" hspace="10" vspace="10" /></td>
                    <td colspan="6" style="text-align: center;"><h1>Actuele reistijden informatie</h1></td>
                    <td></td>
                </tr>
                
                <tr><td colspan="8" style="text-align: center; color: red;"><html:errors /></td></tr>

                <bean:define id="forwardDisabled" name="RouteActionForm" property="forwardDisabled" />
                <bean:define id="backDisabled" name="RouteActionForm" property="backDisabled" />
                <tr>
                    <td style="text-align: right; text-decoration: none;"><a href="<%=request.getContextPath()%>/routeInfo.do?useraction=displayRouteInfo&moveInTime=vroeger"><html:button property="moveInTime" value="vroeger" disabled="${backDisabled}" /></a></td>
                    <td colspan="6" style="text-align: center;"><h2><bean:write name="RouteActionForm" property="currentDisplayedTimestamp" format="dd-MM-yyyy HH:mm" /></h2></td>
                    <td style="text-align: left; text-decoration: none;"><a href="<%=request.getContextPath()%>/routeInfo.do?useraction=displayRouteInfo&moveInTime=later"><html:button property="moveInTime" value="later" disabled="${forwardDisabled}" /></a></td>
                </tr>

                <tr style="font-weight: bold;">
                    <td></td>
                    <td style="width: 150px; background-color: #69c7ef;">Traject<br>&nbsp;</td>
                    <td style="width: 180px; background-color: #69c7ef;">Actuele reistijd<br>(min:sec)</td>
                    <td style="width: 180px; background-color: #69c7ef;">Normale reistijd<br>(min:sec)</td>
                    <td style="width: 150px; background-color: #69c7ef;">Vertraging<br>(min:sec)</td>
                    <td style="width: 150px; background-color: #69c7ef;">Huidige snelheid<br>(km/u)</td>
                    <td style="width: 150px; background-color: #69c7ef;">Lengte<br>(m)</td>
                    <td></td>
                </tr>

                <logic:empty name="RouteActionForm" property="routeInfoList">
                    <tr style="font-weight: bold;">
                        <td></td>
                        <td colspan="6" style="width: 150px; background-color: #ffffff;"><br>Informatie wordt opgehaald...<br>&nbsp;</td>
                        <td></td>
                    </tr>
                </logic:empty>
                <logic:notEmpty name="RouteActionForm" property="routeInfoList">
                    <logic:iterate id="routeInfo" name="RouteActionForm" property="routeInfoList" indexId="index">
                        <bean:define id="backgroundColor" value="#dcf0f8" />
                        <bean:define id="modulus" value="${index%2}" />
                        <logic:notEqual name="modulus" value="0">
                            <bean:define id="backgroundColor" value="#ffffff" />
                        </logic:notEqual>                    
                        <bean:define id="color" name="routeInfo" property="color" />
                        <tr>
                            <td></td>
                            <td style="background-color: ${backgroundColor};"><bean:write name="routeInfo" property="routeNumber" /></td>
                            <td style="background-color: ${backgroundColor};"><bean:write name="routeInfo" property="travelTimeFormatted" /></td>
                            <td style="background-color: ${backgroundColor};"><bean:write name="routeInfo" property="travelTimeFfFormatted" /></td>
                            <td style="background-color: ${backgroundColor};"><p style="color: ${color};"><bean:write name="routeInfo" property="travelTimeDifference" /></p></td>
                            <td style="background-color: ${backgroundColor};">
                                <logic:equal name="routeInfo" property="velocity" value="-1">
                                    onbekend
                                </logic:equal>
                                <logic:notEqual name="routeInfo" property="velocity" value="-1">
                                    <bean:write name="routeInfo" property="velocity" format="#.#" />
                                </logic:notEqual>
                            </td>
                            <td style="background-color: ${backgroundColor};"><bean:write name="routeInfo" property="length" format="#.#" /></td>
                            <td></td>
                        </tr>
                    </logic:iterate>
                </logic:notEmpty>
            </table>
        </div>
    </body>
</html>
