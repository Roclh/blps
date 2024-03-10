package com.roclh.mainmodule.utils;

import lombok.Setter;

public class AccountIdGenerator {
    @Setter
    private static Long Id = 1L;

    public static Long getId() {
        return Id++;
    }

}
