package com.myspace.demo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


import org.kie.api.runtime.process.WorkItemNotFoundException;
import org.kie.kogito.process.Process;
import org.kie.kogito.process.ProcessInstance;
import org.kie.kogito.process.WorkItem;

@Path("/$name$")
public class $Type$Resource {

    Process<$Type$> process;

    @POST()
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)    
    public $Type$ createResource_$name$($Type$ resource) {
        if (resource == null) {
            resource = new $Type$();
        }

        ProcessInstance<$Type$> pi = process.createInstance(resource);
        pi.start();
        return pi.variables();
    }

    @GET()
    @Produces(MediaType.APPLICATION_JSON)
    public List<$Type$> getResources_$name$() {
        return process.instances().values().stream()
                .map(ProcessInstance::variables)
                .collect(Collectors.toList());
    }

    @GET()
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public $Type$ getResource_$name$(@PathParam("id") Long id) {
        return process.instances()
                .findById(id)
                .map(ProcessInstance::variables)
                .orElse(null);
    }

    @DELETE()
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public $Type$ deleteResource_$name$(@PathParam("id") Long id) {
        ProcessInstance<$Type$> pi = process.instances()
                .findById(id)
                .orElse(null);
        if (pi == null) {
            return null;
        } else {
            pi.abort();
            return pi.variables();
        }
    }
    
    @GET()
    @Path("/{id}/tasks")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<Long, String> getTasks_$name$(@PathParam("id") Long id) {
        return process.instances()
                .findById(id)
                .map(ProcessInstance::workItems)
                .map(l -> l.stream().collect(Collectors.toMap(WorkItem::getId, WorkItem::getName)))
                .orElse(null);
    }
}
