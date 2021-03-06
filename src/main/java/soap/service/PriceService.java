package soap.service;

import model.price.*;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class PriceService {

    private DataSource ds;
    private JdbcTemplate jdbcTemplateObject;

    public void setDataSource(DataSource dataSource) {
        this.ds = dataSource;
        this.jdbcTemplateObject = new JdbcTemplate(ds);
    }

    public Price getPrice(int idShoes){
        List<Price> price = jdbcTemplateObject.query("call select_price(?)", new Object[]{idShoes}, new PriceMapper());
        if (price.isEmpty()) {
            return null;
        } else {
            return price.get(0);
        }
    }

    public List<Price> getListPrice(){
        List<Price> prices = jdbcTemplateObject.query("call selectAll_price()", new PriceMapper());
        return prices;
    }

    public List<Price> getListPriceByMinMax(int min, int max){
        List<Price> prices = jdbcTemplateObject.query("call select_price_byMinMax(?,?)",new Object[]{min,max},new PriceMapper());
        return prices;
    }

    public void createPrice(Price price){
        jdbcTemplateObject.update("call create_price(?,?,?)", price.getIdShoes(), price.getPriceEu(),price.getPriceRu());
    }

    public void deletePrice(int id){
        jdbcTemplateObject.update("call delete_price(?)",id);
    }

    public void updatePrice(Price price){
        jdbcTemplateObject.update("call update_price(?,?,?)",price.getIdShoes(),price.getPriceEu(),price.getPriceRu());
    }
}
