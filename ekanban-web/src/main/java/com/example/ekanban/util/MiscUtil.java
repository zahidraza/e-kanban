package com.example.ekanban.util;

import com.example.ekanban.entity.Product;
import com.example.ekanban.enums.ClassType;
import com.example.ekanban.enums.KanbanType;
import com.example.ekanban.exception.MailException;
import com.example.ekanban.storage.BarcodeService;
import com.example.ekanban.storage.StorageProperties;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.sun.mail.util.MailConnectException;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Year;
import java.time.YearMonth;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class MiscUtil {
    private static final Logger logger = LoggerFactory.getLogger(MiscUtil.class);

    private static Map<String,Integer> mapMonth = new HashedMap();
    private static final Font fontHeader;
    private static final Font fontMedium;
    private static final Font fontSmall;

    private static final char[] symbols;
    private static final Random random = new Random();

    static {
        StringBuilder tmp = new StringBuilder();
        for (char ch = '0'; ch <= '9'; ++ch)
            tmp.append(ch);
        for (char ch = 'a'; ch <= 'z'; ch++)
            tmp.append(ch);
        for (char ch = 'A'; ch <= 'Z'; ch++)
            tmp.append(ch);
        symbols = tmp.toString().toCharArray();

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

        fontHeader = new Font();
        fontHeader.setSize(10f);
        fontHeader.setStyle(Font.BOLD);
        fontMedium = new Font();
        fontMedium.setSize(6f);
        fontMedium.setStyle(Font.BOLD);
        fontSmall = new Font();
        fontSmall.setSize(6f);
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

    public static Resource generateBarcodePdf(Product product){
        String productId = getProductId(product.getSubCategory().getCategory().getId(),product.getSubCategory().getId(),product.getId());
        Document document = new Document(PageSize.A4);
        StorageProperties properties = ApplicationContextUtil.getApplicationContext().getBean(StorageProperties.class);
        BarcodeService barcodeService = ApplicationContextUtil.getApplicationContext().getBean(BarcodeService.class);
        Path root = Paths.get(properties.getLocation());
        try {
            PdfWriter docWriter = PdfWriter.getInstance(document, new FileOutputStream(root.resolve(productId + ".pdf").toFile()));
            document.open();
            PdfPTable mainTable = new PdfPTable(2);
            mainTable.setWidthPercentage(100f);

            PdfPCell cell = null;
            for (int i = 1; i <= product.getNoOfBins(); i++){
                cell = new PdfPCell();
                cell.addElement(getTable(docWriter,product,i));
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setPadding(15f);
                cell.setPaddingBottom(40f);
                mainTable.addCell(cell);

            }
            if (product.getNoOfBins()%2 == 1){
                cell = new PdfPCell();
                cell.setBorder(PdfPCell.NO_BORDER);
                mainTable.addCell(cell);
            }
            document.add(mainTable);
            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return barcodeService.loadAsResource(productId + ".pdf");
    }

    public static Resource generateBarcodePdf(Product product, String bins){
        String productId = getProductId(product.getSubCategory().getCategory().getId(),product.getSubCategory().getId(),product.getId());
        Document document = new Document(PageSize.A4);
        StorageProperties properties = ApplicationContextUtil.getApplicationContext().getBean(StorageProperties.class);
        BarcodeService barcodeService = ApplicationContextUtil.getApplicationContext().getBean(BarcodeService.class);
        Path root = Paths.get(properties.getLocation());
        try {
            PdfWriter docWriter = PdfWriter.getInstance(document, new FileOutputStream(root.resolve(productId + ".pdf").toFile()));
            document.open();
            PdfPTable mainTable = new PdfPTable(2);
            mainTable.setWidthPercentage(100f);

            PdfPCell cell = null;
            String[] binArray = bins.trim().split(",");
            for (int i = 0; i < binArray.length; i++){
                String bin = binArray[i];
                if (bin.trim().length() != 0){
                    cell = new PdfPCell();
                    cell.addElement(getTable(docWriter,product,Integer.parseInt(bin)));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setPadding(15f);
                    cell.setPaddingBottom(40f);
                    mainTable.addCell(cell);
                }

            }
            if (binArray.length%2 == 1){
                cell = new PdfPCell();
                cell.setBorder(PdfPCell.NO_BORDER);
                mainTable.addCell(cell);
            }
            document.add(mainTable);
            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return barcodeService.loadAsResource(productId + ".pdf");
    }

    /**
     * Send Email.
     * @param to
     * @param sub
     * @param msg
     * @return
     */
    public static Boolean sendMail(String to,String sub,String msg){
        Properties props = ConfigUtil.getConfigProperties();
        String host = props.getProperty(Constants.MAIL_HOST);
        String user = props.getProperty(Constants.MAIL_USER);
        String password = props.getProperty(Constants.MAIL_PASSWORD);
        //logger.debug("mail.host = {}, mail.user = {}, mail.password = {}", host,user,password);

        Boolean status = false;

        Properties prop = new Properties();

        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.auth",true);

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user,password);
                    }
                });

        //Compose the message
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
            message.setSubject(sub);
            message.setText(msg);

            //send the message
            Transport.send(message);

            status = true;
        } catch (MailConnectException e1) {
            e1.printStackTrace();
            throw new MailException("Unable to send mail. Check your internet connection or try again later.");
        } catch (MessagingException e2) {
            e2.printStackTrace();
            throw new MailException(e2.getMessage());
        }

        return status;
    }

    /**
     * Get random string consisting of alphabets and numbers.
     * @param length length of random string
     * @return
     */
    public static String getRandomString(int length) {
        char[] buf = new char[length];
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        return new String(buf);
    }

    private static String getBinId(Long categoryId,Long subCategoryId, Long productId,int binNo) {
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

    private static String getProductId(Long categoryId,Long subCategoryId, Long productId) {
        String cIdStr = String.valueOf(categoryId);
        String scIdStr = String.valueOf(subCategoryId);
        String pIdStr = String.valueOf(productId);

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

        String result = 'P' + cIdStr + scIdStr + pIdStr;
        return result;
    }

    private static PdfPTable getTable(PdfWriter docWriter, Product product, int binNo) throws DocumentException {
        String binId = getBinId(product.getSubCategory().getCategory().getId(),product.getSubCategory().getId(),product.getId(),binNo);

        PdfPTable table = new PdfPTable(4);
        table.setWidths(new float[]{1f,1.5f,.7f,.8f});
        table.setWidthPercentage(100);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);

        Barcode128 code128 = new Barcode128();
        code128.setCode(binId.trim());
        PdfContentByte cb = docWriter.getDirectContent();

        code128.setCodeType(Barcode128.CODE128);
        Image code128Image = code128.createImageWithBarcode(cb, null, null);
        code128Image.scalePercent(300f,100f);

        PdfPCell cell = null;
            /*Row 1*/
        cell = new PdfPCell(new Phrase("e-Kanban",fontHeader));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5f);
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(product.getKanbanType().equals(KanbanType.N_BIN) ? "N-bin": "2-bin",fontHeader));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5f);
        cell.setColspan(2);
        table.addCell(cell);

            /*Row 2*/
        table.addCell(getCellContent("CATEGORY",product.getSubCategory().getCategory().getName(),Element.ALIGN_LEFT));
        cell = new PdfPCell(getCellContent("PRODUCT NAME",product.getName(),Element.ALIGN_CENTER));
        cell.setRowspan(2);
        table.addCell(cell);
        String classType = product.getClassType().equals(ClassType.CLASS_A) ? "A" : (product.getClassType().equals(ClassType.CLASS_B) ? "B" : "C");
        table.addCell(getCellContent("ITEM CLASS", classType,Element.ALIGN_CENTER));
        table.addCell(getCellContent("ITEM CODE",product.getItemCode(),Element.ALIGN_CENTER));

            /*Row 3*/
        table.addCell(getCellContent("SUB CATEGORY",product.getSubCategory().getName(),Element.ALIGN_LEFT));
        table.addCell(getCellContent("UOM",product.getUomPurchase(),Element.ALIGN_CENTER));
        String pktSize = product.getPacketSize().doubleValue() > 1.0 ? String.valueOf(product.getPacketSize().intValue()) : String.valueOf(product.getPacketSize());
        table.addCell(getCellContent("PACKET SIZE",pktSize,Element.ALIGN_CENTER));

            /*Row last*/
        cell = getCellContent("CARD",""+binNo,Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setImage(code128Image);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setFixedHeight(25f);
        table.addCell(cell);

        cell = getCellContent("LEAD TIME",String.valueOf(product.getTotalLeadTime()),Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
        cell = getCellContent("BIN SIZE",String.valueOf(product.getBinQty()),Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
        return table;
    }

    private static PdfPCell getCellContent(String heading,String content,int alignment){
        PdfPCell cell = null;
        Paragraph paragraph = new Paragraph();
        paragraph.add(new Phrase(heading,fontMedium));
        paragraph.add(new Phrase("\n"+content,fontSmall));
        cell = new PdfPCell(paragraph);
        cell.setHorizontalAlignment(alignment);
        cell.setPadding(2f);
        cell.setFixedHeight(25f);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

}
