package com.ihulsker.traveltime.model;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Inge
 */
public class Route implements Serializable
{
    private int id;
    private String routeId;
    private String routeType;
    private RouteProperties featureProperties;
    private List<Coordinate> coordinates;
    
    public Route(int id, String routeId, String routeType)
    {
        this.id = id;
        this.routeId = routeId;
        this.routeType = routeType;
    }
    
    public Route(String routeId, String routeType)
    {
        this.routeId = routeId;
        this.routeType = routeType;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }
    
    public void setRouteId(String routeId)
    {
        this.routeId = routeId;
    }

    public String getRouteId()
    {
        return routeId;
    }

    public void setRouteType(String routeType)
    {
        this.routeType = routeType;
    }

    public String getRouteType()
    {
        return routeType;
    }

    public void setFeatureProperties(RouteProperties featureProperties)
    {
        this.featureProperties = featureProperties;
    }

    public RouteProperties getFeatureProperties()
    {
        return featureProperties;
    }

    public void setCoordinates(List<Coordinate> coordinates)
    {
        this.coordinates = coordinates;
    }

    public List<Coordinate> getCoordinates()
    {
        return coordinates;
    }

    @Override
    public String toString()
    {
        return "[routeId: " + this.routeId + ",routeType: " + this.routeType + 
                ",featureProperties: " + this.featureProperties + ",coordinates: " + this.coordinates + "]";
    }
}
