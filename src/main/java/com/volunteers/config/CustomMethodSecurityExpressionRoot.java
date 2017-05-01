package com.volunteers.config;

import com.volunteers.areas.events.entities.Event;
import com.volunteers.areas.users.entities.Organization;
import com.volunteers.errors.Errors;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


import java.util.Set;


public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private Object filterObject;
    private Object returnObject;
    private Object target;

    public CustomMethodSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }

    public boolean isMember(Long eventId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         Object principal = authentication.getPrincipal();
        Set<Event> events = null;
        Event event = null;
        if (principal instanceof Organization) {
            events = ((Organization) principal).getEvents();
            event = events.stream().filter(e -> e.getId().equals(eventId)).findAny().orElse(null);
            if (event == null) {
                throw new AccessDeniedException(Errors.ACCESS_DENIED);
            }

            return true;
        }

        return false;
    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getFilterObject() {
        return this.filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getReturnObject() {
        return this.returnObject;
    }

    @Override
    public Object getThis() {
        return this.target;
    }

    public void setThis(Object target) {
        this.target = target;
    }
}