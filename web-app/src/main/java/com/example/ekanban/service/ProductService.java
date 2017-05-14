/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.ekanban.service;

import com.example.ekanban.dto.PConsumption;
import com.example.ekanban.dto.ProductCsv;
import com.example.ekanban.dto.ProductDto;
import com.example.ekanban.dto.ProductError;
import com.example.ekanban.entity.*;
import com.example.ekanban.enums.BinState;
import com.example.ekanban.enums.ClassType;
import com.example.ekanban.enums.KanbanType;
import com.example.ekanban.enums.OrderState;
import com.example.ekanban.exception.ProductDetailsNotValidException;
import com.example.ekanban.page.converter.ProductConverter;
import com.example.ekanban.respository.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;

import com.example.ekanban.util.ConfigUtil;
import com.example.ekanban.util.Constants;
import com.example.ekanban.util.CsvUtils;
import com.example.ekanban.util.MiscUtil;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import org.apache.commons.collections.map.HashedMap;
import org.dozer.Mapper;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;
    private final SectionRepository sectionRepository;
    private final SupplierRepository supplierRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private SubCategoryRepository subCategoryRepository;
    @Autowired private ConsumptionRepositoryImpl consumptionRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private UserRepository userRepository;
    private ProductConverter converter;
    private Mapper mapper;

    int i = -1;
    long totalMonthyConsumption;
    double commulativePercentage;

    @Autowired
    public ProductService(ProductRepository productRepository, SectionRepository sectionRepository, SupplierRepository supplierRepository) {
        this.productRepository = productRepository;
        this.sectionRepository = sectionRepository;
        this.supplierRepository = supplierRepository;
    }

    @Autowired
    public void setConverter(ProductConverter converter) {
        this.converter = converter;
    }

    @Autowired
    public void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }

    public ProductDto findOne(Long id, boolean initSections, boolean initSuupliers) {
        logger.debug("findOne(): id = {}", id);
        Product product = productRepository.findOne(id);
        if (product != null && initSections) {
            Hibernate.initialize(product.getSectionList());
        }
        if (product != null && initSuupliers) {
            Hibernate.initialize(product.getSupplierList());
        }
        return mapper.map(product, ProductDto.class);
    }

    public List<ProductDto> findAll() {
        logger.debug("findAll()");
        return productRepository
                .findAll().stream()
                .map(product -> mapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
    }

    public Page<ProductDto> findPageBySubCategory(SubCategory subCategory, Pageable pageable) {
        logger.debug("findPageBySubCategory()");
        Page<Product> page = productRepository.findBySubCategory(subCategory, pageable);
        page.forEach(p -> {
            Hibernate.initialize(p.getSectionList());
            Hibernate.initialize(p.getSupplierList());
        });
        return page.map(converter);
    }


    public ProductDto findByName(String name) {
        logger.debug("findByNameIgnoreCase(): name = ", name);
        return mapper.map(productRepository.findByNameIgnoreCase(name), ProductDto.class);
    }

    public Boolean exists(Long id) {
        logger.debug("exists(): id = ", id);
        return productRepository.exists(id);
    }

    public Long count() {
        logger.debug("count()");
        return productRepository.count();
    }

    @Transactional
    public ProductDto save(Product product, List<Long> sections, List<Long> suppliers) {
        logger.debug("save()");
        Set<Section> sectionList = new HashSet<>();
        Set<Supplier> supplierList = new HashSet<>();

        if (sections != null) {
            sections.forEach(secId -> {
                sectionList.add(sectionRepository.findOne(secId));
            });
        }
        if (suppliers != null) {
            suppliers.forEach(secId -> {
                supplierList.add(supplierRepository.findOne(secId));
            });
        }
        product.setSectionList(sectionList);
        product.setSupplierList(supplierList);
        product.setStkOnFloor(0L);
        product.setOrderedQty(0L);
        product = productRepository.save(product);
        return mapper.map(product, ProductDto.class);
    }

    @Transactional
    public ProductDto update(Product product, List<Long> sections, List<Long> suppliers) {
        logger.debug("update()");
        Product product2 = productRepository.findOne(product.getId());

        map(product, product2);

        Set<Section> sectionList = product2.getSectionList();
        sectionList.clear();
        sections.forEach(secId -> {
            sectionList.add(sectionRepository.findOne(secId));
        });
        Set<Supplier> supplierList = product2.getSupplierList();
        supplierList.clear();
        suppliers.forEach(secId -> {
            supplierList.add(supplierRepository.findOne(secId));
        });
        return mapper.map(product2, ProductDto.class);
    }

    @Transactional
    public void delete(Long id) {
        logger.debug("delete(): id = {}", id);
        productRepository.delete(id);
    }

    @Transactional
    public void addProductsBatch(MultipartFile file) {
        logger.debug("addProductsBatch");
        List<ProductCsv> list = null;
        //convert to csv string
        String output = null;
        try {
            if (file.getOriginalFilename().contains("xlsx")) {
                output = CsvUtils.fromXlsx(file.getInputStream());
            } else if (file.getOriginalFilename().contains("xls")) {
                output = CsvUtils.fromXls(file.getInputStream());
            }
            //logger.debug(output);
        }catch (IOException e){
            e.printStackTrace();
        }

        //Read as Bean from csv String
        CSVReader reader = new CSVReader(new StringReader(output), ';');
        HeaderColumnNameMappingStrategy<ProductCsv> strategy = new HeaderColumnNameMappingStrategy<>();
        strategy.setType(ProductCsv.class);
        CsvToBean<ProductCsv> csvToBean = new CsvToBean<>();
        list = csvToBean.parse(strategy, reader);
        //Validate Product Data
        validate(list);

        //convert from DTO to ENTITY
        List<Product> products = map(list);
        productRepository.save(products);
    }

    /**
     * Calculate dynamically calculated fields for product.
     */
    @Transactional
    public void sync(){
        totalMonthyConsumption = 0L;
        commulativePercentage = 0.0;
        List<Product> products = productRepository.findAll();
        Map<Long,PConsumption> consumptionMap = new HashedMap();

        /*/////////Processing monthly consumption/////////*/
        products.forEach(product -> {
            Long avgValue = consumptionRepository.findAvgConsumptionOfLastYear(product.getId());
            consumptionMap.put(product.getId(),new PConsumption(product.getPrice().longValue()*avgValue));
            totalMonthyConsumption += product.getPrice().longValue()*avgValue;
        });
        //Sort on basis of consumption value descending
        Map<Long,PConsumption> sortedConsumptionMap = MiscUtil.sortByValueDesc(consumptionMap);
        //Calculate percentage contribution and commulative contribution
        sortedConsumptionMap.forEach((id, pConsumption) -> {
            double percent = (double)pConsumption.getMonthlyConsumption()/(double) totalMonthyConsumption;
            percent *= 100;
            pConsumption.setPercentage(percent);
            pConsumption.setCommulativePercentage(commulativePercentage+percent);
            commulativePercentage += percent;
        });
        //Update product dynamically calculated values
        products.forEach(product -> {
            double cp = sortedConsumptionMap.get(product.getId()).getCommulativePercentage();
            ClassType classType = cp <= 60 ? ClassType.CLASS_A : cp <= 80 ? ClassType.CLASS_B : ClassType.CLASS_C;
            product.setClassType(classType);
            /////////////////
            KanbanType kanbanType = classType == ClassType.CLASS_A || classType == ClassType.CLASS_B ? KanbanType.N_BIN : KanbanType.TWO_BIN;
            product.setKanbanType(kanbanType);
            ///////////////
            Long maxValue = consumptionRepository.findMaxConsumptionOfLastYear(product.getId());
            product.setDemand(maxValue/ Constants.NO_OF_DAYS_IN_MONTH);
            ///////////////////
            long binQty = product.getDemand() > product.getMinOrderQty() ? product.getDemand() : product.getMinOrderQty();
            if (product.getPacketSize().doubleValue() < 1.0){
                binQty = (long)((binQty/product.getPacketSize().doubleValue() + 1)*product.getPacketSize().doubleValue());
            }else {
                binQty = (binQty/product.getPacketSize().longValue() + 1)*product.getPacketSize().longValue();
            }

            product.setBinQty(binQty);
            int noOfBins = 2;
            if (classType != ClassType.CLASS_C) {
                int tat = product.getTimeOrdering() + product.getTimeProcurement() + product.getTimeTransporation() + product.getTimeBuffer();
                noOfBins = (int)(product.getDemand()*tat /binQty + 2);
            }
            product.setNoOfBins(noOfBins);
        });
        //Update Inventory state
        products.forEach(product -> {
            updateInventory(product);
        });
//
//        if (firstSync){
//            User application = userRepository.findOne(1L);
//            products.forEach(product -> {
//                int noOfBins = product.getNoOfBins();
//                int binInStock = getNoOfBins(product.getStkOnFloor(),product.getBinQty());
//                Inventory inv = null;
//
//                if (binInStock <= noOfBins){ //binInStock is less than NoOfBins, So add that number of bins in Stock
//                    for (int i = 0; i < binInStock; i++){
//                        product.addInventory(new Inventory(i+1, BinState.STORE));
//                    }
//                    int binInOrder = getNoOfBins(product.getOrderedQty(),product.getBinQty());
//                    if (binInStock + binInOrder <= noOfBins) { //binInStock+binInOrder is less than NoOfBins, So add binInOrder Bins in Ordered state
//                        StringBuilder builder = new StringBuilder();
//                        for (int i = binInStock; i < binInStock + binInOrder; i++){
//                            inv = new Inventory(i+1, BinState.ORDERED);
//                            product.addInventory(inv);
//                            builder.append((i+1)).append(",");
//                        }
//                        if (builder.length() >= 2) {
//                            builder.setLength(builder.length()-1);
//                            orderRepository.save(new Order(product,builder.toString(),new Date(),application, OrderState.ORDERED.getValue()));
//                        }
//                        if (noOfBins - (binInStock + binInOrder) > 0){ //noOfBins - (binInStock + binInOrder) > 0, means this diff Bins are pending orders
//                            for (int i = binInStock + binInOrder; i < noOfBins; i++){
//                                product.addInventory(new Inventory(i+1, BinState.PURCHASE));
//                            }
//                        }
//                    }else { //binInStock+binInOrder is greater than NoOfBins, So add (noOfBins - binInStock) Bins in Ordered state, and stop furhter processing
//                        StringBuilder builder = new StringBuilder();
//                        for (int i = binInStock; i < noOfBins; i++){
//                            product.addInventory(new Inventory(i+1, BinState.ORDERED));
//                            builder.append((i+1)).append(",");
//                        }
//                        if (builder.length() >= 2) {
//                            builder.setLength(builder.length()-1);
//                            orderRepository.save(new Order(product,builder.toString(),new Date(),application, OrderState.ORDERED.getValue()));
//                        }
//                    }
//                }else { // //binInStock is greater than NoOfBins, So add noOfBins Bin in Stock and stop any further processing Since Stock is full
//                    for (int i = 0; i < noOfBins; i++){
//                        product.addInventory(new Inventory(i+1, BinState.STORE));
//                    }
//                }
//            });
//
//        }else { //There may either be decrease or increase in noOfBins. No action required for decrease
//            products.forEach(product -> {
//                int noOfInventory = product.getInventorySet().size();
//                //if inventory size for a product is less than no of bins, then no of bins got increased after sync.
//                if (noOfInventory < product.getNoOfBins()){
//                    for (int i = noOfInventory+1; i <= product.getNoOfBins(); i++){
//                        product.addInventory(new Inventory(i, BinState.PURCHASE));
//                    }
//                }
//            });
//        }
    }

    private void updateInventory(Product product) {

        if (product.getIgnoreSync()) return;

        /*////For setting ordered by in case bin is ordered by app for first time */
        User application = userRepository.findOne(1L);
        double stkRoundFraction = Double.parseDouble(ConfigUtil.getConfigProperty(Constants.STK_ROUND_FRACTION,"0.05"));

        if (product.getNew()) {
            int noOfBins = product.getNoOfBins();
            int binInStock = getNoOfBins(product.getStkOnFloor(),product.getBinQty(),stkRoundFraction);
            Inventory inv = null;

            if (binInStock <= noOfBins){ //binInStock is less than NoOfBins, So add that number of bins in Stock
                for (int i = 0; i < binInStock; i++){
                    product.addInventory(new Inventory(i+1, BinState.STORE));
                }
                int binInOrder = getNoOfBins(product.getOrderedQty(),product.getBinQty(),stkRoundFraction);
                if (binInStock + binInOrder <= noOfBins) { //binInStock+binInOrder is less than NoOfBins, So add binInOrder Bins in Ordered state
                    StringBuilder builder = new StringBuilder();
                    for (int i = binInStock; i < binInStock + binInOrder; i++){
                        inv = new Inventory(i+1, BinState.ORDERED);
                        product.addInventory(inv);
                        builder.append((i+1)).append(",");
                    }
                    if (builder.length() >= 2) {
                        builder.setLength(builder.length()-1);
                        orderRepository.save(new Order(product,builder.toString(),new Date(),application, OrderState.ORDERED.getValue()));
                    }
                    if (noOfBins - (binInStock + binInOrder) > 0){ //noOfBins - (binInStock + binInOrder) > 0, means this diff Bins are pending orders
                        for (int i = binInStock + binInOrder; i < noOfBins; i++){
                            product.addInventory(new Inventory(i+1, BinState.PURCHASE));
                        }
                    }
                }else { //binInStock+binInOrder is greater than NoOfBins, So add (noOfBins - binInStock) Bins in Ordered state, and stop furhter processing
                    StringBuilder builder = new StringBuilder();
                    for (int i = binInStock; i < noOfBins; i++){
                        product.addInventory(new Inventory(i+1, BinState.ORDERED));
                        builder.append((i+1)).append(",");
                    }
                    if (builder.length() >= 2) {
                        builder.setLength(builder.length()-1);
                        orderRepository.save(new Order(product,builder.toString(),new Date(),application, OrderState.ORDERED.getValue()));
                    }
                }
            }else { // //binInStock is greater than NoOfBins, So add noOfBins Bin in Stock and stop any further processing Since Stock is full
                for (int i = 0; i < noOfBins; i++){
                    product.addInventory(new Inventory(i+1, BinState.STORE));
                }
            }
            product.setNew(false);
        } else {
            int noOfInventory = product.getInventorySet().size();
            //if inventory size for a product is less than no of bins, then no of bins got increased after sync.
            if (noOfInventory < product.getNoOfBins()){
                for (int i = noOfInventory+1; i <= product.getNoOfBins(); i++){
                    product.addInventory(new Inventory(i, BinState.PURCHASE));
                }
            }
        }
    }

    private int getNoOfBins(Long value, Long binQty, double stkRoundFraction){
        int result =(int)(value/binQty);
        int surplus = (int)(value%binQty);
        int roundValue = (int)(stkRoundFraction*binQty.doubleValue());
        result = surplus > roundValue ? result+1 : result;
        return result;
    }

    /**
     * Validate ProductCsv for null values.
     * @param productCsvList
     */
    private void validate(List<ProductCsv> productCsvList) {
        logger.debug("validating {} products ...", productCsvList.size());
        List<ProductCsv> removeList = new ArrayList<>();
        List<ProductError> errors = new ArrayList<>();
        i = 2;
        productCsvList.forEach(p -> {
            if (p.getName() == null && p.getCategory() == null && p.getSubCategory() == null) {
                removeList.add(p);
            }else {
                if (p.getName() == null) {
                    errors.add(new ProductError("PRODUCT_NAME", i, "Product Name cannot be balnk."));
                }
                if (p.getCategory() == null) {
                    errors.add(new ProductError("CATEGORY", i, "Product Category cannot be blank."));
                }
                if (p.getSubCategory() == null) {
                    errors.add(new ProductError("SUB_CATEGORY", i, "Product SubCategory cannot be blank."));
                }
                if (p.getPrice() == null) {
                    errors.add(new ProductError("PRICE", i, "Product Price cannot be blank."));
                }
                if (p.getTimeOrdering() == null) {
                    errors.add(new ProductError("ORDERING_TIME", i, "Product Ordering time cannot be blank."));
                }
                if (p.getTimeProcurement() == null) {
                    errors.add(new ProductError("PRODUCTION_TIME", i, "Product Production time cannot be blank."));
                }
                if (p.getTimeTransporation() == null) {
                    errors.add(new ProductError("TRANSPORTATION_TIME", i, "Product Transportation time cannot be blank."));
                }
                if (p.getTimeBuffer() == null) {
                    errors.add(new ProductError("BUFFER_TIME", i, "Product Buffer time cannot be blank."));
                }
                if (p.getUomPurchase() == null) {
                    errors.add(new ProductError("UOM_PURCHASE", i, "Product Unit of Measurment (Purchase) cannot be blank" +
                            "."));
                }
                if (p.getUomConsumption() == null) {
                    errors.add(new ProductError("UOM_CONSUMPTION", i, "Product Unit of Measurment (Consumption) cannot be" +
                            " blank."));
                }
                if (p.getConversionFactor() == null) {
                    errors.add(new ProductError("CONVERSION_FACTOR", i, "Product Conversion Factor cannot be blank."));
                }
                if (p.getMinOrderQty() == null) {
                    errors.add(new ProductError("MIN_ORDER_QTY", i, "Product minimum order quantity cannot be blank."));
                }
                if (p.getPacketSize() == null) {
                    errors.add(new ProductError("PACKET_SIZE", i, "Product packet size cannot be blank."));
                }
                if (p.getItemCode() == null) {
                    errors.add(new ProductError("ITEM_CODE", i, "Product Item Code cannot be blank."));
                }

                if (p.getSupplierType() != null && !(p.getSupplierType().equals("LOCAL") || p.getSupplierType().equals("NON_LOCAL"))){
                    errors.add(new ProductError("SUPPLIER_TYPE",i,"Supplier type can have [LOCAL,NON_LOCAL] values"));
                }
            }
            i++;
        });
        for (ProductCsv p: removeList) {
            productCsvList.remove(p);
        }
        if (errors.size() > 0) {
            throw new ProductDetailsNotValidException("Product details has errors.", errors);
        }
    }

    /**
     * Called by addProductsBatch() method to map from ProductCsv List to Product List.
     * Also validates product against database.
     * @param list List of ProductCsv
     * @return List of Product
     */
    private List<Product> map(List<ProductCsv> list) {
        List<Product> products = new ArrayList<>();
        List<ProductError> errors = new ArrayList<>();

        Set<String> itemCodeList = new HashSet<>();

        Map<String, Integer> mapMonth = MiscUtil.getMapMonth();
        Set<String> months = mapMonth.keySet();
        int currYear = MiscUtil.getCurrentYear();
        int currMonth = MiscUtil.getCurrentMonth();

        i = 2;
        list.forEach(productCsv -> {
            /*check packet value if it is zero*/
            if (productCsv.getPacketSize().doubleValue() == 0) {
                errors.add(new ProductError("PACKET_SIZE", i, "Packet Size cannot be zero."));
            }
            /*check price value if it is zero*/
            if (productCsv.getPrice().doubleValue() == 0) {
                errors.add(new ProductError("PRICE", i, "Packet Size cannot be zero."));
            }
            /*check for duplicate itemCode*/
            if (itemCodeList.contains(productCsv.getItemCode())){
                errors.add(new ProductError("ITEM_CODE", i, productCsv.getItemCode() + " is duplicate Item Code."));
            }
            /*Check for Duplicate product*/
            if (productRepository.findByNameIgnoreCase(productCsv.getName()) != null) {
                errors.add(new ProductError("PRODUCT_NAME", i, productCsv.getName() + " already exist in database."));
            }
            itemCodeList.add(productCsv.getItemCode());
            Product product = mapper.map(productCsv, Product.class);
            /*////////Mapping SubCategory////////////*/
            SubCategory subCategory = subCategoryRepository.findByNameIgnoreCase(productCsv.getSubCategory().trim());

            //If category of existing subcategory is not equal to category of coming product, report error
            if (subCategory != null && !subCategory.getCategory().getName().trim().equalsIgnoreCase(productCsv.getCategory().trim())) {
                errors.add(new ProductError("SUB_CATEGORY", i, "Sub Category '"+subCategory.getName() + "' belongs to Category '" + productCsv.getCategory() + "' which  can't belong to Category '" + subCategory.getCategory().getName() + "'."));
            } else {
                //If SubCategory is null or category of existing subcategory is not equal to category of coming product,add new SubCategory
                if (subCategory == null) {
                    Category category = categoryRepository.findByNameIgnoreCase(productCsv.getCategory().trim());
                    if (category == null) {
                        category = categoryRepository.save(new Category(productCsv.getCategory().trim()));
                    }
                    subCategory = new SubCategory(productCsv.getSubCategory().trim());
                    subCategory.setCategory(category);
                    subCategory = subCategoryRepository.save(subCategory);
                }

                product.setSubCategory(subCategory);

                /*////////Mapping Suuplier////////////*/
                if (productCsv.getSupplier() != null) {
                    Supplier supplier = supplierRepository.findByNameIgnoreCase(productCsv.getSupplier().trim());
                    if (supplier == null) {
                        supplier = supplierRepository.save(new Supplier(productCsv.getSupplier().trim(), productCsv.getContactPerson(), productCsv.getSupplierType()));
                    }
                    Set<Supplier> suppliers = new HashSet<>();
                    suppliers.add(supplier);
                    product.setSupplierList(suppliers);
                }
                /*Set Total Lead Time*/
                product.setTotalLeadTime(product.getTimeOrdering()+product.getTimeProcurement()+product.getTimeTransporation()+product.getTimeBuffer());

                /*/////Adding Consumptions//////////*/
                Consumption consumption;
                int mnth;
                Long value;
                for (String m : months) {
                    mnth = mapMonth.get(m);
                    value = getConsumptionForMonth(productCsv, m);
                    if (value != null) {
                        if (mnth <= currMonth) {
                            consumption = new Consumption(currYear, mnth, value);
                        } else {
                            consumption = new Consumption(currYear - 1, mnth, value);
                        }
                        product.addConsumption(consumption);
                    }
                }
                /*//////Setting stkOnFloor and orderedQty to zero if value is null*/
                if (product.getStkOnFloor() == null)    product.setStkOnFloor(0L);
                if (product.getOrderedQty() == null)    product.setOrderedQty(0L);
                /*//////Setting ignoreSync and isNew Value///////////////////////*/
                product.setIgnoreSync(false);
                product.setNew(true);
                products.add(product);
            }
            i++;
        });
        if (errors.size() > 0) {
            throw new ProductDetailsNotValidException("Product Details has errors", errors);
        }
        return products;
    }

    /**
     * Get consumption value as long from productCsv Bean for specific month
     * @param productCsv
     * @param month
     * @return Long consumption value if exists in csv bean, else null
     */
    private Long getConsumptionForMonth(ProductCsv productCsv, String month) {
        switch (month) {
            case "JAN":
                return productCsv.getJan() != null ? productCsv.getJan().longValue() : null;
            case "FEB":
                return productCsv.getFeb() != null ? productCsv.getFeb().longValue() : null;
            case "MAR":
                return productCsv.getMar() != null ? productCsv.getMar().longValue() : null;
            case "APR":
                return productCsv.getApr() != null ? productCsv.getApr().longValue() : null;
            case "MAY":
                return productCsv.getMay() != null ? productCsv.getMay().longValue() : null;
            case "JUN":
                return productCsv.getJun() != null ? productCsv.getJun().longValue() : null;
            case "JUL":
                return productCsv.getJul() != null ? productCsv.getJul().longValue() : null;
            case "AUG":
                return productCsv.getAug() != null ? productCsv.getAug().longValue() : null;
            case "SEP":
                return productCsv.getSep() != null ? productCsv.getSep().longValue() : null;
            case "OCT":
                return productCsv.getOct() != null ? productCsv.getOct().longValue() : null;
            case "NOV":
                return productCsv.getNov() != null ? productCsv.getNov().longValue() : null;
            case "DEC":
                return productCsv.getDec() != null ? productCsv.getDec().longValue() : null;
        }
        return null;
    }

    /**
     * Called by Update method
     *
     * @param src  New value that needs to be updated
     * @param dest Old Value from Repository
     */
    private void map(Product src, Product dest) {
        if (src.getName() != null) dest.setName(src.getName());
        if (src.getDescription() != null) dest.setDescription(src.getDescription());
        if (src.getPrice() != null) dest.setPrice(src.getPrice());
        if (src.getItemCode() != null) dest.setItemCode(src.getItemCode());
        if (src.getTimeOrdering() != null) dest.setTimeOrdering(src.getTimeOrdering());
        if (src.getTimeProcurement() != null) dest.setTimeProcurement(src.getTimeProcurement());
        if (src.getTimeTransporation() != null) dest.setTimeTransporation(src.getTimeTransporation());
        if (src.getTimeBuffer() != null) dest.setTimeBuffer(src.getTimeBuffer());
        if (src.getUomPurchase() != null) dest.setUomPurchase(src.getUomPurchase());
        if (src.getUomConsumption() != null) dest.setUomConsumption(src.getUomConsumption());
        if (src.getConversionFactor() != null) dest.setConversionFactor(src.getConversionFactor());
        if (src.getMinOrderQty() != null) dest.setMinOrderQty(src.getMinOrderQty());
        if (src.getPacketSize() != null) dest.setPacketSize(src.getPacketSize());
        if (src.getClassType() != null) dest.setClassType(src.getClassType());
        if (src.getKanbanType() != null) dest.setKanbanType(src.getKanbanType());
        if (src.getBinQty() != null) dest.setBinQty(src.getBinQty());
        if (src.getNoOfBins() != null) dest.setNoOfBins(src.getNoOfBins());
        if (src.getDemand() != null) dest.setDemand(src.getDemand());
    }

    /**
     * Print barcode for specific Bin.
     * @param id Product id
     * @param bins comma separated bin numbers
     * @return pdf containing cards as Resource
     */
    public Resource printBarcode(Long id, String bins){
        Product product = productRepository.findOne(id);
        return MiscUtil.generateBarcodePdf(product,bins);
    }

    /**
     * Print barcode for all Bins.
     * @param id Product id
     * @return pdf containing cards as Resource
     */
    public Resource printBarcode(Long id){
        Product product = productRepository.findOne(id);
        return MiscUtil.generateBarcodePdf(product);
    }

}
