package com.ihulsker.traveltime.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Inge
 */
public class RouteProperties implements Serializable, Comparable<RouteProperties>
{
    private int id;
    private Route route;
    private String location;
    private long travelTime;
    private long travelTimeFf;
    private long velocity;
    private String color;
    private Date timestamp;
    private long length;
    private String dssObjectType;    

    public RouteProperties(int id, String location, long travelTime, long travelTimeFf, 
            long velocity, String color, Date timestamp, long length, String dssObjectType)
    {
        this.id = id;
        this.location = location;
        this.travelTime = travelTime;
        this.travelTimeFf = travelTimeFf;
        this.velocity = velocity;
        this.color = color;
        this.timestamp = timestamp;
        this.length = length;
        this.dssObjectType = dssObjectType;        
    }
    
    public RouteProperties(String location, long travelTime, long travelTimeFf, 
            long velocity, String color, Date timestamp, long length, String dssObjectType)
    {
        this.location = location;
        this.travelTime = travelTime;
        this.travelTimeFf = travelTimeFf;
        this.velocity = velocity;
        this.color = color;
        this.timestamp = timestamp;
        this.length = length;
        this.dssObjectType = dssObjectType;        
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    public void setRoute(Route route)
    {
        this.route = route;
    }

    public Route getRoute()
    {
        return route;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getLocation()
    {
        return location;
    }
    
    public String getRouteNumber()
    {
        String routeNumber = "onbekend";
        if (this.location != null && this.location.contains("Route"))
        {
            int index = this.location.indexOf("Route") + "Route".length();
            routeNumber = this.location.substring(index);
        }
        return routeNumber;
    }

    public void setTravelTime(long travelTime)
    {
        this.travelTime = travelTime;
    }

    public long getTravelTime()
    {
        return travelTime;
    }
    
    public String getTravelTimeFormatted()
    {
        return formatSecondsToTime(this.travelTime, false);
    }
    
    public void setTravelTimeFf(long travelTimeFf)
    {
        this.travelTimeFf = travelTimeFf;
    }

    public long getTravelTimeFf()
    {
        return travelTimeFf;
    }
    
    public String getTravelTimeFfFormatted()
    {
        return formatSecondsToTime(this.travelTimeFf, false);
    }

    public String getTravelTimeDifference()
    {
        return formatSecondsToTime(this.travelTime - this.travelTimeFf, true);
    }
    
    public void setVelocity(long velocity)
    {
        this.velocity = velocity;
    }

    public long getVelocity()
    {
        return velocity;
    }

    public void setColor(String color)
    {
        this.color = color;
    }

    public String getColor()
    {
        return color;
    }

    public void setTimestamp(Date timestamp)
    {
        this.timestamp = timestamp;
    }

    public Date getTimestamp()
    {
        return timestamp;
    }

    public void setLength(long length)
    {
        this.length = length;
    }

    public long getLength()
    {
        return length;
    }

    public void setDssObjectType(String dssObjectType)
    {
        this.dssObjectType = dssObjectType;
    }

    public String getDssObjectType()
    {
        return dssObjectType;
    }
    
    private String formatSecondsToTime(long time, boolean negativeAllowed)
    {
        String formattedTime = "onbekend";
        boolean addMinus = false;
        if (negativeAllowed && time < 0)
        {
            time = Math.abs(time);
            addMinus = true;
        }
        if (time > 0)
        {
            long minutes = time / 60;
            int m = (int) Math.floor(minutes);
            int s = (int)time - (m * 60);
            formattedTime = (addMinus ? "-" : "") + (m < 10 ? "0" : "") + m + ":" + (s < 10 ? "0" : "") + s;
        }
        return formattedTime;
    }

    @Override
    public String toString()
    {
        return "[location: " + this.location + ",travelTime: " + this.travelTime
                + ",travelTimeFF: " + this.travelTimeFf
                + ",velocity: " + this.velocity + ",color: " + this.color
                + ",timestamp: " + this.timestamp + ",length: " + this.length
                + ",dssObjectType: " + this.dssObjectType + "]";
    }

    @Override
    public int compareTo(RouteProperties o)
    {
        if (o.getRoute() == null || this.route == null)
        {
            return 0;
        }
        else
        {
            if (o.getRoute().getRouteId() == null || this.route.getRouteId() == null)
            {
                return 0;
            }
            else
            {
                return this.route.getRouteId().compareTo(o.getRoute().getRouteId());
            }
        }
    }
}
