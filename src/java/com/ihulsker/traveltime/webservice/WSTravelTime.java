package com.ihulsker.traveltime.webservice;

import com.ihulsker.traveltime.model.Coordinate;
import com.ihulsker.traveltime.model.RouteProperties;
import com.ihulsker.traveltime.model.Route;
import com.ihulsker.traveltime.webservice.exception.WebServiceException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Inge
 */
public class WSTravelTime 
{
    private static final String URL = "http://www.trafficlink-online.nl/trafficlinkdata/wegdata/TrajectSensorsNH.GeoJSON";
    private static final String DATETIMEFORMAT = "dd-MM-yyyy HH:mm:ss";
    private SimpleDateFormat format = new SimpleDateFormat(DATETIMEFORMAT);
        
    public List<RouteProperties> getRouteInfo() throws WebServiceException
    {
        List<RouteProperties> routeInfoList = new ArrayList<RouteProperties>();
        JSONObject arr;
        JSONObject featureObject;
        JSONParser parser = new JSONParser();
        RouteProperties routeProperties;
        Route route;
        HttpClient httpClient = new HttpClient();
        try
        {
            GetMethod post = new GetMethod(URL);
            int returnCode = httpClient.executeMethod(post);
            if (returnCode == 200)
            {
                String returnText = post.getResponseBodyAsString();

                if (returnText != null && returnText.length() > 0)
                {
                    Object object = parser.parse(returnText);
                    arr = (JSONObject) object;

//                    String type = (String) arr.get("type");
                    JSONArray jsonArray = (JSONArray) arr.get("features");
                    Iterator iterator = jsonArray.iterator();
                    while(iterator.hasNext())
                    {
                        object = iterator.next();
                        featureObject = (JSONObject) object;

                        String featureId = (String) featureObject.get("id");
                        String featureType = (String)featureObject.get("type");

                        JSONObject properties = (JSONObject) featureObject.get("properties");
                        routeProperties = parseProperties(properties);

                        JSONObject geoObject = (JSONObject) featureObject.get("geometry");
                        JSONArray coordinatesArray = (JSONArray) geoObject.get("coordinates");
                        List<Coordinate> coordinates = parseCoordinates(coordinatesArray);

                        route = new Route(featureId, featureType);
                        route.setCoordinates(coordinates);
                        routeProperties.setRoute(route);
                        
                        routeInfoList.add(routeProperties);
                    }
                    
                    if (!routeInfoList.isEmpty())
                    {
                        Collections.sort(routeInfoList);
                    }
                }
                else
                {
                    throw new WebServiceException("Webserver returned empty result");
                }
            }
            else
            {
                throw new WebServiceException("Could not connect to webserver");
            }
        }
        catch (IOException iox)
        {
            throw new WebServiceException(iox);
        }
        catch (ParseException px)
        {
            throw new WebServiceException(px);
        }
        catch (java.text.ParseException px)
        {
            throw new WebServiceException(px);
        }
        return routeInfoList;
    }
    
    private List<Coordinate> parseCoordinates(JSONArray coordinatesArray)
    {
        List<Coordinate> coordinates = new ArrayList<Coordinate>();
        
        Iterator iterator = coordinatesArray.iterator();
        while (iterator.hasNext())
        {
            Object object = iterator.next();
            JSONArray coordinateArray = (JSONArray) object;
            long x = (long) coordinateArray.get(0);
            long y = (long) coordinateArray.get(1);
            Coordinate coordinate = new Coordinate(x, y);
            coordinates.add(coordinate);
        }
        
        return coordinates;
    }
    
    private RouteProperties parseProperties(JSONObject properties) throws java.text.ParseException
    {
        String location = (String) properties.get("LOCATION");
        Long travelTimeFf = (long) properties.get("TRAVELTIME_FF");
        Long velocity = (long) properties.get("VELOCITY");
        String color = (String) properties.get("COLOR");
        String timestamp = (String) properties.get("TIMESTAMP");
        long length = (long) properties.get("LENGTH");
        String dssObjectType = (String) properties.get("DSSObjectType");
        long travelTime = (long) properties.get("TRAVELTIME");
        return new RouteProperties(location, travelTime, travelTimeFf, velocity, color, format.parse(timestamp), length, dssObjectType);
    }
}
