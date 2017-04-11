package controllers;

import static play.libs.Json.*;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Query;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.util.*;

import javax.inject.Inject;

import models.Product;
import play.mvc.*;
import play.mvc.Http.Request;
import play.mvc.Results.*;
import play.mvc.Result.*;

import java.io.*;

public class Micro extends Controller {
	
	EbeanServer serv;
	public Micro(){
		// Ebean singleton doesnt work
		serv = Ebean.getServer("Ebeanus");
	}
	// validation of object : http://docs.oracle.com/javaee/6/tutorial/doc/gircz.html
	// This method parses json request and inserts product into db (on secondary server)
	public Result microAddProduct()
	{
		List<Product> products = new ArrayList<Product>(200);		
		JsonNode node = Controller.request().body().asJson();
		try{
			
			ObjectMapper mapp = new ObjectMapper();
						
			for(JsonNode curr : node){
				String cstr = curr.toString();
				Product product = mapp.readValue(cstr, Product.class);
				product.save();
			}
			

		} catch (Exception ex){
			System.out.println(ex.getMessage());
			return ok("AddProduct - exception.");
		}
		
		return ok(toJson(products));
		
	}
	   
	// Method for retrieving product on secondary server
	public Result microQueryProduct()
	{
		JsonNode nodes = Controller.request().body().asJson();
		ObjectMapper map = new ObjectMapper();
		List<Product> allproducts = new ArrayList<Product>(200);
		String result = null;
		
		try{
			for(JsonNode curr : nodes)
			{
				Product requested = map.readValue(curr.toString(), Product.class);
				if(requested == null)
					continue;
								
				Query<Product> query = serv.find(Product.class);
				List<Product> products = query.where().eq("name",requested.name).findList();
				if(products != null)
					allproducts.addAll(products);

			}
		} catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		
		try{
			ObjectWriter ow = map.writerFor(new TypeReference<List<Product>>(){});
			result = ow.writeValueAsString(allproducts);
		} catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		
		return ok(result).as("application/json");
	}
	
	// Method for inserting products on primary server
	public int microInsertProduct()
	{
		
		return 0;
	}
	
	public Result microShowProduct()
	{
		return ok("empty");
	}
}
