package EngineMachine;

import java.util.List;

public class Rotor {

     // לשנות כאשר זה יהיה מערך של רוטורים תוכל לדעת בלי לבדוק אם זה אחרון
     protected final String id;
     protected int position;
     protected List<pairOfData> PairOfDataArray;

     public Rotor(int startingPosition, String id, List<pairOfData> setPairArray) {
          this.position = startingPosition;
          this.id = id;
          PairOfDataArray = setPairArray;
     }
     //לאחד לפונקציה אחת את ימים ושמאל ולמנוע שכפול קוד
     public int convertRightToLeft(int index) {
          int indexWithPosition = (index + position) % PairOfDataArray.size();
          Character inputChar = PairOfDataArray.get(indexWithPosition).getInput();
          for (int nextIndex =0; nextIndex<PairOfDataArray.size();nextIndex++) {
               int currentPosition = (nextIndex + position)  % PairOfDataArray.size();
               if (PairOfDataArray.get(currentPosition).getOutput() == inputChar)
                    return nextIndex;
          }
          return -1;
     }

     public int convertLeftToRight(int index) {
          int indexWithPosition = (index + position) % PairOfDataArray.size();
          Character inputChar = PairOfDataArray.get(indexWithPosition).getOutput();
          for (int nextIndex = 0; nextIndex < PairOfDataArray.size(); nextIndex++) {
               int currentPosition = (nextIndex + position)  % PairOfDataArray.size();
               if (PairOfDataArray.get(currentPosition).getInput() == inputChar)
                    return nextIndex;
          }
          return -1;

     }
}
