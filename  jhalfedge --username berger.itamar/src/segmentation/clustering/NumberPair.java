package segmentation.clustering;

/**
 * Created by IntelliJ IDEA.
 * User: amirmore
 * Date: 1/16/11
 * Time: 9:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class NumberPair {
    public int num1,num2;

    public NumberPair(int num1, int num2) {
        this.num1 = num1;
        this.num2 = num2;
    }


   @Override
   public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NumberPair that = (NumberPair) o;

        if (num1 != that.num1) return false;
        if (num2 != that.num2) return false;

        return true;
   }
}
