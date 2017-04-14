package com.example.ics.util;

import org.apache.commons.collections.map.HashedMap;

import java.time.Year;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class MiscUtil {
    private static Map<String,Integer> mapMonth = new HashedMap();

    static {
        mapMonth.put("JAN",1);
        mapMonth.put("FEB",2);
        mapMonth.put("MAR",3);
        mapMonth.put("APR",4);
        mapMonth.put("MAY",5);
        mapMonth.put("JUN",6);
        mapMonth.put("JUL",7);
        mapMonth.put("AUG",8);
        mapMonth.put("SEP",9);
        mapMonth.put("OCT",10);
        mapMonth.put("NOV",11);
        mapMonth.put("DEC",12);
    }

    public static Map<String,Integer> getMapMonth(){
        return mapMonth;
    }

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

    public static int getCurrentYear(){
        return Year.now().getValue();
    }

    public static int getCurrentMonth(){
        return YearMonth.now().getMonthValue();
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueAsc(Map<K, V> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueDesc(Map<K, V> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
}
