package segmentation.cluster;

/**
 * Created by IntelliJ IDEA.
 * User: amirmore
 * Date: 1/16/11
 * Time: 12:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class DihedralProperty extends ClusterProperty<DihedralProperty> {
    private Double dihedralAngle;


    public DihedralProperty(DihedralProperty dihedralProperty) {
        this.dihedralAngle = new Double(dihedralProperty.getDihedral());
    }
    public DihedralProperty(double dihedral) {
        this.dihedralAngle = new Double(dihedral);
    }

    public DihedralProperty(Double dihedral) {
        this.dihedralAngle = dihedral;
    }

    public Double getDihedral() {
        return dihedralAngle;
    }

    public void setDihedral(double dihedral) {
        this.dihedralAngle = new Double(dihedral);
    }

    public void setDihedral(Double dihedral) {
        this.dihedralAngle = dihedral;
    }


    public int compareTo(DihedralProperty otherDihedral) {
        return dihedralAngle.compareTo(otherDihedral.getDihedral());
    }

    public void addToDihedral(Double moreAngle) {
        dihedralAngle+=moreAngle;
    }

}
