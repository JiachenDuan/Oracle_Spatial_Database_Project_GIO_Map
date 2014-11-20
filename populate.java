
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Yishu Chen on 11/1/14.
 */

/**
 * cd /User/b4uloveme/hw3/src
 * javac populate.java
 * java -cp ".:ojdbc6.jar" populate photo.xy building.xy photographer.xy

 */
public class populate {

    final static Charset ENCODING = StandardCharsets.UTF_8;

    private void insertData(List<Building> buildingList,List<Photo> photoList, List<Photographer> photographerList) throws SQLException {


        System.out.println("-- connecting to oracle db --- ");
        try {

            Class.forName("oracle.jdbc.driver.OracleDriver");

        } catch (ClassNotFoundException e) {
            System.out.println("oracle driver is not founded");
            e.printStackTrace();
            return;
        }

        System.out.println("Oracle JDBC Driver Registered");

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:db11g", "ychen5", "WIndows0"

            );
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;
        }

        if (connection != null) {
            System.out.println("Database connected successully!!!");


            Statement st = null;
            st = connection.createStatement();

            try {

                //truncateTable before insert
                truncateTable(st);
                //insert data to table seperatly
                insertBuildingData(buildingList, st);
                insertPhotoData(photoList,st);
                insertPhotographerData(photographerList,st);

                System.out.printf("Rocord is inserted into DBUSER table!");
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (st != null) {
                    st.close();
                }

                if (connection != null) {
                    connection.close();
                }
            }


        } else {
            System.out.println("Failed to make connection!");
        }


    }

    private void insertPhotographerData(List<Photographer> photographerList, Statement st) throws SQLException {
        //execute insert sql;
        for (Photographer b : photographerList) {
            String sql = "insert into photographer values ('" +
                    b.photographerId + "'," +
                    "SDO_GEOMETRY(" +
                    "2001," +
                    "NULL," +
                    "SDO_POINT_TYPE(" + b.coordinates + ", NULL)," +
                    "NULL," +
                    "NULL"+
                    ")" +
                    ")";
            st.addBatch(sql);
            System.out.println(sql);
        }
        st.executeBatch();
    }

    private void insertPhotoData(List<Photo> photoList,Statement st) throws SQLException {
        //execute insert sql;
        for (Photo b : photoList) {
            String sql = "insert into photo values ('" +
                    b.photoId + "','" +
                    b.photographerId + "'," +
                    "SDO_GEOMETRY(" +
                    "2001," +
                    "NULL," +
                    "SDO_POINT_TYPE(" + b.coordinates + ", NULL)," +
                    "NULL," +
                    "NULL"+
                    ")" +
                    ")";
            st.addBatch(sql);
            System.out.println(sql);
        }
        st.executeBatch();
    }

    //Truncate table before insert
    private void truncateTable(Statement st) throws SQLException {
        String sql = "truncate table buidling;truncate table photo;truncate table PHOTOGRAPHER;";
//        System.out.println(sql);
        st.addBatch("truncate table building");
        st.addBatch("truncate table photo");
        st.addBatch("truncate table photographer");
        st.executeBatch();
    }
    private void insertBuildingData(List<Building> buildingList, Statement st) throws SQLException {
        //execute insert sql;
        for (Building b : buildingList) {
            String sql = "insert into building values ('" +
                    b.buildId + "','" +
                    b.buildingName + "'," +
                    b.vertices + "," +
                    "SDO_GEOMETRY(" +
                    "2003," +
                    "NULL," +
                    "NULL," +
                    "SDO_ELEM_INFO_ARRAY(1,1003,1)," +
                    "SDO_ORDINATE_ARRAY(" + b.coordinates + ")" +
                    ")" +
                    ")";
            st.addBatch(sql);
            System.out.println(sql);
        }
        st.executeBatch();
    }

