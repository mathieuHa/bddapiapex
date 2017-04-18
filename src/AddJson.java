import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class AddJson {
    private PrintWriter pw;
    //https://api.themoviedb.org/3/movie/popular?api_key=d53c99e97da849ea55b8ce31fd5e7666&language=en-FR
    //w92", "w154", "w185", "w342", "w500", "w780
    //http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg
    // bfb2dac1747744eaa4532c62d7606ca0 id spotify
    // BQAriOrylvVUloW2_n3NbEpWuPZlg6pO oth2

    //https://api.spotify.com/v1/search?q=ok&type=track&limit=2
    private int rand = (int)(Math.random()*999999);
    private boolean busy = false;
    private boolean erreur = false;
    private String inconnu = "unknown";
    private List<String> cat = new ArrayList<String>();
    private int cati = 0;

    public AddJson(PrintWriter pw) {
        this.pw = pw;


    }

    public int DL(String urlS) {
        URL url = null;

        try {
            url = new URL(urlS);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            System.out.println(connection.getResponseCode());
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                copyInputStreamToFile(connection.getInputStream(),
                        new File("temp.json"));
                return 0;
            } else if (connection.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
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
        String s = "https://www.googleapis.com/books/v1/volumes?q=" + subject + "&&printType=books&orderBy=newest&maxResults=10&langRestrict=fr&key=AIzaSyAT7eTEjPXHy8XGbk5-_thfHG638n_fcYY";
        System.out.println(s);
        return s;
    }

    public String buildRequestMusique(String subject) {
        String s = "https://api.spotify.com/v1/search?q=" + subject + "&type=track&limit=10";
        System.out.println(s);
        return s;
    }

    public String buildRequestMoviePopular(String subject) {
        String s = "https://api.themoviedb.org/3/movie/popular?api_key=d53c99e97da849ea55b8ce31fd5e7666&language=en-FR&page=" + subject;
        System.out.println(s);
        return s;
    }

    public JSONArray getArrayJS() {
        try {
            InputStream is = new FileInputStream("temp.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            return new JSONArray(new String(buffer, "UTF-8"));
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

    public JSONObject getObjectJS() {
        try {
            InputStream is = new FileInputStream("temp.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            return new JSONObject(new String(buffer, "UTF-8"));
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

    private void copyInputStreamToFile(InputStream in, File file) {
        try {
            OutputStream ou = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                ou.write(buf, 0, len);
            }
            ou.close();
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void sendFilm(String title, String description, String image, String date, String id, String language, String note) {
        busy = true;
        title = title.replace("'", "''");
        if (title.length() > 50) title = title.substring(0,49);
        description = description.replace("'", "''");
        if (description.length() > 500) description = description.substring(0,499);
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


    public void sendBook(String title, String auteurs, String description, String image, String date, String id, String category) {
        busy = true;
        File f = new File("book.txt");
        rand++;
        title = title.replace("'", " ");
        //description = description.replace("/", " ");
        description = description.replace("\"", " ");
        description = description.replace("'", " ");
        description = description.replace("�", " ");
        auteurs = auteurs.replace("'", " ");
        image = image.replace("'", " ");
        date = date.replace("'", " ");
        id = id.replace("'", " ");
        category = category.replace("'", " ");
        if (title.length() > 50) title = title.substring(0,49);
        if (description.length() > 500) description = description.substring(0,499);
        String sqlinsert = "";

        try {
            int categoryID = -1;
            for (int i = 0; i<cat.size();i++){
                System.out.println(cat.get(i));
                if (cat.get(i).equals(category))categoryID = i;
            }
            if (categoryID == -1) {
                File fc = new File("categorie.txt");
                if (cati == 0) {
                    categoryID = cati;
                    cat.add(category);
                    System.out.println("Nettoyage");
                    PrintWriter pwc = new PrintWriter(new BufferedWriter(new FileWriter(fc)));
                    pwc.println("insert into bookcategory (bookcategory,category) values ("+cati+",'"+category+"');");
                    cati++;
                    pwc.close();
                } else {
                    categoryID = cati;
                    cat.add(category);
                    PrintWriter pwc = new PrintWriter(new BufferedWriter(new FileWriter(fc, true)));
                    if (category.length()>50)pwc.println("insert into bookcategory (bookcategory,category) values ("+cati+",'"+category.substring(0,49)+"');");
                    else pwc.println("insert into bookcategory (bookcategory,category) values ("+cati+",'"+category+"');");
                    cati++;
                    pwc.close();
                }

            }

            sqlinsert = "INSERT INTO LIVRES (LIVREID,TITRE,AUTEUR,DESCRIPTION,RELEASEDATE,BOOKCATEGORY_BOOKCATEGORY,IMAGE) " +
                    "VALUES ("+rand+","
                    + "'" + title + "',"
                    + "'" + auteurs + "',"
                    + "'" + description + "',"
                    + "TO_DATE('" + date + "','YYYY-MM-DD'),"
                    + "" + categoryID + ","
                    + "'" + image + "');";
            if (cati == 1) {
                System.out.println("Nettoyage");
                PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f)));
                pw.println(sqlinsert);
                pw.close();
            } else {
                PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f, true)));
                pw.println(sqlinsert);
                pw.close();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        busy = false;
    }

    public void sendMusic(String title, String auteur, String album, String image, String id) {
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

    public String stripAccents(String s) {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }

    public void addBook(String nameS) {
        while (DL(this.buildRequestBook(nameS)) != 0) {
            System.out.println("errreur");
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        if (erreur == true) {
            System.out.println("ERRRRRRRRRREURR");
            erreur = false;
        } else {
            JSONObject js = this.getObjectJS();
            if (js != null) {
                JSONArray Array = null;
                try {
                    Array = js.getJSONArray("items");
                    if (Array != null) {
                        for (int i = 0; i < Array.length(); i++) {
                            String title = "";
                            String auteurs = "";
                            String description = "";
                            String image = "";
                            String date = "";
                            String id = "";
                            String category = "";
                            boolean valid = true;
                            JSONObject item = Array.getJSONObject(i);
                            if (item != null) {
                                id = item.getString("id");
                                if (id == null) valid = false;
                                JSONObject volumeInfo = item.getJSONObject("volumeInfo");
                                if (volumeInfo != null) {
                                    title = volumeInfo.getString("title");
                                    if (title == null) valid = false;
                                    description = volumeInfo.getString("description");
                                    if (description == null)valid = false;
                                    date = volumeInfo.getString("publishedDate");
                                    if (date == null)valid = false;
                                    JSONArray autors = volumeInfo.getJSONArray("authors");
                                    JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                                    image = imageLinks.getString("thumbnail");
                                    if (image == null)
                                        valid = false;
                                    JSONArray cat = volumeInfo.getJSONArray("categories");
                                    if (cat != null && cat.length()>0) {
                                        category = cat.getString(0);
                                    } else valid = false;
                                    if (autors != null) {
                                        for (int j = 0; j < autors.length(); j++) {
                                            auteurs += autors.getString(j) + " ";
                                        }
                                        if (auteurs == null) valid = false;
                                    } else valid = false;

                                    if (valid ==true) {//title = inconnu;


                                        while (busy == true) {
                                            try {
                                                System.out.println("erreur");
                                                Thread.sleep(10);
                                            } catch (InterruptedException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                            }
                                        }

                                        sendBook(title, auteurs, description, image, date, id, category);
                                    }
                                }
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void addVideo(int num) {
        System.out.println("VID");
        while (DL(this.buildRequestMoviePopular("" + num)) == 1) {
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
        String image = "";
        String date = "";
        String id = "";
        if (erreur == true) {
            System.out.println("ERRRRRRRRRREURR");
            erreur = false;
        } else {
            JSONObject js = this.getObjectJS();
            if (js != null) {
                JSONArray Array = null;
                try {
                    Array = js.getJSONArray("results");
                    if (Array != null) {
                        for (int i = 0; i < Array.length(); i++) {
                            JSONObject item = Array.getJSONObject(i);
                            if (item != null) {
                                boolean valid = true;
                                id = String.valueOf(item.getInt("id"));
                                if (id == null) valid = false;
                                title = item.getString("original_title");
                                if (title == null) valid = false;
                                description = item.getString("overview");
                                if (description == null) valid = false;
                                date = item.getString("release_date");
                                if (date == null) valid = false;
                                if (item.get("backdrop_path") != null)
                                    image = "http://image.tmdb.org/t/p/w342/" + String.valueOf(item.get("backdrop_path"));
                                else valid = false;;
                                if (item.get("vote_average") != null) note = String.valueOf(item.get("vote_average"));
                                else valid = false;
                                langage = item.getString("original_language");
                                if (langage == null) valid = false;
                                while (busy == true) {
                                    System.out.println("errreur");
                                    try {
                                        Thread.sleep(10);
                                    } catch (InterruptedException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }

                                }
                                if (valid == true) sendFilm(title, description, image, date, id, langage, note);
                            }
                        }
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void addMusic(String name) {
        System.out.println("MUS");
        while (DL(this.buildRequestMusique(name)) != 0) {
            System.out.println("errreur");
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String title = "null";
        String album = "null";
        String auteur = "null";
        String image = "null";
        String id = "0";
        System.out.println("debut");
        JSONObject js = this.getObjectJS();
        if (js == null) {
            System.out.println("js == null");
        } else {
            JSONObject track = null;
            try {
                track = js.getJSONObject("tracks");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (track == null) {
                System.out.println("track == null");
            } else {
                JSONArray Array = null;
                try {
                    Array = track.getJSONArray("items");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (Array == null || Array.length() == 0) {
                    System.out.println("array == null || lenght == 0");
                } else {
                    for (int i = 0; i < Array.length(); i++) {
                        JSONObject item = null;
                        try {
                            item = Array.getJSONObject(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (item == null) {
                            System.out.println("item == null");
                        } else {
                            try {
                                id = String.valueOf(item.getString("id"));
                                if (id == null) id = inconnu;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                title = item.getString("name");
                                if (title == null) title = inconnu;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            JSONObject albumO = null;
                            try {
                                albumO = item.getJSONObject("album");
                                if (albumO == null) album = inconnu;
                                else {
                                    album = albumO.getString("name");
                                    if (album == null) album = inconnu;
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            JSONArray imageA = null;
                            try {
                                imageA = albumO.getJSONArray("images");
                                if (imageA == null) {
                                    image = inconnu;
                                    System.out.println("imageA == null");
                                } else {
                                    JSONObject img = null;
                                    img = imageA.getJSONObject(1);
                                    if (image == null) {
                                        image = "http://business-on-line.typepad.fr/.a/6a0120a8b67743970b01b7c7ca52af970b-pi";
                                        System.out.println("img == null");
                                    } else {
                                        image = img.getString("url");
                                        if (image == null) {
                                            System.out.println("image == null");
                                            image = "http://business-on-line.typepad.fr/.a/6a0120a8b67743970b01b7c7ca52af970b-pi";
                                        }
                                    }


                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            JSONArray artistA = null;
                            try {
                                artistA = item.getJSONArray("artists");
                                if (artistA == null) auteur = inconnu;
                                else {
                                    for (int j = 0; j < artistA.length(); j++) {
                                        auteur += artistA.getJSONObject(j).optString("name") + ", ";
                                    }
                                    if (auteur == null) auteur = inconnu;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            while (busy) {
                                System.out.println("errreur");
                                try {
                                    Thread.sleep(250);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            }
                            System.out.println("ajout");
                            sendMusic(title, auteur, album, image, id);
                        }
                    }
                }
            }
        }
    }

}
