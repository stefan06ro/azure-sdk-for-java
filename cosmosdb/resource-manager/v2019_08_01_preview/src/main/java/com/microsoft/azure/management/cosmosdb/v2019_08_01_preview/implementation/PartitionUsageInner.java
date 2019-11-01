/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.cosmosdb.v2019_08_01_preview.implementation;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The partition level usage data for a usage request.
 */
public class PartitionUsageInner extends UsageInner {
    /**
     * The partition id (GUID identifier) of the usages.
     */
    @JsonProperty(value = "partitionId", access = JsonProperty.Access.WRITE_ONLY)
    private String partitionId;

    /**
     * The partition key range id (integer identifier) of the usages.
     */
    @JsonProperty(value = "partitionKeyRangeId", access = JsonProperty.Access.WRITE_ONLY)
    private String partitionKeyRangeId;

    /**
     * Get the partition id (GUID identifier) of the usages.
     *
     * @return the partitionId value
     */
    public String partitionId() {
        return this.partitionId;
    }

    /**
     * Get the partition key range id (integer identifier) of the usages.
     *
     * @return the partitionKeyRangeId value
     */
    public String partitionKeyRangeId() {
        return this.partitionKeyRangeId;
    }

}
