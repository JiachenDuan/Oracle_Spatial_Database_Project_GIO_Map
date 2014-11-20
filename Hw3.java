

import oracle.spatial.geometry.JGeometry;
import oracle.sql.STRUCT;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.*;

/**
 * @author Jiachen Duan
 *
 * javac -cp ".:ojdbc6.jar:sdoapi.jar" Hw3.java
 * java -cp ".:ojdbc6.jar:sdoapi.jar" Hw3
 */
public class Hw3 extends javax.swing.JFrame {


    //
    private static Connection connection = null;

    /**
     * Creates new form Hw3
     */
    Image mapImage;
    ImageIcon imageIcon;
    ArrayList<Integer> poly_cord = new ArrayList<Integer>();

    Boolean range_query_selected = false;
    Boolean point_query_selected = false;
    Boolean closest_fire_hydrant_selected = false;
    int count = 1;
    int find_photo_count = 0;  //0 is center point, 1 is first point of polygon
    int x1 = 0, y1 = 0, x2 = 0, y2 = 0;
    int xfirst = 0, yfirst = 0;
    Polygon p1 = new Polygon();
    //coordinate of mouse click for range search
    ArrayList<Integer> coordinates_of_mouse_click = new ArrayList<Integer>();
    ArrayList<Integer> find_photo_coordinates_of_mouse_click = new ArrayList<Integer>();
    ArrayList<Integer> point_query_coordinates = new ArrayList<Integer>();
    ArrayList<Integer> find_photo_center_coordinates = new ArrayList<Integer>();
    ArrayList<Integer> find_photo_range_coordinates = new ArrayList<Integer>();


    public Hw3() {
        initComponents();

//        ImageIcon mapImageicon = new ImageIcon("src/map.JPG");
        ImageIcon mapImageicon = new ImageIcon("map.JPG");

        imageIcon = mapImageicon;
        mapjLabel.setIcon(mapImageicon);
        mapImage = mapImageicon.getImage();
        try {
            getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @SuppressWarnings("unchecked")

    private void initComponents() {

        buttonGroup1 = new ButtonGroup();
        jPanel1 = new JPanel();
        mapjLabel = new JLabel();
        jTextField1 = new JTextField();
        jPanel2 = new JPanel();
        BuildingCheckBox = new JCheckBox();
        PhotoCheckBox = new JCheckBox();
        PhotographerCheckBox = new JCheckBox();
        jPanel3 = new JPanel();
        wholeReginRadioButton = new JRadioButton();
        RangeQueryRadioButton = new JRadioButton();
        PointQueryRadioButton = new JRadioButton();
        FindPhotosRadioButton = new JRadioButton();
        FindPhotographerRadioButton = new JRadioButton();
        SubmitButton = new JButton();
        jScrollPane1 = new JScrollPane();
        showQueryTextArea = new JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Jiachen Duan");
        setAlwaysOnTop(true);

        jPanel1.setPreferredSize(new java.awt.Dimension(820, 580));

        mapjLabel.setToolTipText("");
        mapjLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                try {
                    mapjLabelMouseClicked(evt);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        mapjLabel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                mapjLabelMouseMoved(evt);
            }
        });

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(mapjLabel, GroupLayout.PREFERRED_SIZE, 820, GroupLayout.PREFERRED_SIZE)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(mapjLabel, GroupLayout.PREFERRED_SIZE, 580, GroupLayout.PREFERRED_SIZE)
        );