//    @Test
//    public void testReadInputFile() throws SQLException {
//        String[] a = new String[3];
//        a[0] = "building.xy";
//        a[1] = "photo.xy";
//        a[2] = "photographer.xy";
//        readInputFile(a);
//    }

    public void readInputFile(String... filenames) throws SQLException {
       if(filenames == null || filenames.length == 0)
       {
           System.out.println("Please input valid data filename!");
       }

        List<Building> buildingList = new LinkedList<Building>();
        List<Photo> photoList = new LinkedList<Photo>();
        List<Photographer> photographerList = new LinkedList<Photographer>();
        for (String s : filenames) {
            URL url = getClass().getResource(s);
            File file = new File(url.getPath());


            try {

                InputStream input = new FileInputStream(file);

                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                if (input != null) {
                    String str = "";
                    while ((str = reader.readLine()) != null && !str.isEmpty()) {
                        System.out.println(str);
                        if (s.equals("building.xy")) {
                            buildingList.add(parseLineBuilding(str));
                        }else if(s.equals("photo.xy")){
                            photoList.add(parseLinePhoto(str));
                        }else if(s.equals("photographer.xy")){
                            photographerList.add(parseLinePhotographer(str));
                        }
                    }
                }

                //bach insert buidling here
//insertData(c);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        insertData(buildingList,photoList,photographerList);

    }

//    @Test
//    public void testParseLinePhotographer(){
//        String str = "p6, 153, 257";
//        parseLinePhotographer(str);
//    }
    private Photographer parseLinePhotographer(String str) {

        String[] line = str.split(",");
        if (line == null) {
            System.out.println("line does not have data");
            return null;
        }

        Photographer photographer = new Photographer();
        String photographerId = line[0].trim();

        String cooridnates = "";
        for (int i = 1; i < line.length; i++) {
            line[i] = line[i].trim();
            if (i == line.length - 1) {
                cooridnates += line[i];
            } else {
                cooridnates += line[i] + ",";
            }
        }
        photographer.photographerId = photographerId;
        photographer.coordinates = cooridnates;
        System.out.println(cooridnates);
        return photographer;
    }

//    @Test
//    public void testParseLineBuidling() {
//        String str = "b1, OHE, 4, 226, 150, 254, 164, 240, 191, 212, 176";
//        populate p = new populate();
//        p.parseLineBuilding(str);
//    }

    /*
         b1, OHE, 4, 226, 150, 254, 164, 240, 191, 212, 176
     */
    private Building parseLineBuilding(String str) {
        String[] line = str.split(",");
        if (line == null) {
            System.out.println("line does not have data");
            return null;
        }

        Building building = new Building();
        String buildingId = line[0];
        String buildingName = line[1];
        String numStr = line[2].trim();
        int NumOfVertice = Integer.valueOf(numStr);
        String cooridnates = "";
        for (int i = 3; i < line.length; i++) {
            line[i] = line[i].trim();
            if (i == line.length - 1) {
                cooridnates += line[i];
            } else {
                cooridnates += line[i] + ",";
            }
        }
//insert into building values (b1, OHE,4,SDO_GEOMETRY(2003,NULL,NULL,SDO_ELEM_INFO_ARRAY(1,1003,1),SDO_ORDINATE_ARRAY(226,150,254,164,240,191,212,176)))
        building.buildId = buildingId;
        building.buildingName = buildingName;
        building.vertices = NumOfVertice;
        building.coordinates = cooridnates;
        System.out.println(cooridnates);
        return building;
    }

//    @Test
//    public void testParseLinePhoto(){
//        String str = "Photo10, P57, 187, 111";
//        parseLinePhoto(str);
//    }

    private Photo parseLinePhoto(String str){
        String[] line = str.split(",");
        if (line == null) {
            System.out.println("line does not have data");
            return null;
        }

        Photo photo = new Photo();
        String photoId = line[0].trim();
        String photographerId = line[1].trim();

        String cooridnates = "";
        for (int i = 2; i < line.length; i++) {
            line[i] = line[i].trim();
            if (i == line.length - 1) {
                cooridnates += line[i];
            } else {
                cooridnates += line[i] + ",";
            }
        }
        photo.photoId = photoId;
        photo.photographerId = photographerId;
        photo.coordinates = cooridnates;
        System.out.println(cooridnates);
        return photo;
    }

    public static void main(String[] arg)  {


        populate p = new populate();
        try {
            p.readInputFile(arg);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}


class Building {
    String buildId;
    String buildingName;
    int vertices;
    String coordinates;
}
class Photo {
    String photoId;
    String photographerId;
    String coordinates;
}

class Photographer {
    String photographerId;
    String coordinates;
}

