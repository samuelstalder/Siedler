package ch.zhaw.catan;

import ch.zhaw.catan.Config.Land;
import ch.zhaw.hexboard.HexBoardTextView;
import ch.zhaw.hexboard.Label;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class inherits form {@link HexBoardTextView} and adds all the numbers to the fields of the board
 * It holds the information of the mapping of the number and the {@link Config.Land}
 *
 * @author Joël Plambeck
 * @version 04.12.2019
 */
public class SiedlerBoardTextView extends HexBoardTextView<Land, Settlement, Road, String> {
    
    private Map<Point, Label> lowerFieldLabels = new HashMap<>();
    
    /**
     * Creates an object of SiedlerBoardTextView which inherits from {@link HexBoardTextView}
     * and adds all dice numbers to the corresponding fields.
     * @param board the board to be played on
     */
    public SiedlerBoardTextView(SiedlerBoard board) {
        super(board);
        setPointsFromConfig();
    }
    
    private void setPointsFromConfig(){
        Label label;
        Map<Point, Integer> fields = Config.getStandardDiceNumberPlacement();
        for (Map.Entry<Point, Integer> field : fields.entrySet()) {
            label = createLabel(field.getValue());
            lowerFieldLabels.put(field.getKey(), label);
            setLowerFieldLabel(field.getKey(), label);
        }
    }
    
    /**
     * Method to create a two digit label
     * @param number the number to turn into a label
     * @return the resulting label
     */
    public static Label createLabel(Integer number){
        Label label;
        char firstCharacter = Integer.toString(number).charAt(0);
        if (isTwoDigitNumber(number)){
            char secondCharacter = Integer.toString(number).charAt(1);
            label = new Label(firstCharacter,secondCharacter);
        } else {
            label = new Label('0',firstCharacter);
        }
        return label;
    }
    
    /**
     * Method to determine if the specified number has two digits
     * @param number the number to check
     * @return if the number has two digits
     */
    private static boolean isTwoDigitNumber(Integer number){
        return number.toString().length() == 2;
    }
    
    /**
     * Method to get all center points with the specified numbers.
     * Since each number can be on multiple fields, a list is returned with all points
     * @param number the number on the dice
     * @return list with all center points of all the fields with the specified number
     */
    public List<Point> getCenterPoints(Integer number) {
        Label label = createLabel(number);
        List<Point> centerPoints = new ArrayList<>();
        for ( Map.Entry<Point, Label> entry : lowerFieldLabels.entrySet()){
            if (label.toString().equals(entry.getValue().toString())){
                centerPoints.add(entry.getKey());
            }
        }
        return centerPoints;
    }
    
}
