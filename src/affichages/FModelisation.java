﻿package affichages;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JPanel;

import listener_package.MyMouseListener;
import listener_package.MyMouseMotionListener;
import listener_package.MyMouseWheelListener;
import maths_package.Matrice;
import exceptions.MatriceNotCorrespondingException;
import exceptions.SegmentException;
import exceptions.VectorException;

public class FModelisation extends JPanel implements KeyListener{

	private static final long serialVersionUID = 1L;
	private String fichier;
	private int xSize, ySize;
	private GtsReader gts;
	private ArrayList<Face> f;
	private int[] infos;
	private int[][] numsgmts;
	private int[][] numfces;
	private Face[] fces;
	private Point[] pts;
	private Segment[] sgmts;
	private Matrice Matrix;
	private int zoom=10;
	public int getZoom() {
		return zoom;
	}

	public void setZoom(int zoom) {
		this.zoom = zoom;
	}

	private int lastXPos;
	public int getLastXPos() {
		return lastXPos;
	}

	public void setLastXPos(int lastXPos) {
		this.lastXPos = lastXPos;
	}

	private int lastYPos;
	public int getLastYPos() {
		return lastYPos;
	}

	public void setLastYPos(int lastYPos) {
		this.lastYPos = lastYPos;
	}

	private final Matrice UP = new Matrice(new double[][] {{0},{-1},{0}}); ;
	private final Matrice DOWN = new Matrice(new double[][] {{0},{1},{0}}); ;
	private final Matrice LEFT = new Matrice(new double[][] {{-1},{0},{0}}); ;
	private final Matrice RIGHT = new Matrice(new double[][] {{1},{0},{0}}); ;
	
	public FModelisation(String fichier) throws SegmentException {
		try {
			setFocusable(true);
			this.requestFocusInWindow(true);
			this.addMouseMotionListener(new MyMouseMotionListener(this));
			this.addMouseWheelListener(new MyMouseWheelListener(this));
			this.addMouseListener(new MyMouseListener(this));
			this.addKeyListener(this);
			this.fichier = fichier;
			gts = new GtsReader(fichier);
			infos = gts.getInfos();
			numsgmts = gts.getNumsgmts();
			numfces = gts.getNumfces();
			pts = gts.getPoints();
			sgmts = gts.getSegments();
			fces = gts.getFaces();
			setTranslation(getVectorCenter());
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	//fonction dessinant les faces de la figure
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);	
		xSize = this.getWidth();
		ySize = this.getHeight();	

		for(int i=0;i<f.size();i++){
			int[] x= new int[3];
			int [] y= new int[3];
			for(int j =0;j<3;j++){
				x[j]= (int)(f.get(i).xpoints[j]*zoom+xSize/2);
				y[j]= (int)(f.get(i).ypoints[j]*zoom+ySize/2);
			}
			g.setColor(f.get(i).getCouleur());
			g.fillPolygon(x, y, x.length);
		}
	}


	public String ptsToString(){
		String res="";
		if(pts.length>0)
			res+="["+ pts[0].toString();
		for(int i=0; i<pts.length; i++){
			res+=", "+ pts[i].toString();
		}
		res+="]";
		return res;
	}
	

	
	@Override
	public void keyTyped(KeyEvent e) {
		int key = e.getKeyChar();
		try {
			if(key == 'q' ){
				setTranslation(LEFT);
			}
			else if(key == 'd'){
				setTranslation(RIGHT);
			}
			else if(key == 'z'){
				setTranslation(UP);
			}
			else if(key == 's'){
				setTranslation(DOWN);
			}
			else if(key=='c'){
			
				gts = new GtsReader(fichier);
				infos = gts.getInfos();
				numsgmts = gts.getNumsgmts();
				numfces = gts.getNumfces();
				pts = gts.getPoints();
				sgmts = gts.getSegments();
				fces = gts.getFaces();
				setTranslation(getVectorCenter());
			}
			repaint();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
	
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
	
	//fonction triant les faces de la plus éloignée a la plus proche
	public void triFaces(){
		f=new ArrayList<Face>();
		for(int i=0;i<fces.length;i++){
			f.add(fces[i]);
		}
		
		Collections.sort(f);
	}

	//fonction de créer la matrice homogène des points de la figure
	public void setPtsToMatrix(){
		Matrix = new Matrice(4,pts.length);
		for(int i=0; i<pts.length; i++){
			Matrix.setElem(0,i,pts[i].getX());
			Matrix.setElem(1,i,pts[i].getY());
			Matrix.setElem(2,i,pts[i].getZ());
			Matrix.setElem(3,i,1.0);
		}
	}
	//fonction permettant de récupérer les points dans la matrice
	public void setMatrixToPts(){
		for(int i=0; i<Matrix.getnColonnes(); i++){
			pts[i]= new Point(Matrix.getElem(0,i),Matrix.getElem(1,i),Matrix.getElem(2,i));
		}
	}

	//fonction permettant de translater la figure par rapport a un vecteur
	public void setTranslation(Matrice vector) throws VectorException, MatriceNotCorrespondingException, SegmentException{
		setPtsToMatrix();
		Matrix = Matrice.multiplier(Matrice.getTranslation(vector), Matrix);
		setMatrixToPts();
		setSegments();
		setFaces();
		triFaces();
	}
	
	//fonction permettant d'obtenir le barycentre de la figure
	public Matrice getVectorCenter(){
		double x = 0;
		double y = 0;
		double z = 0;
		for(int i=0; i<fces.length; i++){
			x+=fces[i].barycentre().getX();
			y+=fces[i].barycentre().getY();
			z+=fces[i].barycentre().getZ();
		}
		return new Matrice(new double[][]{{x/fces.length},{y/fces.length},{z/fces.length}});
	}

	//fonction permettant d'obtenir une rotation d'angle r autour de l'axe des X
	public void setRotationX(double r) throws MatriceNotCorrespondingException, SegmentException {
		setPtsToMatrix();
		Matrix = Matrice.multiplier(Matrice.getRotationX(r), Matrix);
		setMatrixToPts();
		setSegments();
		setFaces();
		triFaces();
	}

	//fonction permettant d'obtenir une rotation d'angle r autour de l'axe des Y
	public void setRotationY(double r) throws MatriceNotCorrespondingException, SegmentException {
		setPtsToMatrix();
		Matrix = Matrice.multiplier(Matrice.getRotationY(r), Matrix);
		setMatrixToPts();
		setSegments();
		setFaces();
		triFaces();
	}

	//fonction permettant d'obtenir une rotation d'angle r autour de l'axe des Z
	public void setRotationZ(double r) throws MatriceNotCorrespondingException, SegmentException {
		setPtsToMatrix();
		Matrix = Matrice.multiplier(Matrice.getRotationZ(r), Matrix);
		setMatrixToPts();
		setSegments();
		setFaces();
		triFaces();
	}

	//fonction stockant les segments de la figure
	public void setSegments() throws SegmentException{
		sgmts = new Segment[infos[1]];
		for(int i=0; i < infos[1]; i++){
			sgmts[i]=new Segment(pts[numsgmts[i][0]-1], pts[numsgmts[i][1]-1]);
		}
	}

	//fonction stockant les faces de la figure
	public void setFaces() throws SegmentException{
		fces = new Face[infos[2]];
		for(int i=0; i < infos[2]; i++){
			fces[i] = new Face(sgmts[numfces[i][0] - 1], sgmts[numfces[i][1] - 1], sgmts[numfces[i][2] - 1]);
		}
	}

}