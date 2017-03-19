package com.example.ics.util;

import java.util.ArrayList;
import java.util.List;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class MiscUtil {
    /**
     * method for checking invalid resources
     * @param resources List resource uri that needs to to be checked for invalid resource 
     * @param controllerClazz Controller clazz of the resource
     * @return list of invalid resources. empty list if no invalid resources found. return null if resources is null
     */
    public static List<String> findInvalidResources(List<String> resources, Class<?> controllerClazz){
        if(resources == null) return null;
        String base = linkTo(controllerClazz).withSelfRel().getHref();
        
        List<String> invalidResources = new ArrayList<>();
        if(resources == null) return invalidResources;
        
        resources.forEach(resource -> {
            if(!resource.contains(base)){
                invalidResources.add(resource);
            }else if(extractIdFromUri(resource) == null){
                invalidResources.add(resource);
            }
        });
        return invalidResources;
    }
    
    public static Long extractIdFromUri(String uri){
        int idx = uri.lastIndexOf('/');
        String id = uri.substring(idx+1).trim();
        if(!id.isEmpty()){
            return Long.parseLong(id);
        }
        return null;
    }
    
    public static String toStringList(List<String> list){
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        if(list != null){
            list.forEach(s->builder.append(s + ", "));
            builder.setLength(builder.length()-2);  
        }
        builder.append("]");
        return builder.toString();
    }
}
