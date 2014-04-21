package com.ihulsker.traveltime.controller;

import com.ihulsker.traveltime.dataprocessing.RouteInfoDao;
import com.ihulsker.traveltime.dataprocessing.exception.DataAccessException;
import com.ihulsker.traveltime.model.RouteProperties;
import com.ihulsker.traveltime.webservice.WSTravelTime;
import com.ihulsker.traveltime.webservice.exception.WebServiceException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Inge
 */
public class Controller
{
    /**
     * get newest routeInfo from webservice and store it in database
     * @return a list of the newest routeProperties
     */
    public List<RouteProperties> updateRouteInfo()
    {
        List<RouteProperties> routes = new ArrayList<RouteProperties>();
        WSTravelTime wsTravelTime = new WSTravelTime();
        RouteInfoDao dao = new RouteInfoDao();
        
        try
        {
            routes = wsTravelTime.getRouteInfo();
            dao.saveRouteInfo(routes);
        }
        catch (WebServiceException mcx)
        {            
            mcx.printStackTrace();
        } 
        catch (DataAccessException dax)
        {
            dax.printStackTrace();
        }
        return routes;
    }
    
    /**
     * get routeInfo from database with timestamp between specified start and end
     * @param startDate
     * @param endDate
     * @return a list of RouteProperties
     */
    public List<RouteProperties> getRouteInfo(Date startDate, Date endDate)
    {
        List<RouteProperties> routes = new ArrayList<RouteProperties>();
        RouteInfoDao dao = new RouteInfoDao();
        
        try
        {
            routes = dao.getRouteInfo(startDate, endDate);
        }
        catch (DataAccessException dax)
        {
            dax.printStackTrace();
        }
        
        return routes;
    }
    
    public boolean earlierDataAvailable(Date currentDate)
    {
        boolean earlierDataAvailable = true;
        
        RouteInfoDao dao = new RouteInfoDao();
        
        try
        {
            earlierDataAvailable = dao.earlierDataAvailable(new Date(currentDate.getTime() - (3 * 60 * 1000)));
        }
        catch (DataAccessException dax)
        {
            dax.printStackTrace();
        }
        
        return earlierDataAvailable;
    }
    
    public boolean laterDataAvailable(Date currentDate)
    {
        boolean laterDataAvailable = true;
        
        RouteInfoDao dao = new RouteInfoDao();
        
        try
        {
            laterDataAvailable = dao.laterDataAvailable(new Date(currentDate.getTime() + (3 * 60 * 1000)));
        }
        catch (DataAccessException dax)
        {
            dax.printStackTrace();
        }
        
        return laterDataAvailable;
    }
}
