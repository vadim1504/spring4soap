package soap.service;

import model.getPrice.Price;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PriceService {

    private DataSource ds;
    private JdbcTemplate jdbcTemplateObject;

    public void setDataSource(DataSource dataSource) {
        this.ds = dataSource;
        this.jdbcTemplateObject = new JdbcTemplate(ds);
    }

    public Price getPrice(int idShoes){
        Price price = jdbcTemplateObject.queryForObject("call select_price(?)", new Object[]{idShoes}, new RowMapper<Price>() {
            public Price mapRow(ResultSet resultSet, int i) throws SQLException {
                Price price = new Price();
                price.setId(resultSet.getInt("id"));
                price.setIdShoes(resultSet.getInt("id_shoes"));
                price.setPriceEu(resultSet.getDouble("priceEU"));
                price.setPriceRu(resultSet.getDouble("priceRU"));
                return price;
            }
        });
        return price;
    }

    public void createPrice(model.createPrice.Price price){
        jdbcTemplateObject.update("call create_price(?,?,?)", price.getIdShoes(), price.getPriceEu(),price.getPriceRu());
    }
}
