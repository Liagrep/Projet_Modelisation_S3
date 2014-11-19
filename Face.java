package affichage;
import java.awt.Polygon;

import javax.swing.JPanel;

import exceptions.SegmentException;

public class Face extends Polygon implements Comparable<Face> {
		int[] zpoints;
	public Face(Segment s1, Segment s2, Segment s3) throws SegmentException {
		this.npoints=3;
		this.xpoints=new int[3];
		this.ypoints=new int[3];
		this.zpoints=new int[3];
		
		xpoints[0] = (int) s1.getDebut().getX()*10+200;
		xpoints[1] = (int) s1.getFin().getX()*10+200;
		ypoints[0] = (int) s1.getDebut().getY()*10+200;
		ypoints[1] = (int) s1.getFin().getY()*10+200;
		zpoints[0] = (int) s1.getDebut().getZ()*10+200;
		zpoints[1] = (int) s1.getFin().getZ()*10+200;
	
		if(s2.getDebut().getX()==xpoints[0] && s2.getDebut().getY()==ypoints[0] && s2.getDebut().getZ()==zpoints[0]){
			xpoints[2]=(int) s2.getFin().getX()*10+200;
			ypoints[2]=(int) s2.getFin().getY()*10+200;
			zpoints[2] = (int) s2.getFin().getZ()*10+200;
			
		}
		else{		
			xpoints[2]=(int) s2.getDebut().getX()*10+200;
			ypoints[2]=(int) s2.getDebut().getY()*10+200;	
			zpoints[2] = (int) s2.getDebut().getZ()*10+200;
		}

		
	}
	
	public double distanceToCamera() {
			Point b= barycentre();
			return Math.sqrt(Math.pow((Camera.getInstance().getX()-b.getX()),2)+Math.pow((Camera.getInstance().getY()-b.getY()),2)+Math.pow((Camera.getInstance().getZ()-b.getZ()),2));
			

		
		//methode de math pour calculer la distance entre deux pts a voir avec benoit
	}

	//renvoie le barycentre d'une face 
	public Point barycentre(){
		double x =  this.getX1() + this.getX2() + this.getX3(); 
		double y =  this.getY1() + this.getY2() + this.getY3(); 
		double z =  this.getZ1() + this.getZ2() + this.getZ3(); 
		return new Point(x/3,y/3,z/3);
	}

	private int getX3() {
		return xpoints[2];
	}

	private int getX2() {
		return xpoints[1];	
	}

	private int getX1() {
		return xpoints[0];	
	}
	
	private int getY3() {
		return ypoints[2];
	}

	private int getY2() {
		return ypoints[1];	
	}

	private int getY1() {
		return ypoints[0];	
	}
	
	private int getZ3() {
		return zpoints[2];
	}

	private int getZ2() {
		return zpoints[1];	
	}

	private int getZ1() {
		return zpoints[0];	
	}

	@Override
	public int compareTo(Face a) {
		return (int)(this.distanceToCamera() - a.distanceToCamera());
	}
	

}
