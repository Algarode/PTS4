package edwin.team.com.photoclient.Classes;

import android.graphics.Bitmap;
import android.graphics.Point;

/**
 * Created by stefan on 16-Dec-14.
 */
public class NeoBitmap {

    public Bitmap image;
    public int x;
    public int y;
    public int width;
    public int height;
    public int filterID; //0 = none, 1= black and white, 2 = sepia no blue, 3 is Sepia Brown

    public NeoBitmap(Bitmap bmp) {
        this.x =-1;
        this.y=-1;
        this.width=-1;
        this.height=-1;
        this.image=bmp;
        this.filterID= 0;
    }

    public void changeSize(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public  NeoBitmap(Bitmap bmp, int x, int y, int width,int height,int filterID){
        this.x =x;
        this.y=y;
        this.width=width;
        this.height=height;
        this.image=bmp;
        this.filterID= filterID;
    }

    public  NeoBitmap(Bitmap bmp,int filterID){
        this.x =-1;
        this.y=-1;
        this.width=-1;
        this.height=-1;
        this.image=bmp;
        this.filterID= filterID;
    }

    public void resetCrop(){
        this.x =-1;
        this.y=-1;
        this.width=-1;
        this.height=-1;
    }
    public Point getPoint(){
        return new Point(x,y);
    }

    public Bitmap getImageByFilter(){
        Bitmap bmp;
        switch (filterID){
            case 1:
                bmp = filter.BlackNWhite(image);
                break;
            case 2:
                bmp = filter.SepiaNoBlue(image);
                break;
            case 3:
                bmp = filter.SepiaBrown(image);
                break;
            default:
                bmp = image;
                break;
        }
        return bmp;
    }
}
