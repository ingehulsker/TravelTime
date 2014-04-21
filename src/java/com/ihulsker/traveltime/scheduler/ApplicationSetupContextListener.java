package com.ihulsker.traveltime.scheduler;

import java.util.Date;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 * @author Inge
 */
public class ApplicationSetupContextListener implements ServletContextListener
{
    private Scheduler scheduler;

    @Override
    public void contextInitialized(ServletContextEvent sce)
    {
        try
        {
            setupScheduler();
            System.out.println("Scheduler started");
        }
        catch (SchedulerException sx)
        {
            System.out.println("Could not instantiate scheduler");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce)
    {
        try
        {
            shutdownScheduler();
            System.out.println("Scheduler shutdown complete");
        }
        catch (Throwable t)
        {
            System.out.println("Could not shutdown scheduler");
        }
    }

    private void scheduleJob(Class clazz, String jobName, String triggerName, String groupName, Date startAt, Date endAt, int repeatInMinutes)
    {
        try
        {
            JobBuilder jobBuilder = JobBuilder.newJob(clazz);
            jobBuilder.withIdentity(jobName, groupName);
            JobDetail job = jobBuilder.build();
            TriggerBuilder tb = TriggerBuilder.newTrigger().withIdentity(triggerName, groupName);
            tb.startAt(startAt);
            tb.endAt(endAt);
            SimpleScheduleBuilder ssb = SimpleScheduleBuilder.simpleSchedule().repeatForever();
            ssb.withIntervalInMinutes(repeatInMinutes);
            tb.withSchedule(ssb);
            Trigger trigger = tb.build();

            scheduler.scheduleJob(job, trigger);
            System.out.println(jobName + " scheduled. startAt: " + startAt);
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    private void setupScheduler() throws SchedulerException
    {
        if (scheduler == null)
        {
            SchedulerFactory sf = new StdSchedulerFactory();

            scheduler = sf.getScheduler();

            //start reportNotificationSchedule and repeat every 5 minutes
            Date scheduleDate = new Date();
            int repeatInMinutes = 5;
            scheduleJob(RouteInfoUpdateJob.class, "routeInfoUpdateJob", "routeInfoUpdateTrigger", "routeInfoUpdateGroup", scheduleDate, null, repeatInMinutes);
            
            scheduler.startDelayed(5);
        }
        else
        {
            System.out.println("contextInitialized called but scheduler was already initialized");
        }
    }

    private void shutdownScheduler()
    {
        try
        {
            System.out.println(scheduler.getCurrentlyExecutingJobs().size() + " scheduled jobs running.");
            scheduler.shutdown(true);
            Thread.sleep(1000);
        }
        catch (SchedulerException ex)
        {
            System.out.println("Scheduler shutdown exception");
        }
        catch (InterruptedException ix)
        {
            System.out.println("Scheduler shutdown interrupted");
        }
    }
}
