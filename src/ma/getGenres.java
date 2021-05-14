package ma;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import jodd.io.FileUtil;
import jodd.io.NetUtil;
import jodd.jerry.Jerry;
import jodd.lagarto.dom.Node;

public class getGenres {

	
	private static String TEMP_ = "C:\\Users\\Pc-youssef\\Downloads\\Test\\src\\ma";
	// params 
	
	private static String SCRIPTS_DIR = "D:\\ProjectRadio\\Scripts\\"; 
	private static String RADIOS_PAGE = "https://onlineradiobox.com/search";
   private static int id_countr_ =4 ;


	public static void main(String[] args) throws IOException {

	 
		if(args.length == 3){ 
			RADIOS_PAGE = args[1]; 
			System.out.println(args[1]);
			System.out.println( RADIOS_PAGE);
			 
		}
		 
		
		 
		String nameRenre;
		String script;
		int countGenre =0;
	 
		        // download the page super-efficiently
		        File file = new File(TEMP_, "google.html");
		        NetUtil.downloadFile(RADIOS_PAGE , file);
		
		        // create Jerry, i.e. document context
		        Jerry doc = Jerry.jerry(FileUtil.readString(file));
		
		        // remove div for toolbar
		        Node[]  nodesParent = doc.$("ul#tag_all").get();
		        
		          	countGenre  =  nodesParent[0].getChildElementsCount() ;
		        // replace logo with html content
		       
		      
		        	
		        	for (int i = 1; i < countGenre; i++) {
		        		nameRenre =  nodesParent[0].getChildElement(i).getChild(0).getTextContent().trim().replace("'", "\\'");
		        		 System.out.println("_____ region :"+ i  );
		        		 System.out.println("_____ region :"+ nameRenre );
				  
							// generate script
				        	script =  "insert into tbl_genres(id_genre , name_genre ) values(NULL , '"+ nameRenre +  "');";
				        	//create script sql 
				         	writeUsingOutputStream(script, "scriptsGenres.sql");
				        	System.out.println("Script : "+script);
				        	System.out.println();
					}
		        	 
		      
		        
		        System.out.println("**** countGenre : "+ (countGenre-1) );
		        
		        
		       
		        
		      
		        
		        	
		         
		         
		 

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
