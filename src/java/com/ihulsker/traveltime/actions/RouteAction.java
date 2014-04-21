package com.ihulsker.traveltime.actions;

import com.ihulsker.traveltime.controller.Controller;
import com.ihulsker.traveltime.forms.RouteActionForm;
import com.ihulsker.traveltime.model.RouteProperties;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;

/**
 *
 * @author Inge
 */
public class RouteAction extends DispatchAction
{    
    public ActionForward displayRouteInfo(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
    {
        String nextPage = "success";
        ActionMessages messages = new ActionMessages();
        Controller controller = new Controller();
        Date startTime;
        Date endTime;
        List<RouteProperties> routeInfoList;
        
        RouteActionForm actionForm = (RouteActionForm) form;
        String moveInTime = actionForm.getMoveInTime();
        
        if (actionForm.isForwardDisabled() //displaying most recent data, update data
                || actionForm.getMoveInTime() != null) // moving forward or backwards
        {
            Date currentDisplayTimestamp = actionForm.getCurrentDisplayedTimestamp();

            if (moveInTime == null)
            {
                currentDisplayTimestamp = new Date();
                endTime = currentDisplayTimestamp;
                startTime = new Date(endTime.getTime() - (5 * 60 * 1000));
            }
            else
            {
                if (moveInTime.equals("vroeger"))
                {
                    startTime = new Date(currentDisplayTimestamp.getTime() - 420000); //7 minutes ago
                    endTime = new Date(currentDisplayTimestamp.getTime() - 120000); //2 minutes ago
                    actionForm.setForwardDisabled(false);
                }
                else // moveInTime.equals("later")
                {
                    startTime = new Date(currentDisplayTimestamp.getTime() + 120000); //2 minutes later
                    endTime = new Date(currentDisplayTimestamp.getTime() + 420000); //7 minutes later
                }            
            }        

            try
            {
                routeInfoList = controller.getRouteInfo(startTime, endTime);
                if (routeInfoList.isEmpty() && moveInTime != null) //no data available
                {
                    if (moveInTime.equals("vroeger"))
                    {
                        actionForm.setBackDisabled(true);
                        messages.add("error", new ActionMessage("error.noearlierinfo"));
                    }
                    else
                    {
                        actionForm.setForwardDisabled(true);
                        messages.add("error", new ActionMessage("error.nolaterinfo"));
                    }
                    saveErrors(request, messages);
                }
                else
                {
                    if (!routeInfoList.isEmpty())
                    {
                        actionForm.setRouteInfoList(routeInfoList);
                        RouteProperties firstRouteProperties = routeInfoList.get(0);
                        currentDisplayTimestamp = firstRouteProperties.getTimestamp();
                        actionForm.setCurrentDisplayedTimestamp(currentDisplayTimestamp);                        
                        actionForm.setMoveInTime(null);
                    }
                    actionForm.setBackDisabled(!controller.earlierDataAvailable(currentDisplayTimestamp));
                    actionForm.setForwardDisabled(!controller.laterDataAvailable(currentDisplayTimestamp));                    
                }            
            }
            catch (Exception mcx)
            {            
                messages.add("error", new ActionMessage("error.erroroccurred"));
                saveErrors(request, messages);
                nextPage = "error";
            }
        }
        
        return (mapping.findForward(nextPage));
    }
}