        jTextField1.setText("CURRENT MOUSE POSITION ON THE IMAGE");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Active Feature Type", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Cambria", 1, 14))); // NOI18N

        BuildingCheckBox.setText("Building");
        BuildingCheckBox.setName("");

        PhotoCheckBox.setText("Photo");

        PhotographerCheckBox.setText("Photographer");

        GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(PhotographerCheckBox, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                                        .addComponent(PhotoCheckBox, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                                        .addComponent(BuildingCheckBox, GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE))
                                .addGap(67, 67, 67))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(BuildingCheckBox)
                                .addGap(18, 18, 18)
                                .addComponent(PhotoCheckBox)
                                .addGap(18, 18, 18)
                                .addComponent(PhotographerCheckBox)
                                .addContainerGap(11, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), "Query", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Cambria Math", 1, 14))); // NOI18N

        buttonGroup1.add(wholeReginRadioButton);
        wholeReginRadioButton.setText("Whole Region");
        wholeReginRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                WholeRegionRadioButtonActionPerformed(evt);
            }
        });

        buttonGroup1.add(RangeQueryRadioButton);
        RangeQueryRadioButton.setText("Range Query");
        RangeQueryRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RangeQueryRadioButtonActionPerformed(evt);
            }
        });

        buttonGroup1.add(PointQueryRadioButton);
        PointQueryRadioButton.setText("Point Query");
        PointQueryRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PointQueryRadioButtonActionPerformed(evt);
            }
        });

        buttonGroup1.add(FindPhotosRadioButton);
        FindPhotosRadioButton.setText("Find Photos");
        FindPhotosRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    FindPhotoRadioButtonActionPerformed(evt);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });


        buttonGroup1.add(FindPhotographerRadioButton);
        FindPhotographerRadioButton.setText("Find Photographers");
        FindPhotographerRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FindPhotographerRadioButtonActionPerformed(evt);
            }
        });

        GroupLayout jPanel3Layout = new GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(wholeReginRadioButton, GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                                        .addComponent(RangeQueryRadioButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(PointQueryRadioButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(FindPhotosRadioButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(FindPhotographerRadioButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))

                                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addContainerGap(21, Short.MAX_VALUE)
                                .addComponent(wholeReginRadioButton)
                                .addGap(18, 18, 18)
                                .addComponent(RangeQueryRadioButton)
                                .addGap(18, 18, 18)
                                .addComponent(PointQueryRadioButton)
                                .addGap(18, 18, 18)
                                .addComponent(FindPhotosRadioButton)
                                .addGap(18, 18, 18)
                                .addComponent(FindPhotographerRadioButton)
                                .addGap(18, 18, 18))
        );

        SubmitButton.setText("SUBMIT QUERY");
        SubmitButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                try {
                    SubmitButtonMouseClicked(evt);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        SubmitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SubmitButtonActionPerformed(evt);
            }
        });

        showQueryTextArea.setColumns(20);
        showQueryTextArea.setRows(5);
        showQueryTextArea.setText("Your submitted query should be displayed here");
        jScrollPane1.setViewportView(showQueryTextArea);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(jScrollPane1, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 1073, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                        .addComponent(SubmitButton, GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
                                                        .addComponent(jPanel2, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jPanel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addComponent(jTextField1, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 473, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(53, 53, 53)
                                                .addComponent(jPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addGap(31, 31, 31)
                                                .addComponent(jPanel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                                                .addComponent(SubmitButton, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );

        getAccessibleContext().setAccessibleDescription("");

        pack();
    }



    private void mapjLabelMouseMoved(java.awt.event.MouseEvent evt) {
        int xpos = evt.getX();
        int ypos = evt.getY();
        String xy_pos = "Current mouse location:(" + xpos + "," + ypos + ")";
        jTextField1.setText(xy_pos);

    }

    private void SubmitButtonActionPerformed(java.awt.event.ActionEvent evt) {
//       

        Graphics g = mapjLabel.getGraphics();
        super.paint(g);

//        int w = mapImage.getWidth();
//        int h = old.getHeight();
        int w = 820;
        int h = 580;
        BufferedImage img = new BufferedImage(
                w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
//        g2d.drawImage(old, 0, 0, null);
        g2d.setPaint(Color.red);

        int a[] = {607, 641, 617, 584};
        int b[] = {199, 217, 257, 239};
        g2d.drawPolygon(a, b, 4);


//
        System.out.println("button click!!!!");

        // System.out.println(cord);
//        
//        int size=wr.size();

        //  4, 607, 199, 641, 217, 617, 257, 584, 239


    }

    private void SubmitButtonMouseClicked(java.awt.event.MouseEvent evt) throws SQLException {

        Graphics g = mapjLabel.getGraphics();

        g.drawImage(mapImage, 0, 0, this);


        if (wholeReginRadioButton.isSelected()) {

            if (BuildingCheckBox.isSelected()) {
                Thread t = new Thread(new Runnable() {
                    public void run()
                    {
                        // Insert some method call here.
                        System.out.println("running thread building!");
                        ArrayList<BuildingDto> blist = null;
                        try {
                            blist = getAllBuilding();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        draw_buidling(blist, Color.YELLOW);
                        System.out.print("Showing whole region building..");
                    }
                });
                t.start();

            }
            if (PhotoCheckBox.isSelected()) {


                Thread t = new Thread(new Runnable() {
                    public void run()
                    {
                        // Insert some method call here.
                        System.out.println("running thread photo!");
                        ArrayList<PhotoDto> photoList = null;
                        try {
                            photoList = getAllPhotos();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        draw_photo(photoList, Color.BLUE);
                    }
                });
                t.start();
            }
            if (PhotographerCheckBox.isSelected()) {
                System.out.println("Click photographer");


                Thread t = new Thread(new Runnable() {
                    public void run()
                    {
                        // Insert some method call here.
                        System.out.println("running thread photographer!");
                        ArrayList<PhotographerDto> photographerDtos = null;
                        try {
                            photographerDtos = getAllPhotographers();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        draw_photographer(photographerDtos, Color.GREEN);
                    }
                });
                t.start();
            }


        } else if (RangeQueryRadioButton.isSelected()) {
            if (BuildingCheckBox.isSelected()) {
                ArrayList<BuildingDto> buildingDtos = getRangeQueryBuilding(coordinates_of_mouse_click);
                draw_buidling(buildingDtos, Color.YELLOW);
            }

            if (PhotoCheckBox.isSelected()) {
                ArrayList<PhotoDto> photoDtos = getRangeQueryPhoto(coordinates_of_mouse_click);
                draw_photo(photoDtos, Color.BLUE);
            }
            if (PhotographerCheckBox.isSelected()) {
                ArrayList<PhotographerDto> photographerDtos = getRangeQueryPhotographer(coordinates_of_mouse_click);
                draw_photographer(photographerDtos, Color.GREEN);
            }
            //clearup the mouse click coordinates
            //resetCoordinates();

        } else if (PointQueryRadioButton.isSelected()) {
            int x = point_query_coordinates.get(0);
            int y = point_query_coordinates.get(1);

            if (BuildingCheckBox.isSelected()) {
                ArrayList<BuildingDto> buildingDtos = getPointQueryBuilding(x,y);
                draw_buidling(buildingDtos,Color.green);

            }
            if (PhotoCheckBox.isSelected()) {

                ArrayList<PhotoDto> photoDtos = getPointQueryPhotos(x, y);
                draw_photo(photoDtos, Color.green);

            }
            if (PhotographerCheckBox.isSelected()) {
                ArrayList<PhotographerDto> photographerDtos = getPointQueryPhotographers(x, y);
                draw_photographer(photographerDtos, Color.GREEN);
            }


        } else if (FindPhotosRadioButton.isSelected()) {
            int x = find_photo_center_coordinates.get(0);
            int y = find_photo_center_coordinates.get(1);


            //get photo
            ArrayList<PhotoDto> photoDtos = getFindPhotoQuery(x, y,find_photo_range_coordinates);
             draw_photo(photoDtos,Color.red);
//            resetCoordinates();
        }else if(FindPhotographerRadioButton.isSelected()){
            int x = find_photo_center_coordinates.get(0);
            int y = find_photo_center_coordinates.get(1);

            ArrayList<PhotoDto> photoDtos = getPhotoClosedToBuilding(x,y);
            draw_photo(photoDtos,Color.red);


            ArrayList<PhotographerDto> photographerDtos = getFindPhotographers(x, y);
            draw_photographer(photographerDtos,Color.RED);
        }

        //close connection at the end
        //closeConnection();
    }




    private void mapjLabelMouseClicked(java.awt.event.MouseEvent evt) throws SQLException {


        if (RangeQueryRadioButton.isSelected()) {
            Graphics g1 = mapjLabel.getGraphics();
            g1.setColor(Color.RED);
            p1.addPoint(evt.getX(), evt.getY());

            if (SwingUtilities.isLeftMouseButton(evt)) {
                //TODO: record the coordinate of mouse left click
                coordinates_of_mouse_click.add(evt.getX());
                coordinates_of_mouse_click.add(evt.getY());

                if (count == 1) {
                    x1 = evt.getX();
                    y1 = evt.getY();
                    System.out.println("drawing range polygon");
                    System.out.println(x1 + "," + y1);
                    xfirst = x1;
                    yfirst = y1;
                    count++;
                } else if (count >= 2) {
                    x2 = evt.getX();
                    y2 = evt.getY();

                    System.out.println(x2 + "," + y2);
                    count++;
                    g1.drawLine(x1, y1, x2, y2);
                    x1 = x2;
                    y1 = y2;

                }

            }

            if (SwingUtilities.isRightMouseButton(evt)) {
                x1 = evt.getX();
                y1 = evt.getY();

                System.out.println(x1 + " " + y1);
                g1.drawLine(x2, y2, x1, y1);
                g1.drawLine(x1, y1, xfirst, yfirst);
                //  boolean found= p1.contains(100,100);
                g1.setColor(Color.green);
                //  g1.drawPolygon(p1);
                //  System.out.println(found);
                //System.out.println(p1.xpoints[0]+","+p1.xpoints[1]+","+p1.ypoints[0]+","+p1.xpoints[1]);
                System.out.println(p1.npoints);
                for (int i = 0; i < p1.npoints; i++) {
                    System.out.print("inside loop");
                    System.out.println(p1.xpoints[i] + "," + p1.ypoints[i]);
                    poly_cord.add(p1.xpoints[i]);

                    poly_cord.add(p1.ypoints[i]);

                }
                System.out.println(poly_cord);
                System.out.println("tostirng" + poly_cord.toString());
                range_query_selected = false;
                //   s= "poly_cord";


            }
            //  poly_cord_func();

        }


        if (PointQueryRadioButton.isSelected()) {

            //cleanup point query coordinates everytime click new point on map
            point_query_coordinates = null;
            point_query_coordinates = new ArrayList<Integer>();
            //clean map
            Graphics g = mapjLabel.getGraphics();
            // super.paint(g);
            g.drawImage(mapImage, 0, 0, this);

            Graphics g1 = mapjLabel.getGraphics();
            g1.setColor(Color.RED);
//            p1.addPoint(evt.getX(), evt.getY());
            int radius = 100;
            g1.drawOval(evt.getX() - radius, evt.getY() - radius, radius * 2, radius * 2);
            g1.fillRect(evt.getX(), evt.getY(), 5, 5);

            point_query_coordinates.add(evt.getX());
            point_query_coordinates.add(evt.getY());

            //draw ploygang

        }
  //==== find photo ===
        if(FindPhotosRadioButton.isSelected()){
            Graphics g1 = mapjLabel.getGraphics();
            g1.setColor(Color.RED);

            if(find_photo_count==0){
                find_photo_center_coordinates = null;
                find_photo_center_coordinates = new ArrayList<Integer>();
                //clean map
                Graphics g = mapjLabel.getGraphics();
                g.drawImage(mapImage, 0, 0, this);
               //find the nearest photographer
               ArrayList<PhotographerDto> nearestphotographer = findNearestPhotographer(evt.getX(),evt.getY());
               draw_photographer(nearestphotographer,Color.red);
               // g1.fillRect(evt.getX(), evt.getY(), 5, 5);

                find_photo_center_coordinates.add(evt.getX());
                find_photo_center_coordinates.add(evt.getY());

                find_photo_count++;
            }else if(find_photo_count == 1){
                x1 = evt.getX();
                y1 = evt.getY();
                System.out.println("drawing range polygon");
                System.out.println(x1 + "," + y1);
                xfirst = x1;
                yfirst = y1;
                find_photo_count++;
            }else {
                x2 = evt.getX();
                y2 = evt.getY();

                System.out.println(x2 + "," + y2);
                count++;
                g1.drawLine(x1, y1, x2, y2);
                x1 = x2;
                y1 = y2;
            }
            //get all point of range
            if(find_photo_count!=0){
                find_photo_range_coordinates.add(evt.getX());
                find_photo_range_coordinates.add(evt.getY());
            }

        }
//======== Find photographer ===========
        if(FindPhotographerRadioButton.isSelected()){
            Graphics g1 = mapjLabel.getGraphics();
            g1.setColor(Color.RED);

            find_photo_center_coordinates = null;
            find_photo_center_coordinates = new ArrayList<Integer>();
            //clean map
            Graphics g = mapjLabel.getGraphics();
            g.drawImage(mapImage, 0, 0, this);
            //find the nearest photographer
//            ArrayList<PhotographerDto> nearestphotographer = findNearestPhotographer(evt.getX(),evt.getY());
//            draw_photographer(nearestphotographer,Color.red);

            ArrayList<BuildingDto> nearestBuilding = findNearstBuidling(evt.getX(),evt.getY());
            draw_buidling(nearestBuilding,Color.RED);
            // g1.fillRect(evt.getX(), evt.getY(), 5, 5);

            find_photo_center_coordinates.add(evt.getX());
            find_photo_center_coordinates.add(evt.getY());
        }

    }


    private void PointQueryRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {

        String sql1 = " SELECT * FROM building b " +
                "WHERE SDO_WITHIN_DISTANCE " +
                "(b.shape,mdsys.sdo_geometry( " +
                "2001,NULL,mdsys.sdo_point_type( x , y,NULL),NULL,NULL),'DISTANCE=100')='TRUE'\n" +
                " SELECT * FROM photo b " +
                "WHERE SDO_WITHIN_DISTANCE " +
                "(b.shape,mdsys.sdo_geometry( " +
                "2001,NULL,mdsys.sdo_point_type( x , y ,NULL),NULL,NULL),'DISTANCE=100')='TRUE'\n" +
                " SELECT * FROM photo b " +
                "WHERE SDO_WITHIN_DISTANCE " +
                "(b.shape,mdsys.sdo_geometry( " +
                "2001,NULL,mdsys.sdo_point_type(x,y,NULL),NULL,NULL),'DISTANCE=100')='TRUE'";
        showQueryTextArea.setText(sql1);
        Graphics g = mapjLabel.getGraphics();
        // super.paint(g);
        g.drawImage(mapImage, 0, 0, this);

    }

    public void draw_photographer(ArrayList<PhotographerDto> photographerDtoArrayList, Color color) {
        Graphics g1 = mapjLabel.getGraphics();
        g1.setColor(color);
        System.out.print("drawing photographers");
        for (PhotographerDto p : photographerDtoArrayList) {
            int x = (int) p.coordinates[0];
            int y = (int) p.coordinates[1];
            //Ellipse2D.Double circle = new Ellipse2D.Double(x, y, 15, 15);
            int radius = 10;
//            g1.fillOval(x-radius,y-radius,5,5);
//            g1.drawOval(x-radius,y-radius,20,20);
            g1.fillRect(x, y, 5, 5);
        }
    }

    public void draw_photo(ArrayList<PhotoDto> photoDtos, Color color) {
        Graphics g1 = mapjLabel.getGraphics();
        g1.setColor(color);
        System.out.print("drawing photographers");
        for (PhotoDto p : photoDtos) {
            int x = (int) p.coordinates[0];
            int y = (int) p.coordinates[1];
            //Ellipse2D.Double circle = new Ellipse2D.Double(x, y, 15, 15);
            int radius = 3;
//            g1.fillOval(x-radius,y-radius,5,5);
            g1.drawOval(x - radius, y - radius, radius, radius);
//            g1.fillRect(x,y,5,5);
        }
    }


    private void WholeRegionRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {

        String sql1 = "select * from building\n" + "select * from photo\n" + "select * from photographer";

        showQueryTextArea.setText(sql1);

        Graphics g = mapjLabel.getGraphics();
        // super.paint(g);
        g.drawImage(mapImage, 0, 0, this);
        resetCoordinates();

    }

    private void RangeQueryRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {

        String sql1 = "SELECT * FROM building A  WHERE sdo_filter(A.shape, SDO_geometry(2003,NULL,NULL," +
                "                                       SDO_elem_info_array(1,1003,1)," +
                "                                       SDO_ordinate_array( coordinatesStr ))" +
                "                           ) = 'TRUE'\n" + " SELECT * FROM photo A  WHERE sdo_filter(A.shape, SDO_geometry(2003,NULL,NULL," +
                "                                       SDO_elem_info_array(1,1003,1)," +
                "                                       SDO_ordinate_array( coordinatesStr ))" +
                "                           ) = 'TRUE'\n" + " SELECT * FROM photographer A  WHERE sdo_filter(A.shape, SDO_geometry(2003,NULL,NULL," +
                "                                       SDO_elem_info_array(1,1003,1)," +
                "                                       SDO_ordinate_array( coordinatesStr))" +
                "                           ) = 'TRUE'";;
        showQueryTextArea.setText(sql1);
        Graphics g = mapjLabel.getGraphics();
        // super.paint(g);
        g.drawImage(mapImage, 0, 0, this);
        range_query_selected = true;
        resetCoordinates();

    }

    private void FindPhotoRadioButtonActionPerformed(java.awt.event.ActionEvent evt) throws SQLException {

        String sql1 = " SELECT * FROM photo A  " +
                " WHERE sdo_filter(A.shape, SDO_geometry(2003,NULL,NULL, " +
                "                                       SDO_elem_info_array(1,1003,1), " +
                "                                       SDO_ordinate_array(coordinateStr )) " +
                "                           ) = 'TRUE' " +
                "                           and upper(A.PHOTOGRAPHERID) = (select upper(B.photographerid) FROM " +
                "photographer B " +
                "where SDO_NN ( B.shape, " +
                "mdsys.sdo_geometry( " +
                "2001,NULL,mdsys.sdo_point_type(x,y,NULL),NULL,NULL),'sdo_num_res=1' " +
                ") " +
                "= 'TRUE')\n" + "SELECT * FROM photo b  " +
                "WHERE SDO_WITHIN_DISTANCE " +
                "(b.shape,( " +
                "select a.shape FROM " +
                "building A " +
                "where SDO_NN ( A.shape, " +
                "mdsys.sdo_geometry( " +
                "2001,NULL,mdsys.sdo_point_type(x,y,NULL),NULL,NULL),'sdo_num_res=1' " +
                ") " +
                "= 'TRUE' " +
                "),'DISTANCE=80')='TRUE'";;

        showQueryTextArea.setText(sql1);

        Graphics g = mapjLabel.getGraphics();
        // super.paint(g);
        g.drawImage(mapImage, 0, 0, this);
        closest_fire_hydrant_selected = true;
        range_query_selected = false;
        // draw all selected features
        if (BuildingCheckBox.isSelected()) {
//           new Thread(new Runnable() {
//                public void run()
//                {
//
//                }
//            }).start();
            new Thread(new Runnable() {
                public void run()
                {
                    ArrayList<BuildingDto> blist = null;
                    try {
                        blist = getAllBuilding();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    draw_buidling(blist, Color.YELLOW);
                    System.out.print("Showing whole region building..");
                }
            }).start();


        }
        if (PhotoCheckBox.isSelected()) {
            new Thread(new Runnable() {
                                public void run()
                {
                    ArrayList<PhotoDto> photoList = null;
                    try {
                        photoList = getAllPhotos();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    draw_photo(photoList, Color.BLUE);
                }
            }).start();

        }
        if (PhotographerCheckBox.isSelected()) {
            new Thread(new Runnable() {
                public void run()
                {
                    System.out.println("Click photographer");

                    ArrayList<PhotographerDto> photographerDtos = null;
                    try {
                        photographerDtos = getAllPhotographers();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    draw_photographer(photographerDtos, Color.GREEN);
                }
            }).start();

        }
        resetCoordinates();
    }


    private void FindPhotographerRadioButtonActionPerformed(ActionEvent evt) {

        String sql1 = "select * from photographer p " +
                "where( " +
                "select count(*) from building a " +
                "where sdo_relate( a.shape, " +
                "sdo_geometry (2002, null, null, sdo_elem_info_array (1,2,1), " +
                "    sdo_ordinate_array (p.SHAPE.SDO_POINT.X,p.SHAPE.SDO_POINT.Y,x,y)) " +
                ", 'mask=ANYINTERACT querytype=JOIN') = 'TRUE' " +
                ") = 0 \n" + "select * FROM " +
                "building A  " +
                "where SDO_NN ( A.shape," +
                "mdsys.sdo_geometry( " +
                "2001,NULL,mdsys.sdo_point_type(x,y,NULL),NULL,NULL),'sdo_num_res=1' " +
                ") " +
                "= 'TRUE'";;

        showQueryTextArea.setText(sql1);

        Graphics g = mapjLabel.getGraphics();
        // super.paint(g);
        g.drawImage(mapImage, 0, 0, this);

        // draw all selected features
        if (BuildingCheckBox.isSelected()) {
//           new Thread(new Runnable() {
//                public void run()
//                {
//
//                }
//            }).start();
            new Thread(new Runnable() {
                public void run()
                {
                    ArrayList<BuildingDto> blist = null;
                    try {
                        blist = getAllBuilding();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    draw_buidling(blist, Color.YELLOW);
                    System.out.print("Showing whole region building..");
                }
            }).start();


        }
        if (PhotoCheckBox.isSelected()) {
            new Thread(new Runnable() {
                public void run()
                {
                    ArrayList<PhotoDto> photoList = null;
                    try {
                        photoList = getAllPhotos();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    draw_photo(photoList, Color.BLUE);
                }
            }).start();

        }
        if (PhotographerCheckBox.isSelected()) {
            new Thread(new Runnable() {
                public void run()
                {
                    System.out.println("Click photographer");

                    ArrayList<PhotographerDto> photographerDtos = null;
                    try {
                        photographerDtos = getAllPhotographers();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    draw_photographer(photographerDtos, Color.GREEN);
                }
            }).start();

        }
        resetCoordinates();
    }




    public void draw_buidling(ArrayList<BuildingDto> buildingDtoArrayList, Color color) {
        //Color s;
        Graphics2D g1 = (Graphics2D) mapjLabel.getGraphics();
        g1.setColor(color);
        g1.setStroke(new BasicStroke(2));


        for (BuildingDto b : buildingDtoArrayList) {
            int vertice = b.vertices;

            double[] coordinates = b.coordinates;
            int flag = 0;
            int x = 0;
            int y = 0;
            Polygon p = new Polygon();
            for (int i = 0; i < vertice * 2; i++) {
                if (flag == 0) {
                    flag++;
                    x = (int) coordinates[i];
                } else if (flag == 1) {
                    flag = 0;
                    y = (int) coordinates[i];

                    p.addPoint(x, y);

                }
            }

            g1.drawPolygon(p);
        }


    }

    public void resetCoordinates() {
        x1 = 0;
        y1 = 0;
        x2 = 0;
        y2 = 0;
        count = 1;
        find_photo_count = 0;
        xfirst = 0;
        yfirst = 0;
        //coordinate of mouse click for range search
        coordinates_of_mouse_click = null;
        coordinates_of_mouse_click = new ArrayList<Integer>();
        find_photo_coordinates_of_mouse_click = new ArrayList<Integer>();
         point_query_coordinates = new ArrayList<Integer>();
         find_photo_center_coordinates = new ArrayList<Integer>();
         find_photo_range_coordinates = new ArrayList<Integer>();
        System.out.println("Reseted coordinates");
    }


    /**
     * ============ Support Methods for DB access ===============
     */
    public static Connection getConnection() throws SQLException {


        System.out.println("-- connecting to oracle db --- ");
        try {

            Class.forName("oracle.jdbc.driver.OracleDriver");

        } catch (ClassNotFoundException e) {
            System.out.println("oracle driver is not founded");
            e.printStackTrace();
            return null;
        }

        System.out.println("Oracle JDBC Driver Registered");


        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(
                       //Place jdbc connection here
                );
            }
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return null;
        }

        if (connection != null) {
            System.out.println("Database connected successully!!!");
        } else {
            System.out.println("Failed to make connection!");
        }

        return connection;
    }


    public static void closeConnection() throws SQLException {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                connection.close();
            }
        }
    }
    /**
     * =========== Whole Region ===========
     */
    public static ArrayList<BuildingDto> getAllBuilding() throws SQLException {
        Statement st = getConnection().createStatement();
        ResultSet rs;
        String sql = "select * from building";
        rs = st.executeQuery(sql);
        ArrayList<BuildingDto> buildingDtoList = new ArrayList<BuildingDto>();
        while (rs.next()) {
            BuildingDto buildingDto = new BuildingDto();

            String id = rs.getString("id");
            String name = rs.getString("name");
            int vertices = rs.getInt("vertices");
            STRUCT struct = (STRUCT) rs.getObject("shape");
            JGeometry j_geom = JGeometry.load(struct);
            double[] coordinates = j_geom.getOrdinatesArray();
            buildingDto.buildId = id;
            buildingDto.buildingName = name;
            buildingDto.vertices = vertices;
            buildingDto.coordinates = coordinates;
            buildingDtoList.add(buildingDto);

            System.out.println("Id: " + id);
            System.out.println("Name: " + name);
            System.out.println("Vertices: " + vertices);
            for (Double d : coordinates) {
                System.out.print(d + ",");
            }
            System.out.println();
        }


        st.close();
//        closeConnection();
        return buildingDtoList;
    }

    public static ArrayList<PhotoDto> getAllPhotos() throws SQLException {

        Statement st = getConnection().createStatement();
        ResultSet rs;
        String sql = "select * from photo";
        rs = st.executeQuery(sql);
        ArrayList<PhotoDto> photoDtoList = new ArrayList<PhotoDto>();
        while (rs.next()) {
            PhotoDto photoDto = new PhotoDto();

            String id = rs.getString("photoid");
            String photographerid = rs.getString("photographerid");

            STRUCT struct = (STRUCT) rs.getObject("shape");
            JGeometry j_geom = JGeometry.load(struct);
            double[] coordinates = j_geom.getPoint();
            photoDto.photoId = id;
            photoDto.photographerId = photographerid;

            photoDto.coordinates = coordinates;
            photoDtoList.add(photoDto);

            System.out.println("PhotoId: " + id);
            System.out.println("PhotographerId: " + photographerid);

            for (Double d : coordinates) {
                System.out.print(d + ",");
            }
            System.out.println();
        }


        st.close();
//        closeConnection();
        return photoDtoList;
    }

    public static ArrayList<PhotographerDto> getAllPhotographers() throws SQLException {
        Statement st = getConnection().createStatement();
        ResultSet rs;
        String sql = "select * from photographer";
        rs = st.executeQuery(sql);
        ArrayList<PhotographerDto> photographerDtoArrayList = new ArrayList<PhotographerDto>();
        while (rs.next()) {
            PhotographerDto photographerDto = new PhotographerDto();


            String photographerid = rs.getString("photographerid");

            STRUCT struct = (STRUCT) rs.getObject("shape");
            JGeometry j_geom = JGeometry.load(struct);
            double[] coordinates = j_geom.getPoint();

            photographerDto.photographerId = photographerid;

            photographerDto.coordinates = coordinates;
            photographerDtoArrayList.add(photographerDto);

            System.out.println("PhotographerId: " + photographerid);

            for (Double d : coordinates) {
                System.out.print(d + ",");
            }
            System.out.println();
        }


        st.close();
//        closeConnection();
        return photographerDtoArrayList;
    }

    /**
     * =========== Range Query ===========
     */
    public static ArrayList<BuildingDto> getRangeQueryBuilding(ArrayList<Integer> mouseclickcoordinates) throws SQLException {

        if (mouseclickcoordinates == null || mouseclickcoordinates.size() == 0) return null;

        String coordinatesStr = "";
        for (int i = 0; i < mouseclickcoordinates.size(); i++) {
            if (i == mouseclickcoordinates.size() - 1) {
                coordinatesStr = coordinatesStr + mouseclickcoordinates.get(i);
            } else {
                coordinatesStr = coordinatesStr + mouseclickcoordinates.get(i) + ",";
            }
        }


        Statement st = getConnection().createStatement();
        ResultSet rs;
        String sql = "SELECT * FROM building A  WHERE sdo_filter(A.shape, SDO_geometry(2003,NULL,NULL," +
                "                                       SDO_elem_info_array(1,1003,1)," +
                "                                       SDO_ordinate_array(" + coordinatesStr + "))" +
                "                           ) = 'TRUE'";
        rs = st.executeQuery(sql);
        ArrayList<BuildingDto> buildingDtoList = new ArrayList<BuildingDto>();
        while (rs.next()) {
            BuildingDto buildingDto = new BuildingDto();

            String id = rs.getString("id");
            String name = rs.getString("name");
            int vertices = rs.getInt("vertices");
            STRUCT struct = (STRUCT) rs.getObject("shape");
            JGeometry j_geom = JGeometry.load(struct);
            double[] coordinates = j_geom.getOrdinatesArray();
            buildingDto.buildId = id;
            buildingDto.buildingName = name;
            buildingDto.vertices = vertices;
            buildingDto.coordinates = coordinates;
            buildingDtoList.add(buildingDto);

            System.out.println("Id: " + id);
            System.out.println("Name: " + name);
            System.out.println("Vertices: " + vertices);
            for (Double d : coordinates) {
                System.out.print(d + ",");
            }
            System.out.println();
        }


        st.close();
//        closeConnection();
        return buildingDtoList;
    }

    public static ArrayList<PhotoDto> getRangeQueryPhoto(
            ArrayList<Integer> mouseclickcoordinates) throws SQLException {


        if (mouseclickcoordinates == null || mouseclickcoordinates.size() == 0) return null;

        String coordinatesStr = "";
        for (int i = 0; i < mouseclickcoordinates.size(); i++) {
            if (i == mouseclickcoordinates.size() - 1) {
                coordinatesStr = coordinatesStr + mouseclickcoordinates.get(i);
            } else {
                coordinatesStr = coordinatesStr + mouseclickcoordinates.get(i) + ",";
            }
        }

        Statement st = getConnection().createStatement();
        ResultSet rs;
        String sql = " SELECT * FROM photo A  WHERE sdo_filter(A.shape, SDO_geometry(2003,NULL,NULL," +
                "                                       SDO_elem_info_array(1,1003,1)," +
                "                                       SDO_ordinate_array(" + coordinatesStr + "))" +
                "                           ) = 'TRUE'";
        rs = st.executeQuery(sql);
        ArrayList<PhotoDto> photoDtoList = new ArrayList<PhotoDto>();
        while (rs.next()) {
            PhotoDto photoDto = new PhotoDto();

            String id = rs.getString("photoid");
            String photographerid = rs.getString("photographerid");

            STRUCT struct = (STRUCT) rs.getObject("shape");
            JGeometry j_geom = JGeometry.load(struct);
            double[] coordinates = j_geom.getPoint();
            photoDto.photoId = id;
            photoDto.photographerId = photographerid;

            photoDto.coordinates = coordinates;
            photoDtoList.add(photoDto);

            System.out.println("PhotoId: " + id);
            System.out.println("PhotographerId: " + photographerid);

            for (Double d : coordinates) {
                System.out.print(d + ",");
            }
            System.out.println();
        }


        st.close();
//        closeConnection();
        return photoDtoList;
    }


    public static ArrayList<PhotographerDto> getRangeQueryPhotographer(
            ArrayList<Integer> mouseclickcoordinates) throws SQLException {


        if (mouseclickcoordinates == null || mouseclickcoordinates.size() == 0) return null;

        String coordinatesStr = "";
        for (int i = 0; i < mouseclickcoordinates.size(); i++) {
            if (i == mouseclickcoordinates.size() - 1) {
                coordinatesStr = coordinatesStr + mouseclickcoordinates.get(i);
            } else {
                coordinatesStr = coordinatesStr + mouseclickcoordinates.get(i) + ",";
            }
        }
        Statement st = getConnection().createStatement();
        ResultSet rs;
        String sql = " SELECT * FROM photographer A  WHERE sdo_filter(A.shape, SDO_geometry(2003,NULL,NULL," +
                "                                       SDO_elem_info_array(1,1003,1)," +
                "                                       SDO_ordinate_array(" + coordinatesStr + "))" +
                "                           ) = 'TRUE'";
        rs = st.executeQuery(sql);
        ArrayList<PhotographerDto> photographerDtoArrayList = new ArrayList<PhotographerDto>();
        while (rs.next()) {
            PhotographerDto photographerDto = new PhotographerDto();


            String photographerid = rs.getString("photographerid");

            STRUCT struct = (STRUCT) rs.getObject("shape");
            JGeometry j_geom = JGeometry.load(struct);
            double[] coordinates = j_geom.getPoint();

            photographerDto.photographerId = photographerid;

            photographerDto.coordinates = coordinates;
            photographerDtoArrayList.add(photographerDto);

            System.out.println("PhotographerId: " + photographerid);

            for (Double d : coordinates) {
                System.out.print(d + ",");
            }
            System.out.println();
        }


        st.close();
//        closeConnection();
        return photographerDtoArrayList;
    }

    /**
     *  ================= Point Query =================
     */
    /**
     * @param x center coordinate x
     * @param y center coordinate y
     * @return
     * @throws SQLException
     */
    public static ArrayList<BuildingDto> getPointQueryBuilding(int x, int y) throws SQLException {

        Statement st = getConnection().createStatement();
        ResultSet rs;
//        String sql = "SELECT * FROM building A  WHERE sdo_filter(A.shape, SDO_geometry(2003,NULL,NULL," +
//                "                                       SDO_elem_info_array(1,1003,4)," +
//                "                                       SDO_ordinate_array("+coordinateStr+"))" +
//                "                           ) = 'TRUE'";
        String sql = " SELECT * FROM building b " +
                "WHERE SDO_WITHIN_DISTANCE " +
                "(b.shape,mdsys.sdo_geometry( " +
                "2001,NULL,mdsys.sdo_point_type(" + x + "," + y + ",NULL),NULL,NULL),'DISTANCE=100')='TRUE'";

        rs = st.executeQuery(sql);
        ArrayList<BuildingDto> buildingDtoList = new ArrayList<BuildingDto>();
        while (rs.next()) {
            BuildingDto buildingDto = new BuildingDto();

            String id = rs.getString("id");
            String name = rs.getString("name");
            int vertices = rs.getInt("vertices");
            STRUCT struct = (STRUCT) rs.getObject("shape");
            JGeometry j_geom = JGeometry.load(struct);
            double[] coordinates = j_geom.getOrdinatesArray();
            buildingDto.buildId = id;
            buildingDto.buildingName = name;
            buildingDto.vertices = vertices;
            buildingDto.coordinates = coordinates;
            buildingDtoList.add(buildingDto);

            System.out.println("Id: " + id);
            System.out.println("Name: " + name);
            System.out.println("Vertices: " + vertices);
            for (Double d : coordinates) {
                System.out.print(d + ",");
            }
            System.out.println();
        }


        st.close();
        //   closeConnection();
        return buildingDtoList;
    }

    public static ArrayList<PhotoDto> getPointQueryPhotos(int x, int y) throws SQLException {

        Statement st = getConnection().createStatement();
        ResultSet rs;
//        String sql = "select * from photo";
        String sql = " SELECT * FROM photo b " +
                "WHERE SDO_WITHIN_DISTANCE " +
                "(b.shape,mdsys.sdo_geometry( " +
                "2001,NULL,mdsys.sdo_point_type(" + x + "," + y + ",NULL),NULL,NULL),'DISTANCE=100')='TRUE'";
        rs = st.executeQuery(sql);
        ArrayList<PhotoDto> photoDtoList = new ArrayList<PhotoDto>();
        while (rs.next()) {
            PhotoDto photoDto = new PhotoDto();

            String id = rs.getString("photoid");
            String photographerid = rs.getString("photographerid");

            STRUCT struct = (STRUCT) rs.getObject("shape");
            JGeometry j_geom = JGeometry.load(struct);
            double[] coordinates = j_geom.getPoint();
            photoDto.photoId = id;
            photoDto.photographerId = photographerid;

            photoDto.coordinates = coordinates;
            photoDtoList.add(photoDto);

            System.out.println("PhotoId: " + id);
            System.out.println("PhotographerId: " + photographerid);

            for (Double d : coordinates) {
                System.out.print(d + ",");
            }
            System.out.println();
        }


        st.close();
        //  closeConnection();
        return photoDtoList;
    }


    public static ArrayList<PhotographerDto> getPointQueryPhotographers(int x, int y) throws SQLException {
        Statement st = getConnection().createStatement();
        ResultSet rs;
        String sql = " SELECT * FROM photo b " +
                "WHERE SDO_WITHIN_DISTANCE " +
                "(b.shape,mdsys.sdo_geometry( " +
                "2001,NULL,mdsys.sdo_point_type(" + x + "," + y + ",NULL),NULL,NULL),'DISTANCE=100')='TRUE'";
        rs = st.executeQuery(sql);
        ArrayList<PhotographerDto> photographerDtoArrayList = new ArrayList<PhotographerDto>();
        while (rs.next()) {
            PhotographerDto photographerDto = new PhotographerDto();


            String photographerid = rs.getString("photographerid");

            STRUCT struct = (STRUCT) rs.getObject("shape");
            JGeometry j_geom = JGeometry.load(struct);
            double[] coordinates = j_geom.getPoint();

            photographerDto.photographerId = photographerid;

            photographerDto.coordinates = coordinates;
            photographerDtoArrayList.add(photographerDto);

            System.out.println("PhotographerId: " + photographerid);

            for (Double d : coordinates) {
                System.out.print(d + ",");
            }
            System.out.println();
        }


        st.close();
        //  closeConnection();
        return photographerDtoArrayList;
    }

    //============ Find Photo ==============
    public static ArrayList<PhotoDto> getFindPhotoQuery(int x, int y, ArrayList<Integer> find_photo_range_coordinates) throws SQLException {


        Statement st = getConnection().createStatement();
        ResultSet rs;
        //12,33, 44,123, 223,235, 456,445,223,543
        String coordinateStr = "";

        if (find_photo_range_coordinates == null || find_photo_range_coordinates.size() == 0) return null;

        for (int i = 0; i < find_photo_range_coordinates.size(); i++) {
            if (i != find_photo_range_coordinates.size() - 1) {

                coordinateStr = coordinateStr + find_photo_range_coordinates.get(i) + ",";
            } else {
                coordinateStr = coordinateStr + find_photo_range_coordinates.get(i);
            }
        }

        String sql = " SELECT * FROM photo A  " +
                " WHERE sdo_filter(A.shape, SDO_geometry(2003,NULL,NULL, " +
                "                                       SDO_elem_info_array(1,1003,1), " +
                "                                       SDO_ordinate_array(" + coordinateStr + ")) " +
                "                           ) = 'TRUE' " +
                "                           and upper(A.PHOTOGRAPHERID) = (select upper(B.photographerid) FROM " +
                "photographer B " +
                "where SDO_NN ( B.shape, " +
                "mdsys.sdo_geometry( " +
                "2001,NULL,mdsys.sdo_point_type(" + x + "," + y + ",NULL),NULL,NULL),'sdo_num_res=1' " +
                ") " +
                "= 'TRUE')";
        System.out.println("find photo sql: " + sql);
        rs = st.executeQuery(sql);
        ArrayList<PhotoDto> photoDtoList = new ArrayList<PhotoDto>();
        while (rs.next()) {
            PhotoDto photoDto = new PhotoDto();

            String id = rs.getString("photoid");
            String photographerid = rs.getString("photographerid");

            STRUCT struct = (STRUCT) rs.getObject("shape");
            JGeometry j_geom = JGeometry.load(struct);
            double[] coordinates = j_geom.getPoint();
            photoDto.photoId = id;
            photoDto.photographerId = photographerid;

            photoDto.coordinates = coordinates;
            photoDtoList.add(photoDto);

            System.out.println("PhotoId: " + id);
            System.out.println("PhotographerId: " + photographerid);

            for (Double d : coordinates) {
                System.out.print(d + ",");
            }
            System.out.println();
        }


        st.close();
        //  closeConnection();
        return photoDtoList;
    }


    private ArrayList<PhotoDto> getPhotoClosedToBuilding(int x, int y) throws SQLException {

        Statement st = getConnection().createStatement();
        ResultSet rs;

        String sql = "SELECT * FROM photo b  " +
                "WHERE SDO_WITHIN_DISTANCE " +
                "(b.shape,( " +
                "select a.shape FROM " +
                "building A " +
                "where SDO_NN ( A.shape, " +
                "mdsys.sdo_geometry( " +
                "2001,NULL,mdsys.sdo_point_type("+x+","+y+",NULL),NULL,NULL),'sdo_num_res=1' " +
                ") " +
                "= 'TRUE' " +
                "),'DISTANCE=80')='TRUE'";
        System.out.println("find photo sql: " + sql);
        rs = st.executeQuery(sql);
        ArrayList<PhotoDto> photoDtoList = new ArrayList<PhotoDto>();
        while (rs.next()) {
            PhotoDto photoDto = new PhotoDto();

            String id = rs.getString("photoid");
            String photographerid = rs.getString("photographerid");

            STRUCT struct = (STRUCT) rs.getObject("shape");
            JGeometry j_geom = JGeometry.load(struct);
            double[] coordinates = j_geom.getPoint();
            photoDto.photoId = id;
            photoDto.photographerId = photographerid;

            photoDto.coordinates = coordinates;
            photoDtoList.add(photoDto);

            System.out.println("PhotoId: " + id);
            System.out.println("PhotographerId: " + photographerid);

            for (Double d : coordinates) {
                System.out.print(d + ",");
            }
            System.out.println();
        }


        st.close();
        //  closeConnection();
        return photoDtoList;

    }


    /**
     * Find all photographer no interact building between it and click point
     * @param x
     * @param y
     * @return
     */
    private ArrayList<PhotographerDto> getFindPhotographers(int x, int y) throws SQLException {

        Statement st = getConnection().createStatement();
        ResultSet rs;

        String sql = "select * from photographer p " +
                "where( " +
                "select count(*) from building a " +
                "where sdo_relate( a.shape, " +
                "sdo_geometry (2002, null, null, sdo_elem_info_array (1,2,1), " +
                "    sdo_ordinate_array (p.SHAPE.SDO_POINT.X,p.SHAPE.SDO_POINT.Y,"+x+","+y+")) " +
                ", 'mask=ANYINTERACT querytype=JOIN') = 'TRUE' " +
                ") = 0 ";
        rs = st.executeQuery(sql);
        ArrayList<PhotographerDto> photographerDtoArrayList = new ArrayList<PhotographerDto>();
        while (rs.next()) {
            PhotographerDto photographerDto = new PhotographerDto();


            String photographerid = rs.getString("photographerid");

            STRUCT struct = (STRUCT) rs.getObject("shape");
            JGeometry j_geom = JGeometry.load(struct);
            double[] coordinates = j_geom.getPoint();

            photographerDto.photographerId = photographerid;

            photographerDto.coordinates = coordinates;
            photographerDtoArrayList.add(photographerDto);

            System.out.println("PhotographerId: " + photographerid);

            for (Double d : coordinates) {
                System.out.print(d + ",");
            }
            System.out.println();
        }


        st.close();
//        closeConnection();
        return photographerDtoArrayList;
    }



    public static ArrayList<PhotographerDto> findNearestPhotographer(int x, int y) throws SQLException {

        Statement st = getConnection().createStatement();
        ResultSet rs;
        String sql = "select * FROM " +
                "photographer A " +
                "where SDO_NN ( A.shape, " +
                "mdsys.sdo_geometry( " +
                "2001,NULL,mdsys.sdo_point_type("+x+","+y+",NULL),NULL,NULL),'sdo_num_res=1' " +
                ") = 'TRUE'";
        rs = st.executeQuery(sql);
        ArrayList<PhotographerDto> photographerDtoArrayList = new ArrayList<PhotographerDto>();
        while (rs.next()) {
            PhotographerDto photographerDto = new PhotographerDto();


            String photographerid = rs.getString("photographerid");

            STRUCT struct = (STRUCT) rs.getObject("shape");
            JGeometry j_geom = JGeometry.load(struct);
            double[] coordinates = j_geom.getPoint();

            photographerDto.photographerId = photographerid;

            photographerDto.coordinates = coordinates;
            photographerDtoArrayList.add(photographerDto);

            System.out.println("PhotographerId: " + photographerid);

            for (Double d : coordinates) {
                System.out.print(d + ",");
            }
            System.out.println();
        }


        st.close();
//        closeConnection();
        return photographerDtoArrayList;
    }




    private ArrayList<BuildingDto> findNearstBuidling(int x, int y) throws SQLException {

        Statement st = getConnection().createStatement();
        ResultSet rs;

        String sql = "select * FROM " +
                "building A  " +
                "where SDO_NN ( A.shape," +
                "mdsys.sdo_geometry( " +
                "2001,NULL,mdsys.sdo_point_type("+x+","+y+",NULL),NULL,NULL),'sdo_num_res=1' " +
                ") " +
                "= 'TRUE'";
        rs = st.executeQuery(sql);
        ArrayList<BuildingDto> buildingDtoList = new ArrayList<BuildingDto>();
        while (rs.next()) {
            BuildingDto buildingDto = new BuildingDto();

            String id = rs.getString("id");
            String name = rs.getString("name");
            int vertices = rs.getInt("vertices");
            STRUCT struct = (STRUCT) rs.getObject("shape");
            JGeometry j_geom = JGeometry.load(struct);
            double[] coordinates = j_geom.getOrdinatesArray();
            buildingDto.buildId = id;
            buildingDto.buildingName = name;
            buildingDto.vertices = vertices;
            buildingDto.coordinates = coordinates;
            buildingDtoList.add(buildingDto);

            System.out.println("Id: " + id);
            System.out.println("Name: " + name);
            System.out.println("Vertices: " + vertices);
            for (Double d : coordinates) {
                System.out.print(d + ",");
            }
            System.out.println();
        }


        st.close();
        //   closeConnection();
        return buildingDtoList;
    }
    /**
     * =============================
     */


    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {


        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new Hw3().setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private ButtonGroup buttonGroup1;
    private JButton SubmitButton;
    private JCheckBox BuildingCheckBox;
    private JCheckBox PhotoCheckBox;
    private JCheckBox PhotographerCheckBox;
    private JLabel mapjLabel;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JPanel jPanel3;
    private JRadioButton wholeReginRadioButton;
    private JRadioButton RangeQueryRadioButton;
    private JRadioButton PointQueryRadioButton;
    private JRadioButton FindPhotosRadioButton;
    private JRadioButton FindPhotographerRadioButton;
    private JScrollPane jScrollPane1;
    private JTextArea showQueryTextArea;
    private JTextField jTextField1;


}
class BuildingDto {
    String buildId;
    String buildingName;
    int vertices;
    double[] coordinates;
}

class PhotoDto {
    String photoId;
    String photographerId;
    double[] coordinates;
}

class PhotographerDto {
    String photographerId;
    double[] coordinates;
}
