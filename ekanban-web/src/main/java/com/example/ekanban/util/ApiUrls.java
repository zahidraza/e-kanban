/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.ekanban.util;

/**
 *
 * @author razamd
 */
public class ApiUrls {
    /*User Resource*/
    public static final String ROOT_URL_USERS = "/api/users";
    public static final String URL_USERS_USER = "/{userId}";
    /*Category, SubCategory, Product Resource*/
    public static final String ROOT_URL_CATEGORIES = "/api/categories";
    public static final String URL_CATEGORIES_CATEGORY = "/{categoryId}";
    public static final String URL_CATEGORIES_CATEGORY_SUBCATEGORIES = "/{categoryId}/subCategories";
    public static final String URL_CATEGORIES_CATEGORY_SUBCATEGORIES_SUBCATEGORY = 
            "/{categoryId}/subCategories/{subCategoryId}";
    public static final String URL_CATEGORIES_CATEGORY_SUBCATEGORIES_SUBCATEGORY_PRODUCTS = 
            "/{categoryId}/subCategories/{subCategoryId}/products";
    public static final String URL_CATEGORIES_CATEGORY_SUBCATEGORIES_SUBCATEGORY_PRODUCTS_PRODUCT = 
            "/{categoryId}/subCategories/{subCategoryId}/products/{productId}";
    public static final String URL_CATEGORIES_CATEGORY_SUBCATEGORIES_SUBCATEGORY_PRODUCTS_PRODUCT_SECTIONS = 
            "/{categoryId}/subCategories/{subCategoryId}/products/{productId}/sections";
    
    /*Section Resource*/
    public static final String ROOT_URL_SECTIONS = "/api/sections";
    public static final String URL_SECTIONS_SECTION = "/{secId}";
    
    /*Supplier Resource*/
    public static final String ROOT_URL_SUPPLIERS = "/api/suppliers";
    public static final String URL_SUPPLIERS_SUPPLIER = "/{supplierId}";

    /*Upload Resource*/
    public static final String ROOT_URL_UPLOADS = "/api/uploads";
    public static final String URL_UPLOADS_PRODUCTS = "/products";

    /*Product Resource*/
    public static final String ROOT_URL_PRODUCTS = "/api/products";
    public static final String URL_PRODUCTS_PRODUCT = "{productId}";
    public static final String URL_PRODUCTS_SYNC = "/sync";

    /*Inventory Resource*/
    public static final String ROOT_URL_INVENTORY = "/api/inventory";
    public static final String URL_INVENTORY_SINGLE = "/{id}";

    /*Section Resource*/
    public static final String ROOT_URL_ORDERS = "/api/orders";
    public static final String URL_ORDERS_ORDER = "/{orderId}";
    public static final String URL_ORDERS_FOLLOWUP = "/followup";


    /*Miscellaneous Resource*/
    public static final String ROOT_URL_MISCELLANEOUS = "/api/misc";
    public static final String URL_MISC_CHANGE_PASSWORD = "/change_password";
    public static final String URL_MISC_CURRENT_TIME = "/current_time";
    public static final String URL_MISC_FORGOT_PASSWORD = "/forgot_password";

}
