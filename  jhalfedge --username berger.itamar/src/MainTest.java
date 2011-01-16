import java.util.HashSet;
import java.util.Set;

/**
 * User: itamar
 * Date: 1/16/11
 * Time: 10:08 PM
 */
public class MainTest {
    public static void main(String[] args) {
        Set<NumberPair> numpair = new HashSet<NumberPair>();

        numpair.add(new NumberPair(701, 700));

        System.out.println(numpair.contains(new NumberPair(701, 700)));
    }

    static class NumberPair {
        int num1, num2;

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

        @Override
        public int hashCode() {
            int result = num1;
            result = 31 * result + num2;
            return result;
        }
    }

}
