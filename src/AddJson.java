

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AddJson {
    private PrintWriter pw;
	//https://api.themoviedb.org/3/movie/popular?api_key=d53c99e97da849ea55b8ce31fd5e7666&language=en-FR
	//w92", "w154", "w185", "w342", "w500", "w780
	//http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg 
	// bfb2dac1747744eaa4532c62d7606ca0 id spotify
	// BQAriOrylvVUloW2_n3NbEpWuPZlg6pO oth2
    
	//https://api.spotify.com/v1/search?q=ok&type=track&limit=2
	
	private boolean busy = false;
	private boolean erreur = false;
	private String inconnu = "unknown";

	public AddJson (PrintWriter pw){
        this.pw = pw;

		
	}
	
	public int DL (String urlS){
		URL url = null;
	    try {
	        url = new URL (urlS);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("GET");
	        connection.connect();
	        System.out.println(connection.getResponseCode());
	        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
	        	copyInputStreamToFile(connection.getInputStream(),
	                    new File("temp.json"));
	        	return 0;
	        }
	        else if (connection.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST){
	        	erreur = true;
	        	return 0;
	        }
	        
	    } catch (MalformedURLException e1) {
	        e1.printStackTrace();
	    } catch (IOException e1) {
	        e1.printStackTrace();
	    }
	    return 1;
	}
	



	public String buildRequestBook(String subject) {
		String s = "https://www.googleapis.com/books/v1/volumes?q="+subject+"&&printType=books&orderBy=newest&maxResults=10&langRestrict=fr&key=AIzaSyAT7eTEjPXHy8XGbk5-_thfHG638n_fcYY";
		System.out.println(s);
		return s;
	}
	
	public String buildRequestMusique(String subject) {
		String s = "https://api.spotify.com/v1/search?q="+subject+"&type=track&limit=10";
		System.out.println(s);
		return s;
	}
	
	public String buildRequestMoviePopular(String subject) {
		String s = "https://api.themoviedb.org/3/movie/popular?api_key=d53c99e97da849ea55b8ce31fd5e7666&language=en-FR&page="+subject;
		System.out.println(s);
		return s;
	}
	
	public JSONArray getArrayJS(){
		try {
			InputStream is = new FileInputStream("temp.json");
			byte[] buffer = new byte[is.available()];
			is.read(buffer);
			is.close();
			return new JSONArray(new String(buffer,"UTF-8"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public JSONObject getObjectJS(){
		try {
			InputStream is = new FileInputStream("temp.json");
			byte[] buffer = new byte[is.available()];
			is.read(buffer);
			is.close();
			return new JSONObject(new String(buffer,"UTF-8"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
    
    private void copyInputStreamToFile (InputStream in, File file){
        try {
            OutputStream ou =  new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len=in.read(buf))>0){
                ou.write(buf,0,len);
            }
            ou.close();
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public void sendFilm (String title, String description, String image, String date, String id, String language, String note ){
    	busy = true;
    	title = title.replace("'", "''");
    	description = description.replace("'", "''");
    	note = note.replace("'", "''");
    	image = image.replace("'", "''");
    	date = date.replace("'", "''");
    	id = id.replace("'", "''");
    	language = language.replace("'", "''");
    	String sqlinsert = "INSERT INTO VIDEO (TITRE,ANNEE,IMAGE,"
				+ "NBEMPRUNT,NBEXEMPLAIRE,DESCRIPTION,LANGUAGE,NOTE,TARIF) " +
				"VALUES ("
				+ "'" + title + "',"
				+ "'" + date + "',"
				+ "'" + image + "',"
				+ "'" + 0 + "',"
				+ "'" + 0 + "',"
				+ "'" + description + "',"
				+ "'" + language + "',"
				+ "'" + note + "',"
				+ "'" + 0 + "');";
		busy = false;
    }

    
    
     
    public void sendBook (String title, String auteurs, String description, String image, String date, String id, String category){
    	busy = true;
    	title = title.replace("'", "''");
    	//description = description.replace("/", " ");
    	description = description.replace("\"", " ");
    	description = description.replace("'", " ");
    	description = description.replace("�", " ");
    	auteurs = auteurs.replace("'", "''");
    	image = image.replace("'", "''");
    	date = date.replace("'", "''");
    	id = id.replace("'", "''");
    	category = category.replace("'", "''");
		String sqlinsert = "INSERT INTO LIVRE (TITRE,AUTEUR,ANNEE,CATEGORY,IMAGE,"
											+ "NBEMPRUNT,NBEXEMPLAIRE,DESCRIPTION,TARIF) " +
                   "VALUES ("
                   		 + "'" + title + "',"
                   		 + "'" + auteurs + "',"
                   		 + "'" + date + "',"
                   		 + "'" + category + "',"
                   		 + "'" + image + "',"
                   		 + "'" + 0 + "',"
                   		 + "'" + 0 + "',"
                   		 + "'" + description + "',"
                   		 + "'" + 0 + "');"; 
		System.out.println(sqlinsert);
		busy = false;
    }
    
    public void sendMusic (String title, String auteur, String album, String image, String id){
    	busy = true;
    	title = title.replace("'", "''");
    	album = album.replace("'", "''");
    	image = image.replace("'", "''");
    	auteur = auteur.replace("'", "''");

		StringBuilder sb = new StringBuilder();
        sb.append(title);
        sb.append(';');
        sb.append(album);
        sb.append(';');
        sb.append(image);
        sb.append(';');
        sb.append(auteur);
        sb.append('\n');

		pw.append(sb.toString());

		System.out.println("done!");



		busy = false;
    }
    
    public void addBook (String nameS){
    	System.out.println("BOK");
    	while (DL(this.buildRequestBook(nameS))!=0){
    		System.out.println("errreur");
	    	try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
		
		String title = "";
		String auteurs = "";
		String description = "";
		String image ="";
		String date ="";
		String id ="";
		String category ="";
		
		if (erreur==true){
			System.out.println("ERRRRRRRRRREURR");
			erreur = false;}
		else{
			JSONObject js = this.getObjectJS();
			if (js != null) {
				JSONArray Array = js.optJSONArray("items");
				if (Array != null) {
					for (int i=0; i<Array.length(); i++){
						JSONObject item = Array.optJSONObject(i);
						if (item != null){
							id = item.optString("id");
							if (id == null) id = inconnu;
							JSONObject volumeInfo = item.optJSONObject("volumeInfo");
							if (volumeInfo != null){
								title = volumeInfo.optString("title");
								description = volumeInfo.optString("description");
								date = volumeInfo.optString("publishedDate");
								JSONArray autors = volumeInfo.optJSONArray("authors");
								JSONObject imageLinks = volumeInfo.optJSONObject("imageLinks");
								image = imageLinks.optString("thumbnail");
								if (image == null) image = "http://business-on-line.typepad.fr/.a/6a0120a8b67743970b01b7c7ca52af970b-pi";
								JSONArray cat = volumeInfo.optJSONArray("categories");
								if (cat != null){
									for (int k=0; k<cat.length(); k++){
											category+=cat.optString(k) +" ";
									}
									if (category == null) category =inconnu;
								}else category =inconnu;
								if (autors != null){
									for (int j=0; j<autors.length(); j++){
										auteurs+=autors.optString(j) +" ";
									}
									if (auteurs == null) auteurs =inconnu;
								}else auteurs=inconnu;
								
								if (title != null && description !=null && date!=null && autors != null && cat!=null && auteurs !=null && category !=null && image != null) {//title = inconnu;
									
								
									while (busy==true){
										try {
											System.out.println("erreur");
											Thread.sleep(10);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
									
									sendBook(title,auteurs,description,image,date,id,category);
								}else System.out.println("OUT");
								System.out.println("WHYDBde");
							}else System.out.println("volumeINfo");
							System.out.println("WHYDBdede");
						}else System.out.println("ITEM");
						System.out.println("WHYDBdedazd");
					}
				}else System.out.println("ARRAY");
				
				System.out.println("WHYDBdedazaz");
			}
			else System.out.println("JS");
			System.out.println("WHYDBdazze");
		}
		System.out.println("WHYDB");
    }
    
    public void addVideo (int num){
    	System.out.println("VID");
    	while (DL(this.buildRequestMoviePopular(""+num))==1){
    		System.out.println("errreur");
	    	try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
		
		String title = "";
		String langage = "";
		String note = "";
		String description = "";
		String image ="";
		String date ="";
		String id ="";
		if (erreur==true){
			System.out.println("ERRRRRRRRRREURR");
			erreur = false;}
		else{
			JSONObject js = this.getObjectJS();
			if (js != null){
				JSONArray Array = js.optJSONArray("results");
				if (Array != null) {
					for (int i=0; i<Array.length(); i++){
						JSONObject item = Array.optJSONObject(i);
						if (item != null) {
							id = String.valueOf(item.optInt("id")); if (id == null) id = inconnu;
							title = item.optString("original_title"); if (title == null) title = inconnu;
							description = item.optString("overview"); if (description == null) description = inconnu;
							date = item.optString("release_date"); if (date == null) date = inconnu;
							if (item.opt("backdrop_path")!=null) image = "http://image.tmdb.org/t/p/w342/" +String.valueOf(item.opt("backdrop_path"));
							else image = "http://business-on-line.typepad.fr/.a/6a0120a8b67743970b01b7c7ca52af970b-pi";
							if (item.opt("vote_average") != null) note = String.valueOf(item.opt("vote_average"));
							else note = inconnu;
							langage = item.optString("original_language"); if (langage == null) langage = inconnu;
							while (busy==true){
								System.out.println("errreur");
								try {
									Thread.sleep(10);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							}
							sendFilm(title,description,image,date,id,langage,note);
						}
					}
				}
			}
		}
    }
    
    public void addMusic (String name){
    	System.out.println("MUS");
    	while (DL(this.buildRequestMusique(name))!=0){
    		System.out.println("errreur");
	    	try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
		String title = "";
		String album = "";
		String auteur = "";
		String image ="";
		String id ="";
		if (erreur==true){
			System.out.println("ERRRRRRRRRREURR");
			erreur = false;}
		else{
			System.out.println("debut");
			JSONObject js = this.getObjectJS();
			if (js != null){
				JSONObject track = js.optJSONObject("tracks");
				if (track != null){
					JSONArray Array = track.optJSONArray("items");
					if (Array != null){
						for (int i=0; i<Array.length(); i++){
							System.out.println("Item");
							JSONObject item = Array.optJSONObject(i);
							if (item != null) {
								id = String.valueOf(item.optString("id")); if (id == null) id=inconnu;
								title = item.optString("name");	if (title == null) title=inconnu;
								JSONObject albumO = item.optJSONObject("album"); if (albumO == null) album = inconnu;
								else  album = albumO.optString("name"); if (album == null) album = inconnu;
								JSONArray imageA = albumO.optJSONArray("images"); if (imageA == null) image=inconnu;
								else  image = imageA.optJSONObject(1).optString("url");  if (image==null) image="http://business-on-line.typepad.fr/.a/6a0120a8b67743970b01b7c7ca52af970b-pi";
								JSONArray artistA = item.optJSONArray("artists"); if (artistA == null) auteur = inconnu;
								else{
									for (int j=0; j<artistA.length(); j++){
										auteur+=artistA.optJSONObject(j).optString("name")+", ";
									}
									if (auteur == null) auteur = inconnu;
								}
								while (busy==true){
									System.out.println("errreur");
									try {
										Thread.sleep(250);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
								}
								System.out.println("ajout");
								sendMusic(title,auteur,album,image,id);
							}
						}
					}
				}
			}
	    }
    }
}
