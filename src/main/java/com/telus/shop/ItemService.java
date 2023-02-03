package com.telus.shop;

import com.telus.shop.model.Item;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.stereotype.Service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Path("items")
public class ItemService {

    public static final String X_UUID = "X-UUID";

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response list(@QueryParam("reserved") boolean reserved) {
        final String uuid = UUID.randomUUID().toString();
        final Response.ResponseBuilder rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR).header(X_UUID, uuid).entity(new ArrayList<Item>());
        ThreadContext.push(uuid);
        try {

        } catch (Exception e) {

        } finally {

        }
        return rb.build();
    }
}
