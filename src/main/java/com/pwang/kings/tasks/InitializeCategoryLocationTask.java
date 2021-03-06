package com.pwang.kings.tasks;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.pwang.kings.categories.CategoryTypeManager;
import com.pwang.kings.clients.ZomatoConstants;
import com.pwang.kings.db.daos.LocationDao;
import com.pwang.kings.objects.model.ApiProviderType;
import com.pwang.kings.objects.model.CategoryType;
import com.pwang.kings.objects.model.Location;
import com.pwang.kings.objects.model.LocationType;
import com.pwang.kings.serde.ObjectMappers;
import io.dropwizard.servlets.tasks.PostBodyTask;
import org.apache.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;

import javax.ws.rs.WebApplicationException;
import javax.xml.ws.WebServiceException;
import java.io.PrintWriter;
import java.util.Optional;

/**
 * @author pwang on 12/27/17.
 */
public class InitializeCategoryLocationTask extends PostBodyTask {

    Logger LOGGER = Logger.getLogger(InitializeCategoryLocationTask.class);

    private final LocationDao locationDao;
    private final CategoryTypeManager categoryManager;

    public InitializeCategoryLocationTask(
            LocationDao locationDao,
            CategoryType categoryType,
            CategoryTypeManager categoryManager) {

        super("init_" + categoryType.toString());
        this.locationDao = locationDao;
        this.categoryManager = categoryManager;
    }


    @Override
    public void execute(ImmutableMultimap<String, String> parameters, String body, PrintWriter output) throws Exception {
        JsonNode locationRequest = ObjectMappers.RETROFIT_MAPPER.readTree(body);

        ApiProviderType locationApiProviderType = ApiProviderType.valueOf(locationRequest.get("api_provider_type").textValue());
        String locationApiProviderId = locationRequest.get("api_city_id").textValue();

        if (ApiProviderType.zomato != locationApiProviderType) {
            throw new WebApplicationException("unsupported provider type", HttpStatus.NOT_IMPLEMENTED_501);
        }

        // get location
        Optional<Location> locationOptional = locationDao.getByApiId(
                locationApiProviderType.toString(),
                ZomatoConstants.toApiProviderId(LocationType.city, Integer.valueOf(locationApiProviderId)));
        Location location;
        if (!locationOptional.isPresent()) {
            // will create city if not there
            location = categoryManager.getCitiesAndCreate(ImmutableList.of(locationApiProviderId))
                    .stream().findFirst()
                    .orElseThrow(() -> new WebServiceException("could not find location"));
        }

    }
}
