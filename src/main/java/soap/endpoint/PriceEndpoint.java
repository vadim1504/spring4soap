package soap.endpoint;

import model.price.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import soap.service.PriceService;

import java.util.List;

@Endpoint
public class PriceEndpoint {

	private static final String NAMESPACE_URI = "http://peopleShoes.com/soap/price";

	@Autowired
	private PriceService priceService;

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getPriceRequest")
	@ResponsePayload
	public GetPriceResponse getPrice(@RequestPayload GetPriceRequest request) {

		ObjectFactory objectFactory = new ObjectFactory();
		GetPriceResponse priceResponse = objectFactory.createGetPriceResponse();

		priceResponse.setPrice(priceService.getPrice(request.getIdShoes()));

		return priceResponse;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getListPriceRequest")
	@ResponsePayload
	public GetListPriceResponse getListPrice() {

		ObjectFactory objectFactory = new ObjectFactory();
		GetListPriceResponse priceResponse = objectFactory.createGetListPriceResponse();

		List<Price> priceArrayList = priceService.getListPrice();

		priceResponse.getPrice().addAll(priceArrayList);

		return priceResponse;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getListPriceByMinMaxRequest")
	@ResponsePayload
	public GetListPriceResponse getListPriceByMinMax(@RequestPayload GetListPriceByMinMaxRequest request) {

		ObjectFactory objectFactory = new ObjectFactory();
		GetListPriceResponse priceResponse = objectFactory.createGetListPriceResponse();

		List<Price> priceArrayList = priceService.getListPriceByMinMax(request.getMin(),request.getMax());

		priceResponse.getPrice().addAll(priceArrayList);

		return priceResponse;
	}


	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "createPriceRequest")
	@ResponsePayload
	public void createPrice(@RequestPayload CreatePriceRequest request) {

		Price price = new Price();
		price.setIdShoes(request.getIdShoes());
		price.setPriceEu(request.getPriceEu());
		price.setPriceRu(request.getPriceRu());

		priceService.createPrice(price);


	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "deletePriceRequest")
	@ResponsePayload
	public void deletePrice(@RequestPayload DeletePriceRequest request) {

		priceService.deletePrice(request.getIdPrice());

	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "updatePriceRequest")
	@ResponsePayload
	public void updatePrice(@RequestPayload UpdatePriceRequest request) {

		Price price = new Price();
		price.setId(request.getId());
		price.setIdShoes(request.getIdShoes());
		price.setPriceEu(request.getPriceEu());
		price.setPriceRu(request.getPriceRu());
		priceService.updatePrice(price);

	}
}
