package com.meli.challenge.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResource {

    private List<ErrorResource> errors;

    public void addError(String message) {
        if(CollectionUtils.isEmpty(errors)) {
            errors = new ArrayList<>();
        }
        errors.add(new ErrorResource(message));
    }
}
