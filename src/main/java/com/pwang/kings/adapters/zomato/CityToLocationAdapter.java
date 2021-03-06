package com.pwang.kings.adapters.zomato;

import com.pwang.kings.clients.ZomatoConstants;
import com.pwang.kings.objects.api.zomato.City;
import com.pwang.kings.objects.model.ApiProviderType;
import com.pwang.kings.objects.model.ImmutableLocation;
import com.pwang.kings.objects.model.Location;
import com.pwang.kings.objects.model.LocationType;

/**
 * @author pwang on 12/27/17.
 */
public final class CityToLocationAdapter implements ZomatoAdapter<City, Location> {

    @Override
    public Location adapt(City apiObject) {
        LocationType locationType = LocationType.city;

        return ImmutableLocation.builder()
                .locationName(apiObject.name())
                .locationType(locationType.toString())
                .apiProviderType(ApiProviderType.zomato.toString())
                .apiProviderId(ZomatoConstants.toApiProviderId(locationType, apiObject.id()))
                .build();
    }
}
