/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.mysql.v2017_12_01_preview.implementation;

import com.microsoft.azure.management.mysql.v2017_12_01_preview.OperationListResult;
import com.microsoft.azure.arm.model.implementation.WrapperImpl;
import java.util.List;

class OperationListResultImpl extends WrapperImpl<OperationListResultInner> implements OperationListResult {
    private final MySQLManager manager;
    OperationListResultImpl(OperationListResultInner inner, MySQLManager manager) {
        super(inner);
        this.manager = manager;
    }

    @Override
    public MySQLManager manager() {
        return this.manager;
    }

    @Override
    public List<OperationInner> value() {
        return this.inner().value();
    }

}
