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
	public GetListPriceResponse getListPrice(@RequestPayload GetListPriceRequest request) {

		System.out.println(request.getMessage());
		ObjectFactory objectFactory = new ObjectFactory();
		GetListPriceResponse priceResponse = objectFactory.createGetListPriceResponse();

		List<Price> priceArrayList = priceService.getListPrice();

		priceResponse.getPrice().addAll(priceArrayList);

		return priceResponse;
	}


	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "createPriceRequest")
	@ResponsePayload
	public CreatePriceResponse createPrice(@RequestPayload CreatePriceRequest request) {

		ObjectFactory objectFactory = new ObjectFactory();
		CreatePriceResponse priceResponse = objectFactory.createCreatePriceResponse();

		Price price = new Price();
		price.setIdShoes(request.getIdShoes());
		price.setPriceEu(request.getPriceEu());
		price.setPriceRu(request.getPriceRu());

		priceService.createPrice(price);

		Price price1 = priceService.getPrice(request.getIdShoes());

		priceResponse.setPrice(price1);

		return priceResponse;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "deletePriceRequest")
	@ResponsePayload
	public DeletePriceResponse deletePrice(@RequestPayload DeletePriceRequest request) {

		ObjectFactory objectFactory = new ObjectFactory();
		DeletePriceResponse priceResponse = objectFactory.createDeletePriceResponse();

		priceService.deletePrice(request.getIdPrice());
		priceResponse.setMessage("Ok");
		return priceResponse;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "updatePriceRequest")
	@ResponsePayload
	public UpdatePriceResponse createPrice(@RequestPayload UpdatePriceRequest request) {

		ObjectFactory objectFactory = new ObjectFactory();
		UpdatePriceResponse priceResponse = objectFactory.createUpdatePriceResponse();

		Price price = new Price();
		price.setId(request.getId());
		price.setIdShoes(request.getIdShoes());
		price.setPriceEu(request.getPriceEu());
		price.setPriceRu(request.getPriceRu());
		priceService.updatePrice(price);

		price = priceService.getPrice(price.getIdShoes());
		priceResponse.setPrice(price);
		return priceResponse;
	}
}
