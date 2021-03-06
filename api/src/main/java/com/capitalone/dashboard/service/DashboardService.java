package com.capitalone.dashboard.service;

import com.capitalone.dashboard.model.Component;
import com.capitalone.dashboard.model.Dashboard;
import com.capitalone.dashboard.model.Widget;
import org.bson.types.ObjectId;

import java.util.List;


public interface DashboardService {

    /**
     * Fetches all registered dashboards, sorted by title.
     *
     * @return all dashboards
     */
    Iterable<Dashboard> all();


    /**
     * Fetches a Dashboard.
     *
     * @param id dashboard unique identifier
     * @return Dashboard instance
     */
    Dashboard get(ObjectId id);

    /**
     * Creates a new Dashbaord and saves it to the store.
     *
     * @param dashboard new Dashboard to createCollectorItem
     * @return newly created Dashboard
     */
    Dashboard create(Dashboard dashboard);

    /**
     * Updates an existing dashboard instance.
     *
     * @param dashboard Dashboard to update
     * @return updated Dashboard instance
     */
    Dashboard update(Dashboard dashboard);

    /**
     * Deletes an existing Dashboard instance.
     *
     * @param id unique identifier of Dashboard to delete
     */
    void delete(ObjectId id);

    /**
     * Associate a CollectorItem to a Component
     *
     * @param componentId unique identifier of the Component
     * @param collectorItemIds List of unique identifier of the CollectorItem
     * @return Component
     */
    Component associateCollectorToComponent(ObjectId componentId, List<ObjectId> collectorItemIds);

    /**
     * Creates a new Widget and adds it to the Dashboard indicated by the dashboardId parameter.
     *
     * @param dashboard add widget to this Dashboard
     * @param widget Widget to add
     * @return newly created Widget
     */
    Widget addWidget(Dashboard dashboard, Widget widget);

    /**
     * Find the Widget with the specified id in the Dashbaord provided.
     *
     * @param dashboard Dashboard
     * @param widgetId widget ID
     * @return Widget
     */
    Widget getWidget(Dashboard dashboard, ObjectId widgetId);

    /**
     * Updates an existing Widget.
     *
     * @param dashboard update widget on this Dashboard
     * @param widget Widget to update
     * @return updated widget
     */
    Widget updateWidget(Dashboard dashboard, Widget widget);

    
    /**
     * Gets all dashboard belonging to a userId
     * @Param username
     * @return List of dashboards
     */
    
    List<Dashboard> getOwnedDashboards(String username);
    
    /**
     * Get owner of dashboard on supplying dashboard Name
     * @Param dashboardName
     * @return String username
     * 
     */
    
    String getDashboardOwner(String dashboardName);

}




