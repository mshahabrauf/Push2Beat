package com.attribes.push2beat.models.Response.PushFireBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maaz on 11/22/2016.
 */
public class PushResponse {
    private Integer multicastId;
    private Integer success;
    private Integer failure;
    private Integer canonicalIds;
    private List<Result> results = new ArrayList<Result>();

    public Integer getMulticastId() {
        return multicastId;
    }

    public void setMulticastId(Integer multicastId) {
        this.multicastId = multicastId;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public Integer getFailure() {
        return failure;
    }

    public void setFailure(Integer failure) {
        this.failure = failure;
    }

    public Integer getCanonicalIds() {
        return canonicalIds;
    }

    public void setCanonicalIds(Integer canonicalIds) {
        this.canonicalIds = canonicalIds;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public class Result {

        private String messageId;

        public String getMessageId() {
            return messageId;
        }

        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }

    }
}
