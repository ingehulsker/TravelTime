package com.ihulsker.traveltime.scheduler;

import com.ihulsker.traveltime.controller.Controller;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.JobDetailImpl;

/**
 *
 * @author Inge
 */
public class RouteInfoUpdateJob extends JobDetailImpl implements Job
{
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException
    {
        Controller controller = new Controller();
        controller.updateRouteInfo();
    }
}
