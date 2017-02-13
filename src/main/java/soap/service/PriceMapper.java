package soap.service;


import model.price.Price;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PriceMapper implements RowMapper<Price> {

    public Price mapRow(ResultSet resultSet, int i) throws SQLException {
        Price price = new Price();
        price.setIdShoes(resultSet.getInt("id_shoes"));
        price.setPriceEu(resultSet.getDouble("priceEU"));
        price.setPriceRu(resultSet.getDouble("priceRU"));
        return price;
    }
}
