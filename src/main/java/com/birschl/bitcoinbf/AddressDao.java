package com.birschl.bitcoinbf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Component
public class AddressDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // CREATE SCHEMA `bitcoin` DEFAULT CHARACTER SET ascii ;


    public void persist(String address) {
        if (address.isEmpty())
            return;
        try {
            jdbcTemplate.update("INSERT INTO addresses (address) VALUES (?)", address);
        } catch (DuplicateKeyException e) {
            // Do nothing
        }
    }

    public boolean contains(String address) {
        String sql = "SELECT * FROM addresses WHERE address = '?'";
        String result = jdbcTemplate.queryForObject(sql, new Object[]{address}, String.class);
        return result == null ? false : true;
    }

    public void dropAndCreateDb() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS addresses");
        jdbcTemplate.execute("CREATE TABLE addresses(address VARCHAR(34))");
        // jdbcTemplate.execute("CREATE TABLE addresses(address VARCHAR(34) NOT NULL UNIQUE)");
        //  jdbcTemplate.execute("CREATE INDEX address_id ON addresses(address)");
    }

    // TODO The database table needs to be made distinct. But doing this with an unique column is not performant
    public void persistAll(List<String> addrList) {
        String sql = "INSERT INTO addresses (address) VALUES (?) ON DUPLICATE KEY UPDATE address=address";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, addrList.get(i));
            }

            @Override
            public int getBatchSize() {
                return addrList.size();
            }
        });
    }
}
