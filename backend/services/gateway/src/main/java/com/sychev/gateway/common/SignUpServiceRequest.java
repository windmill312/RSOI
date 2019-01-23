package com.sychev.gateway.common;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SignUpServiceRequest {
    @NotBlank
    @Size(min = 4, max = 40)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
