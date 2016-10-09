package com.canmeizhexue.common.entity;

/**EventBus事件参数
 * Created by silence on 2016-10-9.
 */
public class EventBusParams {
    public final String EVENT_BUS_TYPE;
    public Object eventBusParam;


    public EventBusParams(String EVENT_BUS_TYPE, Object eventBusParam ) {
        this.EVENT_BUS_TYPE = EVENT_BUS_TYPE;
        this.eventBusParam = eventBusParam;
    }
}
