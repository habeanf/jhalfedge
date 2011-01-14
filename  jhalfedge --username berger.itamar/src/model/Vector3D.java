package model;

/**
 * User: itamar
 * Date: Dec 9, 2010
 * Time: 12:38:05 PM
 */
public class Vector3D {
    public double x;                                    // the x value of this Vector3D
    public double y;                                    // the y value of this Vector3D
    public double z;                                    // the z value of this Vector3D

    public Vector3D()                                    // Constructor to set x = y = z = 0
    {
        x = 0;
        y = 0;
        z = 0;
    }

    public Vector3D(Vector3D vector)                                    // Constructor to set x = y = z = 0
    {
        x = vector.x;
        y = vector.y;
        z = vector.z;
    }

    public Vector3D(double[] xyz)            // Constructor that initializes this Vector3D to the intended double values of x, y and z
    {
        this.x = xyz[0];
        this.y = xyz[1];
        this.z = xyz[2];
    }

    public Vector3D(float[] xyz)            // Constructor that initializes this Vector3D to the intended float values of x, y and z
    {
        this.x = xyz[0];
        this.y = xyz[1];
        this.z = xyz[2];
    }

    public Vector3D(Vertex v)              // Constructor that initializes this Vector3D to the intended values of the Vertex v
    {
        this(v.getXyz());
    }


    public Vector3D(double x, double y, double z)            // Constructor that initializes this Vector3D to the intended values of x, y and z
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3D add(Vector3D v)            // operator+= is used to add another Vector3D to this Vector3D.
    {
        x += v.x;
        y += v.y;
        z += v.z;
        return this;
    }

    public Vector3D sub(Vector3D v)            // operator-= is used to subtract another Vector3D from this Vector3D.
    {
        Vector3D result = new Vector3D(this);
        result.x -= v.x;
        result.y -= v.y;
        result.z -= v.z;
        return result;
    }

    public float[] getFloatArray()
    {
        float[] result = {(float)x, (float)y,(float)z};
        return result;
    }

    public Vector3D scale(double value)            // operator*= is used to scale this Vector3D by a value.
    {
        x *= value;
        y *= value;
        z *= value;
        return this;
    }

    public Vector3D inverse()                        // operator- is used to set this Vector3D's x, y, and z to the negative of them.
    {
        x = -x;
        y = -y;
        z = -z;
        return this;
    }

    public double length()                                // length() returns the length of this Vector3D
    {
        return Math.sqrt(x * x + y * y + z * z);
    }

    

    public Vector3D normalize()                                // normalize() normalizes this Vector3D that its direction remains the same but its length is 1.
    {
        double length = length();

        if (length == 0)
            return this;

        x /= length;
        y /= length;
        z /= length;
        return this;
    }

    public double calculateDotTo(Vector3D toVector) {
        return this.x*toVector.x + this.y*toVector.y +this.z*toVector.z;
    }
    
    public double calculateAngleTo(Vector3D toVector) {
        return Math.acos((calculateDotTo(toVector)/(length()*toVector.length())));
    }

    public double calculateTriangleArea(Vector3D withVector) {
        return 0.5*length()*withVector.length()*Math.sin(calculateAngleTo(withVector));
    }

    public Vector3D calculateCrossProductWith(Vector3D v2) {
        Vector3D cross = new Vector3D(y * v2.z - v2.y * z,     // x coordinate of cross
                                      z * v2.x - v2.z * x,     // y coordinate of cross
                                      x * v2.y - v2.x * y);    // z coordinate of cross
        return cross;
    }
    public String toString() {
        return x + ", " + y + ", " + z;
    }
}
