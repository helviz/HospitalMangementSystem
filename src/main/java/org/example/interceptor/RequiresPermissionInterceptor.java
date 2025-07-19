package org.example.interceptor;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import org.example.annotation.RequiresPermission;
import org.example.enums.Permissions;  
import org.example.session.UserSession;

@Interceptor
@RequiresPermission(Permissions.NONE)
@Priority(Interceptor.Priority.APPLICATION)
public class RequiresPermissionInterceptor {

    @Inject
    private UserSession userSession;

    @AroundInvoke
    public Object checkPermission(InvocationContext ctx) throws Exception {
        // Get @RequiresPermission from method first
        RequiresPermission annotation = ctx.getMethod().getAnnotation(RequiresPermission.class);

        // If not found on method, check on class
        if (annotation == null) {
            annotation = ctx.getTarget().getClass().getAnnotation(RequiresPermission.class);
        }


        if (annotation != null) {
            Permissions required = annotation.value();  // Get required permission

            if (userSession == null || !userSession.hasPermission(required)) {
                throw new SecurityException("Access denied: missing permission " + required);
            }
        }

        return ctx.proceed();
    }
}
