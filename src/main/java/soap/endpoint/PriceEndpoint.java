package soap.endpoint;

import model.createPrice.Price;
import model.getPrice.ObjectFactory;
import model.getPrice.PriceRequest;
import model.getPrice.PriceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import soap.service.PriceService;

@Endpoint
public class PriceEndpoint {

	private static final String NAMESPACE_URI = "http://peopleShoes.com/soap";
	private static final String NAMESPACE_URI_2 = "http://peopleShoes.com/soap/create";

	@Autowired
	private PriceService priceService;

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "PriceRequest")
	@ResponsePayload
	public PriceResponse getPrice(@RequestPayload PriceRequest request) {

		ObjectFactory objectFactory = new ObjectFactory();
		PriceResponse priceResponse = objectFactory.createPriceResponse();

		priceResponse.setPrice(priceService.getPrice(request.getIdShoes()));

		return priceResponse;
	}

	@PayloadRoot(namespace = NAMESPACE_URI_2, localPart = "PriceRequest")
	@ResponsePayload
	public model.createPrice.PriceResponse createPrice(@RequestPayload model.createPrice.PriceRequest request) {

		model.createPrice.ObjectFactory objectFactory = new model.createPrice.ObjectFactory();
		model.createPrice.PriceResponse priceResponse = objectFactory.createPriceResponse();

		Price price = new Price();
		price.setIdShoes(request.getIdShoes());
		price.setPriceEu(request.getPriceEu());
		price.setPriceRu(request.getPriceRu());

		priceService.createPrice(price);

		model.getPrice.Price price1 = priceService.getPrice(request.getIdShoes());

		price.setId(price1.getId());
		price.setIdShoes(price1.getIdShoes());
		price.setPriceEu(price1.getPriceEu());
		price.setPriceRu(price1.getPriceRu());

		priceResponse.setPrice(price);

		return priceResponse;
	}
}
