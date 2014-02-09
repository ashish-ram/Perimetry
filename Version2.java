import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.filter.*;
import ij.plugin.*;
import java.util.*;
import ij.ImagePlus;
 import ij.gui.GenericDialog;
 import ij.gui.NewImage;
 import ij.plugin.PlugIn;
import java.lang.Object.*;
import java.awt.event.*;
import java.awt.Component;
import ij.plugin.filter.*;
import ij.plugin.filter.PlugInFilter;
import ij.process.*; 	
import java.awt.event.*;

	class Stimulus{
		double alpha;//angular position from central vision
		int numberOfPoints;//number of points for a given alpha
		double angularDifference;//angular displacement between the points
		double theta;
		int normal_threshold;
		int threshold;
		int intensity;
		int  generated;
		int responce;
		int flag;
		Random generator =new Random();
	Stimulus(int m,int n){
		generated=0;
		responce=0;
		 flag=m;
	if(flag==0){
		alpha=3.14*5/180;
		int numberOfPoints=4;angularDifference=6.28/numberOfPoints;}
	if(flag==1){
		alpha=3.14*10/180;
		int numberOfPoints=8;angularDifference=6.28/numberOfPoints;}
	if(flag==2){
		alpha=3.14*15/180;
		int numberOfPoints=10;angularDifference=6.28/numberOfPoints;}
	if(flag==3){
		alpha=3.14*20/180;
		int numberOfPoints=16;angularDifference=6.28/numberOfPoints;}
	if(flag==4){
		alpha=3.14*24/180;
		int numberOfPoints=20;angularDifference=6.28/numberOfPoints;}
		
		theta=(1+2*n)*angularDifference/2;
		 normal_threshold=245+generator.nextInt(10);
		intensity= 250;
				}
		}

public class Version1 extends MouseAdapter implements PlugIn {	

//declare all static data here------------------------------------	
	static String title = "New Image";
	static double width = 1650;
	static double height = 950;
	static int bitdep=24;
	static int backgroundColor=150;
	static Stimulus stimulus[ ][ ];
	
	public void run(String arg) {

//Generic window making---------------------------------------------------------------------------------
	GenericDialog gd = new GenericDialog("New Image");
 	gd.addStringField("Title:", title);
 	gd.addNumericField("Width:", width, 0);
 	gd.addNumericField("Height:", height, 0);
	gd.addNumericField("BitDepth:", bitdep, 0);
	gd.addNumericField("Background color:",backgroundColor , 0);
	
	gd.showDialog();
	 if (gd.wasCanceled())
	 return;
 	title = gd.getNextString();
 	width = (int) gd.getNextNumber();
 	height = (int) gd.getNextNumber();	
	bitdep = (int) gd.getNextNumber();
//------------------------------------------------------------------------------------------------------------------
//setting the Stimulii(adjust alpha and no of pts per alpha)-----------------------------------------------------------------------------
stimulus=new Stimulus[5][];
stimulus[0]=new Stimulus[4];
stimulus[1]=new Stimulus[8];
stimulus[2]=new Stimulus[10];
stimulus[3]=new Stimulus[16];
stimulus[4]=new Stimulus[20];
for(int j=0;j<4;j++){
stimulus[0][j]=new Stimulus(0,j);
//IJ.beep();
//IJ.wait(700);
}
for(int j=0;j<8;j++){
stimulus[1][j]=new Stimulus(1,j);
//IJ.beep();
//IJ.wait(700);
}
for(int j=0;j<10;j++){
stimulus[2][j]=new Stimulus(2,j);
//IJ.beep();
//IJ.wait(700);
}
for(int j=0;j<16;j++){
stimulus[3][j]=new Stimulus(3,j);
//IJ.beep();
//IJ.wait(700);
}
for(int j=0;j<20;j++){
stimulus[4][j]=new Stimulus(4,j);
//IJ.beep();
//IJ.wait(700);
}



//creating image------------------------------------------------------------------------------------------------
ImagePlus im = NewImage.createImage(
 title, (int)width, (int)height, 1,8, NewImage.FILL_BLACK);
ImageProcessor ip=im.getProcessor();
//ip.setColor(backgroundColor);
//ip.setRoi(0,0, (int)width,(int)height);
//ip.fill();
im.show(); // show image on screen
ImageWindow win = im.getWindow();
ImageCanvas canvas = win.getCanvas();
win.setLocationAndSize(0,0,1650,950);
canvas.addMouseListener(this);
ip.snapshot();	

//random numbers used...............................................................	
	Random generator1 =new Random();
	Random generator2 =new Random();
	Random generator3 =new Random();
		
int checkedPointCounter = 0;
while(checkedPointCounter<=58){
//IJ.write("counter"+checkedPointCounter);
int delay= generator3.nextInt();
int i= generator1.nextInt(5);
int MAX=0;
if(i==0)
MAX=4;
if(i==1)
MAX=8;
if(i==2)
MAX=10;
if(i==3)
MAX=16;
if(i==4)
MAX=20;
int j= generator2.nextInt(MAX);
if(stimulus[i][j].responce == -3){
continue;}
get(i,j);

stimulus[i][j].responce--;
stimulus[i][j].generated++;

ip.setColor(stimulus[i][j].intensity);
	int r=(int)(height*Math.tan(stimulus[i][j].alpha)/(2*Math.tan(24*3.14/180)));
	int x=(int)(width/2+r*Math.cos(stimulus[i][j].theta));
	int y=(int)(height/2+r*Math.sin(stimulus[i][j].theta));
	ip.setRoi(new OvalRoi(x,y, 10, 10));
            ip.fill();
            ip.reset(ip.getMask());
	im.updateAndDraw(); 
	IJ.wait(300);
	//ip.reset();
	im.updateAndDraw(); 
	IJ.wait(310);
	//IJ.beep();

//IJ.write("=>"+i+"  "+j+"  "+stimulus[i][j].responce);
if(stimulus[i][j].responce== -3){
IJ.write(checkedPointCounter+"   "+i+"   "+j+"   "+stimulus[i][j].intensity);
//IJ.write("counter"+checkedPointCounter);
checkedPointCounter++;
}


}//while ends
IJ.showMessage("test completed");

			}//run ends 

int ii=0;int jj=0;				
void get(int i,int j){
ii=i;
jj=j;
	}

public void mouseClicked(MouseEvent e) {
	

	
	stimulus[ii][jj].responce=0;//reseting responce value to zero
	stimulus[ii][jj].intensity=stimulus[ii][jj].intensity - 20;
	//IJ.write("mousePressed: "+(8+xx*16)+","+(16*yy+8));
	IJ.beep();
	}

	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}	



				}//plugin ends

