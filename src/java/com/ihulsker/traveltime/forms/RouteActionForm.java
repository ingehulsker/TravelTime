/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ihulsker.traveltime.forms;

import com.ihulsker.traveltime.model.RouteProperties;
import java.util.Date;
import java.util.List;
import org.apache.struts.action.ActionForm;

/**
 *
 * @author Inge
 */
public class RouteActionForm extends ActionForm
{
    private Date currentDisplayedTimestamp;
    private List<RouteProperties> routeInfoList;
    /** value of button to move to earlier or later data **/
    private String moveInTime;
    /** the forward button is disabled if the most recent data is shown **/
    private boolean forwardDisabled = true;
    /** the back button is disabled if there is no earlier data available **/
    private boolean backDisabled = true;
    
    public void setCurrentDisplayedTimestamp(Date currentDisplayedTimestamp)
    {
        this.currentDisplayedTimestamp = currentDisplayedTimestamp;
    }

    public Date getCurrentDisplayedTimestamp()
    {
        return currentDisplayedTimestamp;
    }
    
    public void setRouteInfoList(List<RouteProperties> routeInfoList)
    {
        this.routeInfoList = routeInfoList;
    }

    public List<RouteProperties> getRouteInfoList()
    {
        return routeInfoList;
    }

    public void setMoveInTime(String moveInTime)
    {
        this.moveInTime = moveInTime;
    }

    public String getMoveInTime()
    {
        return moveInTime;
    }

    public void setForwardDisabled(boolean forwardDisabled)
    {
        this.forwardDisabled = forwardDisabled;
    }

    public boolean isForwardDisabled()
    {
        return forwardDisabled;
    }

    public void setBackDisabled(boolean backDisabled)
    {
        this.backDisabled = backDisabled;
    }

    public boolean isBackDisabled()
    {
        return backDisabled;
    }
}
