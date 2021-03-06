package com.pwang.kings.objects.api.kings;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.Optional;

/**
 * @author pwang on 1/11/18.
 */
@JsonDeserialize(as = ImmutableNextContestantResponse.class)
@JsonSerialize(as = ImmutableNextContestantResponse.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Value.Immutable
public interface NextContestantResponse {

    Optional<ContestantEntry> nextContestant();
}
