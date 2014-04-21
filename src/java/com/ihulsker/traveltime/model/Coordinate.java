package com.ihulsker.traveltime.model;

/**
 *
 * @author Inge
 */
public class Coordinate 
{
    private int id;
    private Route route;
    private long xcoordinate;
    private long ycoordinate;
    
    public Coordinate(long xcoordinate, long ycoordinate)
    {
        this.xcoordinate = xcoordinate;
        this.ycoordinate = ycoordinate;
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
    
    public void setXcoordinate(long xcoordinate) 
    {
        this.xcoordinate = xcoordinate;
    }

    public long getXcoordinate() 
    {
        return xcoordinate;
    }

    public void setYcoordinate(long ycoordinate) 
    {
        this.ycoordinate = ycoordinate;
    }

    public long getYcoordinate()
    {
        return ycoordinate;
    }

    @Override
    public String toString() 
    {
        return "[" + xcoordinate + "," + ycoordinate + "]";
    }   
}
