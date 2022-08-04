package engima;

import java.util.List;

public class Rotor {

     // לשנות כאשר זה יהיה מערך של רוטורים תוכל לדעת בלי לבדוק אם זה אחרון
     protected final String id;
     protected int position;
     protected List<pairOfData> PairOfDataArray;

     public Rotor( String id, List<pairOfData> setPairArray) {

          this.id = id;
          PairOfDataArray = setPairArray;
     }

     public void setPosition(int position) {
          this.position = position;
     }

     //לאחד לפונקציה אחת את ימים ושמאל ולמנוע שכפול קוד
     public int convertRightToLeft(int index) {
          int indexWithPosition = (index + position) % PairOfDataArray.size();
          Character inputChar = PairOfDataArray.get(indexWithPosition).getRight();
          for (int nextIndex =0; nextIndex<PairOfDataArray.size();nextIndex++) {
               int currentPosition = (nextIndex + position)  % PairOfDataArray.size();
               if (PairOfDataArray.get(currentPosition).getLeft() == inputChar)
                    return nextIndex;
          }
          return -1;
     }

     public int convertLeftToRight(int index) {
          int indexWithPosition = (index + position) % PairOfDataArray.size();
          Character inputChar = PairOfDataArray.get(indexWithPosition).getLeft();
          for (int nextIndex = 0; nextIndex < PairOfDataArray.size(); nextIndex++) {
               int currentPosition = (nextIndex + position)  % PairOfDataArray.size();
               if (PairOfDataArray.get(currentPosition).getRight() == inputChar)
                    return nextIndex;
          }
          return -1;

     }
}
