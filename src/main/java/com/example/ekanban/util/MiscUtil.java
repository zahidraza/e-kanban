package com.example.ekanban.util;

import com.example.ekanban.entity.Product;
import com.example.ekanban.storage.BarcodeService;
import com.example.ekanban.storage.StorageProperties;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.core.io.Resource;

import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Year;
import java.time.YearMonth;
import java.util.*;
import java.util.List;
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

    public static Resource createBarcodePdf(Product product, int binNo){
        String binId = getBinId(product.getSubCategory().getCategory().getId(),product.getSubCategory().getId(),product.getId(),binNo);
        System.out.println("Bin Id = " + binId);
        Document document = new Document(PageSize.A5);

        StorageProperties properties = ApplicationContextUtil.getApplicationContext().getBean(StorageProperties.class);
        BarcodeService barcodeService = ApplicationContextUtil.getApplicationContext().getBean(BarcodeService.class);

        Path root = Paths.get(properties.getLocation());
        System.out.println(root.getFileName());
        try {

            PdfWriter docWriter = PdfWriter.getInstance(document, new FileOutputStream(root.resolve(binId + ".pdf").toFile()));

            document.open();

            PdfPTable table1 = new PdfPTable(3);

            table1.setWidthPercentage(75);
            float[] columnWidth = {2f, 2f, 2f};
            table1.setWidths(columnWidth);

            Barcode128 code128 = new Barcode128();
            code128.setCode(binId.trim());
            PdfContentByte cb = docWriter.getDirectContent();

            code128.setCodeType(Barcode128.CODE128);
            Image code128Image = code128.createImageWithBarcode(cb, null, null);
            code128Image.setAbsolutePosition(10, 700);
            code128Image.scalePercent(125);
            PdfPCell cell = null;
            cell = new PdfPCell(new Phrase(product.getName()));

            //cell.setImage(code128Image);
            cell.setColspan(3);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase(product.getSubCategory().getCategory().getName()));
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(product.getSubCategory().getName()));
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase(""));
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase(""));
            table1.addCell(cell);
            cell = new PdfPCell();
            cell.setColspan(2);
            cell.setPadding(10);
            cell.setPaddingLeft(30);
            cell.setPaddingRight(30);
            cell.setImage(code128Image);
            table1.addCell(cell);


            for (int i = 1; i <= 6; i++) {
                PdfPCell cell1 = new PdfPCell();

                cell.setImage(code128Image);

                table1.addCell(cell1);
            }

            Paragraph paragraph = new Paragraph("Heading of Page\n\n");
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
            document.add(table1);
            document.add(new Paragraph("\n\n"));
            //document.add(table2);
            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return barcodeService.loadAsResource(binId + ".pdf");
    }

    public static String getBinId(Long categoryId,Long subCategoryId, Long productId,int binNo) {
        String cIdStr = String.valueOf(categoryId);
        String scIdStr = String.valueOf(subCategoryId);
        String pIdStr = String.valueOf(productId);
        String binStr = String.valueOf(binNo);

        if (cIdStr.length() == 1) {
            cIdStr = "0" + cIdStr;
        }

        if (scIdStr.length() == 1) {
            scIdStr = "00" + scIdStr;
        }else if (scIdStr.length() == 2) {
            scIdStr = "0" + scIdStr;
        }

        int l = pIdStr.length();
        l = 5 - l;
        String prefix = "";
        for (int i = 0; i < l ; i++) {
            prefix = prefix + '0';
        }
        pIdStr = prefix + pIdStr;

        if (binStr.length() == 1) {
            binStr = "0" + binStr;
        }

        String result = 'B' + cIdStr + scIdStr + pIdStr + binStr;

        return result;
    }
}
