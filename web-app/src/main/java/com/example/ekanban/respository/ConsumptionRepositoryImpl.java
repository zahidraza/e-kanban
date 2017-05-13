package com.example.ekanban.respository;

import com.example.ekanban.util.MiscUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by razamd on 4/14/2017.
 */
@Component
public class ConsumptionRepositoryImpl {

    @Autowired
    DataSource dataSource;

    public Long findMaxConsumptionOfLastYear(Long productId){
        Connection conn = null;
        int currYear = MiscUtil.getCurrentYear();
        int currMonth = MiscUtil.getCurrentMonth();
        Long result = 0L;
        try {
            conn = dataSource.getConnection();
            String formatStr = "select max(value) as max_value from ekanban.consumption \n "+
                            "   where (product_id = ? and year = %d and month <= %d) or (product_id = ? and year = %d and month > %d)";

            String sql = String.format(formatStr, currYear, currMonth, currYear - 1, currMonth);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1,productId);
            ps.setLong(2,productId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                result = rs.getLong("max_value");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public Long findAvgConsumptionOfLastYear(Long productId){
        Connection conn = null;
        int currYear = MiscUtil.getCurrentYear();
        int currMonth = MiscUtil.getCurrentMonth();
        Long result = 0L;
        try {
            conn = dataSource.getConnection();
            String formatStr = "select avg(value) as avg_value from ekanban.consumption \n "+
                    "   where (product_id = ? and year = %d and month <= %d) or (product_id = ? and year = %d and month > %d)";

            String sql = String.format(formatStr, currYear, currMonth, currYear - 1, currMonth);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1,productId);
            ps.setLong(2,productId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                result = rs.getLong("avg_value");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
