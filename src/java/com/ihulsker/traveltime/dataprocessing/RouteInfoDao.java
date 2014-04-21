/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ihulsker.traveltime.dataprocessing;

import com.ihulsker.traveltime.dataprocessing.exception.DataAccessException;
import com.ihulsker.traveltime.model.Route;
import com.ihulsker.traveltime.model.RouteProperties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Inge
 */
public class RouteInfoDao
{
    private final String URL = "jdbc:mysql://localhost:3306/";
    private final String DATABASE_NAME = "traveltime";
    private final String DATABASE_DRIVER = "com.mysql.jdbc.Driver";
    private final String USERNAME = "traveler"; 
    private final String PASSWORD = "t1metrave!";
    
    public List<RouteProperties> getRouteInfo(Date startDate, Date endDate) throws DataAccessException
    {
        Connection conn = null;
        PreparedStatement statement = null;
        PreparedStatement statement2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        Route route;
        RouteProperties routeProperties;
        List<RouteProperties> routePropertiesList = new ArrayList<RouteProperties>();
        
        try 
        {
            Class.forName(DATABASE_DRIVER).newInstance();
            conn = DriverManager.getConnection(URL+DATABASE_NAME, USERNAME, PASSWORD);

            statement = conn.prepareStatement("SELECT id, routeid, routetype FROM route ORDER BY routeid ASC");
            
            statement2 = conn.prepareStatement("SELECT id,location,traveltime,traveltimeff,"
                    + "velocity,color,timestamp,length,dssobjecttype FROM routeproperties "
                    + "WHERE routeid=? AND timestamp>? AND timestamp<?");
            
            rs = statement.executeQuery();            
            while (rs.next()) 
            {
                int id = rs.getInt(1);
                String routeId = rs.getString(2);
                String routeType = rs.getString(3);
                route = new Route(id, routeId, routeType);
                
                statement2.setInt(1, id);
                statement2.setTimestamp(2, new Timestamp(startDate.getTime()));
                statement2.setTimestamp(3, new Timestamp(endDate.getTime()));
                rs2 = statement2.executeQuery();
                while (rs2.next())
                {
                    routeProperties = new RouteProperties(rs2.getInt(1), rs2.getString(2), rs2.getLong(3), rs2.getLong(4), 
                            rs2.getLong(5), rs2.getString(6), rs2.getTimestamp(7), rs2.getLong(8), rs2.getString(9));
                    routeProperties.setRoute(route);
                    routePropertiesList.add(routeProperties);
                }
            }
        } 
        catch (ClassNotFoundException cnfx)
        {
            throw new DataAccessException(cnfx);
        }
        catch (InstantiationException ix)
        {
            throw new DataAccessException(ix);
        }
        catch (IllegalAccessException iax)
        {
            throw new DataAccessException(iax);
        }
        catch (SQLException sx) 
        {
            throw new DataAccessException(sx);
        }
        finally
        {
            try
            {
                if (conn != null)
                {
                    conn.close();
                }
                if (statement != null)
                {
                    statement.close();
                }
                if (statement2 != null)
                {
                    statement2.close();
                }
                if (rs != null)
                {
                    rs.close();
                }
                if (rs2 != null)
                {
                    rs2.close();
                }
            }
            catch (SQLException sx)
            {
                System.out.println("Could not close all connections and statements");
            }
        }
        return routePropertiesList;   
    }
    
