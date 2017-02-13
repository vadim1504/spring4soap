package soap.endpoint;

import model.price.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import soap.exception.ServiceFault;
import soap.exception.ServiceFaultException;
import soap.service.PriceService;

import java.util.List;

@Endpoint
public class PriceEndpoint {

	@Autowired
	private PriceService priceService;
	private static ObjectFactory objectFactory = new ObjectFactory();
	private static final Logger logger = Logger.getLogger(PriceEndpoint.class);
	public static final String NAMESPACE_URI = "http://peopleShoes.com/soap/price";

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getPriceRequest")
	@ResponsePayload
	public GetPriceResponse getPrice(@RequestPayload GetPriceRequest request) {
		logger.info("getting price with id_shoes: "+request.getIdShoes());
		Price price = priceService.getPrice(request.getIdShoes());
		if (price == null) {
			logger.info("not fount price with id_shoes: "+request.getIdShoes());
			throw new ServiceFaultException("ERROR", new ServiceFault(
					"NOT_FOUND", "Price with id_shoes: " + request.getIdShoes() + " not found."));
		}
		GetPriceResponse priceResponse = objectFactory.createGetPriceResponse();
		priceResponse.setPrice(price);
		return priceResponse;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getListPriceRequest")
	@ResponsePayload
	public GetListPriceResponse getListPrice() {
		logger.info("getting all price");
		List<Price> priceArrayList = priceService.getListPrice();
		if (priceArrayList == null) {
			logger.info("not fount prices");
			throw new ServiceFaultException("ERROR", new ServiceFault(
					"NOT_FOUND", "Price List not found."));
		}
		GetListPriceResponse priceResponse = objectFactory.createGetListPriceResponse();
		priceResponse.getPrice().addAll(priceArrayList);
		return priceResponse;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getListPriceByMinMaxRequest")
	@ResponsePayload
	public GetListPriceResponse getListPriceByMinMax(@RequestPayload GetListPriceByMinMaxRequest request) {
		logger.info("getting price with minPrice: "+request.getMin()+" maxPrice: "+request.getMax());
		List<Price> priceArrayList = priceService.getListPriceByMinMax(request.getMin(),request.getMax());
		if (priceArrayList == null) {
			logger.info("not fount prices");
			throw new ServiceFaultException("ERROR", new ServiceFault(
					"NOT_FOUND", "PriceList with min: " + request.getMin() + "& max: "+request.getMax()+" not found."));
		}
		GetListPriceResponse priceResponse = objectFactory.createGetListPriceResponse();
		priceResponse.getPrice().addAll(priceArrayList);
		return priceResponse;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "createPriceRequest")
	@ResponsePayload
	public void createPrice(@RequestPayload CreatePriceRequest request) {
		logger.info("create new price");
		Price price = new Price();
		price.setIdShoes(request.getIdShoes());
		price.setPriceEu(request.getPriceEu());
		price.setPriceRu(request.getPriceRu());
		priceService.createPrice(price);
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "deletePriceRequest")
	@ResponsePayload
	public void deletePrice(@RequestPayload DeletePriceRequest request) {
		logger.info("delete price with id_shoes: "+request.getIdShoes());
		if(priceService.getPrice(request.getIdShoes())==null){
			logger.info("not fount price with id_shoes: "+request.getIdShoes());
			throw new ServiceFaultException("ERROR", new ServiceFault(
					"NOT_FOUND", "Price not found."));
		}
		priceService.deletePrice(request.getIdShoes());
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "updatePriceRequest")
	@ResponsePayload
	public void updatePrice(@RequestPayload UpdatePriceRequest request) {
		logger.info("update price with id_shoes: "+request.getIdShoes());
		if(priceService.getPrice(request.getIdShoes())==null){
			logger.info("not fount price with id_shoes: "+request.getIdShoes());
			throw new ServiceFaultException("ERROR", new ServiceFault(
					"NOT_FOUND", "Price not found."));
		}
		Price price = new Price();
		price.setIdShoes(request.getIdShoes());
		price.setPriceEu(request.getPriceEu());
		price.setPriceRu(request.getPriceRu());
		priceService.updatePrice(price);

	}
}
