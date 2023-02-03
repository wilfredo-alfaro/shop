package com.telus.shop;

import com.telus.shop.model.Item;
import com.telus.shop.model.Stock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Service
@Path("items")
public class ItemService {

    public static final String UUID_V4_STRING = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-4[a-fA-F0-9]{3}-[89abAB][a-fA-F0-9]{3}-[a-fA-F0-9]{12}";
    final Logger logger = LoggerFactory.getLogger(ItemService.class);

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response listItems(@QueryParam("reserved") boolean reserved) {
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
            logger.error("failed to list items", e);
        }
        return rb.build();
    }

    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    public Response reserveItem(@QueryParam("serialNumber") @NotNull @Valid @Pattern(regexp = UUID_V4_STRING) String serialNumber) {
        final Response.ResponseBuilder rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        try {
            rb.status(Response.Status.NOT_FOUND);
            for (int i = 0; i < Stock.getInstance().getItems().size(); i++) {
                if (!Stock.getInstance().getItems().get(i).getSerialNumber().equalsIgnoreCase(serialNumber)) {
                    continue;
                }
                if (Stock.getInstance().getItems().get(i).isReserved()) {
                    rb.status(Response.Status.CONFLICT);
                    break;
                }
                rb.entity(Stock.getInstance().getItems().get(i));
                Stock.getInstance().getItems().get(i).setReserved(true);
                rb.status(Response.Status.OK);
                break;
            }
        } catch (Exception e) {
            logger.error("failed to reserve item", e);
        }
        return rb.build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    public Response purchaseItem(@QueryParam("serialNumber") @NotNull @Valid @Pattern(regexp = UUID_V4_STRING) String serialNumber) {
        final Response.ResponseBuilder rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        try {
            rb.status(Response.Status.NOT_FOUND);
            for (int i = 0; i < Stock.getInstance().getItems().size(); i++) {
                if (!Stock.getInstance().getItems().get(i).getSerialNumber().equalsIgnoreCase(serialNumber)) {
                    continue;
                }
                rb.entity(Stock.getInstance().getItems().get(i));
                Stock.getInstance().getItems().remove(i);
                rb.status(Response.Status.OK);
                break;
            }
        } catch (Exception e) {
            logger.error("failed to reserve item", e);
        }
        return rb.build();
    }

    @PATCH
    @Produces({MediaType.APPLICATION_JSON})
    public Response loadItem(@QueryParam("serialNumber") @NotNull @Valid @Pattern(regexp = UUID_V4_STRING) String serialNumber, @QueryParam("name") @NotNull String name,  @QueryParam("description") String description) {
        final Response.ResponseBuilder rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        try {
            final Item item = new Item(name, serialNumber);
            item.setDescription(description);
            Stock.getInstance().getItems().add(item);
            rb.entity(item);
            rb.status(Response.Status.OK);
        } catch (Exception e) {
            logger.error("failed to reserve item", e);
        }
        return rb.build();
    }

}