    public boolean saveRouteInfo(List<RouteProperties> routePropertiesList) throws DataAccessException
    {
        boolean success = false;
        Connection conn = null;
        PreparedStatement statement = null;
        PreparedStatement statement2 = null;
        PreparedStatement statement3 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        Route route;
        
        try 
        {
            Class.forName(DATABASE_DRIVER).newInstance();
            conn = DriverManager.getConnection(URL+DATABASE_NAME, USERNAME, PASSWORD);

            statement = conn.prepareStatement("INSERT INTO route(routeid, routetype) values(?,?)", Statement.RETURN_GENERATED_KEYS);
            
            statement2 = conn.prepareStatement("SELECT id FROM route WHERE routeid=?");
            
            statement3 = conn.prepareStatement("INSERT INTO routeproperties(routeid,location,traveltime,"
                    + "traveltimeff,velocity,color,timestamp,length,dssobjecttype) "
                    + "values(?,?,?,?,?,?,?,?,?)");
            
            for (RouteProperties routeProperties : routePropertiesList)
            {
                route = routeProperties.getRoute();
                int id = 0;
                
                //check if route exists
                statement2.setString(1, route.getRouteId());
                rs2 = statement2.executeQuery();
                if (rs2.next()) //get route's database id
                {
                    id = rs2.getInt(1);
                }
                else //add route to database
                {                    
                    statement.setString(1, route.getRouteId());
                    statement.setString(2, route.getRouteType());
                    statement.executeUpdate();                    
                    rs = statement.getGeneratedKeys();
                    if (rs.next())
                    {
                        id = rs.getInt(1);
                    }
                }
                
                //add routeProperties to database
                statement3.setInt(1, id);
                statement3.setString(2, routeProperties.getLocation());
                statement3.setLong(3, routeProperties.getTravelTime());
                statement3.setLong(4, routeProperties.getTravelTimeFf());
                statement3.setLong(5, routeProperties.getVelocity());
                statement3.setString(6, routeProperties.getColor());
                statement3.setTimestamp(7, new Timestamp(routeProperties.getTimestamp().getTime()));
                statement3.setLong(8, routeProperties.getLength());
                statement3.setString(9, routeProperties.getDssObjectType());
                int saved = statement3.executeUpdate();
                if (saved == 1) { success = true; }
            }
        } 
        catch (ClassNotFoundException cnfx)
        {
            throw new DataAccessException(cnfx);
        }
        catch (InstantiationException ix)
        {
            throw new DataAccessException(ix);
        }
        catch (IllegalAccessException iax)
        {
            throw new DataAccessException(iax);
        }
        catch (SQLException sx) 
        {
            throw new DataAccessException(sx);
        }
        finally
        {
            try
            {
                if (conn != null)
                {
                    conn.close();
                }
                if (statement != null)
                {
                    statement.close();
                }
                if (statement2 != null)
                {
                    statement2.close();
                }
                if (statement3 != null)
                {
                    statement3.close();
                }
                if (rs != null)
                {
                    rs.close();
                }
                if (rs2 != null)
                {
                    rs2.close();
                }
            }
            catch (SQLException sx)
            {
                System.out.println("Could not close all connections and statements");
            }
        }
        return success;
    }
    
    public boolean earlierDataAvailable(Date currentDate) throws DataAccessException
    {
        boolean dataAvailable = true;
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        
        try 
        {
            Class.forName(DATABASE_DRIVER).newInstance();
            conn = DriverManager.getConnection(URL+DATABASE_NAME, USERNAME, PASSWORD);

            statement = conn.prepareStatement("SELECT * FROM routeproperties WHERE timestamp<?");
            statement.setTimestamp(1, new Timestamp(currentDate.getTime()));
            rs = statement.executeQuery();
            if (!rs.next())
            {
                dataAvailable = false;
            }
        }
        catch (ClassNotFoundException cnfx)
        {
            throw new DataAccessException(cnfx);
        }
        catch (InstantiationException ix)
        {
            throw new DataAccessException(ix);
        }
        catch (IllegalAccessException iax)
        {
            throw new DataAccessException(iax);
        }
        catch (SQLException sx) 
        {
            throw new DataAccessException(sx);
        }
        finally
        {
            try
            {
                if (conn != null)
                {
                    conn.close();
                }
                if (statement != null)
                {
                    statement.close();
                }
                if (rs != null)
                {
                    rs.close();
                }
            }
            catch (SQLException sx)
            {
                System.out.println("Could not close all connections and statements");
            }
        }
        return dataAvailable;
    }
    
    public boolean laterDataAvailable(Date currentDate) throws DataAccessException
    {
        boolean dataAvailable = true;
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        
        try 
        {
            Class.forName(DATABASE_DRIVER).newInstance();
            conn = DriverManager.getConnection(URL+DATABASE_NAME, USERNAME, PASSWORD);

            statement = conn.prepareStatement("SELECT * FROM routeproperties WHERE timestamp>?");
            statement.setTimestamp(1, new Timestamp(currentDate.getTime()));
            rs = statement.executeQuery();
            if (!rs.next())
            {
                dataAvailable = false;
            }
        }
        catch (ClassNotFoundException cnfx)
        {
            throw new DataAccessException(cnfx);
        }
        catch (InstantiationException ix)
        {
            throw new DataAccessException(ix);
        }
        catch (IllegalAccessException iax)
        {
            throw new DataAccessException(iax);
        }
        catch (SQLException sx) 
        {
            throw new DataAccessException(sx);
        }
        finally
        {
            try
            {
                if (conn != null)
                {
                    conn.close();
                }
                if (statement != null)
                {
                    statement.close();
                }
                if (rs != null)
                {
                    rs.close();
                }
            }
            catch (SQLException sx)
            {
                System.out.println("Could not close all connections and statements");
            }
        }
        return dataAvailable;
    }
}
