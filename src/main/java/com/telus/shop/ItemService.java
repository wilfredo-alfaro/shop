package com.telus.shop;

import com.telus.shop.model.Item;
import com.telus.shop.model.Stock;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(ItemService.class);

    public static final String X_UUID = "X-UUID";

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response list(@QueryParam("reserved") boolean reserved) {
        final Response.ResponseBuilder rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        try {

            final List<Item> items = new ArrayList<Item>();
            for (final Item item : Stock.getInstance().getItems()) {
                if (item.isReserved() == reserved) {
                    items.add(item);
                }
            }

            rb.status(Response.Status.OK);
            rb.entity(items);

        } catch (Exception e) {
            logger.error("failed to get list", e);
        }
        return rb.build();
    }
}
