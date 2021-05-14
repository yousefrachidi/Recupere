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
import java.util.List;
 
 

import jodd.io.FileUtil;
import jodd.io.NetUtil;
import jodd.jerry.Jerry;
import jodd.lagarto.dom.Node;

public class Test {
	
	private static String TEMP_ = "C:\\Users\\Pc-youssef\\Downloads\\Test\\src\\ma";
	// params
	private static String IMAGES_DIR = "D:\\ProjectRadio\\Images\\"; 
	private static String IMAGES_DIR_ER = "D:\\ProjectRadio\\Images\\erreur\\"; 
	private static String SCRIPTS_DIR = "D:\\ProjectRadio\\Scripts\\"; 
	private static String RADIOS_PAGE = "https://onlineradiobox.com/uk/Wales-/?cs=sd.aywa";

	private static List<String> listScript = new ArrayList<>();
	private static List<String> listSource = new ArrayList<>();
	private static List<String> listRadioDoublon = new ArrayList<>();
	private static int COUNT = 3;
	private static int id_reg_ =18;
	  private static int id_countr_ = 1;

	public static void main(String[] args) throws IOException {

		int counter = 1;
		int validCounter = 0;
		if(args.length == 3){
			IMAGES_DIR = args[0];
			RADIOS_PAGE = args[1];
			COUNT = args[2] != null ? Integer.valueOf(args[2]): COUNT;
		}
		
		Node nodesFig  ;
		Node  nodesFig_;
		Node[] nodesCh  ; 
		Node nodeBut;
		String radioStrm;
		String imageName;
		String script;
		String pattern = "YYYY_DD_MM_HH_mm_ss_SSS";
		String categorie ;
		String url_rad ;
		String scriptInfo ;
		getRadioInfo info =new getRadioInfo();
		
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		for(int i=0;i<COUNT;i++){
		        // download the page super-efficiently
		        File file = new File(TEMP_, "google.html");
		        NetUtil.downloadFile(RADIOS_PAGE+"&p="+i, file);
		
		        // create Jerry, i.e. document context
		        Jerry doc = Jerry.jerry(FileUtil.readString(file));
		
		        // remove div for toolbar
		        Node[] nodesParent = doc.$("li.stations__station").get();
		        
		        // replace logo with html content
		        
		        for(Node nodeP:nodesParent){
		        	nodesFig = nodeP.getFirstChildElement("figure");
		        	nodesFig_ = nodeP.getFirstChildElement("ul"); 
		        	nodesCh = nodesFig.getChildNodes(); 
		        	System.out.println();
		        	System.out.println("######  N� : "+ counter++ +" ; PAGE : "+(i+1)+" ; VALID : "+ validCounter +" ##########################");
		        	System.out.println( "title : "+nodesCh[1].getChildNodes()[1].getAttribute("title"));
		        	System.out.println( "image : "+"http:"+nodesCh[1].getChildNodes()[1].getAttribute("src") ); 
		        	url_rad =  nodesCh[1].getAttribute("href") ; 
		         
		        	
		        	categorie ="";
		       
		        	int tags = nodesFig_.getChildElementsCount();
		     	       //System.out.println("____ 3:" + nodesFig_.getChildElementsCount()  );
		        	  for (int j = 0; j < tags  ; j++) {  
		        	     	//System.out.println("____  "+j+":" + nodesFig_.getChildElement(j).getChild(0).getTextContent()  );
		        		    categorie += nodesFig_.getChildElement(j).getChild(0).getTextContent().trim().replace("'", "\\'")+"_";
		        		  
		        	 }
		        	  
		        	  System.out.println("Categories :" + categorie   );
 	      
 
		        	///get radion info
		        	  scriptInfo = info.getInfo("https://onlineradiobox.com"+url_rad);
		        	 System.out.println("infoRadio : "+ scriptInfo ) ;
						
					
		        	
		        	nodeBut = nodeP.getFirstChildElement("button");
		        	radioStrm = nodeBut.getAttribute("stream");
		        	System.out.println( "radio : "+radioStrm);
		        	boolean testRadio = testRadio(nodeBut.getAttribute("stream"));
		        	System.out.println("statut :"+testRadio);
		        	
		        	// download file
		        	
		        	String date = simpleDateFormat.format(new Date());
		        	imageName = date +".png";
		        	
		        	if(testRadio)
		        	downloadAndSaveImage(nodesCh[1].getChildNodes()[1].getAttribute("src"), imageName ,IMAGES_DIR);
		        	else if (!listSource.contains(radioStrm) )
		        	{ 
		        		downloadAndSaveImage(nodesCh[1].getChildNodes()[1].getAttribute("src"), imageName ,IMAGES_DIR_ER);
		        	}
		        	
		        	
		        	// generate script
		        	script = "insert into radios (id_rad, id_countr_, id_reg_, name_rad, img_rad, url_rad, categories ,"
		        			+ "description_rad , like_rad ,  site_rad , tele, address  , email  ) values ("
		        			 + null +","
		        			 + id_countr_ +","+ id_reg_ +",'"
		        			 + nodesCh[1].getChildNodes()[1].getAttribute("title").trim().replace("'", "\\'") +"','"
		        			 + "images/"+  imageName +"','"
		        			 +	nodeBut.getAttribute("stream")	+ "','"+ categorie+"',"+ scriptInfo+");" ;
		        	
		        	System.out.println("Script : "+script);
		        	
		        	if(testRadio && !listSource.contains(radioStrm) ) {
		        		validCounter++;
		        		listScript.add(script);
		        		listSource.add(radioStrm);
		        		writeUsingOutputStream(script, "scripts.sql");
		        	} else if ( testRadio &&  listSource.contains(radioStrm)){
		        		listRadioDoublon.add(script);
		        		writeUsingOutputStream(script, "scripts_doublon.sql");
		        	}
		        	else if ( !listSource.contains(radioStrm)){
		        		writeUsingOutputStream(script, "Errorscripts.sql");
					}
		        	
		        }
		}
		
		// display on console
		 /*System.out.println("############### SHOW SCRIPT VALID RADIO #########");
	        
	        for(String str:listScript){
	        	System.out.println(str);
	        }
	        
	     System.out.println("############### SHOW SCRIPT VALID RADIO : BUT DOUBLE #########");
	        
	        for(String str:listRadioDoublon){
	        	System.out.println(str);
	        	
	        } */
    }
	
	private static boolean testRadio(String url_){
		try {
			
	
		URL url = new URL(url_);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Content-Type", "media");

		BufferedReader in = new BufferedReader(new InputStreamReader(
		con.getInputStream()));
		int code = con.getResponseCode();
		in.close();
		if(code == 200)
			return true;
		
		}
		catch (IOException e) {
		}
		return false;
	}
	
	private static void downloadAndSaveImage(String url_,String name ,String IMAGES_DIR) throws IOException{
		
		URL url = new URL("http:"+url_);
		InputStream in = new BufferedInputStream(url.openStream());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int n = 0;
		while (-1!=(n=in.read(buf)))
		{
		   out.write(buf, 0, n);
		}
		out.close();
		in.close();
		byte[] response = out.toByteArray();
		FileOutputStream fos = new FileOutputStream(IMAGES_DIR+name);
		fos.write(response);
		fos.close();
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

