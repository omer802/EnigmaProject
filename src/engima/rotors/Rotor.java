package engima.rotors;

import engima.pairOfData;

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
          for (int leftIndex =0; leftIndex<PairOfDataArray.size();leftIndex++) {
               if (PairOfDataArray.get(leftIndex).getLeft() == inputChar)
                    return (leftIndex - position + PairOfDataArray.size()) % PairOfDataArray.size();
          }
          return -1;
     }

     public int convertLeftToRight(int index) {
          int indexWithPosition = (index + position) % PairOfDataArray.size();
          Character inputChar = PairOfDataArray.get(indexWithPosition).getLeft();
          for (int rightIndex = 0; rightIndex < PairOfDataArray.size(); rightIndex++) {
               if (PairOfDataArray.get(rightIndex).getRight() == inputChar)
                    return (rightIndex - position + PairOfDataArray.size()) % PairOfDataArray.size();
          }
          return -1;

     }
     public void setPairOfDataArray(List<pairOfData> pairOfDataArray) {
          PairOfDataArray = pairOfDataArray;
     }

     public int getPosition() {
          return position;
     }
}
