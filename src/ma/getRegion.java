package ma;
 

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;



import jodd.io.FileUtil;
import jodd.io.NetUtil;
import jodd.jerry.Jerry;
import jodd.lagarto.dom.Node;

public class getRegion {
	
	private static String TEMP_ = "C:\\Users\\Pc-youssef\\Downloads\\Test\\src\\ma";
	// params 
	
	private static String SCRIPTS_DIR = "D:\\ProjectRadio\\Scripts\\"; 
	private static String RADIOS_PAGE = "https://onlineradiobox.com/it/";
   private static int id_countr_ =4 ;


	public static void main(String[] args) throws IOException {

		int counter = 1;
		int validCounter = 0;
		if(args.length == 3){ 
			RADIOS_PAGE = args[1]; 
			System.out.println(args[1]);
			System.out.println( RADIOS_PAGE);
			 
		}
		 
		
		 
		String nameVille;
		String script;
		int countRegion =0;
	 
		        // download the page super-efficiently
		        File file = new File(TEMP_, "google.html");
		        NetUtil.downloadFile(RADIOS_PAGE , file);
		
		        // create Jerry, i.e. document context
		        Jerry doc = Jerry.jerry(FileUtil.readString(file));
		
		        // remove div for toolbar
		        Node[]  nodesParent = doc.$("ul#regions_all").get();
		        
		        
		        // replace logo with html content
		        for(Node nodeP:nodesParent){
		        	countRegion +=  nodeP.getChildElementsCount();
		        	
		        	for (int i = 0; i < nodeP.getChildElementsCount(); i++) {
		        		nameVille = nodeP.getChildElement(i).getChild(0).getTextContent();
		        		 System.out.println("_____ region :"+(i+1));
						 System.out.println("ville : "+ nameVille  );
						
							// generate script
				        	script = "insert into regions (id_reg, id_countr_ , name_reg ) values ( "+ null
				        			+" , "+id_countr_+" , '"+ nameVille +"'  );" ;
				        	//create script sql 
				        //	writeUsingOutputStream(script, "scriptsRgions.sql");
				        	System.out.println("Script : "+script);
				        	System.out.println();
					}
		        	 
		        }
		        
		        System.out.println("****countRegion : "+ countRegion);
		        
		        
		       
		        
		      
		        
		        	
		         
		         
		 

   }
	private static void writeUsingOutputStream(String data,String file_) {
		try {

			File file = new File(SCRIPTS_DIR+file_);
	        FileWriter fr = new FileWriter(file, true);
	        BufferedWriter br = new BufferedWriter(fr);
	        br.write(data);
	        br.newLine();
	
	        br.close();
	        fr.close();
       
		} catch (Exception e) {
			System.out.println("error writing to file.");
		}
   }
	
	
	
	}

	

	

	



