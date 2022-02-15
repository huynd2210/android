package hda.nzse.tower_defense;

public class Vector2D {

    public double x, y;

    Vector2D(){
        x = 0;
        y = 0;
    }
    Vector2D(double x, double y){
        this.x = x;
        this.y = y;
    }

    Vector2D(Vector2D v){
        this.x = v.x;
        this.y = v.y;
    }

    Vector2D add(Vector2D v){
        return new Vector2D(v.x + x, v.y + y);
    }

    //Result: new Vector = this - v
    Vector2D substract(Vector2D v){
        return new Vector2D(x-v.x , y-v.y);
    }

    public void scale(double s){
        x*=s;
        y*=s;
    }


    public double length(){
        return Math.sqrt(x*x+y*y);
    }

    public void normalize(){
        double l = length();
        x = x / l;
        y = y / l;
    }

    public Vector2D getNormalized(){
        double l = length();
        return new Vector2D(x / l, y / l);
    }

    //Dot Product,Formula: dot(a,b) = a.x*b.x + a.y*b.y
    public double dot(Vector2D v){
        return x*v.x + y*v.y;
    }

    // returns Angle between Vector a and b
    // Note: We do not use the standard formula cos(alpha) = a*b/ (|a| * |b|),
    // since that results only in values between 0 and 180°
    // @param areNormalized: true if both vectors are already normalized, speeds up computation
    public double angleBetween(Vector2D v, boolean areNormalized){
        double a =0;
        if(!areNormalized) {
            Vector2D thisNormalized = new Vector2D(this);
            thisNormalized.normalize();

            Vector2D vNormalized = new Vector2D(v);
            vNormalized.normalize();

            a =  Math.toDegrees(Math.atan2(vNormalized.y,vNormalized.x) - Math.atan2(thisNormalized.y,thisNormalized.x));
        }else
            a = Math.toDegrees(Math.atan2(v.y,v.x) - Math.atan2(y,x));
        if(a<0)
            a+= 360;

        return a;
    }

    // rotates this vector counter clockwise, around angle in degrees
    public void rotate(double angle){
        double theta = Math.toRadians(angle);
        double cs = Math.cos(theta);
        double sn = Math.sin(theta);
        double x = this.x * cs - this.y * sn;
        double y = this.x * sn + this.y * cs;
        this.x = x;
        this.y = y;
    }



    @Override
    public String toString() {
        return this.x + "/" + this.y;
    }
}
