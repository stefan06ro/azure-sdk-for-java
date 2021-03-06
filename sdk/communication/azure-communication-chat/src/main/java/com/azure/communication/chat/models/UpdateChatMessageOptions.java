// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.communication.chat.models;

import com.azure.core.annotation.Fluent;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Request payload for updating a chat message. */
@Fluent
public final class UpdateChatMessageOptions {
    /*
     * Chat message content.
     */
    @JsonProperty(value = "content")
    private String content;

    /**
     * Get the content property: Chat message content.
     *
     * @return the content value.
     */
    public String getContent() {
        return this.content;
    }

    /**
     * Set the content property: Chat message content.
     *
     * @param content the content value to set.
     * @return the UpdateChatMessageOptions object itself.
     */
    public UpdateChatMessageOptions setContent(String content) {
        this.content = content;
        return this;
    }
}
