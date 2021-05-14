package ma;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jodd.io.FileUtil;
import jodd.io.NetUtil;
import jodd.jerry.Jerry;
import jodd.lagarto.dom.Node;
import jodd.madvoc.WebApplication;

public class getRadioInfo {

	 
	public getRadioInfo() {
 
	}


	private static String TEMP_ = "C:\\Users\\Pc-youssef\\Downloads\\Test\\src\\ma";
	
 
	public  String getInfo(String RADIOS_PAGE )  throws IOException  {
 
		 
		String description = "" ;
		String like = "" ;
		String web = "" ;
		String tele ="" ;
		String address ="";
		String email ="";
		int countContac =0;
		int index =2;
		
		
	 
		        // download the page super-efficiently
		        File file = new File(TEMP_, "google.html");
		        NetUtil.downloadFile(RADIOS_PAGE , file);
		
		        // create Jerry, i.e. document context
		        Jerry doc = Jerry.jerry(FileUtil.readString(file));
		
		        // remove div for toolbar
		        Node[]  nodesDesc = doc.$("div.station__description").get();
		        Node[]  nodesWeb = doc.$("a.station__reference--web").get(); 
		        Node[]  nodeslik = doc.$("span.station__actions__pill").get(); 
		        Node[]  nodesContact = doc.$("section.station-description").get();
		       
		     
				 
			  
			    	 
					
				 
		        try {  
		        	 if (nodesContact[0].getChildElementsCount() == 3)  index = 2;
				        else   index =5;
		        description = nodesDesc[0].getTextContent().trim().replace("'", "\\'") ;
		        web = nodesWeb[0].getAttribute("href").trim() ;
		        like = nodeslik[0].getChildElement(1).getTextContent().trim();
		        	countContac = nodesContact[0].getChildElement(2).getChildElementsCount() ; 
		        	
		        	for (int j = 0; j < countContac; j++) {
		        		 
						if (nodesContact[0].getChildElement(index).getChildElement(j).getAttribute("itemprop").equals("address" )    ) {
							address =  nodesContact[j].getChildElement(index).getChildElement(0).getTextContent().replace("Address:", "").trim().replace("'", "\\'")  ;
			        		//System.out.println( address  );
			        		 
						}
						else if (nodesContact[0].getChildElement(index).getChildElement(j).getAttribute("itemprop").equals("telephone")  ) {
							tele = nodesContact[0].getChildElement(index).getChildElement(j).getTextContent().replace("Phone:", "").trim();
			        		//System.out.println(  tele );
			        		 
						}
						else if (nodesContact[0].getChildElement(index).getChildElement(j).getAttribute("itemprop").equals("email") ) {
							email = nodesContact[0].getChildElement(index).getChildElement(j).getChildElement(1).getTextContent().trim() ;
			        		//System.out.println("___Email  : "+  email);
			        		 
						}	
				 
					}
		        	
		        	
				 
		             
				} catch (Exception e) {
					// TODO: handle exception
				}
		         
				 
		       
		        
		      
		         
		        
	 return  String.format(" '%s' , %s ,'%s' ,'%s' , '%s', '%s' " ,description ,like ,web ,tele ,address ,email);   
		 

   }
 
	

}
