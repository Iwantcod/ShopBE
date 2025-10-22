package com.example.shopPJT;

import java.sql.*;
import java.time.LocalDate;
import java.util.Random;

public class BulkInsert {
    public static void main(String[] args) {
        String url = "jdbc:mariadb://121.174.224.9:61010/shop?useUnicode=true&characterEncoding=utf8&sslMode=trust&rewriteBatchedStatements=true";
        String user = "shop_admin";
        String password = "dk41l@0_1!";

        int batchSize = 100_000;   // 한 번에 10만 건
        int total = 10_000_000;    // 총 1천만 건
        int startProductId = 300;

        Random rand = new Random();

        String sql = "INSERT INTO product " +
                "(category_id, created_at, inventory, is_deleted, price, volume, logicalfk, product_id, user_id, description_image_url, name, product_image_url) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection c = DriverManager.getConnection(url, user, password)) {
            c.setAutoCommit(false);
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                Random r = new Random(42);
                int cnt = 0;
                for (int i = 0; i < total; i++) {
                    int pid = startProductId + i;

                    ps.setInt(1, 1);
                    ps.setDate(2, Date.valueOf(LocalDate.now().minusDays(r.nextInt(90))));
                    ps.setInt(3, r.nextInt(1000));
                    ps.setInt(4, 0);
                    ps.setInt(5, r.nextInt(500_000) + 1000);
                    ps.setInt(6, r.nextInt(2000) + 100);
                    ps.setLong(7, 2);
                    ps.setLong(8, pid);
                    ps.setLong(9, 1);
                    ps.setNull(10, Types.VARCHAR);
                    ps.setString(11, "Dummy Product " + pid);
                    ps.setNull(12, Types.VARCHAR);

                    ps.addBatch();
                    if (++cnt % batchSize == 0) {
                        ps.executeBatch();
                        c.commit();
                        ps.clearBatch();
                        System.out.printf("Inserted %,d rows%n", cnt);
                    }
                }
                ps.executeBatch();
                c.commit();
                System.out.println("DONE: " + cnt);
            } catch (BatchUpdateException bue) {
                System.err.println("Batch failed. UpdateCounts length=" + bue.getUpdateCounts().length);
                printSqlEx(bue);
                c.rollback();
            } catch (SQLException se) {
                printSqlEx(se);
                c.rollback();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    static void printSqlEx(SQLException ex) {
        for (Throwable t = ex; t != null; t = ((SQLException)t).getNextException()) {
            if (t instanceof SQLException s) {
                System.err.printf("SQLState=%s ErrorCode=%d Message=%s%n",
                        s.getSQLState(), s.getErrorCode(), s.getMessage());
            } else {
                t.printStackTrace();
            }
        }
    }
}
