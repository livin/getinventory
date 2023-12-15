package com.getinventory.web.rest.mockusers;

import java.lang.annotation.*;
import org.springframework.security.test.context.support.WithMockUser;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(username = "admin", roles = { "ADMIN", "USER" })
public @interface WithAdminUser {
}
