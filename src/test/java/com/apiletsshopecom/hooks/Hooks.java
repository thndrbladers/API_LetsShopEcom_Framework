package com.apiletsshopecom.hooks;

import io.cucumber.java.After;

import com.apiletsshopecom.clients.ProductClient;
import com.apiletsshopecom.payloads.response.DeleteProductResponse;
import com.letsshopecom.utils.ScenarioContext;

public class Hooks {
	private ScenarioContext context;

	public Hooks(ScenarioContext context) {
		this.context = context;
	}

	@After("@AddProduct")
	public void deleteProduct() {
		ProductClient prodClient = new ProductClient();
		String productId = context.getRawResponse().jsonPath().getString("productId");
		System.out.println(productId);
		DeleteProductResponse deleteProduct = prodClient.deleteProductResponse(productId);
		System.out.println("Deleted the Product : " + productId + deleteProduct.toString());

	}

}
