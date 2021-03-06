/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.synapse.v2020_12_01;

import rx.Observable;
import com.microsoft.azure.management.synapse.v2020_12_01.implementation.SqlPoolDataWarehouseUserActivitiesInner;
import com.microsoft.azure.arm.model.HasInner;

/**
 * Type representing SqlPoolDataWarehouseUserActivities.
 */
public interface SqlPoolDataWarehouseUserActivities extends HasInner<SqlPoolDataWarehouseUserActivitiesInner> {
    /**
     * Get SQL pool user activities.
     * Gets the user activities of a SQL pool which includes running and suspended queries.
     *
     * @param resourceGroupName The name of the resource group. The name is case insensitive.
     * @param workspaceName The name of the workspace
     * @param sqlPoolName SQL pool name
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable for the request
     */
    Observable<DataWarehouseUserActivities> getAsync(String resourceGroupName, String workspaceName, String sqlPoolName);

}
