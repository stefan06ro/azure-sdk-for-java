// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.network.fluent.inner;

import com.azure.core.annotation.Fluent;
import com.azure.core.util.logging.ClientLogger;
import com.azure.resourcemanager.network.models.ExpressRouteCrossConnectionRoutesTableSummary;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/** The ExpressRouteCrossConnectionsRoutesTableSummaryListResult model. */
@Fluent
public final class ExpressRouteCrossConnectionsRoutesTableSummaryListResultInner {
    @JsonIgnore
    private final ClientLogger logger =
        new ClientLogger(ExpressRouteCrossConnectionsRoutesTableSummaryListResultInner.class);

    /*
     * A list of the routes table.
     */
    @JsonProperty(value = "value")
    private List<ExpressRouteCrossConnectionRoutesTableSummary> value;

    /*
     * The URL to get the next set of results.
     */
    @JsonProperty(value = "nextLink", access = JsonProperty.Access.WRITE_ONLY)
    private String nextLink;

    /**
     * Get the value property: A list of the routes table.
     *
     * @return the value value.
     */
    public List<ExpressRouteCrossConnectionRoutesTableSummary> value() {
        return this.value;
    }

    /**
     * Set the value property: A list of the routes table.
     *
     * @param value the value value to set.
     * @return the ExpressRouteCrossConnectionsRoutesTableSummaryListResultInner object itself.
     */
    public ExpressRouteCrossConnectionsRoutesTableSummaryListResultInner withValue(
        List<ExpressRouteCrossConnectionRoutesTableSummary> value) {
        this.value = value;
        return this;
    }

    /**
     * Get the nextLink property: The URL to get the next set of results.
     *
     * @return the nextLink value.
     */
    public String nextLink() {
        return this.nextLink;
    }

    /**
     * Validates the instance.
     *
     * @throws IllegalArgumentException thrown if the instance is not valid.
     */
    public void validate() {
        if (value() != null) {
            value().forEach(e -> e.validate());
        }
    }
}